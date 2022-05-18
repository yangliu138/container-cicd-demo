# Introduction
This repo aims at demonstrating CI/CD practices for containerized apps into Kubernetes cluster.
For the demo purpose, this repo identities some [shortcomings or improvements](# Shortcomings/Improvements) 
- The springboot code is not following the best practices for Spring Boot applications structure, not for security.

# Pre-requisites
- Docker is installed on the jenkins worker/master machine
- 


# Shortcomings/Improvements
- Incomplete unit tests: due to time constraints, the unit tests are not complete, and are only tested around the core functionalities.
This might cause functional issues. You can find the coverage report from `/targets/site/jacoco/index.html`
  
- No pipelines for feature/dev brances, so no way quickly test the dev features by skipping all the tests for example.