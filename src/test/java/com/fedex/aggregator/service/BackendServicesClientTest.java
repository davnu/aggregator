package com.fedex.aggregator.service;


import com.fedex.aggregator.service.client.BackendServicesClient;
import com.fedex.aggregator.service.client.BackendServicesClientConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.fedex.aggregator.AggregatorTestUtils.getRandomOrderNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class BackendServicesClientTest {

    @Mock
    private RestTemplate restTemplate;

    private BackendServicesClient backendServicesClient;

    @BeforeEach
    public void setup() {
        backendServicesClient = new BackendServicesClient(new BackendServicesClientConfiguration("http://localhost"), restTemplate);
    }

    @Test
    public void getShipmentProducts_success() throws ExecutionException, InterruptedException {
        Mockito.when(restTemplate.getForObject(any(URI.class), eq(ShipmentProductType[].class))).thenReturn(new ShipmentProductType[]{ShipmentProductType.ENVELOPE});

        var expectedShipmentProducts = List.of(ShipmentProductType.ENVELOPE);
        assertThat(backendServicesClient.getShipmentProducts(getRandomOrderNumber()).get().get()).isEqualTo(expectedShipmentProducts);
    }

    @Test
    public void getShipmentProducts_clientError() throws ExecutionException, InterruptedException {
        Mockito.when(restTemplate.getForObject(any(URI.class), eq(ShipmentProductType[].class))).thenThrow(new RestClientException("error"));

        assertThat(backendServicesClient.getShipmentProducts(getRandomOrderNumber()).get()).isNotPresent();
    }

    @Test
    public void getTrackStatus_success() throws ExecutionException, InterruptedException {
        Mockito.when(restTemplate.getForObject(any(URI.class), eq(TrackStatusType.class))).thenReturn(TrackStatusType.COLLECTED);

        var expectedTrackStatusType = TrackStatusType.COLLECTED;
        assertThat(backendServicesClient.getTrackStatus(getRandomOrderNumber()).get().get()).isEqualTo(expectedTrackStatusType);
    }

    @Test
    public void getTrackStatus_ClientError() throws ExecutionException, InterruptedException {
        Mockito.when(restTemplate.getForObject(any(URI.class), eq(TrackStatusType.class))).thenThrow(new RestClientException("error"));

        assertThat(backendServicesClient.getTrackStatus(getRandomOrderNumber()).get()).isNotPresent();
    }


    @Test
    public void getPricing_success() throws ExecutionException, InterruptedException {
        Mockito.when(restTemplate.getForObject(any(URI.class), eq(BigDecimal.class))).thenReturn(new BigDecimal("10"));

        var expectedPricing = new BigDecimal("10");
        assertThat(backendServicesClient.getPricing(Locale.CANADA.getCountry()).get().get()).isEqualTo(expectedPricing);
    }

    @Test
    public void getPricing_ClientError() throws ExecutionException, InterruptedException {
        Mockito.when(restTemplate.getForObject(any(URI.class), eq(BigDecimal.class))).thenThrow(new RestClientException("error"));

        assertThat(backendServicesClient.getPricing(Locale.CANADA.getCountry()).get()).isNotPresent();
    }
}
