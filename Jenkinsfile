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

                // ✅ Publish browser-viewable report
                publishHTML(target: [
                    reportDir: 'reports',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Automation Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])

                // ✅ Create ZIP for clean local viewing
                bat '''
                if exist ExtentReport.zip del ExtentReport.zip
                powershell Compress-Archive -Path reports\\* -DestinationPath ExtentReport.zip
                '''

                // ✅ Archive everything
                archiveArtifacts artifacts: 'ExtentReport.zip, reports\\**, target\\**', fingerprint: true
            }
        }
    }

    post {
        success {
            emailext(
                subject: "✅ Automation Passed | ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                    <h2 style="color:green;">Nightly Automation Execution Successful</h2>
                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build Number:</b> #${BUILD_NUMBER}</p>

                    <p><b>Online Report (Jenkins):</b>
                        <a href='${BUILD_URL}Extent_20Automation_20Report/'>View Report</a>
                    </p>

                    <p><b>Clean Report:</b>
                        Download <b>ExtentReport.zip</b>, unzip, open <b>ExtentReport.html</b>
                    </p>
                """,
                to: 'ramchavan00001@gmail.com',
                attachmentsPattern: 'ExtentReport.zip'
            )
        }

        failure {
            emailext(
                subject: "❌ Automation Failed | ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                    <h2 style="color:red;">Automation Execution Failed</h2>
                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build Number:</b> #${BUILD_NUMBER}</p>

                    <p><b>Console Logs:</b>
                        <a href='${BUILD_URL}console'>View Logs</a>
                    </p>

                    <p><b>Online Report:</b>
                        <a href='${BUILD_URL}Extent_20Automation_20Report/'>View Report</a>
                    </p>

                    <p><b>Clean Report:</b>
                        Download attached ZIP
                    </p>
                """,
                to: 'ramchavan00001@gmail.com',
                attachmentsPattern: 'ExtentReport.zip'
            )
        }
    }
}
