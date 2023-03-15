package com.fedex.aggregator;

import com.fedex.aggregator.controller.error.RestTemplateErrorHandler;
import com.fedex.aggregator.service.client.BackendServicesClientConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(BackendServicesClientConfiguration.class)
public class AggregatorConfiguration {

    @Bean
    public ResponseErrorHandler responseErrorHandler() {
        return new RestTemplateErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .errorHandler(responseErrorHandler())
                .build();
    }
}
