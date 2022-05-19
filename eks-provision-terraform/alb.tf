# TODO: create alb automatically
# resource "helm_release" "alb_ingress" {
#   name       = "aws-alb-ingress-controller"

#   repository = "http://storage.googleapis.com/kubernetes-charts-incubator"
#   chart      = "incubator/aws-alb-ingress-controller"

#   set {
#     autoDiscoverAwsRegion = "true"
#     autoDiscoverAwsVpcID = "true"
#     clusterName = "${var.project}-cluster"
#   }

#   depends_on = [aws_eks_cluster.this, aws_eks_node_group.this]
# }