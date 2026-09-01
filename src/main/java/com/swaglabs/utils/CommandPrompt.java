package com.swaglabs.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Simple utilities to run OS shell commands (adb / xcrun). */
public class CommandPrompt {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommandPrompt.class.getName());

  private CommandPrompt() {}

  /**
   * Executes a command and returns each output line.
   *
   * @param command the command and its arguments
   * @return the list of output lines
   */
  public static List<String> execute(String... command) {
    List<String> result = new ArrayList<>();
    try {
      ProcessBuilder pb = new ProcessBuilder(command);
      pb.redirectErrorStream(true);
      Process process = pb.start();
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          result.add(line);
        }
      }
      process.waitFor();
    } catch (Exception e) {
      LOGGER.error("Error executing command: {}", String.join(" ", command), e);
    }
    return result;
  }

  public static List<String> execute(String command) {
    return execute("cmd", "/c", command);
  }
}
