package com.swaglabs.utils.helper;

import org.openqa.selenium.WebElement;

/** iOS-specific device operations. */
public class iOSHelper extends WebdriverHelper {

  public void natClick(WebElement element) {
    element.click();
  }

  @Override
  public void prepareExecution() {
    // iOS: nothing special needed for Swag Labs
  }
}
