package com.fedex.aggregator.service.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "backend-services-client")
public record BackendServicesClientConfiguration(String baseUrl) {
}
