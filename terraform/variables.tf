variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "iam_role" {
  description = "Name of the IAM role for EKS"
  type        = string
  default     = "LabRole"
}
variable "cluster_name" {
  description = "Name of the EKS cluster"
  type        = string
  default     = "springboot-eks-cluster"
}

variable "node_instance_type" {
  description = "Instance type for EKS nodes"
  type        = string
  default     = "t3.small"
}

variable "desired_nodes" {
  description = "Desired number of worker nodes"
  type        = number
  default     = 2
}

variable "max_nodes" {
  description = "Maximum number of worker nodes"
  type        = number
  default     = 4
}

variable "min_nodes" {
  description = "Minimum number of worker nodes"
  type        = number
  default     = 2
}