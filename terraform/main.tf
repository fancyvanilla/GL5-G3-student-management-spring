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

resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true
  tags = {
    Name = "springboot-eks-vpc"
  }
}

resource "aws_subnet" "public" {
  count = 2
  vpc_id                  = aws_vpc.main.id
  cidr_block              = cidrsubnet(aws_vpc.main.cidr_block, 8, count.index + 1)
  availability_zone       = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = true
  tags = {
    Name = "springboot-eks-public-subnet-${count.index + 1}"
    "kubernetes.io/role/elb" = "1"
  }
}

resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id
  tags = {
    Name = "springboot-eks-igw"
  }
}

resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }
  tags = {
    Name = "springboot-eks-public-rt"
  }
}

resource "aws_route_table_association" "public" {
  count = 2
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

resource "aws_eks_cluster" "springboot_cluster" {
  name     = "springboot-eks-cluster"
  role_arn = data.aws_iam_role.lab_role.arn
  version  = "1.29"
  vpc_config {
    subnet_ids = aws_subnet.public[*].id
    endpoint_public_access  = true
    endpoint_private_access = false
  }
  enabled_cluster_log_types = ["api", "audit"]
  tags = {
    Name        = "springboot-eks"
    Environment = "production"
  }
}

resource "aws_eks_node_group" "springboot_nodes" {
  cluster_name    = aws_eks_cluster.springboot_cluster.name
  node_group_name = "springboot-nodes"
  node_role_arn   = data.aws_iam_role.lab_role.arn
  subnet_ids      = aws_subnet.public[*].id
  capacity_type = "ON_DEMAND"
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
    Name        = "springboot-eks-nodes"
    Environment = "production"
  }
}

resource "aws_security_group" "eks_cluster" {
  name        = "springboot-eks-cluster-sg"
  description = "Security group for Spring Boot EKS cluster"
  vpc_id      = aws_vpc.main.id
  ingress {
    description = "HTTPS"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    description = "Spring Boot App"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "springboot-eks-cluster-sg"
  }
}