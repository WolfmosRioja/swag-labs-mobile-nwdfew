package page_objects;

import com.swaglabs.capabilities.CapabilityManager;
import com.swaglabs.managers.AppiumDriverManager;
import com.swaglabs.utils.Waits;
import com.swaglabs.utils.helper.AndroidHelper;
import com.swaglabs.utils.helper.WebdriverHelper;
import com.swaglabs.utils.helper.iOSHelper;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.android.AndroidCommonPage;
import page_objects.interfaces.CartPageInterface;
import page_objects.interfaces.CheckoutCompletePageInterface;
import page_objects.interfaces.CheckoutInfoPageInterface;
import page_objects.interfaces.CheckoutOverviewPageInterface;
import page_objects.interfaces.CommonPageInterface;
import page_objects.interfaces.LoginPageInterface;
import page_objects.interfaces.ProductDetailPageInterface;
import page_objects.interfaces.ProductsPageInterface;
import page_objects.ios.iOSCommonPage;

/**
 * Base class for all step definitions. Provides access to the shared driver,
 * waits, platform helpers and the correct platform page object implementation,
 * mirroring the original framework's PageObjectHelper.
 */
public class PageObjectHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(PageObjectHelper.class.getName());

  private final ThreadLocal<Waits> waitsThreadLocal = new ThreadLocal<>();
  private final ThreadLocal<WebdriverHelper> helperThreadLocal = new ThreadLocal<>();
  private final ThreadLocal<CommonPageInterface> commonPageThreadLocal = new ThreadLocal<>();

  protected AppiumDriver getDriver() {
    return AppiumDriverManager.getDriver();
  }

  protected Waits waits() {
    if (waitsThreadLocal.get() == null) {
      waitsThreadLocal.set(new Waits(getDriver()));
    }
    return waitsThreadLocal.get();
  }

  protected WebdriverHelper appiumHelperSelector() {
    if (helperThreadLocal.get() == null) {
      helperThreadLocal.set(
          CapabilityManager.getInstance().isAndroid() ? new AndroidHelper() : new iOSHelper());
    }
    return helperThreadLocal.get();
  }

  /** Returns the platform-specific common page, creating it on first access. */
  protected CommonPageInterface commonPage() {
    if (commonPageThreadLocal.get() == null) {
      String platform = CapabilityManager.getInstance().isAndroid() ? "android" : "ios";
      switch (platform) {
        case "android":
          commonPageThreadLocal.set(new AndroidCommonPage(getDriver()));
          break;
        case "ios":
          commonPageThreadLocal.set(new iOSCommonPage(getDriver()));
          break;
        default:
          throw new IllegalStateException("Unsupported platform: " + platform);
      }
    }
    return commonPageThreadLocal.get();
  }

  // Delegate all page access to the common page factory

  public LoginPageInterface loginPage() {
    return commonPage().loginPage();
  }

  public ProductsPageInterface productsPage() {
    return commonPage().productsPage();
  }

  public ProductDetailPageInterface productDetailPage() {
    return commonPage().productDetailPage();
  }

  public CartPageInterface cartPage() {
    return commonPage().cartPage();
  }

  public CheckoutInfoPageInterface checkoutInfoPage() {
    return commonPage().checkoutInfoPage();
  }

  public CheckoutOverviewPageInterface checkoutOverviewPage() {
    return commonPage().checkoutOverviewPage();
  }

  public CheckoutCompletePageInterface checkoutCompletePage() {
    return commonPage().checkoutCompletePage();
  }

  /** Clears per-thread page-object instances between scenarios. */
  public void cleanUpInstances() {
    commonPageThreadLocal.remove();
    waitsThreadLocal.remove();
    helperThreadLocal.remove();
    LOGGER.info("Page object instances cleared");
  }
}

