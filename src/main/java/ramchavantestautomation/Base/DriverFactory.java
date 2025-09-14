package ramchavantestautomation.Base;

import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import ramchavantestautomation.Utils.ConfigReader;

public class DriverFactory {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver(String browser) {
        String runMode = ConfigReader.get("runMode");

        try {
            if (runMode.equalsIgnoreCase("grid")) {
                String gridUrl = ConfigReader.get("grid.url");

                if (browser.equalsIgnoreCase("chrome")) {
                    ChromeOptions options = new ChromeOptions();
                    driver.set(new RemoteWebDriver(new URL(gridUrl), options));
                } else if (browser.equalsIgnoreCase("firefox")) {
                    FirefoxOptions options = new FirefoxOptions();
                    driver.set(new RemoteWebDriver(new URL(gridUrl), options));
                } else {
                    throw new IllegalArgumentException("Browser not supported on Grid: " + browser);
                }
            } else {
                // local execution
                if (browser.equalsIgnoreCase("chrome")) {
                    driver.set(new ChromeDriver());
                } else if (browser.equalsIgnoreCase("firefox")) {
                    driver.set(new FirefoxDriver());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
