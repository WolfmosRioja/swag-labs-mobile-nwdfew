package com.swaglabs.entities;

/** Supported mobile platforms. */
public enum MobilePlatform {

  ANDROID("android"),
  IOS("ios");

  private final String type;

  MobilePlatform(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
