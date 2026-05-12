output "load_balancer_url" {
  description = "The public DNS name of the Application Load Balancer"
  value       = aws_lb.main.dns_name
}

# ------------------------------------------------------------------------------
# COGNITO OUTPUTS
# ------------------------------------------------------------------------------

output "cognito_user_pool_arn" {
  description = "ARN del User Pool de Cognito"
  value       = aws_cognito_user_pool.main.arn
}

output "cognito_app_client_id" {
  description = "ID del App Client de Cognito"
  value       = aws_cognito_user_pool_client.main.id
}

output "cognito_user_pool_id" {
  value = aws_cognito_user_pool.main.id
}

output "cognito_domain" {
  value = aws_cognito_user_pool_domain.main.domain
}

output "cognito_hosted_ui_url" {
  description = "URL del Hosted UI de Cognito para login"
  value       = "https://${aws_cognito_user_pool_domain.main.domain}.auth.${var.aws_region}.amazoncognito.com/login?client_id=${aws_cognito_user_pool_client.main.id}&response_type=token&scope=openid+email+profile&redirect_uri=${var.cognito_callback_urls[0]}"
}

# ------------------------------------------------------------------------------
# RDS OUTPUTS
# ------------------------------------------------------------------------------

output "rds_endpoint" {
  description = "Endpoint de conexión de la base de datos RDS PostgreSQL"
  value       = aws_db_instance.main.address
}

output "rds_port" {
  description = "Puerto de la base de datos"
  value       = aws_db_instance.main.port
}

output "db_password_ssm_path" {
  description = "Ruta en SSM Parameter Store donde se almacena la contraseña de la BD"
  value       = aws_ssm_parameter.db_password.name
}

# ------------------------------------------------------------------------------
# FRONTEND OUTPUTS
# ------------------------------------------------------------------------------

output "frontend_s3_website_url" {
  description = "URL del sitio web estático en S3"
  value       = aws_s3_bucket_website_configuration.frontend.website_endpoint
}

output "frontend_s3_bucket_name" {
  description = "El nombre del bucket S3 para el frontend"
  value       = aws_s3_bucket.frontend.id
}
