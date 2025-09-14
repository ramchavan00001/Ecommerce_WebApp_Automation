package ramchavantestautomation.Tests;

//package ramchavantestautomation.Tests;

import ramchavantestautomation.Pages.LoginPage;
import ramchavantestautomation.Utils.ExcelDataProvider;
import ramchavantestautomation.Utils.Log;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {

    @Test(dataProvider = "loginTestData", dataProviderClass = ExcelDataProvider.class,
          retryAnalyzer = ramchavantestautomation.Retry.RetryAnalyzer.class)
    public void validLoginTest(String username, String password) {
        Log.info("Testing login with data provider: " + username + " / " + password);

        LoginPage loginPage = new LoginPage(driver);
        boolean result = loginPage.login(username, password);

        // Replace this with more meaningful verification for your AUT
        Assert.assertTrue(result, "Login method returned false for: " + username);
    }

    @Test(dataProvider = "invalidLoginData", dataProviderClass = ExcelDataProvider.class)
    public void invalidLoginTest(String username, String password) {
        Log.info("Testing invalid login with: " + username + " / " + password);

        LoginPage loginPage = new LoginPage(driver);
        boolean result = loginPage.login(username, password);

        // For invalid login we expect either false or some failure - using simple assert
        Assert.assertTrue(result == false || password.length() < 8, "Invalid login logic check");
    }
}

