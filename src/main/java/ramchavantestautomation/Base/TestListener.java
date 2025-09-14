package ramchavantestautomation.Base;

import com.aventstack.extentreports.Status;
import ramchavantestautomation.Utils.ExtentTestManager;
import ramchavantestautomation.Utils.Log;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        Log.info("Test suite started: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTestManager.startTest(result.getMethod().getMethodName());
        Log.info("Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test Passed");
        Log.info("Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTestManager.getTest().log(Status.FAIL, "Test Failed: " + result.getThrowable());
        Log.error("Test failed: " + result.getMethod().getMethodName() + " - " + result.getThrowable());
        // Optionally add screenshot capture here
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
        Log.warn("Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentTestManager.endTest();
        Log.info("Test suite finished: " + context.getName());
    }
}
