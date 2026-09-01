package page_objects.android;

import com.swaglabs.utils.Timeouts;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.util.List;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.BaseMobilePage;
import page_objects.interfaces.ProductDetailPageInterface;

/**
 * Android implementation of the product detail page.
 * Provides interactions for viewing product details and adding to cart.
 */
public class AndroidProductDetailPage extends BaseMobilePage<ProductDetailPageInterface.Element> implements ProductDetailPageInterface {

  private static final Logger LOGGER = LoggerFactory.getLogger(AndroidProductDetailPage.class);

  @AndroidFindBy(uiAutomator = "resourceId(\"back-button\")")
  private WebElement backButton;

  @AndroidFindBy(uiAutomator = "resourceId(\"detail-cart-button\")")
  private WebElement addToCartButton;

  public AndroidProductDetailPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  protected WebElement[] getElementsToCheck(ProductDetailPageInterface.Element element) {
    return switch (element) {
      case BACK_BUTTON -> new WebElement[] {backButton};
      case ADD_TO_CART_BUTTON -> new WebElement[] {addToCartButton};
      case PRODUCT_TITLE -> new WebElement[] {waits.waitForElementBy(titleLocator(), Timeouts.DEFAULT)};
      default -> null;
    };
  }

  @Override
  public void clickElement(ProductDetailPageInterface.Element element) {
    switch (element) {
      case BACK_BUTTON:
        waits.waitForElementClickable(backButton, Timeouts.DEFAULT).click();
        break;
      case ADD_TO_CART_BUTTON:
        waits.waitForElementClickable(addToCartButton, Timeouts.MEDIUM).click();
        break;
      default:
        throw new IllegalArgumentException("Unsupported clickable element: " + element);
    }
  }

  /**
   * Returns the product title text.
   *
   * @return product title or empty Optional if not found
   */
  @Override
  public Optional<String> getProductTitle() {
    try {
      WebElement titleElement = getTitleElement();
      return Optional.ofNullable(titleElement.getText().trim()).filter(t -> !t.isEmpty());
    } catch (Exception e) {
      LOGGER.debug("Failed to get product title: {}", e.getMessage());
      return Optional.empty();
    }
  }

  /**
   * Returns the product price text.
   *
   * @return price text (e.g., "$29.99") or empty Optional if not found
   */
  @Override
  public Optional<String> getProductPrice() {
    try {
      List<WebElement> prices = driver.findElements(By.xpath("//android.widget.TextView[starts-with(@text,'$')]"));
      if (!prices.isEmpty()) {
        return Optional.ofNullable(prices.get(0).getText().trim()).filter(t -> !t.isEmpty());
      }
      return Optional.empty();
    } catch (Exception e) {
      LOGGER.debug("Failed to get product price: {}", e.getMessage());
      return Optional.empty();
    }
  }

  /**
   * Returns the locator for the product title element.
   * Title is the first non-price, non-button TextView on the detail page.
   *
   * @return By locator for the title element
   */
  private By titleLocator() {
    return By.xpath(
        "//android.widget.TextView[not(contains(@text,'$')) and not(contains(@text,'BACK')) "
            + "and not(contains(@text,'ADD')) and translate(@text,' ','')!='']");
  }

  /**
   * Resolves the title element by locator to avoid stale element references
   * after navigation from the product list.
   *
   * @return the title WebElement
   */
  private WebElement getTitleElement() {
    return waits.waitForElementBy(titleLocator(), Timeouts.DEFAULT);
  }
}

