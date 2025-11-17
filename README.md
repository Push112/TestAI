# TestAI - Selenium BDD Framework

A comprehensive Selenium + Cucumber + TestNG framework for testing OrangeHRM with Page Object Model (POM) design pattern.

## ✅ Status: All Tests Passing!

Both login scenarios execute successfully:
- ✅ Successful login with valid credentials (Admin/admin123)
- ✅ Login failure detection with invalid password

```
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS ✓
```

## Project Structure

```
src/
├── main/java/com/example/selenium/
│   ├── pages/               # Page Object Model
│   │   ├── LoginPage.java
│   │   └── HomePage.java
│   ├── base/                # Base classes
│   │   ├── BaseTest.java
│   │   └── BasePage.java
│   └── stepdefinitions/     # (in test for Cucumber integration)
├── test/java/com/example/selenium/
│   ├── stepdefinitions/     # Cucumber step definitions
│   │   ├── Hooks.java       # Setup/teardown
│   │   └── LoginSteps.java
│   └── runner/
│       └── TestRunner.java  # Cucumber + TestNG runner
└── test/resources/
    ├── features/
    │   └── login.feature    # Gherkin scenarios
    └── testng.xml           # TestNG configuration
```

## Dependencies

- **Selenium**: 4.11.0 (WebDriver + Selenium Manager)
- **Cucumber**: 7.14.0 (BDD framework)
- **TestNG**: 6.14.3 (Test execution framework, Java 8 compatible)
- **Java**: 1.8 (target)

## Quick Start

### 1. Install dependencies

```bash
mvn clean install
```

### 2. Run all tests (headless by default)

```bash
mvn test
```

### 3. Run with visible browser

```bash
mvn test -DnoHeadless=true
```

### 4. View HTML report

```bash
open target/cucumber-reports/cucumber.html
```

## Test Framework Features

✅ **Page Object Model**: Cleanly separates page locators and interactions  
✅ **BDD with Cucumber**: Write tests in human-readable Gherkin syntax  
✅ **TestNG Integration**: Sequential execution with proper test reporting  
✅ **Hooks with ThreadLocal**: Automatic WebDriver setup/teardown before/after each scenario  
✅ **Selenium Manager**: No manual chromedriver installation required  
✅ **Flexible Headless Mode**: Control with `-DnoHeadless=true` system property  
✅ **Robust Waits**: Explicit waits with 30-second timeout for element interactions  
✅ **Error Handling**: Retry logic and fallback element locators for stability

## Test Scenarios

### Scenario 1: Successful Login
- Navigate to OrangeHRM login page
- Enter credentials: Admin/admin123
- Click login button
- Verify dashboard is displayed

### Scenario 2: Failed Login
- Navigate to OrangeHRM login page
- Enter credentials: Admin/wrongpass
- Click login button
- Verify error message is shown

## Hooks & Driver Management

The `Hooks.java` class uses `ThreadLocal` for thread-safe WebDriver management:

```java
@Before  // Runs before each scenario
public void setUp() {
    WebDriver newDriver = new ChromeDriver(options);
    driver.set(newDriver);           // ThreadLocal storage
    wait.set(new WebDriverWait(...));
}

@After   // Runs after each scenario
public void tearDown() {
    WebDriver d = driver.get();
    if (d != null) d.quit();
    driver.remove();   // Clean up ThreadLocal
    wait.remove();
}
```

This ensures:
- Each scenario gets a fresh WebDriver instance
- No stale element references between scenarios
- Thread-safe driver access in step definitions
- Proper cleanup after each test

## Writing Tests

### 1. Create a Gherkin feature file

Create `src/test/resources/features/yourfeature.feature`:

```gherkin
Feature: User Login
  
  Scenario: Successful login
    Given User navigates to the OrangeHRM login page
    When User enters username "Admin" and password "admin123"
    And User clicks the login button
    Then User should see the dashboard
```

### 2. Implement step definitions

Create `src/test/java/com/example/selenium/stepdefinitions/YourSteps.java`:

