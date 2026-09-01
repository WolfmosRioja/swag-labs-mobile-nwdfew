package page_objects.android;

import com.swaglabs.utils.Timeouts;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
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
 * Android implementation of the products list page.
 * Provides interactions for browsing products, adding to cart, and navigation.
 */
public class AndroidProductsPage extends BaseMobilePage<ProductsPageInterface.Element> implements ProductsPageInterface {

  private static final Logger LOGGER = LoggerFactory.getLogger(AndroidProductsPage.class);

  @AndroidFindBy(uiAutomator = "resourceId(\"menu-button\")")
  private WebElement menuButton;

  @AndroidFindBy(uiAutomator = "resourceId(\"cart-button\")")
  private WebElement cartButton;

  @AndroidFindBy(uiAutomator = "resourceId(\"sort-picker\")")
  private WebElement sortPicker;

  @AndroidFindBy(uiAutomator = "resourceId(\"product-0\")")
  private WebElement firstProduct;

  private static final String PRODUCT_CONTAINER = "product-%d";
  private static final String CART_ACTION = "cart-action-%d";
  private static final String PRODUCT_TITLE_ID = "product-title";
  private static final String PRODUCT_PRICE_ID = "product-price";
  private static final String PRODUCT_IMAGE_ID = "product-image";
  private static final String REMOVE_BUTTON_ID = "remove-%d";

  public AndroidProductsPage(AppiumDriver driver) {
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
    By locator = By.xpath(String.format("//android.view.ViewGroup[@resource-id='%s']", String.format(CART_ACTION, index)));
    WebElement action = waits.waitForElementClickableBy(locator, Timeouts.DEFAULT);
    action.click();
    LOGGER.debug("Added product at index {} to cart", index);
  }

  /**
   * Opens a product detail page by tapping the product at the given index.
   *
   * @param index zero-based index of the product to open
   */
  @Override
  public void openProduct(int index) {
    By locator = By.xpath(String.format("//android.view.ViewGroup[@resource-id='%s']", String.format(PRODUCT_CONTAINER, index)));
    WebElement product = waits.waitForElementBy(locator, Timeouts.DEFAULT);
    product.click();
    LOGGER.debug("Opened product at index {}", index);
  }

  /**
   * Returns the number of products currently displayed in the list.
   *
   * @return count of product elements
   */
  @Override
  public int getProductCount() {
    List<WebElement> products = driver.findElements(By.xpath("//android.view.ViewGroup[starts-with(@resource-id,'product-')]"));
    return products.size();
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
    String optionXpath = String.format("//android.widget.TextView[@text='%s']", option);
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
        By titleLocator = By.xpath(String.format("//android.view.ViewGroup[@resource-id='%s']//android.widget.TextView[@resource-id='%s']",
            String.format(PRODUCT_CONTAINER, i), PRODUCT_TITLE_ID));
        WebElement titleElement = waits.waitForElementBy(titleLocator, Timeouts.SHORT);
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
        By priceLocator = By.xpath(String.format("//android.view.ViewGroup[@resource-id='%s']//android.widget.TextView[@resource-id='%s']",
            String.format(PRODUCT_CONTAINER, i), PRODUCT_PRICE_ID));
        WebElement priceElement = waits.waitForElementBy(priceLocator, Timeouts.SHORT);
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

