package com.fedex.aggregator.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ShipmentProductsService {
    Map<String, CompletableFuture<Optional<List<ShipmentProductType>>>> getShipmentProducts(List<String> shipmentsOrderNumbers);
}
