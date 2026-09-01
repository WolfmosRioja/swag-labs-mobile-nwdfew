package page_objects.interfaces;

/** Contract for the Swag Labs login page. */
public interface LoginPageInterface {

  enum Element {
    USERNAME_INPUT,
    PASSWORD_INPUT,
    LOGIN_BUTTON,
    LOGIN_ERROR,
    PAGE
  }

  /** Performs a login with the given credentials. */
  void login(String username, String password);

  /** Fills the given username. */
  void fillUsername(String username);

  /** Fills the given password. */
  void fillPassword(String password);

  /** Clicks the login button. */
  void clickLoginButton();

  /** Checks a login page element is present. */
  void checkElement(Element element);

/** Returns the login error message text (or empty when absent). */
  String getLoginErrorMessage();

  /** Waits for the login error message to appear and contain the expected text. */
  String waitForLoginErrorMessage(String expected);
}


