pipeline {
    agent any

    triggers {
        cron('H 1 * * *')   // nightly trigger
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
                echo "Running tests on Selenium Grid..."
                bat 'mvn clean test'
            }
        }

        stage('Publish & Zip Reports') {
            steps {

                // Publish TestNG results
                junit 'target\\surefire-reports\\*.xml'

                // Publish Extent report in Jenkins UI
                publishHTML(target: [
                    reportDir: 'reports',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Automation Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])

                // Zip entire reports folder (HTML + screenshots)
                zip zipFile: 'ExtentReport.zip', dir: 'reports'

                // Archive for Jenkins download
                archiveArtifacts artifacts: 'ExtentReport.zip', fingerprint: true
            }
        }
    }

    post {

        success {
            echo '✅ Automation Passed'

            emailext(
                subject: "✅ Automation Passed | ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                    <h2 style="color:green;">Automation Execution Successful</h2>

                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build:</b> #${BUILD_NUMBER}</p>

                    <p>
                        📊 <b>Online Report:</b>
                        <a href='${BUILD_URL}Extent_20Automation_20Report/'>
                            View in Jenkins
                        </a>
                    </p>

                    <p>
                        📦 <b>Attached ZIP:</b><br/>
                        Download <b>ExtentReport.zip</b>, unzip it, and open
                        <b>ExtentReport.html</b> locally for full screenshots and UI.
                    </p>
                """,
                to: 'ramchavan00001@gmail.com',
                attachmentsPattern: 'ExtentReport.zip'
            )
        }

        failure {
            echo '❌ Automation Failed'

            emailext(
                subject: "❌ Automation Failed | ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                    <h2 style="color:red;">Automation Execution Failed</h2>

                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build:</b> #${BUILD_NUMBER}</p>

                    <p>
                        📜 <b>Console Logs:</b>
                        <a href='${BUILD_URL}console'>View Logs</a>
                    </p>

                    <p>
                        📦 <b>Attached ZIP:</b><br/>
                        Download <b>ExtentReport.zip</b>, unzip it, and open
                        <b>ExtentReport.html</b> locally.
                    </p>
                """,
                to: 'ramchavan00001@gmail.com',
                attachmentsPattern: 'ExtentReport.zip'
            )
        }

        always {
            echo '📌 Build completed. Reports archived.'
        }
    }
}
