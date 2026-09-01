package com.swaglabs.managers;

import io.cucumber.java.Scenario;

/** Holds the current Cucumber {@link Scenario} for the executing thread. */
public class ScenarioManager {

  private static final ThreadLocal<Scenario> scenario = new ThreadLocal<>();

  private ScenarioManager() {}

  public static void setScenario(Scenario sc) {
    scenario.set(sc);
  }

  public static Scenario getScenario() {
    return scenario.get();
  }

  public static void cleanUp() {
    scenario.remove();
  }
}
