package page_objects.android;

import com.swaglabs.utils.Timeouts;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.BaseMobilePage;
import page_objects.interfaces.CartPageInterface;

/**
 * Android implementation of the shopping cart page.
 * Provides interactions for viewing cart items, removing items, and proceeding to checkout.
 */
public class AndroidCartPage extends BaseMobilePage<CartPageInterface.Element> implements CartPageInterface {

  private static final Logger LOGGER = LoggerFactory.getLogger(AndroidCartPage.class);

  @AndroidFindBy(uiAutomator = "resourceId(\"checkout-button\")")
  private WebElement checkoutButton;

  @AndroidFindBy(uiAutomator = "resourceId(\"continue-shopping\")")
  private WebElement continueShopping;

  @AndroidFindBy(uiAutomator = "resourceId(\"cart-button\")")
  private WebElement cartButton;

  public AndroidCartPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  protected WebElement[] getElementsToCheck(CartPageInterface.Element element) {
    return switch (element) {
      case CHECKOUT_BUTTON -> new WebElement[] {checkoutButton};
      case CONTINUE_SHOPPING -> new WebElement[] {continueShopping};
      case EMPTY_CART_MESSAGE -> new WebElement[] {driver.findElement(By.xpath("//android.widget.TextView[@text='Your cart is empty']"))};
      default -> null;
    };
  }

  @Override
  public void clickElement(CartPageInterface.Element element) {
    switch (element) {
      case CHECKOUT_BUTTON:
        waits.waitForElementClickable(checkoutButton, Timeouts.MEDIUM).click();
        break;
      case CONTINUE_SHOPPING:
        waits.waitForElementClickable(continueShopping, Timeouts.DEFAULT).click();
        break;
      default:
        throw new IllegalArgumentException("Unsupported clickable element: " + element);
    }
  }

  /**
   * Removes an item from the cart by its index.
   *
   * @param index zero-based index of the item to remove
   */
  @Override
  public void removeItem(int index) {
    By locator = By.xpath(String.format("//android.view.ViewGroup[@resource-id='remove-%d']", index));
    WebElement remove = waits.waitForElementClickableBy(locator, Timeouts.DEFAULT);
    remove.click();
    LOGGER.debug("Removed cart item at index {}", index);
  }

  /**
   * Returns the number of items currently in the cart.
   *
   * @return count of cart items
   */
  @Override
  public int getItemCount() {
    return driver.findElements(By.xpath("//android.view.ViewGroup[starts-with(@resource-id,'remove-')]")).size();
  }

  /**
   * Checks if the cart is empty by looking for the empty cart message.
   *
   * @return true if cart is empty, false otherwise
   */
  @Override
  public boolean isCartEmpty() {
    return waits.checkIfElementIsShown(By.xpath("//android.widget.TextView[@text='Your cart is empty']"), Timeouts.MEDIUM);
  }

  /**
   * Returns the cart badge count text if visible.
   *
   * @return badge text (e.g., "1") or empty Optional if not present
   */
  @Override
  public Optional<String> getCartCountText() {
    try {
      WebElement badge = cartButton.findElement(By.xpath(".//android.widget.TextView[last()]"));
      String text = badge.getText().trim();
      return Optional.ofNullable(text).filter(t -> !t.isEmpty());
    } catch (Exception e) {
      LOGGER.debug("Cart count badge not found: {}", e.getMessage());
      return Optional.empty();
    }
  }

  /**
   * Returns the cart icon class attribute value.
   *
   * @return the class attribute of the cart button/icon
   */
  @Override
  public String getCartIconClass() {
    try {
      return cartButton.getAttribute("class");
    } catch (Exception e) {
      LOGGER.debug("Failed to get cart icon class: {}", e.getMessage());
      return "";
    }
  }

  /**
   * Returns true if the cart icon has the error class bug.
   * The error_user is known to have a cart icon with an erroneous CSS class.
   *
   * @return true if error class is present, false otherwise
   */
  @Override
  public boolean hasCartIconErrorClass() {
    String className = getCartIconClass();
    // The error class bug typically manifests as an unexpected class like "error" or "cart-error"
    return className != null && (className.contains("error") || className.contains("cart-error"));
  }
}

