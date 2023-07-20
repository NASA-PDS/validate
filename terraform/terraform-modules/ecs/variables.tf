variable "pds_validate_ecr_image_path" {
  type        = string
  description = "PDS Validate ECR Image Path"
  # default     = "<PDS Validate ECR Image Path>"
  sensitive = true
}

variable "efs_file_system_id" {
  type        = string
  description = "EFS File System ID"
  # default     = "<EFS File System ID>"
  sensitive = true
}

variable "pds_validate_data_access_point_id" {
  type        = string
  description = "PDS Validate EFS Access Point ID"
  # default     = "<PDS Validate EFS Access Access Point ID>"
  sensitive = true
}

variable "ecs_task_role_arn" {
  type        = string
  description = "Airflow Task Role ARN"
  # default     = "<ECS Task Role>"
  sensitive = true
}

variable "ecs_task_execution_role_arn" {
  type        = string
  description = "ECS Task Execution Role ARN"
  # default     = "<ECS Task Execution Role>"
  sensitive = true
}
