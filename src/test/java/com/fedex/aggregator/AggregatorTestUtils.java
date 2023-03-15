package com.fedex.aggregator;

import org.apache.commons.lang3.RandomStringUtils;

public class AggregatorTestUtils {

    public static String getRandomOrderNumber() {
        return RandomStringUtils.randomNumeric(9);
    }
}
