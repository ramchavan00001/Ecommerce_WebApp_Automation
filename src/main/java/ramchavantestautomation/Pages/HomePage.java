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

public class HomePage extends BasePage {

	WebAction action;

	@FindBy(xpath = "//button[@class='btn btn-custom'][contains(., 'Sign Out')]")
	private WebElement signOut;

	@FindBy(xpath = "//h3[text()='Automation']")
	private WebElement homePagetext;

	public HomePage(WebDriver driver) {
		super(driver);
		action = new WebAction(driver);
	}

	public String getHomePageText()
	{
		action.waitForElementToBeVisible(homePagetext, 10);
		return homePagetext.getText();
	}
	
	public void logout() {
		action.waitForElementToBeClickable(signOut, 10);
		signOut.click();
	}
}
