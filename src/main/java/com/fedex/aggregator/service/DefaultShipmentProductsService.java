package com.fedex.aggregator.service;

import com.fedex.aggregator.service.client.BackendServicesClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class DefaultShipmentProductsService implements ShipmentProductsService {

    private final BackendServicesClient backendServicesClient;

    public DefaultShipmentProductsService(BackendServicesClient backendServicesClient) {
        this.backendServicesClient = backendServicesClient;
    }

    @Override
    public Map<String, CompletableFuture<Optional<List<ShipmentProductType>>>> getShipmentProducts(List<String> shipmentsOrderNumbers) {
        var futureShipmentProducts = new HashMap<String, CompletableFuture<Optional<List<ShipmentProductType>>>>();

        for (String orderNumber : shipmentsOrderNumbers) {
            var futureShipmentProduct = backendServicesClient.getShipmentProducts(orderNumber);
            futureShipmentProducts.put(orderNumber, futureShipmentProduct);
        }

        return futureShipmentProducts;
    }
}
