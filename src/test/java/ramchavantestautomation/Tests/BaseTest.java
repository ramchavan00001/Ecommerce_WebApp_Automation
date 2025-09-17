package ramchavantestautomation.Tests;

import ramchavantestautomation.Base.DriverFactory;
import ramchavantestautomation.Pages.HomePage;
import ramchavantestautomation.Pages.LoginPage;
import ramchavantestautomation.Utils.ConfigReader;
import ramchavantestautomation.Utils.Log;
import ramchavantestautomation.WebActions.WebAction;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;

@Listeners(ramchavantestautomation.Base.TestListener.class)
public class BaseTest {

    protected WebDriver driver;
    protected WebAction action;
    private final String LOGIN_KEYWORD = "Credentials"; // skip default login for login tests

    /**
     * Initialize driver per <test> (TestNG parallell="tests" will create separate threads for each <test>)
     */
    @BeforeTest(alwaysRun = true)
    @Parameters("browser")
    public void setUpTest(@Optional("") String browserFromXml, ITestContext context) {
        String browser = browserFromXml.isEmpty() ? ConfigReader.get("browser") : browserFromXml;
        DriverFactory.initDriver(browser);               // initializes ThreadLocal for current test thread
        driver = DriverFactory.getDriver();              // get thread-local driver
        driver.manage().window().maximize();

        int wait = ConfigReader.getInt("implicit.wait", 10);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

        driver.get(ConfigReader.get("base.url"));
        Log.info("Browser launched (" + browser + ") & navigated to: " + ConfigReader.get("base.url"));

        action = new WebAction(driver);

        // make driver available in ITestContext if other components want it (optional)
        context.setAttribute("driver", driver);

        // (optional) debug info
        Log.info("Driver hash: " + (driver != null ? driver.hashCode() : "null") + " Thread: " + Thread.currentThread().getId());
    }

    @BeforeMethod(alwaysRun = true)
    public void startTest(Method method) {
        Log.info(">>> Starting test: " + method.getName());

        boolean isLoginTest = method.getName().contains(LOGIN_KEYWORD);

        if (!isLoginTest) {
            try {
                LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
                String username = ConfigReader.get("default.username");
                String password = ConfigReader.get("default.password");
                loginPage.login(username, password);
                Log.info("Default login attempted.");
            } catch (Exception e) {
                Log.warn("Default login failed or skipped: " + e.getMessage());
            }
        } else {
            Log.info("Skipping default login for a login test.");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void endTest(Method method) {
        Log.info("<<< Finished test: " + method.getName());

        boolean isLoginTest = method.getName().contains(LOGIN_KEYWORD);

        if (!isLoginTest) {
            try {
                HomePage homePage = new HomePage(DriverFactory.getDriver());
                homePage.logout();
                Log.info("Default logout attempted.");
            } catch (Exception e) {
                Log.warn("Default logout failed or skipped: " + e.getMessage());
            }
        }
    }

    /**
     * Quit driver per test-thread
     * Important for parallel="tests" so each thread quits its own driver
     */
    @AfterTest(alwaysRun = true)
    public void tearDownTest() {
        DriverFactory.quitDriver();
        Log.info("=== Browser closed for thread " + Thread.currentThread().getId() + " ===");
    }

    private void cleanScreenshotsFolder() {
        // Clean /reports/screenshots (not root /screenshots)
        String screenshotDir = System.getProperty("user.dir") + "/reports/screenshots/";
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
