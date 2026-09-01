package com.swaglabs.stepsDefs;

import com.swaglabs.exceptions.ExceptionController;
import com.swaglabs.testdata.TestConstants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.PageObjectHelper;
import page_objects.interfaces.ProductDetailPageInterface;
import page_objects.interfaces.ProductsPageInterface;

import java.util.List;

/** Step definitions for the products list and product detail page. */
public class ProductsStepsDefs extends PageObjectHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductsStepsDefs.class.getName());

  @Then("I should see the products page")
  public void iShouldSeeTheProductsPage() {
    productsPage().checkElement(ProductsPageInterface.Element.PAGE);
    LOGGER.info("Products page is displayed");
  }

  @And("I should see at least {int} products in the catalog")
  public void iShouldSeeAtLeastProductsInTheCatalog(int expected) {
    int count = productsPage().getProductCount();
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(count)
        .as("Product catalog count")
        .isGreaterThanOrEqualTo(expected);
    softly.assertAll();
    LOGGER.info("Product catalog has {} products", count);
  }

  @When("I add the product at position {int} to the cart")
  public void iAddTheProductAtPositionToTheCart(int index) {
    productsPage().addToCart(index);
    LOGGER.info("Added product at position {} to the cart", index);
  }

  @When("I tap on the product at position {int}")
  public void iTapOnTheProductAtPosition(int index) {
    productsPage().openProduct(index);
    LOGGER.info("Tapped product at position {}", index);
  }

  @Then("I should see the product detail page")
  public void iShouldSeeTheProductDetailPage() {
    productDetailPage().checkElement(ProductDetailPageInterface.Element.PRODUCT_TITLE);
    productDetailPage().checkElement(ProductDetailPageInterface.Element.ADD_TO_CART_BUTTON);
    LOGGER.info("Product detail page is displayed");
  }

  @And("the product title should not be empty")
  public void theProductTitleShouldNotBeEmpty() {
    String title = productDetailPage().getProductTitle().orElse("");
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(title)
        .as("Product title")
        .isNotBlank();
    softly.assertAll();
    LOGGER.info("Product title: {}", title);
  }

  @And("I add the product to the cart from the detail page")
  public void iAddTheProductToTheCartFromTheDetailPage() {
    productDetailPage().clickElement(ProductDetailPageInterface.Element.ADD_TO_CART_BUTTON);
    LOGGER.info("Added product to the cart from detail page");
  }

  @And("I go back to the products list")
  public void iGoBackToTheProductsList() {
    productDetailPage().clickElement(ProductDetailPageInterface.Element.BACK_BUTTON);
    LOGGER.info("Navigated back to the products list");
  }

  @And("the cart badge should show at least {int}")
  public void theCartBadgeShouldShowAtLeast(int expected) {
    productsPage().getCartCountText().ifPresentOrElse(
        text -> {
          try {
            int value = Integer.parseInt(text);
            SoftAssertions softly = new SoftAssertions();
            softly.assertThat(value)
                .as("Cart badge count")
                .isGreaterThanOrEqualTo(expected);
            softly.assertAll();
            LOGGER.info("Cart badge shows: {}", value);
          } catch (NumberFormatException e) {
            ExceptionController.handleException("cart badge parsing", e);
          }
        },
        () -> ExceptionController.assertTrue("Cart badge should be visible but was not found", false)
    );
  }

  @When("I sort products by {string}")
  public void iSortProductsBy(String option) {
    productsPage().selectSortOption(option);
    LOGGER.info("Sorted products by: {}", option);
  }

  @Then("the product list should be sorted by Name A-Z")
  public void theProductListShouldBeSortedByNameAZ() {
    verifySortOrder(TestConstants.SortOptions.NAME_A_TO_Z);
  }

  @Then("the product list should be sorted by Name descending")
  public void theProductListShouldBeSortedByNameDescending() {
    verifySortOrder(TestConstants.SortOptions.NAME_Z_TO_A);
  }

  @Then("the product list should be sorted by Price ascending")
  public void theProductListShouldBeSortedByPriceAscending() {
    verifySortOrder(TestConstants.SortOptions.PRICE_LOW_TO_HIGH);
  }

  @Then("the product list should be sorted by Price descending")
  public void theProductListShouldBeSortedByPriceDescending() {
    verifySortOrder(TestConstants.SortOptions.PRICE_HIGH_TO_LOW);
  }

  private void verifySortOrder(String expectedOrder) {
    List<String> titles = productsPage().getProductTitles();
    List<Double> prices = productsPage().getProductPrices();

    SoftAssertions softly = new SoftAssertions();

    switch (expectedOrder) {
      case TestConstants.SortOptions.NAME_A_TO_Z:
      case TestConstants.SortOptions.NAME_Z_TO_A:
        boolean nameSortedCorrectly = true;
        for (int i = 0; i < titles.size() - 1; i++) {
          int comparison = titles.get(i).compareToIgnoreCase(titles.get(i + 1));
          if ((expectedOrder.equals(TestConstants.SortOptions.NAME_A_TO_Z) && comparison > 0) ||
              (expectedOrder.equals(TestConstants.SortOptions.NAME_Z_TO_A) && comparison < 0)) {
            nameSortedCorrectly = false;
            softly.fail("Products not sorted by " + expectedOrder + " at position " + i +
                ": '" + titles.get(i) + "' vs '" + titles.get(i + 1) + "'");
            break;
          }
        }
        softly.assertThat(nameSortedCorrectly)
            .as("Product list sorted by " + expectedOrder)
            .isTrue();
        break;

      case TestConstants.SortOptions.PRICE_LOW_TO_HIGH:
      case TestConstants.SortOptions.PRICE_HIGH_TO_LOW:
        boolean priceSortedCorrectly = true;
        for (int i = 0; i < prices.size() - 1; i++) {
          if ((expectedOrder.equals(TestConstants.SortOptions.PRICE_LOW_TO_HIGH) && prices.get(i) > prices.get(i + 1)) ||
              (expectedOrder.equals(TestConstants.SortOptions.PRICE_HIGH_TO_LOW) && prices.get(i) < prices.get(i + 1))) {
            priceSortedCorrectly = false;
            softly.fail("Products not sorted by " + expectedOrder + " at position " + i +
                ": " + prices.get(i) + " vs " + prices.get(i + 1));
            break;
          }
        }
        softly.assertThat(priceSortedCorrectly)
            .as("Product list sorted by " + expectedOrder)
            .isTrue();
        break;

      default:
        throw new IllegalArgumentException("Unknown sort order: " + expectedOrder);
    }

    softly.assertAll();
    LOGGER.info("Verified products sorted by {}", expectedOrder);
  }
}