package com.swaglabs.utils.helper;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.WebElement;

/** Android-specific device operations. */
public class AndroidHelper extends WebdriverHelper {

  public void pressBackKey() {
    ((AndroidDriver) driver()).pressKey(new KeyEvent(AndroidKey.BACK));
  }

  public void natClick(WebElement element) {
    element.click();
  }

  @Override
  public void prepareExecution() {
    // Android: nothing special needed for Swag Labs
  }
}
