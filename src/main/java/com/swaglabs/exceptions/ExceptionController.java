package com.swaglabs.exceptions;

import com.swaglabs.managers.ScenarioManager;
import io.cucumber.java.Scenario;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Centralized exception handling for tests. */
public class ExceptionController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class.getName());

  private static FailAssertion failAssertion = null;

  private ExceptionController() {}

  /** Registers the fail assertion (typically from Hooks). */
  public static void regHookFail(FailAssertion thrower) {
    failAssertion = thrower;
  }

  /** Fails the current scenario with a message and exception details. */
  public static void hookFail(String message, Exception error) {
    LOGGER.warn(message, error);
    if (failAssertion != null) {
      failAssertion.fail(message + " ---> " + error);
    }
  }

  /**
   * Handles element not found scenarios. Logs error and fails the test.
   * Screenshot is captured by @After hook.
   */
  public static void handleElementNotFound(String elementDescription, Exception cause) {
    String message = "Element not found: " + elementDescription;
    LOGGER.error(message, cause);
    if (failAssertion != null) {
      failAssertion.fail(message + " ---> " + cause.getClass().getSimpleName() + ": " + cause.getMessage());
    } else {
      throw new AssertionError(message, cause);
    }
  }

  /** Handles timeout waiting for element. */
  public static void handleTimeout(String elementDescription, TimeoutException cause) {
    handleElementNotFound(elementDescription + " (timeout)", cause);
  }

  /** Handles NoSuchElementException. */
  public static void handleNoSuchElement(String elementDescription, NoSuchElementException cause) {
    handleElementNotFound(elementDescription, cause);
  }

  /** Wraps an assertion with automatic fail via registered handler. */
  public static void assertTrue(String message, boolean condition) {
    if (!condition) {
      LOGGER.error("Assertion failed: {}", message);
      if (failAssertion != null) {
        failAssertion.fail(message);
      } else {
        throw new AssertionError(message);
      }
    }
  }

  /** Wraps an assertion with automatic fail via registered handler. */
  public static void assertEquals(String message, Object expected, Object actual) {
    if (!expected.equals(actual)) {
      String fullMessage = message + " Expected: " + expected + " but was: " + actual;
      LOGGER.error("Assertion failed: {}", fullMessage);
      if (failAssertion != null) {
        failAssertion.fail(fullMessage);
      } else {
        throw new AssertionError(fullMessage);
      }
    }
  }

  /** Wraps an assertion with automatic fail via registered handler. */
  public static void assertFalse(String message, boolean condition) {
    assertTrue(message, !condition);
  }

  /** Handles any uncontrolled exception during test execution. */
  public static void handleException(String context, Exception cause) {
    String message = "Exception in " + context + ": " + cause.getClass().getSimpleName();
    LOGGER.error(message, cause);
    if (failAssertion != null) {
      failAssertion.fail(message + " ---> " + cause.getMessage());
    } else {
      throw new RuntimeException(message, cause);
    }
  }
}