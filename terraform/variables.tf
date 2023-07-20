variable "region" {
  type        = string
  description = "Region"
  default     = "us-west-2"
}

variable "pds_validate_ecr_image_path" {
  type        = string
  description = "PDS Validate ECR Image Path"
  sensitive   = true
}

variable "efs_file_system_id" {
  type        = string
  description = "EFS File System ID"
  sensitive   = true
}

variable "pds_validate_data_access_point_id" {
  type        = string
  description = "PDS Validate EFS Access Point ID"
  sensitive   = true
}

variable "ecs_task_role_arn" {
  type        = string
  description = "ECS Task Role ARN"
  sensitive   = true
}

variable "ecs_task_execution_role_arn" {
  type        = string
  description = "ECS Task Execution Role ARN"
  sensitive   = true
}
