package ramchavantestautomation.Base;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import ramchavantestautomation.Utils.ExtentTestManager;
import ramchavantestautomation.Utils.Log;
import ramchavantestautomation.WebActions.WebAction;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        Log.info(">>> Test suite started: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTestManager.startTest(result.getMethod().getMethodName());
        Log.info("Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String screenshotPath = WebAction.captureScreenshot(result.getMethod().getMethodName() + "_PASS");
        if (screenshotPath != null) {
            ExtentTestManager.getTest()
                    .log(Status.PASS, "Test Passed ✅")
                    .pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } else {
            ExtentTestManager.getTest().log(Status.PASS, "Test Passed ✅ (no screenshot)");
        }
        Log.info("Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String screenshotPath = WebAction.captureScreenshot(result.getMethod().getMethodName() + "_FAIL");

        if (screenshotPath != null) {
            ExtentTestManager.getTest()
                    .log(Status.FAIL, "Test Failed ❌: " + result.getThrowable())
                    .fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } else {
            ExtentTestManager.getTest()
                    .log(Status.FAIL, "Test Failed ❌: " + result.getThrowable());
        }

        Log.error("Test failed: " + result.getMethod().getMethodName() + " - " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String screenshotPath = WebAction.captureScreenshot(result.getMethod().getMethodName() + "_SKIP");

        if (screenshotPath != null) {
            ExtentTestManager.getTest()
                    .log(Status.SKIP, "Test Skipped ⚠️")
                    .skip(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } else {
            ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped ⚠️");
        }

        Log.warn("Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentTestManager.endTest(); // flush report once
        Log.info("<<< Test suite finished: " + context.getName());
    }
}
