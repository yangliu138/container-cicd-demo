def imageName = 'leoliu1988/springboot-cicd-demo'
def registry = 'https://hub.docker.com'

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
            }
        )
    }

    stage('Build'){
        docker.build(imageName)
    }

    stage('Push'){
        docker.withRegistry(registry, 'git') {
            if (env.BRANCH_NAME == 'develop') {
                docker.image(imageName).push('develop')
            } else {
                docker.image(imageName).push(commitID())
            }
        }
    }

    // stage('Analyze'){
    //     def scannedImage = "${registry}/${imageName}:${commitID()} ${workspace}/Dockerfile"
    //     writeFile file: 'images', text: scannedImage
    //     anchore name: 'images'
    // }
}

def commitID() {
    sh 'git rev-parse HEAD > .git/commitID'
    def commitID = readFile('.git/commitID').trim()
    sh 'rm .git/commitID'
    commitID
}