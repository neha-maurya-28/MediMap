package com.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.List;

public class DiagnosticPage {
    WebDriver driver;
    private By cityItems = By.cssSelector(".u-margint--standard");

    public DiagnosticPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> getTopCities() {
        List<String> cities = new ArrayList<>();
        List<WebElement> elements = driver.findElements(cityItems);
        for (WebElement el : elements) {
            cities.add(el.getText());
        }
        return cities;
    }
}
