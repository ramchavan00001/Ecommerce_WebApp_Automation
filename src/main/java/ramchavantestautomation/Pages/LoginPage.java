package ramchavantestautomation.Pages;
//package com.ramchavantestautomation.pages;

import ramchavantestautomation.Base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    @FindBy(id = "input-email")
    private WebElement emailInput;

    @FindBy(id = "input-password")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@type='submit' or @value='Login' or @value='Log In']")
    private WebElement submitButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean login(String username, String password) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(emailInput));
            emailInput.clear();
            emailInput.sendKeys(username);

            passwordInput.clear();
            passwordInput.sendKeys(password);

            submitButton.click();
            // You can add wait and verification for successful login here
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

