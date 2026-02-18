package com.selenium.base;

import com.selenium.utility.ExcelUtils;
import com.selenium.utility.ExtentReport;
import com.selenium.utility.Screenshots;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Files;
import java.nio.file.Path;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    private static final ThreadLocal<SoftAssert> SOFT_ASSERT = new ThreadLocal<>();
    protected Screenshots shots;
    protected ExcelUtils excel;
    private Properties config = new Properties();
    protected static Logger logger;

    protected SoftAssert softAssert() {
        return SOFT_ASSERT.get();
    }

    private String prop(String key, String defaultVal) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.trim().isEmpty()) return sys.trim();
        String val = config.getProperty(key);
        return (val == null || val.trim().isEmpty()) ? defaultVal : val.trim();
    }

    @BeforeSuite
    public void startReport() {
        ExtentReport.initReport();
    }

    @AfterSuite
    public void endReport() {
        ExtentReport.flushReport();
    }

    @BeforeClass
    @Parameters("browser")
    public void setup(@Optional("Chrome") String browser) {
        try { Files.createDirectories(Path.of("logs/tests")); } catch (Exception ignored) {}
        logger = LogManager.getLogger(getClass());

        try (InputStream in = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new RuntimeException("config.properties not found");
            config.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }

        System.out.println("Initializing Browser: " + browser);

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-geolocation");
            options.addArguments("--start-maximized");
            driver = new ChromeDriver(options);

        } else if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-geolocation");
            options.addArguments("--start-maximized");
            driver = new EdgeDriver(options);

        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addPreference("permissions.default.microphone", 2);
            options.addPreference("permissions.default.camera", 2);
            options.addPreference("permissions.default.geo", 2);
            options.addPreference("permissions.default.desktop-notification", 2);
            driver = new FirefoxDriver(options);
            driver.manage().window().maximize();
        } else {
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        shots = new Screenshots(driver);
        excel = new ExcelUtils("TestData.xlsx");
        long waitSeconds = Long.parseLong(prop("explicit.wait.seconds", "10"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
        String url = prop("url", "https://www.practo.com");
        driver.get(url);
    }

    @BeforeMethod(alwaysRun = true)
    public void initSoftAssert() {
        SOFT_ASSERT.set(new SoftAssert());
    }

    @AfterMethod(alwaysRun = true)
    public void assertAllAndCleanup() {
            SoftAssert sa = SOFT_ASSERT.get();
            if (sa != null) {
                sa.assertAll();
            }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}