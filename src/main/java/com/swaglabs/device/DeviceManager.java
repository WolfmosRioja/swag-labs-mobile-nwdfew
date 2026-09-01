package com.swaglabs.device;

import com.swaglabs.capabilities.CapabilityManager;
import com.swaglabs.utils.CommandPrompt;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolves the connected physical device / simulator for the active platform.
 *
 * For Android it relies on {@code adb devices}; for iOS it relies on
 * {@code xcrun simctl list devices} (simulators) or {@code xcrun devicectl list devices}.
 * Only a single, simple resolution is provided so the framework runs out of the box;
 * it can be extended for parallel/multi-device allocation.
 */
public class DeviceManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeviceManager.class.getName());

  public static class DeviceInfo {
    public String udid;
    public String name;
    public String osVersion;

    public DeviceInfo(String udid, String name, String osVersion) {
      this.udid = udid;
      this.name = name;
      this.osVersion = osVersion;
    }
  }

  private DeviceManager() {}

  public static DeviceInfo getDevice() {
    return CapabilityManager.getInstance().isAndroid() ? getAndroidDevice() : getIosDevice();
  }

  private static DeviceInfo getAndroidDevice() {
    List<String> devices = CommandPrompt.execute("adb", "devices");
    for (String line : devices) {
      String trimmed = line.trim();
      if (trimmed.endsWith("device") && !trimmed.contains("List of devices")) {
        String udid = trimmed.split("\\s+")[0];
        LOGGER.info("Using Android device: {}", udid);
        String version = CommandPrompt.execute("adb", "-s", udid, "shell", "getprop", "ro.build.version.release")
            .stream()
            .findFirst()
            .orElse("");
        String model = CommandPrompt.execute("adb", "-s", udid, "shell", "getprop", "ro.product.model")
            .stream()
            .findFirst()
            .orElse("Android Device");
        return new DeviceInfo(udid, model.trim(), version.trim());
      }
    }
    throw new IllegalStateException(
        "No Android device/emulator detected. Please connect one or start an AVD and run 'adb devices'.");
  }

  private static DeviceInfo getIosDevice() {
    // Prefer a booted simulator
    List<String> sims = CommandPrompt.execute("xcrun", "simctl", "list", "devices", "booted");
    for (String line : sims) {
      String trimmed = line.trim();
      // Matches lines like:  (UDID) (Booted)
      int parenStart = trimmed.indexOf('(');
      int parenEnd = trimmed.lastIndexOf(") (Booted)");
      if (parenStart > 0 && trimmed.endsWith("(Booted)")) {
        String name = trimmed.substring(0, parenStart).trim();
        String udid = trimmed.substring(parenStart + 1, trimmed.indexOf(')'));
        List<String> osV = CommandPrompt.execute("xcrun", "simctl", "getenv", udid, "SIMULATOR_RUNTIME_VERSION");
        String version = osV.isEmpty() ? "" : osV.get(0).trim();
        LOGGER.info("Using iOS simulator: {} ({})", name, udid);
        return new DeviceInfo(udid, name, version);
      }
    }
    // Fallback: physical iOS device via devicectl
    List<String> phys = CommandPrompt.execute("xcrun", "devicectl", "list", "devices");
    for (String line : phys) {
      String trimmed = line.trim();
      int parenStart = trimmed.indexOf('(');
      int parenEnd = trimmed.indexOf(')');
      if (parenStart > 0 && parenEnd > parenStart && trimmed.contains("Available")) {
        String name = trimmed.substring(0, parenStart).trim();
        String udid = trimmed.substring(parenStart + 1, parenEnd);
        return new DeviceInfo(udid, name, "");
      }
    }
    throw new IllegalStateException(
        "No iOS simulator/device detected. Please boot a simulator via Xcode and retry.");
  }
}
