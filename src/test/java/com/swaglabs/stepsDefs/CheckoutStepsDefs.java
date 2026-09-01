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
import page_objects.interfaces.CheckoutCompletePageInterface;
import page_objects.interfaces.CheckoutInfoPageInterface;
import page_objects.interfaces.CheckoutOverviewPageInterface;

/** Step definitions for the checkout flow (info, overview, complete). */
public class CheckoutStepsDefs extends PageObjectHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutStepsDefs.class.getName());

  @When("I proceed to checkout")
  public void iProceedToCheckout() {
    cartPage().clickElement(CartPageInterface.Element.CHECKOUT_BUTTON);
    LOGGER.info("Proceeded to checkout");
  }

  @Then("I should see the checkout info page")
  public void iShouldSeeTheCheckoutInfoPage() {
    checkoutInfoPage().checkElement(CheckoutInfoPageInterface.Element.PAGE);
    LOGGER.info("Checkout info page is displayed");
  }

  @And("I enter my shipping information {string}, {string}, {string}")
  public void iEnterMyShippingInformation(String firstName, String lastName, String zipCode) {
    checkoutInfoPage().fillShippingInfo(firstName, lastName, zipCode);
    LOGGER.info("Entered shipping info: {} {} {}", firstName, lastName, zipCode);
  }

  @And("I continue to the overview page")
  public void iContinueToTheOverviewPage() {
    checkoutInfoPage().continueCheckout();
    LOGGER.info("Continued to overview page");
  }

  @Then("I should see the checkout overview page")
  public void iShouldSeeTheCheckoutOverviewPage() {
    checkoutOverviewPage().checkElement(CheckoutOverviewPageInterface.Element.PAGE);
    LOGGER.info("Checkout overview page is displayed");
  }

  @And("I should see the checkout error message {string}")
  public void iShouldSeeTheCheckoutErrorMessage(String expected) {
    String actual = checkoutInfoPage().getCheckoutErrorMessage().orElse("");
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual)
        .as("Checkout error message")
        .contains(expected);
    softly.assertAll();
    LOGGER.info("Checkout error verified: {}", actual);
  }

  @And("I should remain on the checkout info page")
  public void iShouldRemainOnTheCheckoutInfoPage() {
    checkoutInfoPage().checkElement(CheckoutInfoPageInterface.Element.PAGE);
    LOGGER.info("Still on the checkout info page");
  }

  @And("the total should equal {string}")
  public void theTotalShouldEqual(String expected) {
    String actual = checkoutOverviewPage().getTotalText().orElse("");
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual)
        .as("Order total")
        .isEqualTo(expected);
    softly.assertAll();
    LOGGER.info("Total verified: {}", actual);
  }

  @When("I finish the order")
  public void iFinishTheOrder() {
    checkoutOverviewPage().finishOrder();
    LOGGER.info("Finished the order");
  }

  @Then("I should see the checkout completion page")
  public void iShouldSeeTheCheckoutCompletionPage() {
    checkoutCompletePage().checkElement(CheckoutCompletePageInterface.Element.THANK_YOU_HEADER);
    checkoutCompletePage().checkElement(CheckoutCompletePageInterface.Element.BACK_HOME_BUTTON);
    LOGGER.info("Checkout completion page is displayed");
  }

  @And("I go back home")
  public void iGoBackHome() {
    checkoutCompletePage().backHome();
    LOGGER.info("Went back home");
  }
}