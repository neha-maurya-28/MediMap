package com.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class CorporateWellnessPage {
    WebDriver driver;

    // Locators
    private By name = By.id("name");
    private By orgName = By.id("organizationName");
    private By contact = By.id("contactNumber");
    private By email = By.id("officialEmailId");
    private By orgSize = By.id("organizationSize");
    private By interest = By.id("interestedIn");
    private By submitBtn = By.xpath("//header//button[@type='submit']"); // Adjusted locator

    public CorporateWellnessPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fillForm(String uName, String uOrg, String uPhone, String uEmail) {
        driver.findElement(name).sendKeys(uName);
        driver.findElement(orgName).sendKeys(uOrg);
        driver.findElement(contact).sendKeys(uPhone);
        driver.findElement(email).sendKeys(uEmail);

        new Select(driver.findElement(orgSize)).selectByVisibleText("<500");
        new Select(driver.findElement(interest)).selectByVisibleText("Taking a demo");
    }

    public boolean isSubmitEnabled() {
        return driver.findElement(submitBtn).isEnabled();
    }

    public void clickSubmit() {
        if(isSubmitEnabled()) {
            driver.findElement(submitBtn).click();
        }
    }
}
