// https://www.jenkins.io/doc/book/pipeline/jenkinsfile/
// https://www.jenkins.io/doc/book/pipeline/syntax/
// https://www.jenkins.io/doc/book/pipeline/jenkinsfile/#using-environment-variables
// https://www.jenkins.io/doc/pipeline/examples/#ansi-color-build-wrapper
// localhost/pipeline-syntax/globals

// setting outside parameters
// pipeline, configure, This project is parameterized
// curl -X POST JENKINS_URL/job/YOUR_JOB/buildWithParameters --data-urlencode "EMAIL=test@hotmail.com"


pipeline {
	agent any
    environment { 
        TESTVAR = 'testvar'
    }
    parameters {
        string(name: 'TESTPARAMETER', defaultValue: 'testparameter', description: 'description')
    }
	options {
		ansiColor('xterm') // Enables color output globally
	}
	stages {
		stage('Pre') {
			steps {
				echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
				echo "${env.TESTVAR} ${params.TESTPARAMETER} ${params.EMAIL}"
				sh 'printenv'
			}
		}
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
			when {
				expression {
					currentBuild.result == null || currentBuild.result == 'SUCCESS' 
				}
			}
			steps {
				echo 'DEPLOY STARTING'
			}
			post {
				success {
					echo 'DEPLOY WAS SUCCESFUL'
					//step([$class: 'Mailer',
					//	notifyEveryUnstableBuild: true,
					//	recipients: "${params.EMAIL}",
					//	sendToIndividuals: true])
					//emailext body: 'BUILD COMPLETED', subject: 'BUILD COMPLETED emailext', to: '${params.EMAIL}'
					mail to: ${params.EMAIL}, subject: 'BUILD COMPLETED mail'
				}
				failure {
					echo 'DEPLOY FAILED'
				}
			}
		}
	}
}