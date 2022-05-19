# Introduction
This repo aims at demonstrating CI/CD practices for containerized apps into Kubernetes cluster.
For the demo purpose, this repo identities some [shortcomings or improvements](#Shortcomings,-Limitation-and-Improvements) 
- The springboot code is not following the best practices for Spring Boot applications structure, not for security.

# Pre-requisites
- Running Jenkins server. On mac recommended to install in `jenkins/jenkins` container.
- Docker, awscli, helm, kubectl (v1.23.6) are installed on the jenkins worker/master machine (This bits could be automated but for simplicity they are placed as)
- Set up git integration regarding credentials (password or ssh) and url in jenkins, and make the credential_id to "git"
- Add docker registry credentials
- Add docker plugin into Jenkins
- Add Blue ocean plugin in Jenkins(Optional)
- Label the node running the pipeline as `workers`


# Shortcomings, Limitation and Improvements
- Incomplete unit tests: due to time constraints, the unit tests are not complete, and are only tested around the core functionalities.
This might cause functional issues. You can find the coverage report from `/targets/site/jacoco/index.html`
  
- No pipelines for feature brances, so no way quickly test the dev features by skipping all the tests for example. This is skipped for this demo. This also indicates that there is no environment preparation across different stages.

- For simplicity, the cluster was designed to have only 2 worker nodes statically with Terraform. But can be extended to have variable number of nodes dynamically. 

- Worker group volume is not encrypted.

- `kubectl 1.24.0` won't work with current version of EKS. [Issues](https://github.com/aws/aws-cli/issues/6920). Workaround: downgrade to v1.23.0 for kubectl

- Terraform state is not remotely stored, so there might be chance the project is gone, and no way to manage the whole stack. This has been put as TODO in `backend.tf` in this project.