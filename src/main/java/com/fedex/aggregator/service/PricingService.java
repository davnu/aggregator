package com.fedex.aggregator.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PricingService {
    Map<String, CompletableFuture<Optional<BigDecimal>>> getPricings(List<String> countryCodes);
}
