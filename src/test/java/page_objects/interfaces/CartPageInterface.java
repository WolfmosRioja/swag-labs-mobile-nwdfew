package page_objects.interfaces;

import java.util.Optional;

/** Contract for the shopping cart page. */
public interface CartPageInterface {

  enum Element {
    CHECKOUT_BUTTON,
    CONTINUE_SHOPPING,
    EMPTY_CART_MESSAGE
  }

  /** Checks a cart element is present. */
  void checkElement(Element element);

  /** Clicks the given element. */
  void clickElement(Element element);

  /** Removes a cart item by index. */
  void removeItem(int index);

  /** Returns the number of items in the cart. */
  int getItemCount();

  /** Returns true when the cart is empty. */
  boolean isCartEmpty();

  /** Returns the cart badge count as text (e.g. "1"), or empty if not present. */
  Optional<String> getCartCountText();

  /** Returns the cart icon class attribute value. */
  String getCartIconClass();

  /** Returns true if the cart icon has the error class bug. */
  boolean hasCartIconErrorClass();
}

