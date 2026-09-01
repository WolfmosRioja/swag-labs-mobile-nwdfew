package com.swaglabs.capabilities;

import com.swaglabs.entities.MobilePlatform;
import com.swaglabs.managers.PropertiesManager;
import org.json.JSONObject;
import org.openqa.selenium.MutableCapabilities;

/**
 * Builds the Appium desired capabilities for the active platform.
 *
 * The {@code devicename} and {@code udid} are resolved from the connected
 * physical device / simulator via the {@link DeviceManager}.
 */
public class DesiredCapabilityBuilder {

  MutableCapabilities caps;

  public DesiredCapabilityBuilder(String deviceName, String udid, String osVersion) {
    caps = buildDesiredCapability(deviceName, udid, osVersion);
  }

  public MutableCapabilities getCaps() {
    return caps;
  }

  private MutableCapabilities buildDesiredCapability(String deviceName, String udid, String osVersion) {
    String platform = CapabilityManager.getInstance().getPlatform();
    MutableCapabilities desiredCapabilities;
    if (platform.equalsIgnoreCase(MobilePlatform.ANDROID.getType())) {
      desiredCapabilities = platformAndroid(getCapsByJson(MobilePlatform.ANDROID));
      desiredCapabilities.setCapability("appium:udid", udid);
    } else {
      desiredCapabilities = platformIOS(getCapsByJson(MobilePlatform.IOS));
      desiredCapabilities.setCapability("appium:udid", udid);
      desiredCapabilities.setCapability("appium:deviceName", deviceName);
      desiredCapabilities.setCapability("appium:platformVersion", osVersion);
    }
    return desiredCapabilities;
  }

  private MutableCapabilities getCapsByJson(MobilePlatform platform) {
    return readFromJson(CapabilityManager.getInstance().getCapabilityObjectFromKey(platform.getType()));
  }

  private MutableCapabilities platformAndroid(MutableCapabilities androidCapabilities) {
    androidCapabilities.setCapability(
        "appium:app",
        System.getProperty("user.dir")
            + PropertiesManager.getInstance().getProperty(PropertiesManager.Property.ANDROID_APP));
    androidCapabilities.setCapability(
        "appium:appPackage",
        PropertiesManager.getInstance().getProperty(PropertiesManager.Property.ANDROID_PACKAGE));
    return androidCapabilities;
  }

  private MutableCapabilities platformIOS(MutableCapabilities iOSCapabilities) {
    iOSCapabilities.setCapability(
        "appium:app",
        System.getProperty("user.dir")
            + PropertiesManager.getInstance().getProperty(PropertiesManager.Property.IOS_APP));
    iOSCapabilities.setCapability(
        "appium:bundleId",
        PropertiesManager.getInstance().getProperty(PropertiesManager.Property.IOS_BUNDLE_ID));
    return iOSCapabilities;
  }

  private MutableCapabilities readFromJson(JSONObject platformCapabilities) {
    MutableCapabilities mutableCapabilities = new MutableCapabilities();
    for (String key : platformCapabilities.keySet()) {
      mutableCapabilities.setCapability(key, platformCapabilities.get(key));
    }
    return mutableCapabilities;
  }
}
