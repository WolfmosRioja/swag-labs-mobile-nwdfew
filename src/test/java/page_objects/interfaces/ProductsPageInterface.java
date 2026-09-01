package page_objects.interfaces;

import java.util.List;
import java.util.Optional;

/** Contract for the products list page. */
public interface ProductsPageInterface {

  enum Element {
    MENU_BUTTON,
    CART_BUTTON,
    SORT_PICKER,
    PAGE,
    CART_COUNT
  }

  /** Checks a products page element is present. */
  void checkElement(Element element);

  /** Clicks the given element. */
  void clickElement(Element element);

  /** Adds a product to the cart by its index on the list. */
  void addToCart(int index);

  /** Clicks a product card to open its detail page. */
  void openProduct(int index);

  /** Returns the number of products shown on the list. */
  int getProductCount();

  /** Returns the cart badge count as text (e.g. "1"), or empty if not present. */
  Optional<String> getCartCountText();

  /** Opens the menu drawer. */
  void openMenu();

  /** Opens the cart. */
  void openCart();

  /** Selects a sort option from the sort dropdown. */
  void selectSortOption(String option);

  /** Returns the list of product titles in current order. */
  List<String> getProductTitles();

  /** Returns the list of product prices in current order. */
  List<Double> getProductPrices();

  }

