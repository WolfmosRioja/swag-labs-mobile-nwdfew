package page_objects.interfaces;

import page_objects.interfaces.CheckoutCompletePageInterface;
import page_objects.interfaces.CheckoutInfoPageInterface;
import page_objects.interfaces.CheckoutOverviewPageInterface;
import page_objects.interfaces.CartPageInterface;
import page_objects.interfaces.LoginPageInterface;
import page_objects.interfaces.ProductDetailPageInterface;
import page_objects.interfaces.ProductsPageInterface;

/** Common page contract implemented by both Android and iOS page objects.
 *  Provides factory methods to obtain platform-specific page implementations. */
public interface CommonPageInterface {

  enum Status {
    ENABLED,
    DISABLED,
    VISIBLE,
    NOT_VISIBLE
  }

  /** Returns the login page implementation for the current platform. */
  LoginPageInterface loginPage();

  /** Returns the products list page implementation for the current platform. */
  ProductsPageInterface productsPage();

  /** Returns the product detail page implementation for the current platform. */
  ProductDetailPageInterface productDetailPage();

  /** Returns the shopping cart page implementation for the current platform. */
  CartPageInterface cartPage();

  /** Returns the checkout info page implementation for the current platform. */
  CheckoutInfoPageInterface checkoutInfoPage();

  /** Returns the checkout overview page implementation for the current platform. */
  CheckoutOverviewPageInterface checkoutOverviewPage();

  /** Returns the checkout complete page implementation for the current platform. */
  CheckoutCompletePageInterface checkoutCompletePage();
}

