package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utilities.ExtentReportManager;
import utilities.ScreenshotUtils;

public class TestListener implements ITestListener {
    ExtentReports extent = ExtentReportManager.getReportInstance();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public void onTestStart(ITestResult result) {
        // Extract the Test Case ID (e.g., TC_001) from the first parameter in the Excel sheet
        String testCaseID = result.getParameters().length > 0 ? result.getParameters()[0].toString() : "Unknown_TC";
        ExtentTest extentTest = extent.createTest(testCaseID + " - " + result.getMethod().getMethodName());
        test.set(extentTest);
    }

    public void onTestSuccess(ITestResult result) {
        String testCaseID = result.getParameters().length > 0 ? result.getParameters()[0].toString() : "Unknown_TC";
        test.get().log(Status.PASS, "Test Passed. Assertions verified.");
        
        // Take Screenshot and attach it to the Extent HTML Report
        String screenshotPath = ScreenshotUtils.takeScreenshot(testCaseID, "PASSED");
        if (screenshotPath != null) {
            test.get().addScreenCaptureFromPath("../" + screenshotPath, "Success Screenshot");
        }
    }

    public void onTestFailure(ITestResult result) {
        String testCaseID = result.getParameters().length > 0 ? result.getParameters()[0].toString() : "Unknown_TC";
        test.get().log(Status.FAIL, result.getThrowable());
        
        // Take Screenshot and attach it to the Extent HTML Report
        String screenshotPath = ScreenshotUtils.takeScreenshot(testCaseID, "FAILED");
        if (screenshotPath != null) {
            test.get().addScreenCaptureFromPath("../" + screenshotPath, "Failure Screenshot");
        }
    }

    public void onFinish(ITestContext context) {
        extent.flush(); 
    }
}
