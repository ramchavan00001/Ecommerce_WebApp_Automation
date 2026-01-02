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

                // ✅ Publish Extent Report
                publishHTML(target: [
                    reportDir: 'reports',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Automation Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])

                // ✅ Publish TestNG HTML Report
                publishHTML(target: [
                    reportDir: 'target\\surefire-reports',
                    reportFiles: 'index.html',
                    reportName: 'TestNG Execution Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])

                archiveArtifacts artifacts: 'reports\\ExtentReport.html, target\\surefire-reports\\**', fingerprint: true
            }
        }
    }

    post {

        always {
            echo 'Build finished. Reports generated.'
        }

        success {
            echo '✅ Nightly Regression Executed Successfully'

            emailext(
                subject: "✅ Automation Passed | ${JOB_NAME} #${BUILD_NUMBER}",
                mimeType: 'text/html',
                to: 'ramchavan00001@gmail.com',

                body: """
                    <h2 style="color:green;">Nightly Automation Execution Successful</h2>

                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build Number:</b> #${BUILD_NUMBER}</p>

                    <hr/>

                    <p><b>📊 Extent Report (Jenkins Link):</b><br/>
                    <a href='${BUILD_URL}Extent_20Automation_20Report/'>
                        View Extent Report
                    </a></p>

                    <p><b>🧪 TestNG Execution Report:</b><br/>
                    <a href='${BUILD_URL}TestNG_20Execution_20Report/'>
                        View TestNG Report
                    </a></p>

                    <hr/>
                    <p>📎 ExtentReport.html is attached for quick reference.</p>
                """,

                // ✅ ATTACH EXTENT HTML
                attachmentsPattern: 'reports\\ExtentReport.html'
            )
        }

        failure {
            echo '❌ Nightly Regression Failed'

            emailext(
                subject: "❌ Automation Failed | ${JOB_NAME} #${BUILD_NUMBER}",
                mimeType: 'text/html',
                to: 'ramchavan00001@gmail.com',

                body: """
                    <h2 style="color:red;">Nightly Automation Execution Failed</h2>

                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build Number:</b> #${BUILD_NUMBER}</p>

                    <p><b>Console Logs:</b><br/>
                    <a href='${BUILD_URL}console'>View Console Logs</a></p>

                    <hr/>

                    <p><b>📊 Extent Report:</b><br/>
                    <a href='${BUILD_URL}Extent_20Automation_20Report/'>
                        View Extent Report
                    </a></p>

                    <p><b>🧪 TestNG Execution Report:</b><br/>
                    <a href='${BUILD_URL}TestNG_20Execution_20Report/'>
                        View TestNG Report
                    </a></p>

                    <hr/>
                    <p>📎 ExtentReport.html is attached.</p>
                """,

                // ✅ ATTACH EXTENT HTML EVEN ON FAILURE
                attachmentsPattern: 'reports\\ExtentReport.html'
            )
        }
    }
}
