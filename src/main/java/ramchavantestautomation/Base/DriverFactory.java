package ramchavantestautomation.Base;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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
                    default:
                        throw new RuntimeException("Unsupported browser for grid: " + browser);
                }
            } else { // local
                switch (browser.toLowerCase()) {
                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        driver.set(new ChromeDriver(new ChromeOptions()));
                        break;
                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driver.set(new FirefoxDriver(new FirefoxOptions()));
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
