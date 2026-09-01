package com.swaglabs.stepsDefs.config;

import com.swaglabs.managers.PropertiesManager;
import io.cucumber.java.ParameterType;

/** Custom Cucumber parameter types used across the feature files. */
public class ParameterTypes {

  /** Maps a friendly user alias to the known Swag Labs account. */
  @ParameterType(
      "standard_user|locked_out_user|problem_user|performance_glitch_user|error_user|visual_user")
  public String swagUser(String value) {
    return value;
  }

  /** Returns the shared password for all Swag Labs accounts. */
  public String swagPassword() {
    return PropertiesManager.getInstance()
        .getProperty(PropertiesManager.Property.SWAG_PASSWORD);
  }
}
