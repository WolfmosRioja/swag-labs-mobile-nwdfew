package page_objects.ios;

import com.swaglabs.utils.Timeouts;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import java.util.Optional;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.BaseMobilePage;
import page_objects.interfaces.CheckoutInfoPageInterface;

/**
 * iOS implementation of the checkout "Your Info" page.
 * Provides interactions for entering shipping information and proceeding to overview.
 */
public class iOSCheckoutInfoPage extends BaseMobilePage<CheckoutInfoPageInterface.Element> implements CheckoutInfoPageInterface {

  private static final Logger LOGGER = LoggerFactory.getLogger(iOSCheckoutInfoPage.class);

  @iOSXCUITFindBy(id = "firstName-input")
  private WebElement firstNameInput;

  @iOSXCUITFindBy(id = "lastName-input")
  private WebElement lastNameInput;

  @iOSXCUITFindBy(id = "zipCode-input")
  private WebElement zipCodeInput;

  @iOSXCUITFindBy(id = "continue-button")
  private WebElement continueButton;

  @iOSXCUITFindBy(id = "cancel-button")
  private WebElement cancelButton;

  @iOSXCUITFindBy(id = "checkout-error")
  private WebElement checkoutError;

  public iOSCheckoutInfoPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  protected WebElement[] getElementsToCheck(CheckoutInfoPageInterface.Element element) {
    return switch (element) {
      case PAGE -> new WebElement[] {firstNameInput, lastNameInput, zipCodeInput, continueButton};
      case FIRST_NAME_INPUT -> new WebElement[] {firstNameInput};
      case LAST_NAME_INPUT -> new WebElement[] {lastNameInput};
      case ZIP_CODE_INPUT -> new WebElement[] {zipCodeInput};
      case CONTINUE_BUTTON -> new WebElement[] {continueButton};
      case CANCEL_BUTTON -> new WebElement[] {cancelButton};
      default -> null;
    };
  }

  @Override
  public void clickElement(CheckoutInfoPageInterface.Element element) {
    switch (element) {
      case CONTINUE_BUTTON:
        waits.waitForElementClickable(continueButton, Timeouts.MEDIUM).click();
        break;
      case CANCEL_BUTTON:
        waits.waitForElementClickable(cancelButton, Timeouts.DEFAULT).click();
        break;
      default:
        throw new IllegalArgumentException("Unsupported clickable element: " + element);
    }
  }

  /**
   * Fills the shipping information form.
   *
   * @param firstName first name
   * @param lastName last name
   * @param zipCode postal code
   */
  @Override
  public void fillShippingInfo(String firstName, String lastName, String zipCode) {
    firstNameInput.click();
    firstNameInput.clear();
    firstNameInput.sendKeys(firstName);
    lastNameInput.click();
    lastNameInput.clear();
    lastNameInput.sendKeys(lastName);
    zipCodeInput.click();
    zipCodeInput.clear();
    zipCodeInput.sendKeys(zipCode);
    LOGGER.debug("Filled shipping info: {} {} {}", firstName, lastName, zipCode);
  }

  /**
   * Clicks the continue button to proceed to checkout overview.
   */
  @Override
  public void continueCheckout() {
    waits.waitForElementClickable(continueButton, Timeouts.MEDIUM).click();
  }

  /**
   * Returns the checkout error message text if visible.
   *
   * @return error message or empty Optional if not present
   */
  @Override
  public Optional<String> getCheckoutErrorMessage() {
    try {
      waits.waitForElement(checkoutError, Timeouts.SHORT);
      String text = waits.waitForNonBlankText(checkoutError, Timeouts.MEDIUM);
      if (text != null && !text.trim().isEmpty()) {
        return Optional.of(text.trim());
      }
    } catch (Exception e) {
      LOGGER.debug("Primary error element not found: {}", e.getMessage());
    }
    return Optional.empty();
  }

  /**
   * Cancels the checkout and returns to the cart.
   */
  @Override
  public void cancelCheckout() {
    waits.waitForElementClickable(cancelButton, Timeouts.DEFAULT).click();
  }
}

