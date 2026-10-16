package utilities;

import base.BaseClass;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    public static String takeScreenshot(String testCaseID, String status) {
        try {
            // 1. Create the Screenshots folder if it does not exist
            File screenshotDir = new File("Screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdir();
            }

            // 2. Generate a unique file name using the Test Case ID and a timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testCaseID + "_" + status + "_" + timestamp + ".png";
            String destinationPath = "Screenshots/" + fileName;

            // 3. Command Selenium to capture the screen
            File src = ((TakesScreenshot) BaseClass.driver).getScreenshotAs(OutputType.FILE);
            
            // 4. Save the file to the destination folder
            FileHandler.copy(src, new File(destinationPath));

            return destinationPath;
        } catch (Exception e) {
            System.out.println("Screenshot capture failed: " + e.getMessage());
            return null;
        }
    }
}
