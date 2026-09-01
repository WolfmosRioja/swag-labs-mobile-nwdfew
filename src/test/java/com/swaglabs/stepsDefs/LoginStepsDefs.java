package com.swaglabs.stepsDefs;

import com.swaglabs.exceptions.ExceptionController;
import com.swaglabs.managers.PropertiesManager;
import com.swaglabs.testdata.TestConstants;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.PageObjectHelper;
import page_objects.interfaces.LoginPageInterface;

/** Step definitions for the Swag Labs login flow. */
public class LoginStepsDefs extends PageObjectHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginStepsDefs.class.getName());

  private String password() {
    return PropertiesManager.getInstance().getProperty(PropertiesManager.Property.SWAG_PASSWORD);
  }

  @Given("I am on the login page")
  public void iAmOnTheLoginPage() {
    LoginPageInterface login = loginPage();
    login.checkElement(LoginPageInterface.Element.PAGE);
    LOGGER.info("User lands on the login page");
  }

  @When("I login as {swagUser}")
  public void iLoginAs(String user) {
    loginPage().login(user, password());
    LOGGER.info("Logged in as {}", user);
  }

  @And("I enter username {string}")
  public void iEnterUsername(String username) {
    loginPage().fillUsername(username);
  }

  @And("I enter password {string}")
  public void iEnterPassword(String password) {
    loginPage().fillPassword(password);
  }

  @And("I tap on the login button")
  public void iTapOnTheLoginButton() {
    loginPage().clickLoginButton();
  }

  @Then("I should see the login error message")
  public void iShouldSeeTheLoginErrorMessage() {
    loginPage().checkElement(LoginPageInterface.Element.LOGIN_ERROR);
    LOGGER.info("Login error message is displayed");
  }

  @Then("I should see the login error message {string}")
  public void iShouldSeeTheLoginErrorMessage(String expected) {
    loginPage().checkElement(LoginPageInterface.Element.LOGIN_ERROR);
    String actual = loginPage().waitForLoginErrorMessage(expected);
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual)
        .as("Login error message")
        .contains(expected);
    softly.assertAll();
    LOGGER.info("Login error verified: {}", actual);
  }

  @Then("I should remain on the login page")
  public void iShouldRemainOnTheLoginPage() {
    loginPage().checkElement(LoginPageInterface.Element.PAGE);
  }

  @And("I should see the locked out error message")
  public void iShouldSeeTheLockedOutErrorMessage() {
    iShouldSeeTheLoginErrorMessage(TestConstants.ErrorMessages.LOCKED_OUT_USER);
  }

  @And("I should see the invalid credentials error message")
  public void iShouldSeeTheInvalidCredentialsErrorMessage() {
    iShouldSeeTheLoginErrorMessage(TestConstants.ErrorMessages.INVALID_CREDENTIALS);
  }
}