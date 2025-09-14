package ramchavantestautomation.Tests;

import ramchavantestautomation.Base.DriverFactory;
import ramchavantestautomation.Pages.HomePage;
import ramchavantestautomation.Pages.LoginPage;
import ramchavantestautomation.Utils.ConfigReader;
import ramchavantestautomation.Utils.Log;
import ramchavantestautomation.WebActions.WebAction;
import ramchavantestautomation.Utils.ExtentManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;

@Listeners(ramchavantestautomation.Base.TestListener.class)
public class BaseTest {

    protected static WebDriver driver;
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();
    protected WebAction action;

    @BeforeSuite(alwaysRun = true)
    @Parameters("browser")
    public void setUpSuite(@Optional("") String browserFromXml) {
    	
    	//clean or deletes the older folders
    	cleanScreenshotsFolder();
    	
        // 🔹 ExtentReports setup
        extent = ExtentManager.createInstance();

        // 🔹 Driver setup
        String browser = browserFromXml.isEmpty() ? ConfigReader.get("browser") : browserFromXml;
        DriverFactory.initDriver(browser);
        driver = DriverFactory.getDriver();
        driver.manage().window().maximize();

        int wait = ConfigReader.getInt("implicit.wait", 1);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

        driver.get(ConfigReader.get("base.url"));
        Log.info("Browser launched (" + browser + ") & navigated to: " + ConfigReader.get("base.url"));
        
        action = new WebAction(driver); // ✅ initialize WebAction
    }

    @BeforeMethod(alwaysRun = true)
    public void startTest(Method method) {
        // 🔹 Create ExtentTest entry
        ExtentTest test = extent.createTest(method.getName());
        testReport.set(test);

        Log.info(">>> Starting test: " + method.getName());

        // 🔹 Default login before each test
        try {
            LoginPage loginPage = new LoginPage(driver);
            String username = ConfigReader.get("default.username");
            String password = ConfigReader.get("default.password");
            boolean ok = loginPage.login(username, password);
            Log.info("loginToApplication attempted: " + ok);
        } catch (Exception e) {
            Log.warn("Default login failed or skipped: " + e.getMessage());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void captureResult(ITestResult result) {
        ExtentTest test = testReport.get();

        if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test Passed ✅");
        } else if (result.getStatus() == ITestResult.FAILURE) {
        	
        	String screenshotPath = action.captureScreenshot(result.getName()); // ✅ use WebAction
            test.fail("Test Failed ❌")
                .fail(result.getThrowable())
                .fail("Screenshot:",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test Skipped ⚠️");
        }

        Log.info("<<< Finished test: " + result.getName());

        // 🔹 Default logout after each test
        try {
            HomePage homePage = new HomePage(driver);
            homePage.logout();
            Log.info("logoutFromApplication attempted");
        } catch (Exception e) {
            Log.warn("Default logout failed or skipped: " + e.getMessage());
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        // 🔹 Flush ExtentReports
        if (extent != null) {
            extent.flush();
        }

        DriverFactory.quitDriver();
        Log.info("=== Browser closed & Extent report flushed ===");
    }
    
    private void cleanScreenshotsFolder() {
        String screenshotDir = System.getProperty("user.dir") + "/screenshots/";
        File folder = new File(screenshotDir);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".png")) {
                        file.delete();
                    }
                }
            }
            Log.info("Old screenshots cleaned from: " + screenshotDir);
        }
    }
    
    
}
