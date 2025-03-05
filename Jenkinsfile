pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'BUILD STARTING'
				sh 'mvn install -DskipTests  -f jhaws/pom.xml'
            }
            post {
                success {
					echo 'BUILD WAS SUCCESFUL'
                }
				failure {
					echo 'BUILD FAILED'
                }
            }
        }
        stage('Test') {
            steps {
                echo 'TEST STARTING'
				sh 'mvn test -f jhaws/pom.xml'
            }
            post {
                success {
					echo 'TEST WAS SUCCESFUL'
                }
				failure {
					echo 'TEST FAILED'
                }
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'DEPLOY STARTING'
            }
            post {
                success {
					echo 'DEPLOY WAS SUCCESFUL'
                }
				failure {
					echo 'DEPLOY FAILED'
                }
            }
        }
    }
	
	post {
		always {
          step([$class: 'Mailer',
            notifyEveryUnstableBuild: true,
            recipients: "test@test.test",
            sendToIndividuals: true])
		}
	}
}