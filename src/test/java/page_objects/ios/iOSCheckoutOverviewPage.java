package page_objects.ios;

import com.swaglabs.utils.Timeouts;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.BaseMobilePage;
import page_objects.interfaces.CheckoutOverviewPageInterface;

/**
 * iOS implementation of the checkout overview page.
 * Provides interactions for reviewing order details and finishing the order.
 */
public class iOSCheckoutOverviewPage extends BaseMobilePage<CheckoutOverviewPageInterface.Element> implements CheckoutOverviewPageInterface {

  private static final Logger LOGGER = LoggerFactory.getLogger(iOSCheckoutOverviewPage.class);

  @iOSXCUITFindBy(id = "finish-button")
  private WebElement finishButton;

  @iOSXCUITFindBy(id = "cancel-button")
  private WebElement cancelButton;

  public iOSCheckoutOverviewPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  protected WebElement[] getElementsToCheck(CheckoutOverviewPageInterface.Element element) {
    return switch (element) {
      case PAGE -> new WebElement[] {finishButton, cancelButton};
      case FINISH_BUTTON -> new WebElement[] {finishButton};
      case CANCEL_BUTTON -> new WebElement[] {cancelButton};
      case ITEM_TOTAL -> new WebElement[] {driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Item total:']"))};
      case TOTAL -> new WebElement[] {driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Total:']"))};
      default -> null;
    };
  }

  @Override
  public void clickElement(CheckoutOverviewPageInterface.Element element) {
    switch (element) {
      case FINISH_BUTTON:
        waits.waitForElementClickable(finishButton, Timeouts.MEDIUM).click();
        break;
      case CANCEL_BUTTON:
        waits.waitForElementClickable(cancelButton, Timeouts.DEFAULT).click();
        break;
      default:
        throw new IllegalArgumentException("Unsupported clickable element: " + element);
    }
  }

  /**
   * Returns the total price text from the order summary.
   *
   * @return total text (e.g., "Total: $58.29") or empty Optional if not found
   */
  @Override
  public Optional<String> getTotalText() {
    try {
      return driver.findElements(By.xpath("//XCUIElementTypeStaticText[starts-with(@name,'Total:')]"))
          .stream()
          .map(WebElement::getText)
          .map(String::trim)
          .filter(t -> !t.isEmpty())
          .findFirst();
    } catch (Exception e) {
      LOGGER.debug("Failed to get total text: {}", e.getMessage());
      return Optional.empty();
    }
  }

  /**
   * Completes the order by clicking the finish button.
   */
  @Override
  public void finishOrder() {
    waits.waitForElementClickable(finishButton, Timeouts.MEDIUM).click();
    LOGGER.debug("Clicked finish button to complete order");
  }

  /**
   * Cancels the order and returns to the previous page.
   */
  @Override
  public void cancelOrder() {
    waits.waitForElementClickable(cancelButton, Timeouts.DEFAULT).click();
    LOGGER.debug("Clicked cancel button to cancel order");
  }
}

