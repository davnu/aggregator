package com.fedex.aggregator.controller;

import com.fedex.aggregator.service.Aggregation;
import com.fedex.aggregator.service.AggregationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/aggregation")
public class AggregationController {

    private final AggregationService aggregationService;

    public AggregationController(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @GetMapping
    public Aggregation getAggregation(
            @RequestParam(defaultValue = "") List<String> shipmentsOrderNumbers,
            @RequestParam(defaultValue = "") List<String> trackOrderNumbers,
            @RequestParam(defaultValue = "") List<String> pricingCountryCodes
    ) {
        return aggregationService.getAggregation(shipmentsOrderNumbers, trackOrderNumbers, pricingCountryCodes);
    }
}
