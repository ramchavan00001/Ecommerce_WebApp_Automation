package ramchavantestautomation.Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static ExtentReports extent;

    public synchronized static ExtentReports createInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport.html";
            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
            reporter.config().setReportName("Ecommerce Automation Test Report");
            reporter.config().setDocumentTitle("Automation Report");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Project", "Ecommerce Automation");
            extent.setSystemInfo("Tester", "Ram Chavan");
        }
        return extent;
    }
}
