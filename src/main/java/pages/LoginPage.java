package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    WebDriver driver;

    @FindBy(id = "Email") WebElement emailField;
    @FindBy(id = "Password") WebElement passwordField;
    @FindBy(xpath = "//input[@value='Log in']") WebElement loginBtn;
    @FindBy(linkText = "Log out") WebElement logoutBtn;
    @FindBy(linkText = "Forgot password?") WebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String email, String password) {
        emailField.clear();
        if (email != null && !email.equalsIgnoreCase("N/A")) {
            emailField.sendKeys(email);
        }
        passwordField.clear();
        if (password != null && !password.equalsIgnoreCase("N/A")) {
            passwordField.sendKeys(password);
        }
        loginBtn.click();
    }

    public boolean isLoginSuccessful(String expectedEmail) {
        // True if the user account link or logout link is present on the page
        return !driver.findElements(By.cssSelector(".account")).isEmpty() 
            || !driver.findElements(By.linkText("Log out")).isEmpty();
    }

    public boolean isErrorDisplayed() {
        return !driver.findElements(By.cssSelector(".validation-summary-errors")).isEmpty()
            || !driver.findElements(By.cssSelector(".field-validation-error")).isEmpty();
    }

    public void clickForgotPassword() {
        forgotPasswordLink.click();
    }
}