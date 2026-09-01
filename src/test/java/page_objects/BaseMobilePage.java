package page_objects;

import com.swaglabs.exceptions.ExceptionController;
import com.swaglabs.utils.Timeouts;
import com.swaglabs.utils.Waits;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import java.time.Duration;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class for mobile page objects that provides a template method for
 * {@code checkElement} to reduce duplication across platform implementations.
 * <p>
 * Subclasses must implement {@link #getElementsToCheck(Object)} to return the
 * WebElements that should be verified for a given enum element constant.
 * </p>
 *
 * @param <E> the enum type representing page elements
 */
public abstract class BaseMobilePage<E extends Enum<E>> extends BasePage {

  protected BaseMobilePage(AppiumDriver driver) {
    super(driver);
  }

  /**
   * Verifies that the specified page element is present and visible.
   *
   * @param element the element enum constant to verify
   * @throws IllegalArgumentException if the element is not supported
   */
  public final void checkElement(E element) {
    WebElement[] elements = getElementsToCheck(element);
    if (elements == null || elements.length == 0) {
      throw new IllegalArgumentException("Unsupported element: " + element);
    }
    for (WebElement el : elements) {
      if (el != null) {
        try {
          waits.waitForElement(el);
        } catch (TimeoutException e) {
          ExceptionController.handleTimeout(element.toString(), e);
        } catch (NoSuchElementException e) {
          ExceptionController.handleNoSuchElement(element.toString(), e);
        }
      }
    }
  }

  /**
   * Returns the WebElement(s) to verify for the given element constant.
   * Subclasses must implement this to map their element enums to actual fields.
   *
   * @param element the element enum constant
   * @return array of WebElements to wait for, or null/empty if unsupported
   */
  protected abstract WebElement[] getElementsToCheck(E element);
}