package com.fedex.aggregator.service.client;

import com.fedex.aggregator.controller.error.RestTemplateErrorHandler;
import com.fedex.aggregator.service.ShipmentProductType;
import com.fedex.aggregator.service.TrackStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Component
public class BackendServicesClient {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateErrorHandler.class);

    private final BackendServicesClientConfiguration configuration;
    private final RestTemplate restTemplate;

    public BackendServicesClient(BackendServicesClientConfiguration configuration, RestTemplate restTemplate) {
        this.configuration = configuration;
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<Optional<List<ShipmentProductType>>> getShipmentProducts(String orderNumber) {
        return executeWithExceptionHandling(() -> {
            Map<String, String> params = Collections.singletonMap("orderNumber", orderNumber);
            var shipmentProductTypes = restTemplate.getForObject(buildUri("/shipment-products", params), ShipmentProductType[].class);
            return (shipmentProductTypes == null) ? null : Arrays.asList(shipmentProductTypes);
        });
    }

    @Async
    public CompletableFuture<Optional<TrackStatusType>> getTrackStatus(String orderNumber) {
        return executeWithExceptionHandling(() -> {
            Map<String, String> params = Collections.singletonMap("orderNumber", orderNumber);
            return restTemplate.getForObject(buildUri("/track-status", params), TrackStatusType.class);
        });
    }

    @Async
    public CompletableFuture<Optional<BigDecimal>> getPricing(String countryCode) {
        return executeWithExceptionHandling(() -> {
            Map<String, String> params = Collections.singletonMap("countryCode", countryCode);
            return restTemplate.getForObject(buildUri("/pricing", params), BigDecimal.class);
        });
    }

    @Async
    public <T> CompletableFuture<Optional<T>> executeWithExceptionHandling(Supplier<T> supplier) {
        try {
            T response = supplier.get();
            return CompletableFuture.completedFuture(Optional.ofNullable(response));
        } catch (RestClientException e) {
            logger.warn("RestClientException", e.getCause());
            return CompletableFuture.completedFuture(Optional.empty());
        }
    }

    private URI buildUri(String path, Map<String, String> queryParams) {
        var params = new LinkedMultiValueMap();
        queryParams.forEach((key, value) -> params.put(key, List.of(value)));

        return UriComponentsBuilder
                .fromUriString(configuration.baseUrl())
                .path(path)
                .queryParams(params)
                .build()
                .toUri();
    }
}
