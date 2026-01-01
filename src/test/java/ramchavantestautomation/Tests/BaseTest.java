package ramchavantestautomation.Tests;

import ramchavantestautomation.Base.DriverFactory;
import ramchavantestautomation.Pages.LoginPage;
import ramchavantestautomation.Pages.HomePage;
import ramchavantestautomation.Utils.ConfigReader;
import ramchavantestautomation.Utils.Log;
import ramchavantestautomation.Utils.ExtentTestManager;
import ramchavantestautomation.WebActions.WebAction;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;

@Listeners(ramchavantestautomation.Base.TestListener.class)
public class BaseTest {

    protected WebDriver driver;
    protected WebAction action;

    private static final String LOGIN_KEYWORD = "Credentials";

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

        browserTL.set(browser);

        DriverFactory.initDriver(browser);
        driver = DriverFactory.getDriver();

        driver.get(ConfigReader.get("base.url"));
        Log.info("Browser launched (" + browser + ") & navigated to base URL");

        action = new WebAction(driver);
        context.setAttribute("driver", driver);

        ExtentTestManager.startParentTest(
                context.getCurrentXmlTest().getName() + " [" + browser + "]"
        );
    }

    // ================= BEFORE METHOD =================
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {

        String browser = getBrowser();

        ExtentTestManager.startTest(method.getName());
        Log.info(">>> Starting test: " + method.getName() + " | Browser: " + browser);

        boolean isLoginTest = method.getName().contains(LOGIN_KEYWORD);

        if (!isLoginTest) {
            try {
                LoginPage loginPage = new LoginPage(driver);
                loginPage.login(
                        ConfigReader.get("default.username"),
                        ConfigReader.get("default.password")
                );

                Log.info("Default login successful");
                ExtentTestManager.getTest().info("User logged in");

            } catch (Exception e) {
                Log.error("Login failed: " + e.getMessage());
                throw e; // login failure should fail test
            }
        } else {
            Log.info("Skipping login for login-related test");
        }
    }

    // ================= AFTER METHOD =================
    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method) {

        String browser = getBrowser();
        boolean isLoginTest = method.getName().contains(LOGIN_KEYWORD);

        if (!isLoginTest) {
            try {
                HomePage homePage = new HomePage(driver);
                homePage.logout();

                Log.info("User logged out successfully");
                ExtentTestManager.getTest().info("User logged out");

            } catch (Exception e) {
                Log.warn("Logout skipped or failed: " + e.getMessage());
                ExtentTestManager.getTest().warning("Logout not performed");
            }
        } else {
            Log.info("Skipping logout for login test");
        }

        ExtentTestManager.getTest().pass(
                "Test finished on browser: " + browser
        );
        ExtentTestManager.endTest();
    }

    // ================= AFTER TEST =================
    @AfterTest(alwaysRun = true)
    public void tearDownTest() {
        DriverFactory.quitDriver();
        browserTL.remove();
        Log.info("Browser closed for thread " + Thread.currentThread().getId());
    }
}
