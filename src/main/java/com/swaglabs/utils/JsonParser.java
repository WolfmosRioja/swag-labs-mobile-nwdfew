package com.swaglabs.utils;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Reads a JSON file into a {@link JSONObject}. */
public class JsonParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonParser.class.getName());

  private final String filePath;

  public JsonParser(String filePath) {
    this.filePath = filePath;
  }

  public JSONObject getObjectFromJSON() {
    try {
      String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
      return new JSONObject(content);
    } catch (IOException e) {
      LOGGER.error("Error reading JSON file: {}", filePath, e);
      throw new RuntimeException("Error reading JSON file: " + filePath, e);
    }
  }
}
