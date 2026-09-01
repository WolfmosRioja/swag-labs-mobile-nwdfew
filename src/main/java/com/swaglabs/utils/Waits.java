package com.swaglabs.utils;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumDriver;

/** Reusable explicit-wait helpers. */
public class Waits {

  private final AppiumDriver driver;

  public Waits(AppiumDriver driver) {
    this.driver = driver;
  }

  public WebElement waitForElement(WebElement element) {
    return waitForElement(element, Duration.ofSeconds(20));
  }

  public WebElement waitForElement(WebElement element, Duration timeout) {
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    return wait.until(ExpectedConditions.visibilityOf(element));
  }

  public WebElement waitForElementClickable(WebElement element, Duration timeout) {
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    return wait.until(ExpectedConditions.elementToBeClickable(element));
  }

  /**
   * Waits until an element located by {@code by} is present and clickable,
   * re-polling the locator. Unlike the {@code WebElement} overload, this keeps
   * retrying while the element is not yet rendered, so it is resilient to slow
   * page loads.
   */
  public WebElement waitForElementClickableBy(By by, Duration timeout) {
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    return wait.until(ExpectedConditions.elementToBeClickable(by));
  }

  /** Waits until an element located by {@code by} is visible, re-polling the locator. */
  public WebElement waitForElementBy(By by, Duration timeout) {
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
  }

  /**
   * Waits until the given element contains the expected text (re-polls getText()
   * so it is resilient to text being applied slightly after the element becomes
   * visible). Returns {@code true} if the text was matched within the timeout.
   */
  public boolean waitForElementText(WebElement element, String expected, Duration timeout) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, timeout);
      wait.until(ExpectedConditions.textToBePresentInElement(element, expected));
      return true;
    } catch (TimeoutException | NoSuchElementException e) {
      return false;
    }
  }

  /**
   * Waits until the given element exposes a non-blank text value. Useful when a
   * label becomes visible before its text is fully hydrated. The element may be a
   * container whose message is rendered in a child TextView/StaticText, so this
   * method also polls any descendant text node (Android {@code android.widget
   * .TextView} or iOS {@code XCUIElementTypeStaticText}) and returns the first
   * non-blank value found. Returns the element (or child) text at the end of the
   * wait.
   */
  public String waitForNonBlankText(WebElement element, Duration timeout) {
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    return wait.until(
        webDriver -> {
          String text = element.getText();
          if (text != null && !text.trim().isEmpty()) {
            return text;
          }
          for (String childType : new String[] {"android.widget.TextView", "XCUIElementTypeStaticText"}) {
            for (WebElement child : element.findElements(By.xpath(".//" + childType))) {
              String childText = child.getText();
              if (childText != null && !childText.trim().isEmpty()) {
                return childText;
              }
            }
          }
          return null;
        });
  }

  public boolean checkIfElementIsShown(WebElement element, Duration timeout) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, timeout);
      wait.until(ExpectedConditions.visibilityOf(element));
      return true;
    } catch (TimeoutException | NoSuchElementException e) {
      return false;
    }
  }

  public boolean checkIfElementIsShown(By by, Duration timeout) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, timeout);
      wait.until(ExpectedConditions.visibilityOfElementLocated(by));
      return true;
    } catch (TimeoutException | NoSuchElementException e) {
      return false;
    }
  }

  public boolean waitUntilElementDisappear(WebElement element, Duration timeout) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, timeout);
      return wait.until(ExpectedConditions.invisibilityOf(element));
    } catch (TimeoutException e) {
      return false;
    }
  }
}