package com.example.selenium.stepdefinitions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

import java.util.concurrent.TimeUnit;
import java.time.Duration;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Paths;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;

/**
 * Cucumber Hooks for setup and teardown of WebDriver
 * Manages driver lifecycle for each scenario
 */
public class Hooks {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

    public static WebDriver getDriver() {
        WebDriver d = driver.get();
        if (d == null) {
            throw new RuntimeException("WebDriver not initialized. Make sure @Before hook ran.");
        }
        return d;
    }

    public static WebDriverWait getWait() {
        WebDriverWait w = wait.get();
        if (w == null) {
            throw new RuntimeException("WebDriverWait not initialized. Make sure @Before hook ran.");
        }
        return w;
    }

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        String noHeadlessProperty = System.getProperty("noHeadless");
        if (noHeadlessProperty == null || noHeadlessProperty.isEmpty()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1200,800");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        WebDriver newDriver = new ChromeDriver(options);
        newDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        
        driver.set(newDriver);
        wait.set(new WebDriverWait(newDriver, Duration.ofSeconds(30)));
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver d = driver.get();
        if (d != null) {
            try {
                // If scenario failed, capture screenshot
                if (scenario != null && scenario.isFailed()) {
                    try {
                        File src = ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
                        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String name = scenario.getName().replaceAll("[^a-zA-Z0-9-_]", "_");
                        String screenshotsDir = Paths.get("target", "screenshots").toString();
                        new File(screenshotsDir).mkdirs();
                        String destPath = Paths.get(screenshotsDir, timestamp + "_" + name + ".png").toString();
                        FileUtils.copyFile(src, new File(destPath));
                        System.out.println("Saved screenshot: " + destPath);
                    } catch (Exception ex) {
                        System.out.println("Failed to capture screenshot: " + ex.getMessage());
                    }
                }

                d.quit();
            } catch (Exception e) {
                System.out.println("Error quitting driver: " + e.getMessage());
            } finally {
                driver.remove();
                wait.remove();
            }
        }
    }
}
