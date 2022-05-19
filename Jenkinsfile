def imageName = 'leoliu1988/springboot-cicd-demo'
def registry = 'https://hub.docker.com'
def region = 'us-east-2'
def accounts = [master:'springboot-cicd-demo-cluster', preprod:'staging', develop:'springboot-cicd-demo-cluster']

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
        def scannedImage = "${imageName}:${commitID()}"
        if (env.BRANCH_NAME == 'develop') {
            scannedImage = "${imageName}:develop"
        }
        echo "${scannedImage} ******"
        sh "docker scan --accept-license ${scannedImage}"
    }
}

def commitID() {
    sh 'git rev-parse HEAD > .git/commitID'
    def commitID = readFile('.git/commitID').trim()
    sh 'rm .git/commitID'
    commitID
}