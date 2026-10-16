package tests;

import base.BaseClass;
import listeners.TestListener;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utilities.ExcelUtils;

public class LoginTests extends BaseClass {

    @DataProvider
    public Object[][] loginData() {
        return ExcelUtils.getTestData("LoginCases"); 
    }

    @Test(dataProvider = "loginData")
    public void verifyLoginScenarios(String testCaseID, String email, String password, String expectedResult) {
        if (TestListener.test.get() != null) {
            TestListener.test.get().info("Executing Test Case: " + testCaseID);
        }

        // Handle TC_010: Forgot Password flow
        if (testCaseID.equalsIgnoreCase("TC_010")) {
            driver.get("http://demowebshop.tricentis.com/login");
            LoginPage lp = new LoginPage(driver);
            lp.clickForgotPassword();
            Assert.assertTrue(driver.getCurrentUrl().contains("passwordrecovery"), "Failed to navigate to Password Recovery.");
            if (TestListener.test.get() != null) TestListener.test.get().pass("Forgot Password recovery page loaded.");
            return;
        }

        // For valid login tests (TC_006, TC_009), ensure the account exists first
        if (expectedResult.equalsIgnoreCase("Pass") && !email.equalsIgnoreCase("N/A")) {
            ensureAccountExists(email, password);
        }

        driver.get("http://demowebshop.tricentis.com/login");
        LoginPage lp = new LoginPage(driver);
        lp.login(email, password);

        if (testCaseID.equalsIgnoreCase("TC_009")) {
            // Handle TC_009: Logout verification
            if (!driver.findElements(By.linkText("Log out")).isEmpty()) {
                driver.findElement(By.linkText("Log out")).click();
            }
            Assert.assertTrue(!driver.findElements(By.linkText("Log in")).isEmpty(), "Logout failed.");
            if (TestListener.test.get() != null) TestListener.test.get().pass("User logged out successfully.");
        } else if (expectedResult.equalsIgnoreCase("Pass")) {
            boolean status = lp.isLoginSuccessful(email);
            Assert.assertTrue(status, "Login failed for valid data.");
            if (TestListener.test.get() != null) TestListener.test.get().pass("User logged in successfully.");
        } else {
            boolean status = lp.isErrorDisplayed();
            Assert.assertTrue(status, "Error message not displayed for invalid data.");
            if (TestListener.test.get() != null) TestListener.test.get().pass("Error message displayed correctly.");
        }
    }

    private void ensureAccountExists(String email, String pwd) {
        driver.get("http://demowebshop.tricentis.com/register");
        if (!driver.findElements(By.id("register-button")).isEmpty()) {
            driver.findElement(By.id("gender-male")).click();
            driver.findElement(By.id("FirstName")).sendKeys("Test");
            driver.findElement(By.id("LastName")).sendKeys("User");
            driver.findElement(By.id("Email")).sendKeys(email);
            driver.findElement(By.id("Password")).sendKeys(pwd);
            driver.findElement(By.id("ConfirmPassword")).sendKeys(pwd);
            driver.findElement(By.id("register-button")).click();
            
            // Log out if automatically logged in so login tests can run clean
            if (!driver.findElements(By.linkText("Log out")).isEmpty()) {
                driver.findElement(By.linkText("Log out")).click();
            }
        }
    }
}