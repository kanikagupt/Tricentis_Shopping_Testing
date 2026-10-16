package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
    public static ExtentReports extent;
    
    public static ExtentReports getReportInstance() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter("Reports/TestReport.html");
            reporter.config().setReportName("Tricentis 50 Test Cases Report");
            extent = new ExtentReports();
            extent.attachReporter(reporter);
        }
        return extent;
    }
}