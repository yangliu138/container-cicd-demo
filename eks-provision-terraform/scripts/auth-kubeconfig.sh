#!/bin/bash
aws eks --region us-east-2 update-kubeconfig --name $(terraform output -raw cluster_name)