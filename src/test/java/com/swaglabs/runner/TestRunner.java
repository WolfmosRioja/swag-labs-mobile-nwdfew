package com.swaglabs.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

/**
 * Runner that executes the Cucumber features with the JUnit 5 platform suite.
 *
 * <p>Configuration is declared via annotations mirroring the reference framework:
 * feature files are discovered under {@code src/test/resources/features} and the
 * glue code lives in {@code com.swaglabs.stepsDefs} and {@code com.swaglabs.hooks}.
 */
@Suite
@IncludeEngines("cucumber")
@SelectDirectories("src/test/resources/features")
@ConfigurationParameter(
    key = "cucumber.plugin",
    value =
        "pretty,"
            + "timeline:target/junit-cucumber-reports/timeline,"
            + "html:target/junit-cucumber-reports/cucumber.html,"
            + "json:target/junit-cucumber-reports/cucumber.json")
@ConfigurationParameter(key = "cucumber.glue", value = "com.swaglabs.stepsDefs,com.swaglabs.hooks")
@ConfigurationParameter(key = "cucumber.monochrome", value = "true")
public class TestRunner {
  // The class body remains empty.
}
