package com.selenium.utility;

import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Screenshots {
    private final WebDriver driver;

    public Screenshots(WebDriver driver) {
        this.driver = driver;
    }

    public void takeViewportScreenshot(String nameHint) throws IOException {
        DateTimeFormatter ts = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
        try{
            String root = System.getProperty("user.dir");
            Path dir = Path.of(root, "screenshots");
            if(!Files.exists(dir)){
                Files.createDirectories(dir);
            }

            String base = (nameHint==null)? UUID.randomUUID().toString() : nameHint.replaceAll("[^a-zA-Z0-9._-]", "_");
            String filename = base + "_" + LocalDateTime.now().format(ts) + ".png";

            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            File dest = dir.resolve(filename).toFile();
            FileHandler.copy(src, dest);

        } catch (Exception e) {
            System.out.println("Failed to capture screenshot!!");
            throw e;
        }
    }

    public void takeElementScreenshot(By locator, String nameHint) throws IOException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // make sure it's in view before screenshot (optional but safer)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);

            // highlight the element for clarity
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.outline='2px solid #ff0066'; arguments[0].style.background='rgba(255,0,102,0.08)';", element);

            // prepare folder & file
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            Path dir = Path.of("screenshots");
            if (!Files.exists(dir)) Files.createDirectories(dir);

            File src = element.getScreenshotAs(OutputType.FILE);
            File dst = dir.resolve(nameHint + "_" + time + ".png").toFile();
            FileHandler.copy(src, dst);
        } catch (Exception e) {
            System.out.println("Failed to capture element screenshot");
            throw e;
        }
    }

}
