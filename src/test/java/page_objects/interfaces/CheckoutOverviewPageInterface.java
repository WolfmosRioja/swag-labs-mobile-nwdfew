package page_objects.interfaces;

import java.util.Optional;

/** Contract for the checkout overview (review) page. */
public interface CheckoutOverviewPageInterface {

  enum Element {
    FINISH_BUTTON,
    CANCEL_BUTTON,
    ITEM_TOTAL,
    TOTAL,
    PAGE
  }

  /** Checks an overview element is present. */
  void checkElement(Element element);

  /** Clicks the given element. */
  void clickElement(Element element);

  /** Returns the total text (e.g. "Total: $29.99"), or empty if not found. */
  Optional<String> getTotalText();

  /** Clicks finish. */
  void finishOrder();

  /** Clicks cancel. */
  void cancelOrder();
}


