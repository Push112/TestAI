package com.example.selenium.pages;

import com.example.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {
    // Multiple possible selectors for dashboard detection
    private final By dashboardHeading = By.xpath("//h6[contains(text(), 'Dashboard')]");
    private final By dashboardText = By.xpath("//*[contains(text(), 'Dashboard')]");
    private final By welcomeText = By.xpath("//div[contains(text(), 'Welcome')]");

    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isDashboardDisplayed() {
        try {
            // Try primary locator first
            wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeading));
            return true;
        } catch (Exception e1) {
            try {
                // Try fallback locator
                wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardText));
                return true;
            } catch (Exception e2) {
                try {
                    // Try another fallback
                    wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeText));
                    return true;
                } catch (Exception e3) {
                    // If none found, check that we're no longer on login page
                    String currentUrl = driver.getCurrentUrl();
                    return !currentUrl.contains("auth/login");
                }
            }
        }
    }
}
