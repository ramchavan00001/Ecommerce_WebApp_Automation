pipeline {
    agent any

   

    triggers {
        cron('H 1 * * *')   // Runs every night ~1 AM
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-credentials',
                    url: 'https://github.com/ramchavan00001/Ecommerce_WebApp_Automation.git'
            }
        }

        stage('Clean & Compile') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Run TestNG Suite') {
            steps {
                bat 'mvn test'
            }
        }
    }

    post {
        always {
            junit 'target\\surefire-reports\\*.xml'
            archiveArtifacts artifacts: 'target\\**', fingerprint: true
        }

        success {
            echo '✅ Nightly Regression Executed Successfully'
        }

        failure {
            echo '❌ Nightly Regression Failed - Check Logs'
        }
    }
}
