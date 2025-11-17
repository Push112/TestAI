Feature: OrangeHRM Login
  As a user
  I want to log in to OrangeHRM
  So that I can access the dashboard

  Scenario: Successful login with valid credentials
    Given User navigates to the OrangeHRM login page
    When User enters username "Admin" and password "admin123"
    And User clicks the login button
    Then User should see the dashboard
    And User should be on the home page

  Scenario: Login failure with invalid password
    Given User navigates to the OrangeHRM login page
    When User enters username "Admin" and password "wrongpass"
    And User clicks the login button
    Then User should see an error message
