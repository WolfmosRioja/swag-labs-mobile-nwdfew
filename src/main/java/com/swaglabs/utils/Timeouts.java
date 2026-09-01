package com.swaglabs.utils;

import java.time.Duration;

/**
 * Centralized timeout constants for explicit waits.
 * <p>
 * Using constants avoids magic numbers scattered across page objects and makes
 * timeout tuning a single-point change.
 * </p>
 */
public final class Timeouts {

  private Timeouts() {
  }

  /** Default timeout for most element interactions (20 seconds). */
  public static final Duration DEFAULT = Duration.ofSeconds(20);

  /** Short timeout for quick checks and error messages (3 seconds). */
  public static final Duration SHORT = Duration.ofSeconds(3);

  /** Medium timeout for text hydration and error messages (10 seconds). */
  public static final Duration MEDIUM = Duration.ofSeconds(10);

  /** Long timeout for slow operations and performance glitch user (60 seconds). */
  public static final Duration LONG = Duration.ofSeconds(60);
}