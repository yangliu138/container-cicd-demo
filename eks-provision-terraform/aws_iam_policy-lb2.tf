resource "aws_iam_policy" "load-balancer-policy" {
  depends_on  = [null_resource.policy, aws_eks_cluster.this, aws_eks_node_group.this]
  name        = "AWSLoadBalancerControllerIAMPolicy"
  path        = "/"
  description = "AWS LoadBalancer Controller IAM Policy"

  policy = file("iam-policy.json")
  
}