package com.selenium.utility;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.BuildInfo;

public class ExtentReport {

    private static ExtentReports extent;
    private static ExtentTest test;

    public static void initReport() {
        if (extent == null) {

            ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent-report.html");
            spark.config().setReportName("Practo Hackathon Automation Report");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            // ---- SIMPLE SYSTEM INFO ----
            extent.setSystemInfo("Tester", "QA Team");
            extent.setSystemInfo("Application", "Practo");
            extent.setSystemInfo("Browser", "Chrome / Edge");

            // OS Info
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("OS Version", System.getProperty("os.version"));
            extent.setSystemInfo("OS Architecture", System.getProperty("os.arch"));

            // Java Info
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Java Vendor", System.getProperty("java.vendor"));

            // TestNG Version
            extent.setSystemInfo("TestNG Version",
                    org.testng.TestNG.class.getPackage().getImplementationVersion());

            try {
                String selVersion = new BuildInfo().getReleaseLabel(); // Selenium 4+
                extent.setSystemInfo("Selenium Version", selVersion);
            } catch (Exception e) {
                extent.setSystemInfo("Selenium Version", "Unknown");
            }

            // User Info
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Timezone", System.getProperty("user.timezone"));
        }
    }

    public static ExtentTest createTest(String testName) {
        test = extent.createTest(testName);
        return test;
    }

    public static void logInfo(String message) {
        if (test != null) test.info(message);
    }

    public static void logPass(String message) {
        if (test != null) test.pass(message);
    }

    public static void logFail(String message) {
        if (test != null) test.fail(message);
    }

    public static void flushReport() {
        if (extent != null) extent.flush();
    }
}