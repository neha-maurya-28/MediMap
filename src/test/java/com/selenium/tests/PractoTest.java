package com.selenium.tests;

import com.selenium.base.BaseTest;
import com.selenium.pages.CorporateWellnessPage;
import com.selenium.pages.DiagnosticPage;
import com.selenium.pages.HomePage;
import com.selenium.pages.HospitalListingPage;
import com.selenium.utils.Screenshots;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class PractoTest extends BaseTest {

    @Test(priority = 1)
    public void testHospitalSearch() throws InterruptedException, IOException {

        HomePage home = new HomePage(driver);
        shots.takeViewportScreenshot("Home_Page");
        home.searchLocation("Bangalore");
        home.searchService("Hospital");

        HospitalListingPage listing = new HospitalListingPage(driver);
        List<String> hospitals = listing.getHospitalsWithParking(3.5);
        shots.takeViewportScreenshot("Hospitals_List");
        System.out.println("--- Hospitals with Parking (>3.5 Rating) ---");
        for (String h : hospitals) {
            System.out.println(h);
        }

        softAssert().assertNotNull(hospitals, "Hospitals list should not be null");
        softAssert().assertTrue(hospitals.size() >= 1,
                "Expected at least 1 hospital with parking and rating > 3.5");
    }

    @Test(priority = 2)
    public void testTopCities() throws IOException {
        driver.navigate().back(); // Go back to home
        HomePage home = new HomePage(driver);
        home.clickLabTests();
        DiagnosticPage diag = new DiagnosticPage(driver);
        List<String> cities = diag.getTopCities();
        shots.takeViewportScreenshot("Top_Cities");
        System.out.println("--- Top Diagnostic Cities ---");
        for (String city : cities) {
            System.out.println(city);
        }

        softAssert().assertNotNull(cities, "Cities list should not be null");
        softAssert().assertTrue(!cities.isEmpty(), "Cities list should not be empty");
        softAssert().assertTrue(cities.size() >= 3, "Expected at least 3 top cities (adjust if needed)");
    }

    @Test(priority = 3)
    public void testCorporateForm() throws InterruptedException, IOException {
        String mainWindowHandle = driver.getWindowHandle();

        HomePage home = new HomePage(driver);
        home.navigateToCorporateWellness();

        Set<String> allWindowHandles = driver.getWindowHandles();

        boolean switched = false;
        for (String handle : allWindowHandles) {
            driver.switchTo().window(handle);
            String currentTitle = driver.getTitle();
            System.out.println(currentTitle);
            if (currentTitle != null &&
                    currentTitle.contains("Employee Health | Corporate Health & Wellness Plans | Practo")) {
                switched = true;
                break;
            }
        }

        softAssert().assertTrue(switched, "Should switch to Corporate Wellness tab");
        softAssert().assertTrue(driver.getTitle() != null && driver.getTitle().toLowerCase().contains("corporate"),
                "Page title should indicate Corporate Wellness");

        shots.takeViewportScreenshot("Corporate_Wellness_Form");

        CorporateWellnessPage form = new CorporateWellnessPage(driver);
        form.fillForm("Gopal", "Cognizant", "8970657", "gopal@cognizant.com");

        boolean enabled = form.isSubmitEnabled();
        System.out.println("Submit Button Enabled: " + enabled);

        softAssert().assertTrue(!enabled, "Submit button should be enabled after filling valid data");

        shots.takeViewportScreenshot("Submit_Button_Status");

        // Switch back
        driver.switchTo().window(mainWindowHandle);
    }
}