variable "aws_region" {
  description = "AWS region"
  default     = "us-east-1"
}

variable "cluster_name" {
  description = "EKS cluster name"
  default     = "my-eks-cluster"
}

variable "node_group_name" {
  description = "EKS worker node group name"
  default     = "my-node-group"
}

variable "node_instance_type" {
  description = "EC2 instance type for nodes"
  default     = "t3.medium"
}

variable "desired_capacity" {
  description = "Desired number of nodes"
  default     = 2
}

variable "lab_role_arn" {
  description = "ARN of the existing LabRole from AWS Academy"
  type        = string
  default     = "arn:aws:iam::457778020562:role/LabRole"
}