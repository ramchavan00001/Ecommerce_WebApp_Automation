pipeline {
    agent any

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
                junit 'target\\surefire-reports\\*.xml'

                publishHTML(target: [
                    reportDir: 'reports',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Automation Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])

                archiveArtifacts artifacts: 'reports\\**, target\\**', fingerprint: true
            }
        }
    }

    post {
        always {
            echo 'Build finished. Reports generated and archived.'
        }

        success {
            echo '✅ Nightly Regression Executed Successfully'

            emailext(
                subject: "✅ Automation Passed | ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                    <h2 style="color:green;">Nightly Automation Execution Successful</h2>
                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build Number:</b> #${BUILD_NUMBER}</p>
                    <p><b>Extent Report:</b>
                        <a href='${BUILD_URL}Extent_20Automation_20Report/'>View Report</a>
                    </p>
                """,
                to: 'ramchavan00001@gmail.com',
                attachmentsPattern: 'reports\\ExtentReport.html'
            )
        }

        failure {
            echo '❌ Nightly Regression Failed - Check Logs'

            emailext(
                subject: "❌ Automation Failed | ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                    <h2 style="color:red;">Nightly Automation Execution Failed</h2>
                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build Number:</b> #${BUILD_NUMBER}</p>
                    <p><b>Console Logs:</b>
                        <a href='${BUILD_URL}console'>View Logs</a>
                    </p>
                    <p><b>Extent Report:</b>
                        <a href='${BUILD_URL}Extent_20Automation_20Report/'>View Report</a>
                    </p>
                """,
                to: 'ramchavan00001@gmail.com',
                attachmentsPattern: 'reports\\ExtentReport.html'
            )
        }
    }
}
