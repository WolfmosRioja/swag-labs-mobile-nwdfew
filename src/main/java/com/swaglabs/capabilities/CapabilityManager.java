package com.swaglabs.capabilities;

import com.swaglabs.entities.MobilePlatform;
import com.swaglabs.managers.PropertiesManager;
import com.swaglabs.utils.JsonParser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads and exposes the Appium capabilities defined in the caps JSON file.
 *
 * Mirroring the original framework, the active platform is driven by the
 * {@code PLATFORM} environment variable ({@code android} or {@code ios}).
 */
public class CapabilityManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(CapabilityManager.class.getName());

  private static CapabilityManager instance;

  private final JSONObject capabilities;

  private CapabilityManager() {
    String capabilityPath =
        System.getProperty("user.dir")
            + PropertiesManager.getInstance().getProperty(PropertiesManager.Property.CAPS);
    JsonParser jsonParser = new JsonParser(capabilityPath);
    capabilities = jsonParser.getObjectFromJSON();
    LOGGER.info("Loaded capabilities from {}", capabilityPath);
  }

  public static CapabilityManager getInstance() {
    if (instance == null) {
      instance = new CapabilityManager();
    }
    return instance;
  }

  /** Returns the active platform: {@code android} or {@code ios}. */
  public String getPlatform() {
    String platform = System.getenv("PLATFORM");
    if (platform == null) {
      platform = "android";
    }
    return platform.toLowerCase().contains("android") ? "android" : "ios";
  }

  public String getEnvironment() {
    String environment = System.getenv("ENVIRONMENT");
    return environment == null || environment.equalsIgnoreCase("branch")
        ? "local"
        : environment.toLowerCase();
  }

  public boolean isAndroid() {
    return getPlatform().equalsIgnoreCase(MobilePlatform.ANDROID.getType());
  }

  public boolean isIos() {
    return getPlatform().equalsIgnoreCase(MobilePlatform.IOS.getType());
  }

  public JSONObject getCapabilityObjectFromKey(String key) {
    return capabilities.has(key) ? (JSONObject) capabilities.get(key) : null;
  }

  public JSONObject getCapabilities() {
    return capabilities;
  }

  public String appPath() {
    return System.getProperty("user.dir")
        + (isAndroid()
            ? PropertiesManager.getInstance().getProperty(PropertiesManager.Property.ANDROID_APP)
            : PropertiesManager.getInstance().getProperty(PropertiesManager.Property.IOS_APP));
  }
}
