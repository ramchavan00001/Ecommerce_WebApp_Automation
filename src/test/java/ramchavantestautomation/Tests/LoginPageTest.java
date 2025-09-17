package ramchavantestautomation.Tests;

import ramchavantestautomation.Pages.HomePage;

//package ramchavantestautomation.Tests;

import ramchavantestautomation.Pages.LoginPage;
import ramchavantestautomation.Utils.ExcelDataProvider;
import ramchavantestautomation.Utils.Log;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginPageTest extends BaseTest {

	LoginPage loginPage;
	HomePage home;
	SoftAssert soft;

	@BeforeMethod(alwaysRun = true)
	public void setupLoginPage() {
		loginPage = new LoginPage(driver);
		soft = new SoftAssert();
	}

	@Test(dataProvider = "loginTestData", dataProviderClass = ExcelDataProvider.class, retryAnalyzer = ramchavantestautomation.Retry.RetryAnalyzer.class, groups = {
			"smoke" })
	public void verifyThatUserIsAbleToLoginWithValidCredentials(String username, String password) {
		Log.startTestCase("verifyThatUserIsAbleToLoginWithValidCredentialsTest");

		Log.info("Testing login with data provider: " + username + " / " + password);
		try {
			loginPage.enterUsername(username);
			Log.info("Entered username: " + username);
			loginPage.enterPassword(password);
			Log.info("Entered password: " + password);
			home = loginPage.clickOnLoginButton();
			Log.info("Clicked on login button");
			String actualHomePageText = home.getHomePageText();
			Log.info("Fetched Homepage text after successfull login: " + actualHomePageText);
			soft.assertEquals(actualHomePageText, "AUTOMATION",
					"Homepage text is not properly fetched, please verify again");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			home.logout();
			soft.assertAll();
		}

		Log.endTestCase("verifyThatUserIsAbleToLoginWithValidCredentialsTest");

	}

	@Test(dataProvider = "invalidLoginData", dataProviderClass = ExcelDataProvider.class, groups = { "smoke" })
	public void verifyThatUserIsNotAbleToLoginWithInValidCredentials(String username, String password) {

		Log.startTestCase("verifyThatUserIsNotAbleToLoginWithInValidCredentialsTest");
		Log.info("Testing invalid login with: " + username + " / " + password);

		try {
			loginPage.enterUsername(username);
			Log.info("Entered username: " + username);
			loginPage.enterPassword(password);
			Log.info("Entered password: " + password);
			home = loginPage.clickOnLoginButton();
			Log.info("Clicked on login button");
			Boolean errorDisplayed = loginPage.isErrorMessageDisplayed();
			Log.info("Error displayed: "+errorDisplayed);
			soft.assertTrue(errorDisplayed, "Error message should be displayed for invalid login!");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			soft.assertAll();
		}
		
		Log.endTestCase("verifyThatUserIsNotAbleToLoginWithInValidCredentialsTest");
	}
}
