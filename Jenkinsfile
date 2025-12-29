pipeline {
    agent any

    // Schedule nightly run at ~1 AM
    triggers {
        cron('H 1 * * *')
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-credentials',
                    url: 'https://github.com/ramchavan00001/Ecommerce_WebApp_Automation.git'
            }
        }

        stage('Build & Test on Docker Grid') {
            steps {
                echo "Running tests on Docker Selenium Grid in parallel..."
                bat 'mvn clean test'
            }
        }

        stage('Publish Reports') {
            steps {
                // Publish TestNG / Surefire XML reports
                junit 'target\\surefire-reports\\*.xml'

                // Publish Extent HTML report
                publishHTML(target: [
                    reportDir: 'target',
                    reportFiles: 'index.html',
                    reportName: 'Automation Execution Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])

                // Archive all artifacts (screenshots, reports, logs)
                archiveArtifacts artifacts: 'target\\**', fingerprint: true
            }
        }
    }

    post {
        always {
            echo 'Build finished. Reports generated and archived.'
        }

        success {
            echo '✅ Nightly Regression Executed Successfully'

            // Send success email
            emailext(
                subject: "✅ Automation Passed | ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                    <h2 style="color:green;">Nightly Automation Execution Successful</h2>
                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build Number:</b> #${BUILD_NUMBER}</p>
                    <p><b>View HTML Report:</b> <a href='${BUILD_URL}HTML_20Report/'>Click Here</a></p>
                """,
                to: 'ramchavan00001@gmail.com',
                attachmentsPattern: 'target\\**\\*.html,target\\**\\*.pdf'
            )
        }

        failure {
            echo '❌ Nightly Regression Failed - Check Logs'

            // Send failure email
            emailext(
                subject: "❌ Automation Failed | ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                    <h2 style="color:red;">Nightly Automation Execution Failed</h2>
                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build Number:</b> #${BUILD_NUMBER}</p>
                    <p><b>Check Console Logs:</b> <a href='${BUILD_URL}console'>Click Here</a></p>
                    <p><b>View HTML Report:</b> <a href='${BUILD_URL}HTML_20Report/'>Click Here</a></p>
                """,
                to: 'ramchavan00001@gmail.com',
                attachmentsPattern: 'target\\**\\*.html,target\\**\\*.pdf'
            )
        }
    }
}
