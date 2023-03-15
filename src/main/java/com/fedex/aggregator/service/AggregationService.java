package com.fedex.aggregator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class AggregationService {
    private static final Logger logger = LoggerFactory.getLogger(AggregationService.class);

    private final ShipmentProductsService shipmentProductsService;
    private final TrackStatusService trackStatusService;
    private final PricingService pricingService;

    public AggregationService(ShipmentProductsService shipmentProductsService, TrackStatusService trackStatusService, PricingService pricingService) {
        this.shipmentProductsService = shipmentProductsService;
        this.trackStatusService = trackStatusService;
        this.pricingService = pricingService;
    }

    public Aggregation getAggregation(List<String> shipmentsOrderNumbers, List<String> trackOrderNumbers, List<String> pricingCountryCodes) {
        // Get completable futures for shipment products, track statuses, and pricings
        var completableShipmentProducts = shipmentProductsService.getShipmentProducts(shipmentsOrderNumbers);
        var completableTrackStatuses = trackStatusService.getTrackStatuses(trackOrderNumbers);
        var completablePricings = pricingService.getPricings(pricingCountryCodes);

        // Wait for all completable futures to complete
        var completableFutures = flatMapToArray(completableShipmentProducts, completableTrackStatuses, completablePricings);
        CompletableFuture.allOf(completableFutures).join();

        // Get values from completable futures
        var shipmentProducts = getValues(completableShipmentProducts);
        var trackStatuses = getValues(completableTrackStatuses);
        var pricings = getValues(completablePricings);

        return new Aggregation(shipmentProducts, trackStatuses, pricings);
    }

    private CompletableFuture<?>[] flatMapToArray(Map<String, ? extends CompletableFuture<?>>... completableMaps) {
        return Arrays.stream(completableMaps)
                .flatMap(map -> map.values().stream())
                .toArray(CompletableFuture[]::new);
    }

    private <T> Map<String, T> getValues(Map<String, CompletableFuture<Optional<T>>> completableFuturesToKey) {
        var valuesToKey = new HashMap<String, T>();
        for (Map.Entry<String, CompletableFuture<Optional<T>>> entry : completableFuturesToKey.entrySet()) {
            try {
                Optional<T> optionalValue = entry.getValue().get();
                if (optionalValue.isPresent()) {
                    valuesToKey.put(entry.getKey(), optionalValue.get());
                }
            } catch (Exception e) {
                logger.warn("Error filling CompletableFuture", e);
            }
        }
        return valuesToKey;
    }
}
