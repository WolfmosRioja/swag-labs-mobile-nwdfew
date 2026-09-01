package com.swaglabs.utils.helper;

import com.swaglabs.managers.AppiumDriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HidesKeyboard;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.cucumber.java.Scenario;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base helper with common Appium operations shared by both platforms.
 */
public abstract class WebdriverHelper {

  protected static final Logger LOGGER = LoggerFactory.getLogger(WebdriverHelper.class.getName());

  protected AppiumDriver driver() {
    return AppiumDriverManager.getDriver();
  }

  /** Hides the soft keyboard. */
  public void hideKeyboard() {
    ((HidesKeyboard) driver()).hideKeyboard();
  }

  /** Simulates a scroll/swipe. */
  public void swipe(int startX, int startY, int endX, int endY, long durationMs) {
    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
    Sequence swipe =
        new Sequence(finger, 1)
            .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
            .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
            .addAction(
                finger.createPointerMove(
                    Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, endY))
            .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
    driver().perform(java.util.List.of(swipe));
  }

  /** Takes a screenshot and attaches it to the scenario on failure. */
  public static void getScreenshot(Scenario scenario, AppiumDriver driver) {
    try {
      byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
      scenario.attach(screenshot, "image/png", "screenshot");
    } catch (Exception e) {
      LOGGER.warn("Failed to take screenshot: {}", e.getMessage());
    }
  }

  /**
   * Starts Appium screen recording for the current platform.
   *
   * @param driver the Appium driver
   */
  public static void startRecording(AppiumDriver driver) {
    try {
      if (driver instanceof AndroidDriver) {
        AndroidStartScreenRecordingOptions options =
            new AndroidStartScreenRecordingOptions()
                .enableBugReport()
                .withBitRate(2_000_000)
                .withVideoSize("1280x720")
                .withTimeLimit(Duration.ofMinutes(10))
                .enableForcedRestart();
        ((CanRecordScreen) driver).startRecordingScreen(options);
      } else {
        IOSStartScreenRecordingOptions options =
            new IOSStartScreenRecordingOptions()
                .withFps(10)
                .withVideoScale("240:-2")
                .withVideoType("mpeg4")
                .withTimeLimit(Duration.ofMinutes(10))
                .enableForcedRestart();
        ((CanRecordScreen) driver).startRecordingScreen(options);
      }
      LOGGER.info("Screen recording started");
    } catch (WebDriverException e) {
      LOGGER.warn("Recording not available: {}", String.valueOf(e));
    }
  }

  /**
   * Stops Appium screen recording. On failure the recorded MP4 is saved under
   * {@code videos/} and a link is attached to the scenario; otherwise the
   * recording is discarded.
   *
   * <p>The video is written to {@code target/generated-report/videos/} so the
   * relative {@code videos/...} source in the embedded HTML resolves against the
   * generated Cluecumber report. A copy is also kept under {@code videos/} at the
   * project root for direct access.
   *
   * @param scenario the failed scenario (recording is attached only on failure)
   * @param driver   the Appium driver
   */
  public static void stopRecording(Scenario scenario, AppiumDriver driver) {
    try {
      if (scenario.isFailed()) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        LocalDateTime now = LocalDateTime.now();
        String videoName = scenario.getName().replace(" ", "_") + "_" + dtf.format(now) + ".mp4";
        String reportVideoDir = "target" + File.separator + "generated-report" + File.separator + "videos";
        String outputRoute = reportVideoDir + File.separator + videoName;
        File dir = new File(reportVideoDir);
        if (!dir.exists() && !dir.mkdirs()) {
          LOGGER.warn("Could not create video directory: {}", dir.getAbsolutePath());
        }
        String base64output = ((CanRecordScreen) driver).stopRecordingScreen();
        byte[] videoBytes = Base64.getDecoder().decode(base64output);
        try (FileOutputStream out = new FileOutputStream(outputRoute)) {
          out.write(videoBytes);
        }
        File rootVideos = new File("videos");
        if ((rootVideos.exists() || rootVideos.mkdirs())
            && !new File(outputRoute).getCanonicalPath()
                .equals(new File("videos", videoName).getCanonicalPath())) {
          try (FileOutputStream out = new FileOutputStream("videos" + File.separator + videoName)) {
            out.write(videoBytes);
          }
        }
        String outputLink = getVideoHTML(videoName);
        scenario.attach(outputLink, "text/html", scenario.getName());
        LOGGER.info("Recording saved and attached to failed scenario '{}'", scenario.getName());
      } else {
        ((CanRecordScreen) driver).stopRecordingScreen();
      }
    } catch (Exception e) {
      LOGGER.warn("Error stopping recording: {}", e.toString());
    }
  }

  /**
   * Builds the HTML block that embeds the recorded video in the report.
   *
   * <p>Cluecumber renders scenario attachments inside
   * {@code target/generated-report/pages/...}, so the video must be placed under
   * {@code target/generated-report/videos/} and referenced with a relative path
   * ({@code ../../videos/..}) that climbs back up to {@code generated-report/}.
   */
  public static String getVideoHTML(String videoName) {
    return "<html><body>"
        + "<h2>"
        + videoName
        + "</h2>"
        + "<video width='640' height='480' controls>"
        + "<source src='"
        + "../../videos/"
        + videoName
        + "' type='video/mp4'>"
        + "Your browser does not support the video tag."
        + "</video>"
        + "</body></html>";
  }

  public static String base64Screenshot(AppiumDriver driver) {
    return Base64.getEncoder().encodeToString(driver.getScreenshotAs(OutputType.BYTES));
  }

  /** Prepares the environment before each scenario. */
  public void prepareExecution() {
    // Platform-specific preparation
  }
}
