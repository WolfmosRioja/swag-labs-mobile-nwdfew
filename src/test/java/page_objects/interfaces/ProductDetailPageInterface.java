package page_objects.interfaces;

import java.util.Optional;

/** Contract for the product detail page. */
public interface ProductDetailPageInterface {

  enum Element {
    BACK_BUTTON,
    ADD_TO_CART_BUTTON,
    PRODUCT_TITLE
  }

  /** Checks a detail element is present. */
  void checkElement(Element element);

  /** Clicks the given element. */
  void clickElement(Element element);

  /** Returns the displayed product title, or empty if not found. */
  Optional<String> getProductTitle();

  /** Returns the displayed product price, or empty if not found. */
  Optional<String> getProductPrice();
}


