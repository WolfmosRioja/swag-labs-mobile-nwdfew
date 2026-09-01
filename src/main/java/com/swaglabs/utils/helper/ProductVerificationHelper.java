package com.swaglabs.utils.helper;

import com.swaglabs.testdata.TestConstants;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/** Helper for verifying product list sort order. */
public final class ProductVerificationHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductVerificationHelper.class.getName());

  private ProductVerificationHelper() {}

  /**
   * Verifies that the product list is sorted according to the expected order.
   *
   * @param titles      list of product titles
   * @param prices      list of product prices
   * @param expectedOrder the expected sort order from {@link TestConstants.SortOptions}
   */
  public static void verifySortOrder(List<String> titles, List<Double> prices, String expectedOrder) {
    SoftAssertions softly = new SoftAssertions();

    switch (expectedOrder) {
      case TestConstants.SortOptions.NAME_A_TO_Z:
      case TestConstants.SortOptions.NAME_Z_TO_A:
        verifyNameSort(titles, expectedOrder, softly);
        break;

      case TestConstants.SortOptions.PRICE_LOW_TO_HIGH:
      case TestConstants.SortOptions.PRICE_HIGH_TO_LOW:
        verifyPriceSort(prices, expectedOrder, softly);
        break;

      default:
        throw new IllegalArgumentException("Unknown sort order: " + expectedOrder);
    }

    softly.assertAll();
    LOGGER.info("Verified products sorted by {}", expectedOrder);
  }

  private static void verifyNameSort(List<String> titles, String expectedOrder, SoftAssertions softly) {
    boolean nameSortedCorrectly = true;
    for (int i = 0; i < titles.size() - 1; i++) {
      int comparison = titles.get(i).compareToIgnoreCase(titles.get(i + 1));
      if ((expectedOrder.equals(TestConstants.SortOptions.NAME_A_TO_Z) && comparison > 0)
          || (expectedOrder.equals(TestConstants.SortOptions.NAME_Z_TO_A) && comparison < 0)) {
        nameSortedCorrectly = false;
        softly.fail(
            "Products not sorted by "
                + expectedOrder
                + " at position "
                + i
                + ": '"
                + titles.get(i)
                + "' vs '"
                + titles.get(i + 1)
                + "'");
        break;
      }
    }
    softly.assertThat(nameSortedCorrectly).as("Product list sorted by " + expectedOrder).isTrue();
  }

  private static void verifyPriceSort(List<Double> prices, String expectedOrder, SoftAssertions softly) {
    boolean priceSortedCorrectly = true;
    for (int i = 0; i < prices.size() - 1; i++) {
      if ((expectedOrder.equals(TestConstants.SortOptions.PRICE_LOW_TO_HIGH)
              && prices.get(i) > prices.get(i + 1))
          || (expectedOrder.equals(TestConstants.SortOptions.PRICE_HIGH_TO_LOW)
              && prices.get(i) < prices.get(i + 1))) {
        priceSortedCorrectly = false;
        softly.fail(
            "Products not sorted by "
                + expectedOrder
                + " at position "
                + i
                + ": "
                + prices.get(i)
                + " vs "
                + prices.get(i + 1));
        break;
      }
    }
    softly.assertThat(priceSortedCorrectly).as("Product list sorted by " + expectedOrder).isTrue();
  }
}