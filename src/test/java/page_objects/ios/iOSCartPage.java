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
import page_objects.interfaces.CartPageInterface;

/**
 * iOS implementation of the shopping cart page.
 * Provides interactions for viewing cart items, removing items, and proceeding to checkout.
 */
public class iOSCartPage extends BaseMobilePage<CartPageInterface.Element> implements CartPageInterface {

  private static final Logger LOGGER = LoggerFactory.getLogger(iOSCartPage.class);

  @iOSXCUITFindBy(id = "checkout-button")
  private WebElement checkoutButton;

  @iOSXCUITFindBy(id = "continue-shopping")
  private WebElement continueShopping;

  @iOSXCUITFindBy(id = "cart-button")
  private WebElement cartButton;

  public iOSCartPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  protected WebElement[] getElementsToCheck(CartPageInterface.Element element) {
    return switch (element) {
      case CHECKOUT_BUTTON -> new WebElement[] {checkoutButton};
      case CONTINUE_SHOPPING -> new WebElement[] {continueShopping};
      case EMPTY_CART_MESSAGE -> new WebElement[] {driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Your cart is empty']"))};
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
    WebElement remove = driver.findElement(By.id("remove-" + index));
    waits.waitForElementClickable(remove, Timeouts.MEDIUM).click();
    LOGGER.debug("Removed cart item at index {}", index);
  }

  /**
   * Returns the number of items currently in the cart.
   *
   * @return count of cart items
   */
  @Override
  public int getItemCount() {
    return driver.findElements(By.xpath("//XCUIElementTypeOther[contains(@name,'remove-')]")).size();
  }

  /**
   * Checks if the cart is empty by looking for the empty cart message.
   *
   * @return true if cart is empty, false otherwise
   */
  @Override
  public boolean isCartEmpty() {
    return waits.checkIfElementIsShown(By.xpath("//XCUIElementTypeStaticText[@name='Your cart is empty']"), Timeouts.MEDIUM);
  }

  /**
   * Returns the cart badge count text if visible.
   *
   * @return badge text (e.g., "1") or empty Optional if not present
   */
  @Override
  public Optional<String> getCartCountText() {
    try {
      String name = cartButton.getAttribute("name");
      int comma = name.lastIndexOf(',');
      String text = comma >= 0 ? name.substring(comma + 1).trim() : "";
      return Optional.ofNullable(text).filter(t -> !t.isEmpty());
    } catch (Exception e) {
      LOGGER.debug("Cart count badge not found: {}", e.getMessage());
      return Optional.empty();
    }
  }

  @Override
  public String getCartIconClass() {
    return "";
  }

  @Override
  public boolean hasCartIconErrorClass() {
    return false;
  }
}

