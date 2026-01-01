package ramchavantestautomation.Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static ExtentReports extent = ExtentManager.getInstance();

    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Parent = Browser level
    public static synchronized void startParentTest(String name) {
        parentTest.set(extent.createTest(name));
    }

    // Child = Test method
    public static synchronized void startTest(String name) {
        test.set(parentTest.get().createNode(name));
    }

    public static synchronized ExtentTest getTest() {
        return test.get();
    }

    // Flush ONCE per suite
    public static synchronized void flush() {
        extent.flush();
    }
}
