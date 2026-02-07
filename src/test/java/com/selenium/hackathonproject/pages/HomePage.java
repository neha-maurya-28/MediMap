package com.selenium.hackathonproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
    WebDriver driver;

    // Locators
    private By cityInput = By.xpath("//*[@id=\"c-omni-container\"]/div/div[1]/div/input");
    private By searchInput = By.xpath("//input[contains(@placeholder,'Search doctors')]");
    private By labTestsLink = By.xpath("//*[text()='Lab Tests']");
    private By forCorporatesLink = By.xpath("//*[text()='For Corporates' and @class='nav-interact']");
    private By healthPlansLink = By.xpath("//*[text()='Health & Wellness Plans' and @class='nav-interact']");

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void searchLocation(String location) throws InterruptedException {
        WebElement city = driver.findElement(cityInput);
        city.clear();
        city.sendKeys(location);
        Thread.sleep(1000); // Wait for auto-suggest
        driver.findElement(By.xpath("//div[text()='" + location + "']")).click();
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
