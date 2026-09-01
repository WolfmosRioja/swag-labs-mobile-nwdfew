package page_objects;

import com.swaglabs.utils.Timeouts;
import com.swaglabs.utils.Waits;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import java.time.Duration;
import org.openqa.selenium.support.PageFactory;

/**
 * Shared base for all page objects. Initializes the PageFactory fields with an
 * Appium decorator and exposes the shared waits instance.
 */
public abstract class BasePage {

  protected AppiumDriver driver;
  protected Waits waits;

  public BasePage(AppiumDriver driver) {
    this.driver = driver;
    this.waits = new Waits(driver);
    PageFactory.initElements(new AppiumFieldDecorator(driver, Timeouts.DEFAULT), this);
  }
}
