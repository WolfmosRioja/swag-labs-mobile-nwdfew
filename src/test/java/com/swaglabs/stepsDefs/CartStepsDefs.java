package com.swaglabs.stepsDefs;

import com.swaglabs.exceptions.ExceptionController;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.PageObjectHelper;
import page_objects.interfaces.CartPageInterface;

/** Step definitions for the shopping cart page. */
public class CartStepsDefs extends PageObjectHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(CartStepsDefs.class.getName());

  @When("I open the cart")
  public void iOpenTheCart() {
    productsPage().openCart();
    LOGGER.info("Opened the cart");
  }

  @Then("I should see the cart page")
  public void iShouldSeeTheCartPage() {
    cartPage().checkElement(CartPageInterface.Element.CHECKOUT_BUTTON);
    LOGGER.info("Cart page is displayed");
  }

  @And("the cart should contain {int} item(s)")
  public void theCartShouldContainItems(int expected) {
    int actual = cartPage().getItemCount();
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual)
        .as("Cart item count")
        .isEqualTo(expected);
    softly.assertAll();
    LOGGER.info("Cart has {} item(s)", actual);
  }

  @When("I remove the item at position {int} from the cart")
  public void iRemoveTheItemAtPositionFromTheCart(int index) {
    cartPage().removeItem(index);
    LOGGER.info("Removed cart item at position {}", index);
  }

  @Then("the cart should be empty")
  public void theCartShouldBeEmpty() {
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(cartPage().isCartEmpty())
        .as("Cart should be empty")
        .isTrue();
    softly.assertAll();
    LOGGER.info("Cart is empty");
  }

  @Then("the cart icon should display the error class bug")
  public void theCartIconShouldDisplayTheErrorClassBug() {
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(cartPage().hasCartIconErrorClass())
        .as("Cart icon should have error class bug for error_user")
        .isTrue();
    softly.assertAll();
    LOGGER.info("Confirmed cart icon error class bug for error_user: class='{}'", cartPage().getCartIconClass());
  }
}