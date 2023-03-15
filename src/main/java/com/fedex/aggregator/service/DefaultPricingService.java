package com.fedex.aggregator.service;

import com.fedex.aggregator.service.client.BackendServicesClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class DefaultPricingService implements PricingService {
    private final BackendServicesClient backendServicesClient;

    public DefaultPricingService(BackendServicesClient backendServicesClient) {
        this.backendServicesClient = backendServicesClient;
    }

    @Override
    public Map<String, CompletableFuture<Optional<BigDecimal>>> getPricings(List<String> countryCodes) {
        var futurePricings = new HashMap<String, CompletableFuture<Optional<BigDecimal>>>();

        for (String countryCode : countryCodes) {
            var futurePricing = backendServicesClient.getPricing(countryCode);
            futurePricings.put(countryCode, futurePricing);
        }

        return futurePricings;
    }
}
