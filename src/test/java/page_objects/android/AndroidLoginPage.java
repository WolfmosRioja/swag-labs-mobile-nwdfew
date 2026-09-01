package page_objects.android;

import com.swaglabs.utils.Timeouts;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.BaseMobilePage;
import page_objects.interfaces.LoginPageInterface;

/** Android implementation of the Swag Labs login page. */
public class AndroidLoginPage extends BaseMobilePage<LoginPageInterface.Element> implements LoginPageInterface {

  private static final Logger LOGGER = LoggerFactory.getLogger(AndroidLoginPage.class);

  @AndroidFindBy(uiAutomator = "resourceId(\"username-input\")")
  private WebElement usernameField;

  @AndroidFindBy(uiAutomator = "resourceId(\"password-input\")")
  private WebElement passwordField;

  @AndroidFindBy(uiAutomator = "resourceId(\"login-button\")")
  private WebElement loginButton;

  @AndroidFindBy(uiAutomator = "resourceId(\"login-error\")")
  private WebElement loginError;

  public AndroidLoginPage(AppiumDriver driver) {
    super(driver);
  }

  @Override
  public void login(String username, String password) {
    fillUsername(username);
    fillPassword(password);
    clickLoginButton();
  }

  @Override
  public void fillUsername(String username) {
    usernameField.click();
    usernameField.clear();
    usernameField.sendKeys(username);
  }

  @Override
  public void fillPassword(String password) {
    passwordField.click();
    passwordField.clear();
    passwordField.sendKeys(password);
  }

  @Override
  public void clickLoginButton() {
    waits.waitForElementClickable(loginButton, Timeouts.DEFAULT).click();
  }

  @Override
  protected WebElement[] getElementsToCheck(LoginPageInterface.Element element) {
    return switch (element) {
      case PAGE -> new WebElement[] {usernameField, passwordField, loginButton};
      case USERNAME_INPUT -> new WebElement[] {usernameField};
      case PASSWORD_INPUT -> new WebElement[] {passwordField};
      case LOGIN_BUTTON -> new WebElement[] {loginButton};
      case LOGIN_ERROR -> new WebElement[] {loginError};
    };
  }

@Override
  public String getLoginErrorMessage() {
    try {
      waits.waitForElement(loginError, Timeouts.MEDIUM);
      String text = waits.waitForNonBlankText(loginError, Timeouts.LONG);
      if (text != null && !text.trim().isEmpty()) {
        return text;
      }
    } catch (Exception e) {
      // fall back to original XPath search from driver root
    }
    try {
      WebDriverWait wait = new WebDriverWait(driver, Timeouts.LONG);
      String result = wait.until(webDriver -> {
        for (WebElement textEl : driver.findElements(
            By.xpath("//*[contains(@resource-id,'login-error')]//android.widget.TextView"))) {
          String value = textEl.getText();
          if (value != null && !value.trim().isEmpty()) {
            return value;
          }
        }
        return null;
      });
      if (result != null && !result.trim().isEmpty()) {
        return result;
      }
    } catch (Exception e) {
      // ignore
    }
    return "";
  }

  @Override
  public String waitForLoginErrorMessage(String expected) {
    By errorContainerLocator = By.xpath("//*[contains(@resource-id,'login-error')]");
    By errorTextLocator = By.xpath("//*[contains(@resource-id,'login-error')]//android.widget.TextView");
    // Fallback: any element containing the error text
    By fallbackLocator = By.xpath("//*[contains(@text,'Epic sadface') or contains(@text,'Sorry')]");
    WebDriverWait wait = new WebDriverWait(driver, Timeouts.LONG);
    
    // First wait for the container to be visible
    wait.until(ExpectedConditions.visibilityOfElementLocated(errorContainerLocator));
    
    // Then wait for the text to appear in the container or its children
    return wait.until(webDriver -> {
      // Try direct text on container
      WebElement container = driver.findElement(errorContainerLocator);
      String containerText = container.getText();
      if (containerText != null && containerText.contains(expected)) {
        return containerText;
      }
      // Try child TextViews
      for (WebElement textEl : driver.findElements(errorTextLocator)) {
        String value = textEl.getText();
        if (value != null && value.contains(expected)) {
          return value;
        }
      }
      // Fallback: search for any element with the error text
      for (WebElement textEl : driver.findElements(fallbackLocator)) {
        String value = textEl.getText();
        if (value != null && value.contains(expected)) {
          return value;
        }
      }
      return null;
    });
  }
}


