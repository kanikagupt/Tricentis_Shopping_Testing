package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage {
    WebDriver driver;

    @FindBy(id = "gender-male") WebElement genderMale;
    @FindBy(id = "FirstName") WebElement firstNameField;
    @FindBy(id = "LastName") WebElement lastNameField;
    @FindBy(id = "Email") WebElement emailField;
    @FindBy(id = "Password") WebElement passwordField;
    @FindBy(id = "ConfirmPassword") WebElement confirmPasswordField;
    @FindBy(id = "register-button") WebElement registerBtn;
    @FindBy(css = ".field-validation-error, .validation-summary-errors") WebElement errorMsg;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void registerUser(String fName, String lName, String email, String password) {
        // Only select gender if there is a valid first name
        if (fName != null && !fName.trim().isEmpty() && !fName.equalsIgnoreCase("empty")) {
            genderMale.click();
            firstNameField.clear();
            firstNameField.sendKeys(fName);
        }
        
        if (lName != null && !lName.trim().isEmpty() && !lName.equalsIgnoreCase("empty")) {
            lastNameField.clear();
            lastNameField.sendKeys(lName);
        }
        
        if (email != null && !email.trim().isEmpty() && !email.equalsIgnoreCase("empty")) {
            emailField.clear();
            emailField.sendKeys(email);
        }
        
        if (password != null && !password.trim().isEmpty() && !password.equalsIgnoreCase("empty")) {
            passwordField.clear();
            passwordField.sendKeys(password);
            confirmPasswordField.clear();
            confirmPasswordField.sendKeys(password);
        }

        registerBtn.click();
    }

    public boolean isRegistrationSuccessful() {
        // Check without triggering implicit wait hangs
        return !driver.findElements(By.cssSelector(".result")).isEmpty() 
            && driver.findElement(By.cssSelector(".result")).getText().contains("Your registration completed");
    }

    public boolean isErrorDisplayed() {
        try { 
            return errorMsg.isDisplayed(); 
        } catch (Exception e) { 
            return false; 
        }
    }
}