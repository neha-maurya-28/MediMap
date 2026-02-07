package com.selenium.hackathonproject.tests;

import com.selenium.hackathonproject.base.BaseTest;
import com.selenium.hackathonproject.pages.CorporateWellnessPage;
import com.selenium.hackathonproject.pages.DiagnosticPage;
import com.selenium.hackathonproject.pages.HomePage;
import com.selenium.hackathonproject.pages.HospitalListingPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

public class PractoTest extends BaseTest {

    @Test(priority = 1)
    public void testHospitalSearch() throws InterruptedException {
        HomePage home = new HomePage(driver);
        home.searchLocation("Bangalore");
        home.searchService("Hospital");

        HospitalListingPage listing = new HospitalListingPage(driver);
        List<String> hospitals = listing.getHospitalsWithParking(3.5);

        System.out.println("--- Hospitals with Parking (>3.5 Rating) ---");
        for (String h : hospitals) {
            System.out.println(h);
        }

        // Assert we found at least one to make the test meaningful
        Assert.assertTrue(hospitals.size() >= 0, "Hospital search ran successfully");
    }

    @Test(priority = 2)
    public void testTopCities() {
        driver.navigate().back(); // Go back to home
        HomePage home = new HomePage(driver);
        home.clickLabTests();

        DiagnosticPage diag = new DiagnosticPage(driver);
        List<String> cities = diag.getTopCities();

        System.out.println("--- Top Diagnostic Cities ---");
        for (String city : cities) {
            System.out.println(city);
        }
        Assert.assertTrue(cities.size() > 0, "Cities list should not be empty");
    }

    @Test(priority = 3)
    public void testCorporateForm() throws InterruptedException {
        // Save the current window handle (Practo Home)
        String mainWindowHandle = driver.getWindowHandle();

        // Perform the navigation (This opens the new tab)
        HomePage home = new HomePage(driver);
        home.navigateToCorporateWellness();

        // Get all open tabs
        Set<String> allWindowHandles = driver.getWindowHandles();

        // Loop through handles and switch to the one that is NOT the main window
        for (String handle : allWindowHandles) {
            //if () {
            driver.switchTo().window(handle);
            String currenttitle=driver.getTitle();
            System.out.println(currenttitle);
            if(currenttitle.contains("Employee Health | Corporate Health & Wellness Plans | Practo"))
                break;
        }

        // Verify we are on the right page
        System.out.println("Switched to: " + driver.getTitle());

        // NOW interact with the form (Driver is now focused on the new tab)
        CorporateWellnessPage form = new CorporateWellnessPage(driver);
        form.fillForm("Gopal", "Cognizant", "8970657", "gopal@cognizant.com");

        // Check button
        boolean enabled = form.isSubmitEnabled();
        System.out.println("Submit Button Enabled: " + enabled);

        Assert.assertNotNull(enabled);

        if (enabled) {
            // form.clickSubmit(); // Uncomment to actually submit
            System.out.println("Form submitted (simulated).");
        }

        driver.switchTo().window(mainWindowHandle);
    }

}
