# ------------------------------------------------------------------------------
# ASYNC INFRASTRUCTURE: SQS + LAMBDA + API GATEWAY
# ------------------------------------------------------------------------------

# 1. SQS Queue
resource "aws_sqs_queue" "report_queue" {
  name                      = "agenda-report-queue"
  delay_seconds             = 0
  max_message_size          = 262144
  message_retention_seconds = 86400
  receive_wait_time_seconds = 10
  
  # Dead Letter Queue (optional but recommended)
  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.report_dlq.arn
    maxReceiveCount     = 5
  })
}

resource "aws_sqs_queue" "report_dlq" {
  name = "agenda-report-dlq"
}

# 2. Lambda Function
resource "aws_lambda_function" "report_processor" {
  filename      = "lambda_processor.zip"
  function_name = "agenda-report-processor"
  role          = "arn:aws:iam::033588268326:role/LabRole" # Using LabRole for simplicity in AWS Academy
  handler       = "lambda_processor.handler"
  runtime       = "nodejs18.x"
  timeout       = 30

  environment {
    variables = {
      QUEUE_URL = aws_sqs_queue.report_queue.id
    }
  }
}

# Package Lambda
data "archive_file" "lambda_zip" {
  type        = "zip"
  source_file = "lambda_processor.js"
  output_path = "lambda_processor.zip"
}

# SQS Trigger for Lambda
resource "aws_lambda_event_source_mapping" "sqs_trigger" {
  event_source_arn = aws_sqs_queue.report_queue.arn
  function_name    = aws_lambda_function.report_processor.arn
  batch_size       = 5
}

# 3. API Gateway (REST API)
resource "aws_api_gateway_rest_api" "async_api" {
  name        = "AgendaAsyncAPI"
  description = "API for asynchronous report requests"
}

resource "aws_api_gateway_resource" "reports" {
  rest_api_id = aws_api_gateway_rest_api.async_api.id
  parent_id   = aws_api_gateway_rest_api.async_api.root_resource_id
  path_part   = "reports"
}

resource "aws_api_gateway_method" "post_report" {
  rest_api_id   = aws_api_gateway_rest_api.async_api.id
  resource_id   = aws_api_gateway_resource.reports.id
  http_method   = "POST"
  authorization = "NONE"
}

# Integration: API Gateway -> SQS
resource "aws_api_gateway_integration" "api_sqs" {
  rest_api_id             = aws_api_gateway_rest_api.async_api.id
  resource_id             = aws_api_gateway_resource.reports.id
  http_method             = aws_api_gateway_method.post_report.http_method
  integration_http_method = "POST"
  type                    = "AWS"
  uri                     = "arn:aws:apigateway:us-east-1:sqs:path/${aws_sqs_queue.report_queue.name}"
  credentials             = "arn:aws:iam::033588268326:role/LabRole"

  request_parameters = {
    "integration.request.header.Content-Type" = "'application/x-www-form-urlencoded'"
  }

  # Map JSON to SQS format
  request_templates = {
    "application/json" = "Action=SendMessage&MessageBody=$input.body"
  }
}

resource "aws_api_gateway_method_response" "ok" {
  rest_api_id = aws_api_gateway_rest_api.async_api.id
  resource_id = aws_api_gateway_resource.reports.id
  http_method = aws_api_gateway_method.post_report.http_method
  status_code = "200"
}

resource "aws_api_gateway_integration_response" "api_sqs_response" {
  rest_api_id = aws_api_gateway_rest_api.async_api.id
  resource_id = aws_api_gateway_resource.reports.id
  http_method = aws_api_gateway_method.post_report.http_method
  status_code = aws_api_gateway_method_response.ok.status_code

  depends_on = [aws_api_gateway_integration.api_sqs]
}

# Deployment
resource "aws_api_gateway_deployment" "async_deploy" {
  depends_on  = [aws_api_gateway_integration.api_sqs]
  rest_api_id = aws_api_gateway_rest_api.async_api.id
  stage_name  = "prod"
}

# Outputs
output "async_api_url" {
  value = "${aws_api_gateway_deployment.async_deploy.invoke_url}/reports"
}

output "sqs_queue_url" {
  value = aws_sqs_queue.report_queue.id
}
