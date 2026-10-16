package tests;

import base.BaseClass;
import listeners.TestListener;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.RegisterPage;
import utilities.ExcelUtils;

public class RegisterTests extends BaseClass {

    @DataProvider
    public Object[][] registerData() {
        return ExcelUtils.getTestData("RegisterCases"); 
    }

    @Test(dataProvider = "registerData")
    public void verifyRegistration(String tcID, String fName, String lName, String email, String pwd, String expected) {
        if (TestListener.test.get() != null) {
            TestListener.test.get().info("Executing Registration Case: " + tcID);
        }
        
        driver.get("http://demowebshop.tricentis.com/register");
        RegisterPage rp = new RegisterPage(driver);

        // Generate unique dynamic email for valid cases so it never fails on duplicate user
        String dynamicEmail = email;
        if (expected.equalsIgnoreCase("Pass") && email.contains("@")) {
            String[] parts = email.split("@");
            dynamicEmail = parts[0] + "_" + System.currentTimeMillis() + "@" + parts[1];
        }

        rp.registerUser(fName, lName, dynamicEmail, pwd);

        if (expected.equalsIgnoreCase("Pass")) {
            Assert.assertTrue(rp.isRegistrationSuccessful(), "Valid registration failed for: " + tcID);
            if (TestListener.test.get() != null) {
                TestListener.test.get().pass("User registered successfully.");
            }
        } else {
            Assert.assertTrue(rp.isErrorDisplayed(), "Validation error not shown for: " + tcID);
            if (TestListener.test.get() != null) {
                TestListener.test.get().pass("Appropriate validation error displayed.");
            }
        }
    }
}