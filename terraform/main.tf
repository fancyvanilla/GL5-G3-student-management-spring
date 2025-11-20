# -----------------------
# 1️⃣ VPC
# -----------------------
resource "aws_vpc" "eks_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true
  tags = { Name = "eks-vpc" }
}

# -----------------------
# 2️⃣ Subnets PUBLICS (pour NAT Gateway & Load Balancers)
# -----------------------
resource "aws_subnet" "public_subnet_a" {
  vpc_id                  = aws_vpc.eks_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.aws_region}a"
  map_public_ip_on_launch = true
  tags = {
    Name                                        = "eks-public-subnet-a"
    "kubernetes.io/role/elb"                    = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
  }
}

resource "aws_subnet" "public_subnet_b" {
  vpc_id                  = aws_vpc.eks_vpc.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "${var.aws_region}b"
  map_public_ip_on_launch = true
  tags = {
    Name                                        = "eks-public-subnet-b"
    "kubernetes.io/role/elb"                    = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
  }
}

# -----------------------
# 3️⃣ Subnets PRIVÉS (pour les worker nodes)
# -----------------------
resource "aws_subnet" "private_subnet_a" {
  vpc_id            = aws_vpc.eks_vpc.id
  cidr_block        = "10.0.10.0/24"
  availability_zone = "${var.aws_region}a"
  tags = {
    Name                                        = "eks-private-subnet-a"
    "kubernetes.io/role/internal-elb"           = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
  }
}

resource "aws_subnet" "private_subnet_b" {
  vpc_id            = aws_vpc.eks_vpc.id
  cidr_block        = "10.0.11.0/24"
  availability_zone = "${var.aws_region}b"
  tags = {
    Name                                        = "eks-private-subnet-b"
    "kubernetes.io/role/internal-elb"           = "1"
    "kubernetes.io/cluster/${var.cluster_name}" = "shared"
  }
}

# -----------------------
# 4️⃣ Internet Gateway (pour subnets publics)
# -----------------------
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.eks_vpc.id
  tags   = { Name = "eks-igw" }
}

# -----------------------
# 5️⃣ Elastic IP pour NAT Gateway
# -----------------------
resource "aws_eip" "nat_eip" {
  domain = "vpc"
  tags   = { Name = "eks-nat-eip" }
  depends_on = [aws_internet_gateway.igw]
}

# -----------------------
# 6️⃣ NAT Gateway (dans subnet public)
# -----------------------
resource "aws_nat_gateway" "nat" {
  allocation_id = aws_eip.nat_eip.id
  subnet_id     = aws_subnet.public_subnet_a.id
  tags          = { Name = "eks-nat-gateway" }
  depends_on    = [aws_internet_gateway.igw]
}

# -----------------------
# 7️⃣ Route Table PUBLIC
# -----------------------
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.eks_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
  tags = { Name = "eks-public-rt" }
}

resource "aws_route_table_association" "public_a" {
  subnet_id      = aws_subnet.public_subnet_a.id
  route_table_id = aws_route_table.public.id
}

resource "aws_route_table_association" "public_b" {
  subnet_id      = aws_subnet.public_subnet_b.id
  route_table_id = aws_route_table.public.id
}

# -----------------------
# 8️⃣ Route Table PRIVÉE
# -----------------------
resource "aws_route_table" "private" {
  vpc_id = aws_vpc.eks_vpc.id
  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat.id
  }
  tags = { Name = "eks-private-rt" }
}

resource "aws_route_table_association" "private_a" {
  subnet_id      = aws_subnet.private_subnet_a.id
  route_table_id = aws_route_table.private.id
}

resource "aws_route_table_association" "private_b" {
  subnet_id      = aws_subnet.private_subnet_b.id
  route_table_id = aws_route_table.private.id
}

# -----------------------
# 9️⃣ Security Group
# -----------------------
resource "aws_security_group" "eks_sg" {
  name        = "eks-node-sg"
  description = "Security group for EKS nodes"
  vpc_id      = aws_vpc.eks_vpc.id

  ingress {
    description = "Allow traffic within VPC"
    from_port   = 0
    to_port     = 65535
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    description = "Allow all outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = { Name = "eks-node-sg" }
}

# -----------------------
# 🔟 EKS Cluster
# -----------------------
resource "aws_eks_cluster" "main" {
  name     = var.cluster_name
  role_arn = var.lab_role_arn
  version  = "1.29"

  vpc_config {
    # Utiliser TOUS les subnets (publics + privés)
    subnet_ids = [
      aws_subnet.public_subnet_a.id,
      aws_subnet.public_subnet_b.id,
      aws_subnet.private_subnet_a.id,
      aws_subnet.private_subnet_b.id
    ]
    endpoint_public_access  = true
    endpoint_private_access = true
    security_group_ids      = [aws_security_group.eks_sg.id]
  }

  tags = {
    Environment = "dev"
    Terraform   = "true"
  }
}

# -----------------------
# 1️⃣1️⃣ EKS Node Group (dans subnets PRIVÉS uniquement)
# -----------------------
resource "aws_eks_node_group" "main" {
  cluster_name    = aws_eks_cluster.main.name
  node_group_name = var.node_group_name
  node_role_arn   = var.lab_role_arn
  
  # ⚠️ WORKER NODES dans subnets PRIVÉS uniquement
  subnet_ids = [
    aws_subnet.private_subnet_a.id,
    aws_subnet.private_subnet_b.id
  ]

  scaling_config {
    desired_size = var.desired_capacity
    max_size     = 3
    min_size     = 1
  }

  instance_types = [var.node_instance_type]

  tags = {
    Environment = "dev"
    Terraform   = "true"
  }

  depends_on = [
    aws_eks_cluster.main,
    aws_nat_gateway.nat
  ]
}