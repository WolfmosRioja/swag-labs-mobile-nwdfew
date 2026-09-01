@login
Feature: Login
  As a shopper
  I want to sign in to the Swag Labs store
  So that I can browse products and place an order

  Background:
    Given I am on the login page

  @smoke @sanity @sc-login-001
  Scenario: Standard user can log in successfully
    When I login as standard_user
    Then I should see the products page

  @smoke @sanity @sc-login-002
  Scenario: Locked out user is blocked from logging in
    When I login as locked_out_user
    Then I should see the login error message "Epic sadface: Sorry, this user has been locked out."
    And I should remain on the login page

  @smoke @sanity @sc-login-007
  Scenario: Login with empty credentials is rejected
    When I enter username ""
    And I enter password ""
    And I tap on the login button
    Then I should see the login error message

  @sanity @sc-login-008
  Scenario: Wrong password is rejected
    When I enter username "standard_user"
    And I enter password "wrong_password"
    And I tap on the login button
    Then I should see the login error message "Epic sadface: Username and password do not match"

  @sanity @sc-login-009
  Scenario: Username only (no password) is rejected
    When I enter username "standard_user"
    And I enter password ""
    And I tap on the login button
    Then I should see the login error message

  @sanity @sc-login-010
  Scenario: Password only (no username) is rejected
    When I enter username ""
    And I enter password "secret_sauce"
    And I tap on the login button
    Then I should see the login error message