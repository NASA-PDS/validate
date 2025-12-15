terraform {
  required_version = ">= 0.14.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.26.0"
    }
  }
}

provider "aws" {
  region = var.region
}
