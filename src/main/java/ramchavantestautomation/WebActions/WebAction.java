package ramchavantestautomation.WebActions;

import ramchavantestautomation.Base.DriverFactory;

import ramchavantestautomation.Utils.Log;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WebAction extends DriverFactory {

    private WebDriver driver;

    public WebAction(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Capture screenshot with timestamp
     */
    public static String captureScreenshot(String testName) {
        WebDriver driver = DriverFactory.getDriver(); // thread-safe

        if (driver == null) {
            System.out.println("⚠️ Screenshot skipped because driver is NULL");
            return null;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS")
                .format(new Date());

        String reportsDir = System.getProperty("user.dir") + "/reports";
        String screenshotDir = reportsDir + "/screenshots";

        String fileName = testName + "_" + timestamp + ".png";
        String fullPath = screenshotDir + "/" + fileName;

        try {
            // Create screenshots directory if not present
            File dir = new File(screenshotDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File src = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            FileUtils.copyFile(src, new File(fullPath));

            // ✅ IMPORTANT: return RELATIVE PATH (NO ./)
            return "screenshots/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
    }

    // Method to wait for an element to be visible
    public void waitForElementToBeVisible(WebElement element, int timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)).until(ExpectedConditions.visibilityOf(element));
    }

    // Method to wait for an element to be clickable
    public void waitForElementToBeClickable(WebElement element, int timeoutInSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    // Method to click an element
    public void click(WebElement element) {
        waitForElementToBeClickable(element, 10);
        element.click();
    }

    public void clickCheckbox(WebElement checkbox) {
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void waitForLoaderToDisappear() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        List<WebElement> loaders = driver.findElements(By.className("loader-container"));
        if (!loaders.isEmpty()) {
            wait.until(ExpectedConditions.invisibilityOfAllElements(loaders));
        }
    }

    public boolean verifyButtonClicked(WebElement button) {
        waitForElementToBeVisible(button, 6);
        boolean isDisabled = Boolean.parseBoolean(button.getAttribute("disabled"));
        return isDisabled;
    }

    // You had a duplicate 'click' method, removed it.
    // public void click(WebElement element) {
    // new Actions(driver).moveToElement(element).click().perform();
    // }

    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementSelected(WebElement element) {
        return isElementDisplayed(element) && element.isSelected();
    }

    public boolean isElementEnabled(WebElement element) {
        return isElementDisplayed(element) && element.isEnabled();
    }

    public boolean enterText(WebElement element, String text) {
        if (isElementDisplayed(element)) {
            element.clear();
            element.sendKeys(text);
            return true;
        }
        return false;
    }

    public boolean selectBySendKeys(WebElement element, String value) {
        if (isElementDisplayed(element)) {
            element.sendKeys(value);
            return true;
        }
        return false;
    }

    public boolean selectByIndex(WebElement element, int index) {
        try {
            new Select(element).selectByIndex(index);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean selectByValue(WebElement element, String value) {
        try {
            new Select(element).selectByValue(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean selectByVisibleText(WebElement element, String visibleText) {
        try {
            new Select(element).selectByVisibleText(visibleText);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean mouseHoverUsingJavaScript(WebElement element) {
        try {
            String script = "var event = document.createEvent('MouseEvents');"
                    + "event.initMouseEvent('mouseover', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
                    + "arguments[0].dispatchEvent(event);";
            ((JavascriptExecutor) driver).executeScript(script, element);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clickUsingJavaScript(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchToFrameByIndex(int index) {
        try {
            driver.switchTo().frame(index);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchToFrameById(String id) {
        try {
            driver.switchTo().frame(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchToFrameByName(String name) {
        try {
            driver.switchTo().frame(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void hoverOverElement(WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }

    public boolean dragAndDrop(WebElement source, WebElement target) {
        try {
            new Actions(driver).dragAndDrop(source, target).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean dragAndDropByOffset(WebElement source, int xOffset, int yOffset) {
        try {
            new Actions(driver).dragAndDropBy(source, xOffset, yOffset).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean rightClick(WebElement element) {
        try {
            new Actions(driver).contextClick(element).perform();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchToWindowByTitle(String title) {
        try {
            Set<String> windowHandles = driver.getWindowHandles();
            for (String handle : windowHandles) {
                driver.switchTo().window(handle);
                if (driver.getTitle().contains(title)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean switchToNewWindow() {
        try {
            Set<String> windowHandles = driver.getWindowHandles();
            String newWindowHandle = windowHandles.toArray(new String[0])[windowHandles.size() - 1];
            driver.switchTo().window(newWindowHandle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean switchToOldWindow() {
        try {
            Set<String> windowHandles = driver.getWindowHandles();
            String oldWindowHandle = windowHandles.toArray(new String[0])[0];
            driver.switchTo().window(oldWindowHandle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Switch to the newly opened tab
    public void switchToNewTab() {
        Set<String> windowHandles = driver.getWindowHandles();
        for (String handle : windowHandles) {
            driver.switchTo().window(handle);
        }
    }

    // Close the current tab and switch back to the original one
    public void closeCurrentTabAndSwitchBack() {
        String originalTab = driver.getWindowHandles().iterator().next();
        driver.close(); // Close the current tab
        driver.switchTo().window(originalTab); // Switch back to the original tab
    }

    public int getColumnCount(WebElement row) {
        return row.findElements(By.tagName("td")).size();
    }

    public int getRowCount(WebElement table) {
        return table.findElements(By.tagName("tr")).size() - 1;
    }

    public Map<String, List<List<String>>> getTableData(By tableLocator) {
        Map<String, List<List<String>>> tableData = new HashMap<>();
        WebElement table = driver.findElement(tableLocator);

        List<List<String>> headers = new ArrayList<>();
        List<WebElement> headerRows = table.findElements(By.xpath(".//thead/tr"));
        for (WebElement headerRow : headerRows) {
            List<String> headerData = new ArrayList<>();
            List<WebElement> headerCells = headerRow.findElements(By.xpath(".//th"));
            for (WebElement cell : headerCells) {
                headerData.add(cell.getText().trim());
            }
            headers.add(headerData);
        }
        tableData.put("headers", headers);

        List<List<String>> bodyRows = new ArrayList<>();
        List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));
        for (WebElement row : rows) {
            List<String> rowData = new ArrayList<>();
            List<WebElement> cells = row.findElements(By.xpath(".//td"));
            for (WebElement cell : cells) {
                rowData.add(cell.getText().trim());
            }
            bodyRows.add(rowData);
        }
        tableData.put("bodyRows", bodyRows);
        return tableData;
    }

    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public boolean acceptAlert() {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public boolean launchUrl(String url) {
        try {
            driver.navigate().to(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getText(WebElement ele) {
        return ele.getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean clickElement(WebElement element) {
        try {
            element.click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollByVisibilityOfElement(WebElement ele) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
        } catch (Exception e) {
            System.out.println("Failed to scroll on element due to: " + e.getMessage());
        }
    }

    public void scrollToElementHorizontally(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'nearest', inline: 'center'});", element);
    }

    public boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void openLinkInNewTab(String url) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.open('" + url + "','_blank');");
    }

    public void openInNewTab(WebElement element) throws InterruptedException {
        element.sendKeys(Keys.chord(Keys.CONTROL, Keys.RETURN));
    }

    public boolean type(WebElement ele, String text) {
        return false; // Implement this logic
    }

    public boolean findElement(WebElement ele) {
        return false; // Implement this logic
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public void printElementsText(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        for (WebElement element : elements) {
            System.out.println(element.getText());
        }
    }

    public boolean isSelected(WebElement ele) {
        return false; // Implement this logic
    }

    public boolean isEnabled(WebElement ele) {
        return false; // Implement this logic
    }

    public boolean selectByVisibleText(String visibletext, WebElement ele) {
        return false; // Implement this logic
    }

    public boolean JSClick(WebElement ele) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ele);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForTabsToOpen(int expectedTabs, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(d -> d.getWindowHandles().size() >= expectedTabs);
    }

    public List<String> getAllOpenTabs() {
        return new ArrayList<>(driver.getWindowHandles());
    }

    public void switchToTab(int tabIndex) {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabIndex));
    }

    public String waitForTitle(int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(d -> !d.getTitle().isEmpty() ? d.getTitle() : null);
    }

    public boolean switchToDefaultFrame() {
        return false; // Implement this logic
    }

    public void mouseOverElement(WebElement element) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).build().perform();
        } catch (Exception e) {
            System.out.println("Failed to hover over the element: " + e.getMessage());
        }
    }

    public String standardizeText(String text) {
        return text.replaceAll("\\r\\n|\\r|\\n", "\n").trim();
    }

    public boolean draggable(WebElement source, int x, int y) {
        return false; // Implement this logic
    }

    public boolean draganddrop(WebElement source, WebElement target) {
        return false; // Implement this logic
    }

    public boolean slider(WebElement ele, int x, int y) {
        return false; // Implement this logic
    }

    public boolean Alert() {
        return false; // Implement this logic
    }

    public void fluentWait(WebElement element, int timeOut) {
        // Implement fluent wait logic
    }

    public void implicitWait(int timeOut) {
        driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
    }

    public void explicitWait(WebElement element, Duration i) {
        WebDriverWait wait = new WebDriverWait(driver, i);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void pageLoadTimeOut(int timeOut) {
        driver.manage().timeouts().pageLoadTimeout(timeOut, TimeUnit.SECONDS);
    }

    public String getCurrentTime() {
        return null; // Implement this logic
    }
}