package page_objects.ios;

import com.swaglabs.utils.Timeouts;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.iOSXCUITBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.BaseMobilePage;
import page_objects.interfaces.ProductsPageInterface;

/**
 * iOS implementation of the products list page.
 * Provides interactions for browsing products, adding to cart, and navigation.
 */
public class iOSProductsPage extends BaseMobilePage<ProductsPageInterface.Element> implements ProductsPageInterface {

  private static final Logger LOGGER = LoggerFactory.getLogger(iOSProductsPage.class);

  @iOSXCUITFindBy(id = "menu-button")
  private WebElement menuButton;

  @iOSXCUITFindBy(id = "cart-button")
  private WebElement cartButton;

  @iOSXCUITFindBy(id = "sort-picker")
  private WebElement sortPicker;

  private static final String PRODUCT =
      "(//XCUIElementTypeOther[contains(@name,'Sauce Labs')])[%d]";

  public iOSProductsPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  protected WebElement[] getElementsToCheck(ProductsPageInterface.Element element) {
    return switch (element) {
      case PAGE -> new WebElement[] {menuButton, cartButton, sortPicker};
      case MENU_BUTTON -> new WebElement[] {menuButton};
      case CART_BUTTON -> new WebElement[] {cartButton};
      case SORT_PICKER -> new WebElement[] {sortPicker};
      case CART_COUNT -> new WebElement[] {cartButton};
      default -> null;
    };
  }

  @Override
  public void clickElement(ProductsPageInterface.Element element) {
    switch (element) {
      case MENU_BUTTON:
        waits.waitForElementClickable(menuButton, Timeouts.DEFAULT).click();
        break;
      case CART_BUTTON:
        waits.waitForElementClickable(cartButton, Timeouts.DEFAULT).click();
        break;
      case SORT_PICKER:
        waits.waitForElementClickable(sortPicker, Timeouts.DEFAULT).click();
        break;
      default:
        throw new IllegalArgumentException("Unsupported clickable element: " + element);
    }
  }

  /**
   * Adds a product to the cart by its index in the list.
   *
   * @param index zero-based index of the product to add
   */
  @Override
  public void addToCart(int index) {
    String id = "cart-action-" + index;
    WebElement el = driver.findElement(By.id(id));
    waits.waitForElementClickable(el, Timeouts.MEDIUM).click();
    LOGGER.debug("Added product at index {} to cart", index);
  }

  /**
   * Opens a product detail page by tapping the product at the given index.
   *
   * @param index zero-based index of the product to open
   */
  @Override
  public void openProduct(int index) {
    String id = "product-" + index;
    WebElement el = driver.findElement(By.id(id));
    waits.waitForElement(el, Timeouts.MEDIUM).click();
    LOGGER.debug("Opened product at index {}", index);
  }

  /**
   * Returns the number of products currently displayed in the list.
   *
   * @return count of product elements
   */
  @Override
  public int getProductCount() {
    return driver.findElements(By.xpath("//XCUIElementTypeImage")).size();
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

  /**
   * Opens the side menu drawer.
   */
  @Override
  public void openMenu() {
    waits.waitForElementClickable(menuButton, Timeouts.DEFAULT).click();
  }

  /**
   * Opens the shopping cart page.
   */
  @Override
  public void openCart() {
    waits.waitForElementClickable(cartButton, Timeouts.DEFAULT).click();
  }

  /**
   * Selects a sort option from the sort dropdown.
   *
   * @param option the sort option to select (e.g., "Name (A to Z)", "Price (low to high)")
   */
  @Override
  public void selectSortOption(String option) {
    waits.waitForElementClickable(sortPicker, Timeouts.DEFAULT).click();
    String optionXpath = String.format("//XCUIElementTypeStaticText[@name='%s']", option);
    WebElement optionElement = waits.waitForElementClickableBy(By.xpath(optionXpath), Timeouts.DEFAULT);
    optionElement.click();
    LOGGER.debug("Selected sort option: {}", option);
  }

  /**
   * Returns the list of product titles in current order.
   *
   * @return list of product titles
   */
  @Override
  public List<String> getProductTitles() {
    List<String> titles = new ArrayList<>();
    int count = getProductCount();
    for (int i = 0; i < count; i++) {
      try {
        String id = "product-title-" + i;
        WebElement titleElement = waits.waitForElementBy(By.id(id), Timeouts.SHORT);
        titles.add(titleElement.getText().trim());
      } catch (Exception e) {
        LOGGER.warn("Failed to get title for product index {}: {}", i, e.getMessage());
        titles.add("");
      }
    }
    return titles;
  }

  /**
   * Returns the list of product prices in current order.
   *
   * @return list of product prices
   */
  @Override
  public List<Double> getProductPrices() {
    List<Double> prices = new ArrayList<>();
    int count = getProductCount();
    for (int i = 0; i < count; i++) {
      try {
        String id = "product-price-" + i;
        WebElement priceElement = waits.waitForElementBy(By.id(id), Timeouts.SHORT);
        String priceText = priceElement.getText().trim().replace("$", "");
        prices.add(Double.parseDouble(priceText));
      } catch (Exception e) {
        LOGGER.warn("Failed to get price for product index {}: {}", i, e.getMessage());
        prices.add(0.0);
      }
    }
    return prices;
  }
}

