package page_objects.ios;

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

/** iOS implementation of the common page factory. */
public class iOSCommonPage extends BasePage implements CommonPageInterface {

  public iOSCommonPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public LoginPageInterface loginPage() {
    return new iOSLoginPage(driver);
  }

  @Override
  public ProductsPageInterface productsPage() {
    return new iOSProductsPage(driver);
  }

  @Override
  public ProductDetailPageInterface productDetailPage() {
    return new iOSProductDetailPage(driver);
  }

  @Override
  public CartPageInterface cartPage() {
    return new iOSCartPage(driver);
  }

  @Override
  public CheckoutInfoPageInterface checkoutInfoPage() {
    return new iOSCheckoutInfoPage(driver);
  }

  @Override
  public CheckoutOverviewPageInterface checkoutOverviewPage() {
    return new iOSCheckoutOverviewPage(driver);
  }

  @Override
  public CheckoutCompletePageInterface checkoutCompletePage() {
    return new iOSCheckoutCompletePage(driver);
  }
}

