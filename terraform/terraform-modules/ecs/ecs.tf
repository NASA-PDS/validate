# CloudWatch Log Group for PDS Validate ECS Task
resource "aws_cloudwatch_log_group" "pds-validate-log-group" {
  name = "/ecs/pds-validate"
}

# Replace PDS Validate ECR Image Path in pds-validate-containers.json
data "template_file" "pds-validate-containers-json-template" {
  template = file("terraform-modules/ecs/container-definitions/pds-validate-containers.json")
  vars = {
    pds_validate_ecr_image_path = var.pds_validate_ecr_image_path
  }
}

# PDS Validate ECS Task Definition
resource "aws_ecs_task_definition" "pds-validate-task-definition" {
  family                   = "pds-validate-task-definition"
  requires_compatibilities = ["EC2", "FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 4096
  memory                   = 8192
  runtime_platform {
    operating_system_family = "LINUX"
  }

  volume {
    name = "pds-validate-data"

    efs_volume_configuration {
      file_system_id      = var.efs_file_system_id
      root_directory      = "/"
      transit_encryption  = "ENABLED"
      authorization_config {
        access_point_id   = var.pds_validate_data_access_point_id
        iam               = "ENABLED"
      }
    }
  }

  container_definitions       = data.template_file.pds-validate-containers-json-template.rendered
  task_role_arn               = var.ecs_task_role_arn
  execution_role_arn          = var.ecs_task_execution_role_arn

  depends_on = [data.template_file.pds-validate-containers-json-template]
}
