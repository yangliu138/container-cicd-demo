resource "helm_release" "aws-load-balancer-controller" {
  name       = "aws-load-balancer-controller"
  depends_on=[null_resource.post-policy, aws_eks_cluster.this, aws_eks_node_group.this]

  repository = "https://aws.github.io/eks-charts"
  chart      = "aws-load-balancer-controller"
  namespace = "kube-system"

  set {
    name  = "clusterName"
    value = "${var.project}-cluster"
  }

  set {
    name  = "serviceAccount.name"
    value = "aws-load-balancer-controller"
  }

  set {
    name  = "image.repository"
    value = format("602401143452.dkr.ecr.%s.amazonaws.com/amazon/aws-load-balancer-controller",var.region)
  }

  set {
    name  = "image.tag"
    value = "v2.4.0"
  }

}

