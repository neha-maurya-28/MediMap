package com.selenium.base;

import com.selenium.utils.Screenshots;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected WebDriver driver;
    private static final ThreadLocal<SoftAssert> SOFT_ASSERT = new ThreadLocal<>();
    protected Screenshots shots;

    protected SoftAssert softAssert() {
        return SOFT_ASSERT.get();
    }

    @BeforeClass
    @Parameters("browser")
    public void setup(@Optional("Chrome") String browser) {
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
        driver.get("https://www.practo.com/");
    }

    @BeforeMethod(alwaysRun = true)
    public void initSoftAssert() {
        SOFT_ASSERT.set(new SoftAssert());
    }

    @AfterMethod(alwaysRun = true)
    public void assertAllAndCleanup() {
        try {
            SoftAssert sa = SOFT_ASSERT.get();
            if (sa != null) {
                sa.assertAll();
            }
        } finally {
            SOFT_ASSERT.remove();
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}