package com.swaglabs.managers;

import com.swaglabs.capabilities.CapabilityManager;
import com.swaglabs.capabilities.DesiredCapabilityBuilder;
import com.swaglabs.device.DeviceManager;
import com.swaglabs.device.DeviceManager.DeviceInfo;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import org.openqa.selenium.MutableCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Manages the lifecycle of the Appium driver for the current thread. */
public class AppiumDriverManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppiumDriverManager.class.getName());

  private static final ThreadLocal<AppiumDriver> appiumDriver = new ThreadLocal<>();

  private static final ThreadLocal<DeviceInfo> deviceInfo = new ThreadLocal<>();

  private AppiumDriverManager() {}

  public static AppiumDriver getDriver() {
    return appiumDriver.get();
  }

  public static DeviceInfo getDeviceInfo() {
    return deviceInfo.get();
  }

  /** Starts an Appium driver session against the detected device. */
  public static void startAppiumDriverInstance() {
    DeviceInfo device = DeviceManager.getDevice();
    deviceInfo.set(device);
    DesiredCapabilityBuilder builder = new DesiredCapabilityBuilder(device.name, device.udid, device.osVersion);
    MutableCapabilities caps = builder.getCaps();
    String url = PropertiesManager.getInstance().getProperty(PropertiesManager.Property.APPIUM_URL);
    LOGGER.info("Starting Appium session: {} with caps {}", url, caps);
    appiumDriver.set(localDriver(url, caps));
  }

  private static AppiumDriver localDriver(String host, MutableCapabilities capabilities) {
    try {
      URL url = URI.create(host).toURL();
      if (CapabilityManager.getInstance().isAndroid()) {
        return new AndroidDriver(url, capabilities);
      }
      return new IOSDriver(url, capabilities);
    } catch (MalformedURLException e) {
      throw new IllegalStateException("Malformed Appium URL: " + host, e);
    }
  }

  public static void stopAppiumDriver() {
    if (getDriver() != null) {
      try {
        getDriver().quit();
      } catch (Exception e) {
        LOGGER.warn("Error quitting driver: {}", e.getMessage());
      }
    }
    cleanUp();
  }

  public static void cleanUp() {
    appiumDriver.remove();
    deviceInfo.remove();
    LOGGER.info("Local driver variables cleared");
  }
}
