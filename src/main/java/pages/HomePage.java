package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    WebDriver driver;

    @FindBy(id = "newsletter-email") WebElement newsletterBox;
    @FindBy(id = "newsletter-subscribe-button") WebElement subscribeBtn;
    @FindBy(id = "newsletter-result-block") WebElement newsletterResult;
    
    @FindBy(linkText = "Computers") WebElement computersMenu;
    @FindBy(linkText = "Contact us") WebElement contactUsLink;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void subscribeNewsletter(String email) {
        newsletterBox.clear();
        if(!email.equalsIgnoreCase("empty")) {
            newsletterBox.sendKeys(email);
        }
        subscribeBtn.click();
    }

    public boolean isNewsletterResultDisplayed() {
        try { 
            Thread.sleep(1000); // Wait for ajax response
            return newsletterResult.isDisplayed(); 
        } 
        catch (Exception e) { return false; }
    }
    
    public void clickMenu(String menuName) {
        if(menuName.contains("Computers")) computersMenu.click();
        else if(menuName.contains("Contact")) contactUsLink.click();
    }
}