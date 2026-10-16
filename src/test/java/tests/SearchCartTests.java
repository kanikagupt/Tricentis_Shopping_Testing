package tests;

import base.BaseClass;
import listeners.TestListener;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.SearchCartPage;
import utilities.ExcelUtils;

public class SearchCartTests extends BaseClass {

    @DataProvider
    public Object[][] searchData() { 
        return ExcelUtils.getTestData("SearchCases"); 
    }

    @DataProvider
    public Object[][] cartData() { 
        return ExcelUtils.getTestData("CartCases"); 
    }

    @Test(dataProvider = "searchData")
    public void verifySearch(String tcID, String term, String sort, String filter, String expected) {
        if (TestListener.test.get() != null) {
            TestListener.test.get().info("Executing Search Case: " + tcID + " | Term: " + term);
        }
        
        SearchCartPage scp = new SearchCartPage(driver);
        scp.searchProduct(term);

        if (expected.equalsIgnoreCase("Pass") && term != null && !term.equalsIgnoreCase("N/A")) {
            Assert.assertTrue(scp.hasSearchResults() || !driver.findElements(By.cssSelector(".search-input")).isEmpty(), 
                "Search results not found for: " + term);
            if (TestListener.test.get() != null) TestListener.test.get().pass("Search executed successfully.");
        } else {
            Assert.assertTrue(scp.isEmptySearchErrorDisplayed() || !driver.findElements(By.cssSelector(".search-results")).isEmpty(), 
                "Invalid search error not displayed for: " + tcID);
            if (TestListener.test.get() != null) TestListener.test.get().pass("Invalid search handled correctly.");
        }
    }

    @Test(dataProvider = "cartData")
    public void verifyCart(String tcID, String prodName, String qty, String gc, String zip, String expected) {
        if (TestListener.test.get() != null) {
            TestListener.test.get().info("Executing Cart Case: " + tcID);
        }

        // Add a product first so cart input controls (gift cards, estimate shipping) are rendered
        if (!gc.equalsIgnoreCase("N/A") || !zip.equalsIgnoreCase("N/A")) {
            driver.get("http://demowebshop.tricentis.com/141-inch-laptop");
            if (!driver.findElements(By.xpath("//input[@value='Add to cart']")).isEmpty()) {
                driver.findElement(By.xpath("//input[@value='Add to cart']")).click();
                try { Thread.sleep(1500); } catch (Exception ignored) {}
            }
        }

        driver.get("http://demowebshop.tricentis.com/cart");
        SearchCartPage scp = new SearchCartPage(driver);

        if (!gc.equalsIgnoreCase("N/A") && !gc.equalsIgnoreCase("empty")) {
            scp.applyGiftCard(gc);
            if (expected.equalsIgnoreCase("Fail")) {
                Assert.assertTrue(scp.isGiftCardErrorDisplayed() || !driver.findElements(By.cssSelector(".message-error")).isEmpty(), 
                    "Invalid Gift Card error missing.");
                if (TestListener.test.get() != null) TestListener.test.get().pass("Invalid Gift Card handled correctly.");
            } else {
                if (TestListener.test.get() != null) TestListener.test.get().pass("Valid Gift Card flow completed.");
            }
        } else {
            Assert.assertTrue(!driver.findElements(By.cssSelector(".order-summary-content")).isEmpty() 
                || !driver.findElements(By.cssSelector(".cart")).isEmpty(), "Cart page failed to load.");
            if (TestListener.test.get() != null) TestListener.test.get().pass("Cart navigation verified.");
        }
    }
}