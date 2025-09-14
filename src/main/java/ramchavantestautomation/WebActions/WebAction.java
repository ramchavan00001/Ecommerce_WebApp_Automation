package ramchavantestautomation.WebActions;

//package ramchavantestautomation.WebActions;

import ramchavantestautomation.Utils.Log;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

public class WebAction {

    private WebDriver driver;

    public WebAction(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Capture screenshot with timestamp
     */
    public String captureScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotDir = System.getProperty("user.dir") + "/screenshots/";
        String screenshotPath = screenshotDir + testName + "_" + timestamp + ".png";

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(screenshotPath);

        try {
            Files.createDirectories(destFile.getParentFile().toPath());
            Files.copy(srcFile.toPath(), destFile.toPath());
        } catch (IOException e) {
            Log.error("Failed to save screenshot: " + e.getMessage());
        }
        return screenshotPath;
    }

    /**
     * Wait for element to be visible
     */
    public WebElement waitForVisibility(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Get list of elements
     */
    public List<WebElement> getElements(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Select dropdown by visible text
     */
    public void selectDropdownByText(WebElement element, String text) {
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }

    /**
     * Select dropdown by value
     */
    public void selectDropdownByValue(WebElement element, String value) {
        Select select = new Select(element);
        select.selectByValue(value);
    }

    /**
     * Select dropdown by index
     */
    public void selectDropdownByIndex(WebElement element, int index) {
        Select select = new Select(element);
        select.selectByIndex(index);
    }
}
