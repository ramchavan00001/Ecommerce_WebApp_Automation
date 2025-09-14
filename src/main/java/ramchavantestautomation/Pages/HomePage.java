package ramchavantestautomation.Pages;

//package com.ramchavantestautomation.pages;

import ramchavantestautomation.Base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BasePage {

    @FindBy(linkText = "Logout")
    private WebElement logoutLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void logout() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
            logoutLink.click();
        } catch (Exception e) {
            // If logout link not present, ignore
        }
    }
}

