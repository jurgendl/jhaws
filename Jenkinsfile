pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
				sh 'mvn install -DskipTests -f jhaws/pom.xml'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
				sh 'mvn test -f jhaws/pom.xml'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}