package com.example.selenium.pages;

import com.example.selenium.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private final By usernameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By submitButton = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void open(String url) {
        driver.navigate().to(url);
    }

    public void enterUsername(String username) {
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public void clickLogin() {
        try {
            // Try clicking with wait for clickable
            WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
            loginBtn.click();
        } catch (Exception e) {
            // Retry once if fails
            try {
                Thread.sleep(500);
                WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
                loginBtn.click();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Login button click interrupted", ie);
            }
        }
    }

    public void loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}
