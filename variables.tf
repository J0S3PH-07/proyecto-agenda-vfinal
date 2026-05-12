variable "aws_region" {
  description = "AWS Region"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Name of the project"
  type        = string
  default     = "restricted-ecs-project"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "public_subnet_a_cidr" {
  description = "CIDR block for public subnet A"
  type        = string
  default     = "10.0.1.0/24"
}

variable "public_subnet_b_cidr" {
  description = "CIDR block for public subnet B"
  type        = string
  default     = "10.0.2.0/24"
}

variable "az_a" {
  description = "Availability Zone A"
  type        = string
  default     = "us-east-1a"
}

variable "az_b" {
  description = "Availability Zone B"
  type        = string
  default     = "us-east-1b"
}

variable "ecr_image_url" {
  description = "URL of the image to deploy"
  type        = string
  default     = "033588268326.dkr.ecr.us-east-1.amazonaws.com/restricted-ecs-project-repo:latest"
}

variable "container_port" {
  description = "Port the container is listening on"
  type        = number
  default     = 8085
}

# ------------------------------------------------------------------------------
# COGNITO
# ------------------------------------------------------------------------------

variable "cognito_callback_urls" {
  description = "URLs de callback permitidas para OAuth 2.0 (Cognito Hosted UI)"
  type        = list(string)
  default     = ["https://app.manzanaazul.cat/login.html"]
}

variable "cognito_logout_urls" {
  description = "URLs de logout permitidas para OAuth 2.0 (Cognito Hosted UI)"
  type        = list(string)
  default     = ["http://localhost:8080"]
}

# ------------------------------------------------------------------------------
# PRIVATE SUBNETS
# ------------------------------------------------------------------------------

variable "private_subnet_a_cidr" {
  description = "CIDR block for private subnet A (RDS)"
  type        = string
  default     = "10.0.3.0/24"
}

variable "private_subnet_b_cidr" {
  description = "CIDR block for private subnet B (RDS)"
  type        = string
  default     = "10.0.4.0/24"
}

# ------------------------------------------------------------------------------
# DATABASE
# ------------------------------------------------------------------------------

variable "db_name" {
  description = "Nombre de la base de datos PostgreSQL"
  type        = string
  default     = "room_reservation_system"
}

variable "db_username" {
  description = "Usuario maestro de la base de datos"
  type        = string
  default     = "postgres"
}

variable "google_client_id" {
  description = "Google OAuth Client ID"
  type        = string
  default     = "PONER_AQUI_ID_GOOGLE"
}

variable "google_client_secret" {
  description = "Google OAuth Client Secret"
  type        = string
  default     = "PONER_AQUI_SECRET_GOOGLE"
}
