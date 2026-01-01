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

        Log.info("Browser launched: " + browser);
        action = new WebAction(driver);
        context.setAttribute("driver", driver);

        // ✅ ONLY place where parent test is created
        ExtentTestManager.startParentTest(
                context.getCurrentXmlTest().getName() + " [" + browser + "]"
        );
    }

    // ================= BEFORE METHOD =================
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {

        Log.info("Starting test: " + method.getName());
        boolean isLoginTest = method.getName().contains(LOGIN_KEYWORD);

        if (!isLoginTest) {
            LoginPage loginPage = new LoginPage(driver);
            loginPage.login(
                    ConfigReader.get("default.username"),
                    ConfigReader.get("default.password")
            );
        }
    }

    // ================= AFTER METHOD =================
    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method) {

        boolean isLoginTest = method.getName().contains(LOGIN_KEYWORD);

        if (!isLoginTest) {
            try {
                HomePage homePage = new HomePage(driver);
                homePage.logout();
            } catch (Exception e) {
                Log.warn("Logout skipped: " + e.getMessage());
            }
        }
    }

    // ================= AFTER TEST =================
    @AfterTest(alwaysRun = true)
    public void tearDownTest() {
        DriverFactory.quitDriver();
        browserTL.remove();
        Log.info("Browser closed");
    }
}
