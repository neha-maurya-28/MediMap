package com.selenium.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    WebDriver driver;

    // Locators
    private By cityInput = By.xpath("//*[@id=\"c-omni-container\"]/div/div[1]/div/input");
    private By searchInput = By.xpath("//input[contains(@placeholder,'Search doctors')]");
    private By labTestsLink = By.xpath("//*[text()='Lab Tests']");

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void searchLocation(String location) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement city = wait.until(ExpectedConditions.visibilityOfElementLocated(cityInput));
        city.clear();
        city.sendKeys(location);

        By suggestionLocator = By.xpath("//div[text()='" + location + "']");

        try {
            WebElement suggestion = wait.until(ExpectedConditions.elementToBeClickable(suggestionLocator));
            suggestion.click();
        } catch (Exception e) {
            System.out.println("Suggestion '" + location + "' did not appear within 10 seconds.");
        }
    }

    public void searchService(String service) throws InterruptedException {
        WebElement search = driver.findElement(searchInput);
        search.sendKeys(service);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[text()='" + service + "']")).click();
    }

    public void clickLabTests() {
        driver.findElement(labTestsLink).click();
    }

    public void navigateToCorporateWellness() {
        driver.navigate().to("https://www.practo.com/");

        driver.findElement(By.xpath("//*[text()='For Corporates' and @class='nav-interact']" )).click();
        driver.findElement(By.xpath("//*[text()='Health & Wellness Plans' and @class='nav-interact'] ")).click();
    }
}