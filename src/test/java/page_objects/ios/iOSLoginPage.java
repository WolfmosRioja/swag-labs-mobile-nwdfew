package page_objects.ios;

import com.swaglabs.utils.Timeouts;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import page_objects.BaseMobilePage;
import page_objects.interfaces.LoginPageInterface;

/** iOS implementation of the Swag Labs login page. */
public class iOSLoginPage extends BaseMobilePage<LoginPageInterface.Element> implements LoginPageInterface {

  @iOSXCUITFindBy(id = "username-input")
  private WebElement usernameField;

  @iOSXCUITFindBy(id = "password-input")
  private WebElement passwordField;

  @iOSXCUITFindBy(id = "login-button")
  private WebElement loginButton;

  @iOSXCUITFindBy(id = "login-error")
  private WebElement loginError;

  public iOSLoginPage(AppiumDriver driver) {
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
      return waits.waitForNonBlankText(loginError, Timeouts.LONG);
    } catch (Exception e) {
      return "";
    }
  }

  @Override
  public String waitForLoginErrorMessage(String expected) {
    WebDriverWait wait = new WebDriverWait(driver, Timeouts.LONG);
    return wait.until(webDriver -> {
      String text = getLoginErrorMessage();
      if (text != null && text.contains(expected)) {
        return text;
      }
      return null;
    });
  }
}


