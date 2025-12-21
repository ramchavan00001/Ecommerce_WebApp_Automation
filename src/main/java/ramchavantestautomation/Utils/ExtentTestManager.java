package ramchavantestautomation.Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {
    private static ExtentReports extent = ExtentManager.createInstance();

    // ThreadLocal for parent (browser) test
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<>();
    // ThreadLocal for individual method
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Start parent test for browser
    public static synchronized void startParentTest(String testName) {
        ExtentTest parent = extent.createTest(testName);
        parentTest.set(parent);
    }

    // Start child test (actual method)
    public static synchronized void startTest(String testName) {
        ExtentTest child = parentTest.get().createNode(testName);
        test.set(child);
    }

    public static synchronized ExtentTest getTest() {
        return test.get();
    }

    public static synchronized void endTest() {
        extent.flush();
    }
}
