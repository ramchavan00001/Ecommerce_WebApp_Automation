package ramchavantestautomation.Utils;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {
    private static ExtentReports extent = ExtentManager.createInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static synchronized void startTest(String testName) {
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }

    public static synchronized ExtentTest getTest() {
        return test.get();
    }

    public static synchronized void endTest() {
        extent.flush();
    }
}

