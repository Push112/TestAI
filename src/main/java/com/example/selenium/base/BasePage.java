package com.example.selenium.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    protected WebElement find(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected void click(By locator) {
        find(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = find(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public void saveScreenshot(String filename) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filepath = Paths.get("target/screenshots", timestamp + "_" + filename + ".png").toString();
            new File(Paths.get("target/screenshots").toString()).mkdirs();
            org.openqa.selenium.TakesScreenshot screenshot = (org.openqa.selenium.TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            org.apache.commons.io.FileUtils.copyFile(srcFile, new File(filepath));
            System.out.println("Screenshot saved: " + filepath);
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
        }
    }
}
