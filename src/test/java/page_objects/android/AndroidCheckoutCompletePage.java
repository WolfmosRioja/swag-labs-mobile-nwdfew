package page_objects.android;

import com.swaglabs.utils.Timeouts;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.BaseMobilePage;
import page_objects.interfaces.CheckoutCompletePageInterface;

/**
 * Android implementation of the checkout completion page.
 * Provides verification of successful order completion and navigation back home.
 */
public class AndroidCheckoutCompletePage extends BaseMobilePage<CheckoutCompletePageInterface.Element> implements CheckoutCompletePageInterface {

  private static final Logger LOGGER = LoggerFactory.getLogger(AndroidCheckoutCompletePage.class);

  @AndroidFindBy(uiAutomator = "resourceId(\"back-home-button\")")
  private WebElement backHomeButton;

  public AndroidCheckoutCompletePage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  protected WebElement[] getElementsToCheck(CheckoutCompletePageInterface.Element element) {
    return switch (element) {
      case BACK_HOME_BUTTON -> new WebElement[] {backHomeButton};
      case PAGE -> new WebElement[] {backHomeButton};
      case THANK_YOU_HEADER -> new WebElement[] {
          driver.findElement(By.xpath(
              "//android.widget.TextView[contains(translate(@text,'THANK YOU','thank you'),'thank you for your order')]"))};
      default -> null;
    };
  }

  @Override
  public void clickElement(CheckoutCompletePageInterface.Element element) {
    switch (element) {
      case BACK_HOME_BUTTON:
        waits.waitForElementClickable(backHomeButton, Timeouts.MEDIUM).click();
        break;
      default:
        throw new IllegalArgumentException("Unsupported clickable element: " + element);
    }
  }

  /**
   * Returns to the home/products page after order completion.
   */
  @Override
  public void backHome() {
    waits.waitForElementClickable(backHomeButton, Timeouts.MEDIUM).click();
    LOGGER.debug("Clicked back home button");
  }
}

