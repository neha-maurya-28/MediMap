
package com.selenium.utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReport {
    private static ExtentReports extent;
    private static ExtentTest test;

    // Initialize Extent Report
    public static void initReport() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent-report.html");
            spark.config().setReportName("Practo Hackathon Automation Report");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            // Add environment/system info
            extent.setSystemInfo("Tester", "QA Team");
            extent.setSystemInfo("Browser", "Chrome/Edge");
            extent.setSystemInfo("Application", "Practo");
        }
    }

    // Create a new test entry
    public static ExtentTest createTest(String testName) {
        test = extent.createTest(testName);
        return test;
    }

    // Log info
    public static void logInfo(String message) {
        if (test != null) {
            test.info(message);
        }
    }

    // Log pass
    public static void logPass(String message) {
        if (test != null) {
            test.pass(message);
        }
    }

    // Log fail
    public static void logFail(String message) {
        if (test != null) {
            test.fail(message);
        }
    }

    // Flush report to disk
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
 