```java
public class YourSteps {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    
    public YourSteps() {
        this.driver = Hooks.getDriver();
        this.wait = Hooks.getWait();
        this.loginPage = new LoginPage(driver, wait);
    }
    
    @Given("User navigates to the OrangeHRM login page")
    public void userNavigatesToLoginPage() {
        loginPage.open("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }
}
```

### 3. Use page objects

Create `src/main/java/com/example/selenium/pages/YourPage.java`:

```java
public class YourPage extends BasePage {
    private final By usernameField = By.name("username");
    
    public YourPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
    
    public void enterUsername(String username) {
        type(usernameField, username);
    }
}
```

## Advanced Configuration

### Increase Timeout
Edit `Hooks.java`:
```java
wait = new WebDriverWait(driver, Duration.ofSeconds(60));  // 60 seconds
```

### Disable Headless Mode
```bash
mvn test -DnoHeadless=true
```

### Use Custom URL
Edit `login.feature`:
```gherkin
Given User navigates to "https://custom-url.com/auth/login"
```

## Test Execution Flow

```
mvn test
  ├─ Compile Page Objects (src/main/java)
  ├─ Compile Step Definitions & Hooks (src/test/java)
  ├─ Run TestRunner (Cucumber with TestNG)
  │  ├─ @Before Hooks.setUp()
  │  │  └─ Create ChromeDriver + WebDriverWait (ThreadLocal)
  │  ├─ Execute Scenario 1: Successful Login
  │  ├─ @After Hooks.tearDown()
  │  │  └─ Quit driver + clean ThreadLocal
  │  ├─ @Before Hooks.setUp() 
  │  │  └─ Create new ChromeDriver for Scenario 2
  │  ├─ Execute Scenario 2: Failed Login
  │  ├─ @After Hooks.tearDown()
  │  │  └─ Quit driver + clean ThreadLocal
  ├─ Generate Reports
  │  ├─ target/cucumber-reports/cucumber.html
  │  └─ target/cucumber-reports/cucumber.json
  └─ BUILD SUCCESS ✓
```

## Reports

After running tests, view the HTML report:

```bash
open target/cucumber-reports/cucumber.html
```

The report includes:
- Feature name and description
- All scenarios with status (Pass/Fail)
- Step-by-step execution details
- Pass/Fail counts and duration

## Troubleshooting

### Chrome driver not found
- Selenium Manager should download it automatically
- If issues persist, install Chrome manually

### Tests timeout
- Increase timeout in `Hooks.java` to 60 seconds
- Verify network connectivity to OrangeHRM site

### Tests fail with "Dashboard not found"
- The XPath may need adjustment based on actual HTML
- Check browser to verify dashboard element
- Update `HomePage.java` isDashboardDisplayed() method

### StaleElementReference errors
- ✅ **Resolved**: ThreadLocal driver + sequential execution
- Uses explicit waits for element clickability
- Retry logic on click failures

### Connection refused to https://opensource-demo.orangehrmlive.com
- Verify internet connectivity
- Check if OrangeHRM site is accessible
- Verify system proxy settings if behind corporate network

## Environment Variables

Override credentials or URL via environment variables:

```bash
export ORANGEHRM_USER="Admin"
export ORANGEHRM_PASS="admin123"
mvn test
```

## CI/CD Integration

The project is configured for GitHub Actions (see `.github/workflows/selenium.yml`). On push/PR:

1. Installs JDK 1.8
2. Installs Chrome browser
3. Runs `mvn test`
4. Generates Cucumber HTML reports

## Next Steps

- Add more page objects (Logout, Employee List, etc.)
- Create data-driven scenarios with Cucumber Examples
- Integrate with CI/CD (GitHub Actions, Jenkins)
- Add API testing with Rest-Assured
- Implement custom Allure/Extent Reports
- Add parallel execution with proper driver isolation

---

**Built with**: Selenium 4.11.0 + Cucumber 7.14.0 + TestNG 6.14.3 + POM  
**Java**: 1.8 compatible  
**Status**: ✅ Production-Ready  
**Last Updated**: November 18, 2025
