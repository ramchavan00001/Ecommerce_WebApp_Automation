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

                publishHTML(target: [
                    reportDir: 'target\\surefire-reports',
                    reportFiles: 'index.html',
                    reportName: 'TestNG Execution Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])

                archiveArtifacts artifacts: '''
                    reports\\ExtentReport.html,
                    target\\surefire-reports\\**
                ''', fingerprint: true
            }
        }
    }

    post {

        always {
            script {
                FAILED_TEST_COUNT =
                    currentBuild.rawBuild
                        .getAction(hudson.tasks.junit.TestResultAction)
                        ?.getFailCount() ?: 0
            }
        }

        success {
            emailext(
                subject: "Ecommerce_Web_Automation TestSuite Result – ${FAILED_TEST_COUNT} Testcase Failed",
                mimeType: 'text/html',
                to: 'ramchavan00001@gmail.com',
                body: """
                    <h2 style="color:green;">Automation Execution Completed Successfully</h2>

                    <p><b>Failed Testcases:</b> ${FAILED_TEST_COUNT}</p>

                    <p><b>Extent Report:</b><br/>
                    <a href='${BUILD_URL}Extent_20Automation_20Report/'>View Extent Report</a></p>

                    <p><b>TestNG Report:</b><br/>
                    <a href='${BUILD_URL}TestNG_20Execution_20Report/'>View TestNG Report</a></p>
                """,
                attachmentsPattern: 'reports\\ExtentReport.html'
            )
        }

        unstable {
            emailext(
                subject: "Ecommerce_Web_Automation TestSuite Result – ${FAILED_TEST_COUNT} Testcase Failed",
                mimeType: 'text/html',
                to: 'ramchavan00001@gmail.com',
                body: """
                    <h2 style="color:orange;">Automation Execution Completed with Failures</h2>

                    <p><b>Failed Testcases:</b> ${FAILED_TEST_COUNT}</p>

                    <p><b>Extent Report (Failures highlighted):</b><br/>
                    <a href='${BUILD_URL}Extent_20Automation_20Report/'>View Extent Report</a></p>

                    <p><b>TestNG Execution Report:</b><br/>
                    <a href='${BUILD_URL}TestNG_20Execution_20Report/'>View TestNG Report</a></p>
                """,
                attachmentsPattern: '''
                    reports\\ExtentReport.html,
                    target\\surefire-reports\\testng-failed.xml
                '''
            )
        }

        failure {
            emailext(
                subject: "Jenkins Build Failed – ${JOB_NAME}",
                mimeType: 'text/html',
                to: 'ramchavan00001@gmail.com',
                body: """
                    <h2 style="color:red;">Jenkins Pipeline Failed</h2>

                    <p>The build failed due to infrastructure or execution issues.</p>

                    <p><b>Job:</b> ${JOB_NAME}</p>
                    <p><b>Build:</b> #${BUILD_NUMBER}</p>

                    <p><b>Console Logs:</b><br/>
                    <a href='${BUILD_URL}console'>View Logs</a></p>
                """
            )
        }
    }
}
