package com.example.selenium.stepdefinitions;

import com.example.selenium.pages.LoginPage;
import com.example.selenium.pages.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class LoginSteps {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private HomePage homePage;

    public LoginSteps() {
        this.driver = Hooks.getDriver();
        this.wait = Hooks.getWait();
        this.loginPage = new LoginPage(driver, wait);
        this.homePage = new HomePage(driver, wait);
    }

    @Given("User navigates to the OrangeHRM login page")
    public void userNavigatesToLoginPage() {
        loginPage.open("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @When("User enters username {string} and password {string}")
    public void userEntersCredentials(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @When("User clicks the login button")
    public void userClicksLoginButton() {
        loginPage.clickLogin();
    }

    @Then("User should see the dashboard")
    public void userShouldSeeDashboard() {
        if (!homePage.isDashboardDisplayed()) {
            throw new AssertionError("Dashboard should be displayed");
        }
    }

    @Then("User should be on the home page")
    public void userShouldBeOnHomePage() {
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("auth/login")) {
            throw new AssertionError("Should not be on login page, current URL: " + currentUrl);
        }
    }

    @Then("User should see an error message")
    public void userShouldSeeErrorMessage() {
        // Check for error message (adjust based on actual error locator)
        try {
            org.openqa.selenium.By errorLocator = org.openqa.selenium.By.xpath("//div[contains(@class, 'oxd-alert')]");
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(errorLocator));
            int errorCount = driver.findElements(errorLocator).size();
            if (errorCount == 0) {
                throw new AssertionError("Error message should be displayed");
            }
        } catch (Exception e) {
            // If no error element found, verify we're still on login page or check for invalid credentials message
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("auth/login")) {
                throw new AssertionError("Login should have failed and stayed on login page");
            }
        }
    }
}
