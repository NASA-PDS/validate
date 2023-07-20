# PDS Validate ECS Task Definition Deployment

The Terraform scripts in this directory deploy an ECS Task Definition for PDS Validate tool and a CloudWatch 
log group for the ECS Task.


## Prerequisites to Deploy Nucleus Baseline System

1. An ECS Cluster should be available

2. The docker image of the PDS Validate tool should be available in ECR 
 - If an ECR image is not available, 
   - Build a docker image using the instructions available in the docker directory for this repository
   - Follow the instructions available as "View Push Commands" at https://us-west-2.console.aws.amazon.com/ecr/repositories/private/991026519024/pds-validate?region=us-west-2 

3. Ability to get AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY and AWS_SESSION_TOKEN for the AWS account

4. Terraform is installed in local environment (This was tested with Terraform v1.3.7. Any higher version should also work)
- Instructions to install Terraform is available at https://developer.hashicorp.com/terraform/tutorials/aws-get-started/install-cli

5. A VPC and one or more subnets should be available on AWS (obtain the VPC ID and subnet IDs from AWS console or from the AWS
   system admin team of your AWS account)

6. An EFS file system with an access point to read and write date


## Steps to Deploy the PDS Nucleus Baseline System

1. Clone the https://github.com/NASA-PDS/validate.git repository.

```shell
git clone https://github.com/NASA-PDS/validate.git
```

2. Open a terminal and change current working directory to the `nucleus/terraform` directory.

```shell
cd validate/terraform
```

3. Set following environment variables in terminal window
    - AWS_ACCESS_KEY_ID
    - AWS_SECRET_ACCESS_KEY
    - AWS_SESSION_TOKEN
    - AWS_DEFAULT_REGION

4. Open the `terraform.tfvars` file locally and enter the value for following variables. Ensure these values match with your AWS Setup. Most of the below values can be obtained by the system admin team of your AWS account.

   - pds_validate_ecr_image_path = "ECR Image path of the PDS Validate docker image"
   - efs_file_system_id = "EFS file system ID"
   - pds_validate_data_access_point_id = "EFS file system - Data access point ID"
   - ecs_task_role_arn = "ECS Task Role ARN"
   - ecs_task_execution_role_arn = "ECS Task Execution Role ARN"

> Note: `terraform.tfvars` is only used to test with your configuration with the actual values in your AWS account. This file will not be uploaded to GitHub as it's ignored by Git. Once testing is completed successfully work with your admin to get the values for these tested variables updated via GitHub secrets, which are dynamically passed in during runtime.

5. Initialize Terraform working directory.

```shell
terraform init
```

6. [Optional] Check the Terraform plan to see the changes to be applied.

```shell
terraform plan
```

7. Deploy Nucleus baseline system using Terraform apply.

```shell
terraform apply
```

8. Login to the AWS Console with your AWS Account.

9. Make sure that the correct AWS Region is selected and search for "Managed Apache Airflow".

10. Visit the "Amazon Elastic Container Service" (Amazon ECS) page and check the list of task definitions (left side).

11. Check if the pds-validate-task-definition is deployed.

12. You can execute this Task in Airflow using the ECSOperator or you can run this task as a standalone task 
by following the instructions available at https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ecs_run_task-v2.html (most of the steps are optional in this Amazon guide).
