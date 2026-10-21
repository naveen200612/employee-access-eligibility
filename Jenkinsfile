
pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Environment') {
            steps {
                bat '''
                    echo Checking Java...
                    java -version
                    if errorlevel 1 exit /b 1

                    echo Checking Maven...
                    where mvn
                    if errorlevel 1 (
                        echo ERROR: Maven is missing from PATH.
                        exit /b 1
                    )

                    mvn -version
                    if errorlevel 1 exit /b 1
                '''
            }
        }

        stage('Compile and Test') {
            steps {
                bat 'mvn clean test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn -DskipTests package'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts(
                    artifacts: 'target/*.jar',
                    fingerprint: true
                )
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: Employee Access Eligibility build completed.'
        }

        failure {
            echo 'FAILURE: Check Jenkins Console Output.'
        }

        always {
            echo 'Pipeline execution finished.'
        }
    }
}
