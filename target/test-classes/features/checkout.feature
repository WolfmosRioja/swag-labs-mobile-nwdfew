@checkout
Feature: Checkout
  As a shopper
  I want to complete a purchase
  So that the order is processed successfully

  @smoke @sanity
  Scenario Outline: User completes a full purchase
    Given I am on the login page
    When I login as <user>
    And I add the product at position 0 to the cart
    And I open the cart
    And I proceed to checkout
    Then I should see the checkout info page
    And I enter my shipping information "John", "Doe", "12345"
    And I continue to the overview page
    Then I should see the checkout overview page
    When I finish the order
    Then I should see the checkout completion page
    And I go back home

    Examples:
      | user           |
      | standard_user  |
      | problem_user   |
