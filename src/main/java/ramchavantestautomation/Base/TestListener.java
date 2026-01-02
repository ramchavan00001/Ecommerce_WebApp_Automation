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
    public void onTestStart(ITestResult result) {
        ExtentTestManager.startTest(result.getMethod().getMethodName());
        Log.info("Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        // 🔴 Screenshot capture on SUCCESS is intentionally disabled
        // String screenshotPath =
        //         WebAction.captureScreenshot(result.getMethod().getMethodName() + "_PASS");

        // if (screenshotPath != null) {
        //     ExtentTestManager.getTest()
        //             .log(Status.PASS, "Test Passed")
        //             .pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        // } else {
        //     ExtentTestManager.getTest().log(Status.PASS, "Test Passed");
        // }

        // ✅ Only log PASS status (no screenshot)
        ExtentTestManager.getTest().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        String screenshotPath =
                WebAction.captureScreenshot(result.getMethod().getMethodName() + "_FAIL");

        if (screenshotPath != null) {
            ExtentTestManager.getTest()
                    .log(Status.FAIL, result.getThrowable())
                    .fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } else {
            ExtentTestManager.getTest()
                    .log(Status.FAIL, result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest()
                .log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentTestManager.flush();
        Log.info("Test suite finished");
    }
}
