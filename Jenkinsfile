def imageName = 'leoliu1988/springboot-cicd-demo'
def registry = 'https://hub.docker.com'
def region = 'us-east-2'
def accounts = [master:'springboot-cicd-demo-cluster', stage:'springboot-cicd-demo-cluster-stage', develop:'springboot-cicd-demo-cluster']

node('workers'){
    stage('Checkout'){
        checkout scm
    }

    def imageTest= docker.build("${imageName}-test", "-f Dockerfile.test .")

    stage('Tests'){
        parallel(
            'Quality Tests': {
                sh "docker run --rm ${imageName}-test ./mvnw checkstyle:checkstyle"
                // TODO: add sonartube for deeper static code analysis, e.g. vulnerability scanning
            },
            'Unit and Integration Tests for APIs': {
                sh "docker run --rm ${imageName}-test ./mvnw clean test"
            },
            'Coverage Reports': {
                sh "docker run --rm -v $PWD/target/site:/workspace/app/target/site ${imageName}-test ./mvnw jacoco:report"
                publishHTML (target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: false,
                    keepAll: true,
                    reportDir: "$PWD/target/site/jacoco",
                    reportFiles: "index.html",
                    reportName: "Coverage Report"
                ])
            },
            'Dependence Tests': {
                 sh "docker run --rm ${imageName}-test ./mvnw org.owasp:dependency-check-maven:check"
            }
        )
    }

    stage('Build'){
        docker.build(imageName)
    }


    stage('Push'){
        docker.withRegistry('', 'docker') {
            if (env.BRANCH_NAME == 'develop') {
                docker.image(imageName).push('develop')
            } else {
                docker.image(imageName).push(commitID())
            }
        }
    }

    stage('Scan image vulneribilities'){
        // docker.withRegistry('', 'docker') {
        //     def scannedImage = "${imageName}:${commitID()}"
        //     if (env.BRANCH_NAME == 'develop') {
        //         scannedImage = "${imageName}:develop"
        //     }
        //     echo "${scannedImage} ******"
        //     sh "docker scan --accept-license ${scannedImage}"   
        // }
        def scannedImage = "${imageName}:${commitID()}"
        if (env.BRANCH_NAME == 'develop') {
            scannedImage = "${imageName}:develop"
        }
        echo "${scannedImage} ******"
        sh "docker run --rm ${imageName}-test grype ${scannedImage}"
    }

    stage('Authentication'){
         withCredentials([usernamePassword(credentialsId: 'aws', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
             sh """
                export AWS_ACCESS_KEY_ID=${USERNAME}
                export AWS_SECRET_ACCESS_KEY=${PASSWORD}
                export AWS_DEFAULT_REGION=us-east-2
                aws eks update-kubeconfig --name ${accounts[env.BRANCH_NAME]} --region ${region}
             """
             
         } 
    }

    stage('Deploy') {
        def imageTag = getCommitId()
        if (env.BRANCH_NAME == 'develop') {
            imageTag = env.BRANCH_NAME
        }
        sh """
            helm upgrade --install cicd-demo ./springboot-cicd-demo-cluster \
                --set metadata.jenkins.buildTag=${env.BUILD_TAG} \
                --set metadata.git.commitId=${getCommitId()} \
                --set deployment.image.name="${imageName}:${imageTag}" \
                --set namespace="${env.BRANCH_NAME}"
        """
    }
}

def commitID() {
    sh 'git rev-parse HEAD > .git/commitID'
    def commitID = readFile('.git/commitID').trim()
    sh 'rm .git/commitID'
    commitID
}