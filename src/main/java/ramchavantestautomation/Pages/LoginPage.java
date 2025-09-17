package ramchavantestautomation.Pages;
//package com.ramchavantestautomation.pages;

import ramchavantestautomation.Base.BasePage;
import ramchavantestautomation.WebActions.WebAction;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

	WebAction action;
	
    @FindBy(id = "userEmail")
    private WebElement emailInput;

    @FindBy(id = "userPassword")
    private WebElement passwordInput;

    @FindBy(id="login")
    private WebElement loginButton;
    
    @FindBy(xpath="//div[contains(@aria-label, 'Incorrect')]")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
        action=new WebAction(driver);
    }

    public HomePage login(String username, String password) {
        
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(emailInput));
            emailInput.clear();
            emailInput.sendKeys(username);

            passwordInput.clear();
            passwordInput.sendKeys(password);

            loginButton.click();
            return new HomePage(driver);
            // You can add wait and verification for successful login here
            
        
    }
    
    public void enterUsername(String email)
    {
    	
    	action.waitForElementToBeVisible(emailInput, 10);
    	emailInput.clear();//to clear the field 
    	emailInput.sendKeys(email);
    }
    
    public void enterPassword(String pass)
    {
    	action.waitForElementToBeVisible(passwordInput, 10);
    	emailInput.clear();
    	passwordInput.sendKeys(pass);
    }
    
    public HomePage clickOnLoginButton()
    {
    	action.waitForElementToBeClickable(loginButton, 10);
    	loginButton.click();
    	return new HomePage(driver);
    }
    
    public boolean isErrorMessageDisplayed()
    {
    	action.waitForElementToBeVisible(errorMessage, 10);
    	return action.isElementDisplayed(errorMessage);
    
    }


}

