package page_objects.interfaces;

import java.util.Optional;

/** Contract for the checkout "Your Info" page. */
public interface CheckoutInfoPageInterface {

  enum Element {
    FIRST_NAME_INPUT,
    LAST_NAME_INPUT,
    ZIP_CODE_INPUT,
    CONTINUE_BUTTON,
    CANCEL_BUTTON,
    PAGE
  }

  /** Checks a checkout info element is present. */
  void checkElement(Element element);

  /** Clicks the given element. */
  void clickElement(Element element);

  /** Fills the shipping details. */
  void fillShippingInfo(String firstName, String lastName, String zipCode);

  /** Clicks continue. */
  void continueCheckout();

  /** Returns the checkout info error message text (or empty when absent). */
  Optional<String> getCheckoutErrorMessage();

  /** Clicks cancel. */
  void cancelCheckout();
}


