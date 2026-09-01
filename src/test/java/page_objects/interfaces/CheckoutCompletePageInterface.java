package page_objects.interfaces;

/** Contract for the checkout completion page. */
public interface CheckoutCompletePageInterface {

  enum Element {
    BACK_HOME_BUTTON,
    THANK_YOU_HEADER,
    PAGE
  }

  /** Checks a completion element is present. */
  void checkElement(Element element);

  /** Clicks the given element. */
  void clickElement(Element element);

  /** Clicks "Back Home". */
  void backHome();
}


