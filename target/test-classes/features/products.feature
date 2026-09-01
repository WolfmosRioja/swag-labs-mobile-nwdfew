@products
Feature: Products Catalog
  As a shopper
  I want to browse the product catalog and view product details
  So that I can decide what to buy

  Background:
    Given I am on the login page

  @smoke @sanity @sc-prod-001
  Scenario: User can see the product catalog after login
    When I login as standard_user
    Then I should see the products page
    And I should see at least 4 products in the catalog

  @smoke @sanity @sc-cart-001 @sc-cart-003
  Scenario: User can add a product to the cart
    When I login as standard_user
    And I add the product at position 0 to the cart
    Then the cart badge should show at least 1

  @smoke @sanity
  Scenario: User can open a product and view its details
    When I login as standard_user
    And I tap on the product at position 0
    Then I should see the product detail page
    And the product title should not be empty

  @sanity
  Scenario: User can add a product from its detail page
    When I login as standard_user
    And I tap on the product at position 0
    And I add the product to the cart from the detail page
    And I go back to the products list
    Then the cart badge should show at least 1

  @sanity
  Scenario: Products sorted by default (Name A-Z)
    When I login as standard_user
    Then the product list should be sorted by Name A-Z

  @standard_user @sanity
  Scenario Outline: Sort products by <sortOption>
    When I login as standard_user
    And I sort products by "<sortOption>"
    Then the product list should be sorted by <expectedOrder>

  Examples:
    | sortOption           | expectedOrder       |
    | Price (low to high)  | Price ascending     |
    | Price (high to low)  | Price descending    |
    | Name (Z to A)        | Name descending     |