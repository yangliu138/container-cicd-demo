# Introduction
This repo aims at demonstrating CI/CD practices for containerized apps into Kubernetes cluster. The app used it self is a simple RESTful API with `/version`, and the image of app is hosted currently at dockerhub named `leoliu1988/springboot-cicd-demo`

The pipeline focuses on the prinple of shift left testing for security.

This repo is written for demo purpose, and there might be some shortcomings or risks. Please refer to  [shortcomings or improvements](#Shortcomings,-Limitation-and-Improvements).

## Pipeline overview
1. Check out source code 
2. Parallel tests: Quality tests, unit tests, and OWASP dependency tests. For dependency tests, it CVSS score is currently set to 8, which can be adjusted later. So once there is a dependency higher than that value, it will fail the project. Like the following message
```bash
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  06:25 min
[INFO] Finished at: 2022-05-19T23:57:44Z
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.owasp:dependency-check-maven:6.5.1:check (default-cli) on project container-cicd-demo: 
[ERROR] 
[ERROR] One or more dependencies were identified with vulnerabilities that have a CVSS score greater than or equal to '8.0': 
[ERROR] 
[ERROR] spring-aop-5.3.19.jar: CVE-2016-1000027
[ERROR] spring-core-5.3.19.jar: CVE-2016-1000027
[ERROR] 
[ERROR] See the dependency-check report for more details.
```
3. Build the source codes into docker image

4. Push the image into the docker hub, where on the master branch, the image tag on the docker hub will be the latest git commit number.

5. Scan the image vulneribiities, currntly use open sourced [anchore grype](https://github.com/anchore/grype). 

6. Authenticate with AWS credentials, and also get eks kubeconfig file to local

7. Use helm tools to deploy to the target cluster.

# Pre-requisites
- Github and Dockerhub accounts
- Running Jenkins server with all recommended plugin installed especially "Credentials binding plugin". On mac recommended to install in `jenkins/jenkins` container.
- Docker, awscli, helm, kubectl (v1.23.6) are installed on the jenkins worker machine. Use the following dockerfile for jenkins with blueocean, and other required tools setup: (Note this dockerfile is not for production use atm.)
```Dockerfile
FROM jenkins/jenkins:2.332.3-jdk11
USER root
RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli 

RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
unzip awscliv2.zip && \
./aws/install && \
aws --version

RUN curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 && \
chmod 700 get_helm.sh && \
./get_helm.sh

RUN curl -LO "https://dl.k8s.io/release/v1.23.6/bin/linux/amd64/kubectl" && \
chmod +x kubectl && \
install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl && \
kubectl version --client

RUN jenkins-plugin-cli --plugins "blueocean:1.25.3 docker-workflow:1.28"
```
- Set up git integration regarding credentials (password or ssh) and url in jenkins, and make the credential_id to "git"
- Add Blue ocean plugin in Jenkins(Optional)
- Label the node running the pipeline as `workers`

# Steps to configure
- Add dockerhub credentials with id `docker` to global credentials
- Add git credentials with id `git` to global credentials. Notice the password should be the `token` from the developer settings on github
- Add aws credentials with id `aws` to global credentials
- Configure Jenkins to create a "Multiple-branch pipeline", and add git repo in that pipeline configuration.
- The repo should automatically recognised the Jenkins file for various branches, and start the build.


# Shortcomings, Limitation and Improvements
- Incomplete unit tests: due to time constraints, the unit tests are not complete, and are only tested around the core functionalities.
This might cause functional issues. You can find the coverage report from `/targets/site/jacoco/index.html`

- More integrate testing should be done after the app has been deployed to the EKS cluster.s
  
- No pipelines for feature brances, so no way quickly test the dev features by skipping all the tests for example. This is skipped for this demo. This also indicates that there is no environment preparation across different stages.

- No deeper satic code analysis, for example using Sonarqube due to time constraint.

- For simplicity, the cluster was designed to have only 2 worker nodes statically with Terraform. But can be extended to have variable number of nodes dynamically. 

- Worker group volume is not encrypted.

- Terraform state is not remotely stored, so there might be chance the project is gone, and no way to manage the whole stack. This has been put as TODO in `backend.tf` in this project.

- Terraform code to deploy EKS cluster on AWS is in the code repo, and used to deploy the test EKS cluster. But the cluster deployment has not yet put in the jenkins. Ideally spinning the cluster pipeline should trigger the app to work by passing the cluster info, and authentication parameters.


# Issues
- `kubectl 1.24.0` won't work with current version of EKS. [Issues](https://github.com/aws/aws-cli/issues/6920). Workaround: downgrade to v1.23.0 for kubectl