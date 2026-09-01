package com.swaglabs.managers;

import com.swaglabs.capabilities.CapabilityManager;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts and manages a local Appium server as part of the test run, mirroring
 * the reference framework's {@code AppiumServerManager}.
 *
 * <p>The server is launched on the host and port declared by the
 * {@code appium.url} property. It is stopped automatically after the run.
 */
public class AppiumServerManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppiumServerManager.class.getName());

  private static final ThreadLocal<AppiumDriverLocalService> threadLocalService = new ThreadLocal<>();

  private AppiumServerManager() {}

  public static AppiumDriverLocalService getService() {
    return threadLocalService.get();
  }

  /** Starts the local Appium server on the configured host/port. */
  public static void startAppiumServer() {
    URL appiumUrl = appiumUrl();
    String host = appiumUrl.getHost();
    int port = appiumUrl.getPort();
    if (isAppiumServerRunning(host, port)) {
      LOGGER.info("Appium server already running on {}:{} - stopping to reuse clean state", host, port);
      stopAppiumServer();
    }

    File logsDir =
        new File(System.getProperty("user.dir") + File.separator + "target" + File.separator + "appium-logs");
    if (!logsDir.exists()) {
      logsDir.mkdirs();
    }

    AppiumServiceBuilder builder = new AppiumServiceBuilder();
    builder.withLogFile(new File(logsDir, "appium.log"));
    builder.withArgument(GeneralServerFlag.LOG_LEVEL, "warn");
    builder.usingPort(port);
    builder.withIPAddress(host);
    builder.withEnvironment(environmentWithToolingPaths());

    threadLocalService.set(builder.build());
    threadLocalService.get().start();
    LOGGER.info("Appium server started on {}", appiumUrl);
  }

  /** Stops the local Appium server, if one is running. */
  public static void stopAppiumServer() {
    AppiumDriverLocalService service = threadLocalService.get();
    if (service != null) {
      if (service.isRunning()) {
        service.stop();
        LOGGER.info("Appium server stopped");
      }
      threadLocalService.remove();
    }
  }

  /**
   * Returns true when an Appium server is already listening on the given
   * host/port.
   */
  public static boolean isAppiumServerRunning(String host, int port) {
    try (Socket socket = new Socket(host, port)) {
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private static URL appiumUrl() {
    String raw =
        PropertiesManager.getInstance().getProperty(PropertiesManager.Property.APPIUM_URL);
    try {
      return URI.create(raw).toURL();
    } catch (IOException e) {
      throw new IllegalStateException("Malformed Appium URL: " + raw, e);
    }
  }

  /**
   * Builds an environment that ensures the Appium local service can find the
   * {@code appium} and {@code node} executables, since the inherited PATH may be
   * incomplete depending on how the suite is launched.
   */
  private static Map<String, String> environmentWithToolingPaths() {
    Map<String, String> env = new HashMap<>(System.getenv());
    String existing = env.getOrDefault("PATH", env.getOrDefault("Path", ""));
    StringBuilder path = new StringBuilder(existing);
    appendIfMissing(path, "C:\\Program Files\\nodejs");
    appendIfMissing(path, System.getProperty("user.home") + "\\AppData\\Roaming\\npm");
    env.put("Path", path.toString());
    if (CapabilityManager.getInstance().isIos()) {
      env.put("APPIUM_XCUITEST_PREFER_DEVICECTL", "true");
    }
    return env;
  }

  private static void appendIfMissing(StringBuilder path, String dir) {
    if (path.toString().toLowerCase().contains(dir.toLowerCase())) {
      return;
    }
    if (path.length() > 0 && !path.toString().endsWith(";")) {
      path.append(";");
    }
    path.append(dir);
  }
}
