package com.swaglabs.managers;

import java.io.FileInputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads the environment-specific properties file.
 *
 * Configured through the {@code ENVIRONMENT} environment variable. If the variable
 * is not present or equals {@code branch}, the {@code local} properties are used so
 * the framework can be run locally out of the box.
 */
public final class PropertiesManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesManager.class.getName());

  private static PropertiesManager instance;

  private final Properties properties;

  private PropertiesManager() {
    properties = new Properties();
    String environment = System.getenv("ENVIRONMENT");
    if (environment == null || environment.equalsIgnoreCase("branch")) {
      environment = "local"; // local by default so it runs out of the box
    }
    String propFile =
        "src/main/resources/project_properties/project_config_" + environment.toLowerCase() + ".properties";
    try {
      properties.load(new FileInputStream(propFile));
      LOGGER.info("Loaded properties file: {}", propFile);
    } catch (Exception e) {
      LOGGER.error("Error loading properties file: {}", propFile, e);
    }
  }

  public static PropertiesManager getInstance() {
    if (instance == null) {
      instance = new PropertiesManager();
    }
    return instance;
  }

  public String getProperty(Property key) {
    return properties.getProperty(key.getValue());
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public void setProperty(String key, String value) {
    properties.setProperty(key, value);
  }

  public enum Property {
    CAPS,
    FIND_TIMEOUT,
    ANDROID_APP,
    ANDROID_PACKAGE,
    IOS_APP,
    IOS_BUNDLE_ID,
    SWAG_PASSWORD,
    APPIUM_URL;

    private final String property;

    Property() {
      switch (this) {
        case CAPS:
          property = "capacities.path";
          break;
        case FIND_TIMEOUT:
          property = "find.timeout";
          break;
        case ANDROID_APP:
          property = "android.app";
          break;
        case ANDROID_PACKAGE:
          property = "android.package";
          break;
        case IOS_APP:
          property = "ios.app";
          break;
        case IOS_BUNDLE_ID:
          property = "ios.bundleId";
          break;
        case SWAG_PASSWORD:
          property = "swag.password";
          break;
        case APPIUM_URL:
          property = "appium.url";
          break;
        default:
          property = this.toString().toLowerCase();
      }
    }

    public String getValue() {
      return property;
    }
  }
}
