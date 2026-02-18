package com.selenium.pages;

import com.selenium.utility.ExcelWriter;
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

        ExcelWriter writer = new ExcelWriter(System.getProperty("user.dir") + "/src/test/resources/testdata/TestData.xlsx");
        writer.writeCityList("(OUTPUT)Top_Cities", cities);

        return cities;
    }
}
