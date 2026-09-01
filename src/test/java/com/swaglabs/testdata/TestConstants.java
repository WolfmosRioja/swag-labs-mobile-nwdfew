package com.swaglabs.testdata;

/**
 * Centralized test data constants to avoid magic strings in step definitions and page objects.
 */
public final class TestConstants {

  private TestConstants() {}

  public static final class SortOptions {
    public static final String NAME_A_TO_Z = "Name (A to Z)";
    public static final String NAME_Z_TO_A = "Name (Z to A)";
    public static final String PRICE_LOW_TO_HIGH = "Price (low to high)";
    public static final String PRICE_HIGH_TO_LOW = "Price (high to low)";

    private SortOptions() {}
  }

  public static final class ErrorMessages {
    public static final String LOCKED_OUT_USER = "Epic sadface: Sorry, this user has been locked out.";
    public static final String INVALID_CREDENTIALS = "Epic sadface: Username and password do not match";
    public static final String EMPTY_CREDENTIALS = "Epic sadface: Username is required";
    public static final String CART_EMPTY = "Your cart is empty";

    private ErrorMessages() {}
  }

  public static final class UiText {
    public static final String THANK_YOU_FOR_ORDER = "thank you for your order";
    public static final String ITEM_TOTAL = "Item total:";
    public static final String TOTAL_PREFIX = "Total:";

    private UiText() {}
  }
}