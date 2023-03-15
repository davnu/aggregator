package com.fedex.aggregator.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentProducts(List<ShipmentProductType> type) {
}
