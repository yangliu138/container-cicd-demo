data "aws_eks_cluster" "eks_cluster" {
  depends_on=[aws_eks_cluster.this, aws_eks_node_group.this]
  name = "${var.project}-cluster"
}

