package ramchavantestautomation.Base;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import ramchavantestautomation.Utils.ConfigReader;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver(String browser) {

        if (browser == null || browser.isEmpty()) {
            browser = ConfigReader.get("browser");
        }

        String runMode = ConfigReader.get("runMode");

        try {

            if ("grid".equalsIgnoreCase(runMode)) {

                String gridUrl = ConfigReader.get("grid.url");

                switch (browser.toLowerCase()) {

                    case "chrome":
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                        driver.set(new RemoteWebDriver(new URL(gridUrl), chromeOptions));
                        break;

                    case "firefox":
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        driver.set(new RemoteWebDriver(new URL(gridUrl), firefoxOptions));
                        break;

                    case "edge":
                        EdgeOptions edgeOptions = new EdgeOptions();
                        driver.set(new RemoteWebDriver(new URL(gridUrl), edgeOptions));
                        break;

                    case "safari":
                        SafariOptions safariOptions = new SafariOptions();
                        driver.set(new RemoteWebDriver(new URL(gridUrl), safariOptions));
                        break;

                    default:
                        throw new RuntimeException("Unsupported browser for grid: " + browser);
                }

            } else { // LOCAL EXECUTION

                switch (browser.toLowerCase()) {

                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        driver.set(new ChromeDriver(new ChromeOptions()));
                        break;

                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driver.set(new FirefoxDriver(new FirefoxOptions()));
                        break;

                    case "edge":
                        WebDriverManager.edgedriver().setup();
                        driver.set(new EdgeDriver(new EdgeOptions()));
                        break;

                    case "safari":
                        // Safari works ONLY on macOS and does NOT need WebDriverManager
                        driver.set(new SafariDriver());
                        break;

                    default:
                        throw new RuntimeException("Unsupported browser: " + browser);
                }
            }

            driver.get().manage().window().maximize();
            int implicit = ConfigReader.getInt("implicit.wait", 10);
            driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid grid URL", e);
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
