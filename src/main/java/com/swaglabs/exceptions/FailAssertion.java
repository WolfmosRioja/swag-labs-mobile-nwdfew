package com.swaglabs.exceptions;

/** Functional interface for failing a test with a message. */
@FunctionalInterface
public interface FailAssertion {
  void fail(String message);
}