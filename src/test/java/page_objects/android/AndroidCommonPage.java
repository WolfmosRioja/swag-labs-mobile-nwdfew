package page_objects.android;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import page_objects.BasePage;
import page_objects.interfaces.CartPageInterface;
import page_objects.interfaces.CheckoutCompletePageInterface;
import page_objects.interfaces.CheckoutInfoPageInterface;
import page_objects.interfaces.CheckoutOverviewPageInterface;
import page_objects.interfaces.CommonPageInterface;
import page_objects.interfaces.LoginPageInterface;
import page_objects.interfaces.ProductDetailPageInterface;
import page_objects.interfaces.ProductsPageInterface;
import com.swaglabs.utils.Timeouts;

/** Android implementation of the common page factory. */
public class AndroidCommonPage extends BasePage implements CommonPageInterface {

  public AndroidCommonPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public LoginPageInterface loginPage() {
    return new AndroidLoginPage(driver);
  }

  @Override
  public ProductsPageInterface productsPage() {
    return new AndroidProductsPage(driver);
  }

  @Override
  public ProductDetailPageInterface productDetailPage() {
    return new AndroidProductDetailPage(driver);
  }

  @Override
  public CartPageInterface cartPage() {
    return new AndroidCartPage(driver);
  }

  @Override
  public CheckoutInfoPageInterface checkoutInfoPage() {
    return new AndroidCheckoutInfoPage(driver);
  }

  @Override
  public CheckoutOverviewPageInterface checkoutOverviewPage() {
    return new AndroidCheckoutOverviewPage(driver);
  }

  @Override
  public CheckoutCompletePageInterface checkoutCompletePage() {
    return new AndroidCheckoutCompletePage(driver);
  }
}

