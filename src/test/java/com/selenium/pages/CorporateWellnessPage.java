package com.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CorporateWellnessPage {
    WebDriver driver;

    private By name = By.id("name");
    private By orgName = By.id("organizationName");
    private By contact = By.id("contactNumber");
    private By email = By.id("officialEmailId");
    private By orgSize = By.id("organizationSize");
    private By interest = By.id("interestedIn");
    private By submitBtn = By.xpath("//header//button[@type='submit']");
    private By submitBtnFallback = By.xpath("//form//button[@type='submit' or contains(.,'Schedule a demo')]");

    public CorporateWellnessPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clearForm() {
        driver.findElement(name).clear();
        driver.findElement(orgName).clear();
        driver.findElement(contact).clear();
        driver.findElement(email).clear();
    }

    public void fillForm(String uName, String uOrg, String uPhone, String uEmail, String uOrgSize, String uInterest) {
        driver.findElement(name).sendKeys(uName);
        driver.findElement(orgName).sendKeys(uOrg);
        driver.findElement(contact).sendKeys(uPhone);
        driver.findElement(email).sendKeys(uEmail);

        if (uOrgSize != null && !uOrgSize.isBlank()) {
            new Select(driver.findElement(orgSize)).selectByVisibleText(uOrgSize.trim());
        }
        if (uInterest != null && !uInterest.isBlank()) {
            new Select(driver.findElement(interest)).selectByVisibleText(uInterest.trim());
        }
    }

    private WebElement resolveSubmit() {
        try {
            return driver.findElement(submitBtn);
        } catch (Exception ignore) {
            return driver.findElement(submitBtnFallback);
        }
    }

    public boolean isSubmitEnabled() {
        return resolveSubmit().isEnabled();
    }
}