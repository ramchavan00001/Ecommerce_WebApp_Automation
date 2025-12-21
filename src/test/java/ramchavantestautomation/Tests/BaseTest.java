package ramchavantestautomation.Tests;

import ramchavantestautomation.Base.DriverFactory;
import ramchavantestautomation.Pages.LoginPage;
import ramchavantestautomation.Utils.ConfigReader;
import ramchavantestautomation.Utils.Log;
import ramchavantestautomation.Utils.ExtentTestManager;
import ramchavantestautomation.WebActions.WebAction;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;

@Listeners(ramchavantestautomation.Base.TestListener.class)
public class BaseTest {

    protected WebDriver driver;
    protected WebAction action;

    private final String LOGIN_KEYWORD = "Credentials";

    // ✅ Browser stored per thread
    private static ThreadLocal<String> browserTL = new ThreadLocal<>();

    protected String getBrowser() {
        return browserTL.get();
    }

    // ================= BEFORE TEST =================
    @BeforeTest(alwaysRun = true)
    @Parameters("browser")
    public void setUpTest(@Optional("") String browserFromXml, ITestContext context) {

        String browser = (browserFromXml == null || browserFromXml.isEmpty())
                ? ConfigReader.get("browser")
                : browserFromXml;

        browserTL.set(browser); // ✅ store browser safely

        DriverFactory.initDriver(browser);
        driver = DriverFactory.getDriver();

        driver.get(ConfigReader.get("base.url"));
        Log.info("Browser launched (" + browser + ") & navigated to: " + ConfigReader.get("base.url"));

        action = new WebAction(driver);
        context.setAttribute("driver", driver);

        // Parent Extent test per browser
        ExtentTestManager.startParentTest(
                context.getCurrentXmlTest().getName() + " [" + browser + "]"
        );
        Log.info("Parent ExtentTest created for browser: " + browser);
    }

    // ================= BEFORE METHOD =================
    @BeforeMethod(alwaysRun = true)
    public void startTest(Method method) {

        String browser = getBrowser();

        ExtentTestManager.startTest(method.getName());
        Log.info(">>> Starting test: " + method.getName() + " on browser: " + browser);

        boolean isLoginTest = method.getName().contains(LOGIN_KEYWORD);

        if (!isLoginTest) {
            try {
                LoginPage loginPage = new LoginPage(driver);
                loginPage.login(
                        ConfigReader.get("default.username"),
                        ConfigReader.get("default.password")
                );

                Log.info("Default login attempted.");
                ExtentTestManager.getTest().info(
                        "Default login attempted on browser: " + browser
                );

            } catch (Exception e) {
                Log.warn("Default login failed or skipped: " + e.getMessage());
                ExtentTestManager.getTest().warning(
                        "Default login failed/skipped on " + browser + ": " + e.getMessage()
                );
            }
        } else {
            Log.info("Skipping default login for login test method.");
            ExtentTestManager.getTest().info("Skipping login for login test method.");
        }
    }

    // ================= AFTER METHOD =================
    @AfterMethod(alwaysRun = true)
    public void endTest(Method method) {

        String browser = getBrowser();

        Log.info("<<< Finished test: " + method.getName() + " on browser: " + browser);
        ExtentTestManager.getTest().pass(
                "Test finished successfully on browser: " + browser
        );

        ExtentTestManager.endTest();
    }

    // ================= AFTER TEST =================
    @AfterTest(alwaysRun = true)
    public void tearDownTest() {
        DriverFactory.quitDriver();
        browserTL.remove(); // ✅ clean ThreadLocal
        Log.info("=== Browser closed for thread " + Thread.currentThread().getId() + " ===");
    }

    // ================= UTIL =================
    private void cleanScreenshotsFolder() {
        String screenshotDir = System.getProperty("user.dir") + "/reports/screenshots/";
        File folder = new File(screenshotDir);
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".png")) {
                    file.delete();
                }
            }
            Log.info("Old screenshots cleaned from: " + screenshotDir);
        }
    }
}
