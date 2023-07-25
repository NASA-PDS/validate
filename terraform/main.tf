 module "ecs" {
   source = "./terraform-modules/ecs"

   pds_validate_ecr_image_path       = var.pds_validate_ecr_image_path
   pds_validate_cloudwatch_logs_group = var.pds_validate_cloudwatch_logs_group
   pds_validate_cloudwatch_logs_region = var.region
   efs_file_system_id                = var.efs_file_system_id
   pds_validate_data_access_point_id = var.pds_validate_data_access_point_id
   ecs_task_role_arn                 = var.ecs_task_role_arn
   ecs_task_execution_role_arn       = var.ecs_task_execution_role_arn
 }
