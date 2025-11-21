terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

data "aws_availability_zones" "available" {
  state = "available"
}

data "aws_iam_role" "lab_role" {
  name = "LabRole"
}
resource "aws_vpc" "main_v2" {
  cidr_block           = "10.1.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true
  tags = {
    Name = "springboot-eks-vpc-new"
  }
}

resource "aws_subnet" "public_v2" {
  count                   = 2
  vpc_id                  = aws_vpc.main_v2.id
  cidr_block              = cidrsubnet(aws_vpc.main_v2.cidr_block, 8, count.index + 1)
  availability_zone       = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = true
  tags = {
    Name                                              = "springboot-eks-public-subnet-v2-${count.index + 1}"
    "kubernetes.io/role/elb"                          = "1"
    "kubernetes.io/cluster/springboot-eks-cluster-devops" = "shared"
  }
}

resource "aws_internet_gateway" "main_v2" {
  vpc_id = aws_vpc.main_v2.id
  tags = {
    Name = "springboot-eks-igw-v2"
  }
}

resource "aws_route_table" "public_v2" {
  vpc_id = aws_vpc.main_v2.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main_v2.id
  }
  tags = {
    Name = "springboot-eks-public-rt-v2"
  }
}

resource "aws_route_table_association" "public_v2" {
  count          = 2
  subnet_id      = aws_subnet.public_v2[count.index].id
  route_table_id = aws_route_table.public_v2.id
}

resource "aws_eks_cluster" "springboot_cluster_v2" {
  name     = "springboot-eks-cluster-devops"
  role_arn = data.aws_iam_role.lab_role.arn
  version  = "1.29"

  vpc_config {
    subnet_ids              = aws_subnet.public_v2[*].id
    endpoint_public_access  = true
    endpoint_private_access = false
  }

  tags = {
    Name        = "springboot-eks-v2"
    Environment = "production"
  }
}

resource "aws_eks_node_group" "springboot_nodes_v2" {
  cluster_name    = aws_eks_cluster.springboot_cluster_v2.name
  node_group_name = "springboot-nodes-v2"
  node_role_arn   = data.aws_iam_role.lab_role.arn
  subnet_ids      = aws_subnet.public_v2[*].id

  capacity_type  = "ON_DEMAND"
  instance_types = ["t3.small"]

  scaling_config {
    desired_size = 2
    max_size     = 4
    min_size     = 2
  }

  disk_size = 30

  update_config {
    max_unavailable = 1
  }

  tags = {
    Name        = "springboot-eks-nodes-v2"
    Environment = "production"
  }

  depends_on = [aws_eks_cluster.springboot_cluster_v2]
}

resource "aws_security_group_rule" "nodeport_ingress" {
  type              = "ingress"
  from_port         = 30000
  to_port           = 32767
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_eks_cluster.springboot_cluster_v2.vpc_config[0].cluster_security_group_id
  description       = "Allow NodePort access for LoadBalancer"
}

resource "aws_security_group_rule" "http_ingress" {
  type              = "ingress"
  from_port         = 80
  to_port           = 80
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_eks_cluster.springboot_cluster_v2.vpc_config[0].cluster_security_group_id
  description       = "Allow HTTP"
}

resource "aws_security_group_rule" "https_ingress" {
  type              = "ingress"
  from_port         = 443
  to_port           = 443
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_eks_cluster.springboot_cluster_v2.vpc_config[0].cluster_security_group_id
  description       = "Allow HTTPS"
}

output "cluster_endpoint" {
  description = "EKS cluster endpoint"
  value       = aws_eks_cluster.springboot_cluster_v2.endpoint
}

output "cluster_name" {
  description = "EKS cluster name"
  value       = aws_eks_cluster.springboot_cluster_v2.name
}

output "configure_kubectl" {
  description = "Command to configure kubectl"
  value       = "aws eks update-kubeconfig --region us-east-1 --name springboot-eks-cluster-devops"
}