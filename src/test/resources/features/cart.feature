@cart
Feature: Shopping Cart
  As a shopper
  I want to manage the items in my cart
  So that I can control what I purchase

  @smoke @sanity
  Scenario Outline: User can add a product to the cart
    Given I am on the login page
    When I login as <user>
    And I add the product at position 0 to the cart
    And I open the cart
    Then I should see the cart page
    And the cart should contain 1 item

    Examples:
      | user           |
      | standard_user  |
      | error_user     |

  @smoke @sanity
  Scenario: User can remove an item from the cart
    Given I am on the login page
    When I login as standard_user
    And I add the product at position 0 to the cart
    And I open the cart
    And I remove the item at position 0 from the cart
    Then the cart should be empty