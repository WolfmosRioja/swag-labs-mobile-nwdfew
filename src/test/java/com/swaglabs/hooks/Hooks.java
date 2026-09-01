package com.swaglabs.hooks;

import com.swaglabs.capabilities.CapabilityManager;
import com.swaglabs.exceptions.ExceptionController;
import com.swaglabs.managers.AppiumDriverManager;
import com.swaglabs.managers.AppiumServerManager;
import com.swaglabs.managers.ScenarioManager;
import com.swaglabs.utils.helper.WebdriverHelper;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import page_objects.PageObjectHelper;

/** Cucumber hooks: starts/stops the Appium session and handles cleanup. */
public class Hooks extends PageObjectHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class.getName());

  @BeforeAll
  public static void beforeAll() {
    LOGGER.info(
        "Swag Labs automation started. Platform={}, Environment={}",
        CapabilityManager.getInstance().getPlatform(),
        CapabilityManager.getInstance().getEnvironment());
  }

  @Before
  public void beforeScenario(Scenario scenario) {
    ScenarioManager.setScenario(scenario);
    ExceptionController.regHookFail(msg -> Assert.fail(msg));
    LOGGER.info("******* Scenario: {} started *******", scenario.getName());
    try {
      AppiumServerManager.startAppiumServer();
      AppiumDriverManager.startAppiumDriverInstance();
      appiumHelperSelector().prepareExecution();
      dismissAutofillDialogIfPresent();
      WebdriverHelper.startRecording(AppiumDriverManager.getDriver());
    } catch (Exception e) {
      ExceptionController.hookFail("Failed to initialize test environment", e);
    }
  }

  @After(order = 3)
  public void afterScenario(Scenario scenario) {
    if (scenario.isFailed()) {
      AppiumDriver driver = AppiumDriverManager.getDriver();
      LOGGER.info("Scenario '{}' FAILED. Capturing screenshot...", scenario.getName());
      if (driver != null) {
        try {
          WebdriverHelper.getScreenshot(scenario, driver);
        } catch (Exception e) {
          LOGGER.warn("Problem capturing screenshot: {}", e.getMessage());
        }
      } else {
        LOGGER.warn("Driver is null — skipping screenshot");
      }
    } else {
      LOGGER.info("Scenario '{}' PASSED.", scenario.getName());
    }
  }

  @After(order = 2)
  public void afterScenarioRecord(Scenario scenario) {
    if (AppiumDriverManager.getDriver() == null) {
      return;
    }
    try {
      WebdriverHelper.stopRecording(scenario, AppiumDriverManager.getDriver());
    } catch (Exception e) {
      LOGGER.warn("Problem stopping recording: {}", e.getMessage());
    }
  }

  @After(order = 1)
  public void afterDriverCleanup() {
    AppiumDriverManager.stopAppiumDriver();
    cleanUpInstances();
  }

  @AfterAll
  public static void afterAll() {
    AppiumServerManager.stopAppiumServer();
    LOGGER.info("Swag Labs automation finished.");
  }

  /**
   * The Google Password Manager autofill dialog can appear after login on the
   * physical Android device. It must be dismissed so it does not interfere with
   * the following steps.
   */
  private void dismissAutofillDialogIfPresent() {
    AppiumDriver driver = AppiumDriverManager.getDriver();
    if (driver == null || !CapabilityManager.getInstance().isAndroid()) {
      return;
    }
    try {
      java.util.List<org.openqa.selenium.WebElement> elements =
          driver.findElements(
              By.xpath(
                  "//android.widget.Button[contains(@text,'No thanks') or contains(@text,'Dismiss') "
                      + "or contains(@text,\"Don't ask again\") "
                      + "or contains(@text,'Close')]"));
      if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
        elements.get(0).click();
        LOGGER.info("Dismissed Google Password Manager autofill dialog");
      }
    } catch (Exception e) {
      LOGGER.debug("No autofill dialog to dismiss: {}", e.getMessage());
    }
  }
}
