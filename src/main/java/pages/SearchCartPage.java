package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchCartPage {
    WebDriver driver;

    @FindBy(id = "small-searchterms") WebElement searchBox;
    @FindBy(xpath = "//input[@value='Search']") WebElement searchBtn;

    public SearchCartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void searchProduct(String productName) {
        searchBox.clear();
        if (productName != null && !productName.equalsIgnoreCase("empty") && !productName.equalsIgnoreCase("N/A")) {
            searchBox.sendKeys(productName);
        }
        searchBtn.click();
    }

    public boolean hasSearchResults() {
        return !driver.findElements(By.cssSelector(".product-item")).isEmpty();
    }

    public boolean isEmptySearchErrorDisplayed() {
        try {
            // Dismiss JavaScript popup alert triggered on empty search (TC_019)
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.accept();
            return alertText.toLowerCase().contains("search");
        } catch (Exception e) {
            return !driver.findElements(By.cssSelector(".search-results")).isEmpty();
        }
    }

    public void applyGiftCard(String code) {
        if (!driver.findElements(By.id("giftcardcouponcode")).isEmpty()) {
            WebElement box = driver.findElement(By.id("giftcardcouponcode"));
            box.clear();
            box.sendKeys(code);
            driver.findElement(By.name("applygiftcardcouponcode")).click();
        }
    }

    public boolean isGiftCardErrorDisplayed() {
        return !driver.findElements(By.cssSelector(".message-error")).isEmpty()
            || !driver.findElements(By.cssSelector(".validation-summary-errors")).isEmpty();
    }
}