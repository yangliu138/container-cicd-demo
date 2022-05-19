terraform {
  required_version = ">= 1.1.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }

    helm = {
      source = "hashicorp/helm"
      version = ">= 2.5.1"
    }
  }
}

provider "aws" {
  region = var.region

  access_key = var.aws_access_key
  secret_key = var.aws_secret_key

  # other options for authentication
}

provider "helm" {
  kubernetes {
    config_path = "~/.kube/config"
  }
}
