package com.fedex.aggregator.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record Aggregation(
        Map<String, List<ShipmentProductType>> shipments,
        Map<String, TrackStatusType> track,
        Map<String, BigDecimal> pricing) {
}
