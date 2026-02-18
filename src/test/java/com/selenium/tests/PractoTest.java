package com.selenium.tests;

import com.selenium.base.BaseTest;
import com.selenium.pages.CorporateWellnessPage;
import com.selenium.pages.DiagnosticPage;
import com.selenium.pages.HomePage;
import com.selenium.pages.HospitalListingPage;
import com.selenium.utils.ExcelUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;

public class PractoTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(PractoTest.class);

    private final String writableExcelPath = System.getProperty("user.dir") + "/target/test-output/TestData.xlsx";

    private ExcelUtils dataExcel;

    @BeforeClass
    public void setupWritableExcel() throws IOException {
        File targetFile = new File(writableExcelPath);
        File parent = targetFile.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testdata/TestData.xlsx");
             FileOutputStream fos = new FileOutputStream(targetFile)) {
            if (is == null) throw new FileNotFoundException("Classpath resource testdata/TestData.xlsx not found");
            byte[] buf = new byte[8192];
            int n;
            while ((n = is.read(buf)) > 0) fos.write(buf, 0, n);
        }

        dataExcel = new ExcelUtils(targetFile);
    }

    @Test(priority = 1)
    public void testHospitalSearch() throws InterruptedException, IOException {
        logger.info("Starting testHospitalSearch...");
        HomePage home = new HomePage(driver);
        shots.takeViewportScreenshot("Home_Page");

        home.searchLocation("Bangalore");
        logger.info("Searched location: Bangalore");

        home.searchService("Hospital");
        logger.info("Searched service: Hospital");

        HospitalListingPage listing = new HospitalListingPage(driver);
        List<String> hospitals = listing.getHospitalsWithParking(3.5);
        logger.info("Hospitals with Parking (>3.5 Rating): {}", hospitals);

        shots.takeViewportScreenshot("Hospitals_List");

        System.out.println("--- Hospitals with Parking (>3.5 Rating) ---");
        for (String h : hospitals) {
            System.out.println(h);
        }

        com.selenium.utils.ExcelWriter writer = new com.selenium.utils.ExcelWriter(System.getProperty("user.dir") + "/src/test/resources/testdata/TestData.xlsx");
        writer.writeHospitalList("(OUTPUT)Hospitals_List", hospitals);

        softAssert().assertNotNull(hospitals, "Hospitals list should not be null");
        softAssert().assertTrue(hospitals.size() >= 1, "Expected at least 1 hospital with parking and rating > 3.5");
        logger.info("testHospitalSearch completed successfully.");
    }

    @Test(priority = 2)
    public void testTopCities() throws IOException {
        logger.info("Starting testTopCities...");
        driver.navigate().back();

        logger.info("Navigated back to home page.");
        HomePage home = new HomePage(driver);

        home.clickLabTests();
        logger.info("Clicked on Lab Tests link.");

        DiagnosticPage diag = new DiagnosticPage(driver);
        List<String> cities = diag.getTopCities();

        shots.takeViewportScreenshot("Top_Cities");

        logger.info("Top Diagnostic Cities: {}", cities);

        System.out.println("--- Top Diagnostic Cities ---");
        for (String city : cities) {
            System.out.println(city);
        }

        softAssert().assertNotNull(cities, "Cities list should not be null");
        softAssert().assertTrue(!cities.isEmpty(), "Cities list should not be empty");
        softAssert().assertTrue(cities.size() >= 3, "Expected at least 3 top cities (adjust if needed)");

        logger.info("testTopCities completed successfully.");
    }

    @Test(priority = 3)
    public void testCorporateForm_InvalidData_FromExcel() throws InterruptedException, java.io.IOException {
        logger.info("Starting testCorporateForm...");
        String mainWindowHandle = driver.getWindowHandle();

        logger.info("Main window handle saved.");
        HomePage home = new HomePage(driver);
        home.navigateToCorporateWellness();
        logger.info("Navigated to Corporate Wellness page.");

        java.util.Set<String> allWindowHandles = driver.getWindowHandles();
        boolean switched = false;
        for (String handle : allWindowHandles) {
            driver.switchTo().window(handle);
            String currentTitle = driver.getTitle();
            logger.info("Switched to window with title: {}", currentTitle);
            if (currentTitle != null &&
                    currentTitle.contains("Employee Health | Corporate Health & Wellness Plans | Practo")) {
                switched = true;
                break;
            }
        }
        softAssert().assertTrue(switched, "Should switch to Corporate Wellness tab");

        CorporateWellnessPage form = new CorporateWellnessPage(driver);

        final String SHEET = "(INPUT)CorporateForm_TestData";

        int totalRows = dataExcel.getRowCount(SHEET);
        softAssert().assertTrue(totalRows >= 2, "Need at least 1 data row in sheet: " + SHEET + " (header + 1 data row)");

        System.out.println("--- Corporate Form: Invalid data validation ---");

        for (int r = 1; r < totalRows; r++) {
            String uName = safe(dataExcel.getCell(SHEET, r, 1)); // Name
            String uOrg = safe(dataExcel.getCell(SHEET, r, 2)); // Organization Name
            String uPhone = safe(dataExcel.getCell(SHEET, r, 3)); // Contact Number
            String uEmail = safe(dataExcel.getCell(SHEET, r, 4)); // Official Email Id
            String uOrgSize = safe(dataExcel.getCell(SHEET, r, 5)); // Organization Size
            String uInterest = safe(dataExcel.getCell(SHEET, r, 6)); // Interested In

            form.clearForm();
            form.fillForm(uName, uOrg, uPhone, uEmail, uOrgSize, uInterest);
            logger.info("Filled Corporate Wellness form with sample data.");

//            Thread.sleep(500);

            boolean enabled = form.isSubmitEnabled();
            System.out.printf("Row %d -> Submit Enabled? %s [Name=%s, Org=%s, Phone=%s, Email=%s, Size=%s, Interest=%s]%n", r, enabled, uName, uOrg, uPhone, uEmail, uOrgSize, uInterest);

            logger.info("Submit Button Enabled: {}", enabled);

            softAssert().assertFalse(enabled, "Submit should be disabled for invalid data at row " + r);

            shots.takeViewportScreenshot("Corporate_Invalid_Row_" + r);
        }

        driver.switchTo().window(mainWindowHandle);
        logger.info("Switched back to main window.");
        logger.info("testCorporateForm completed successfully.");
    }

    private static String safe(String v) {
        return (v == null) ? "" : v.trim();
    }
}