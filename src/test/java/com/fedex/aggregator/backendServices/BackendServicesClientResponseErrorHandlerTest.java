package com.fedex.aggregator.backendServices;

import com.fedex.aggregator.AggregatorConfiguration;
import com.fedex.aggregator.controller.error.ApiException;
import com.fedex.aggregator.service.client.BackendServicesClient;
import com.fedex.aggregator.service.client.BackendServicesClientConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AggregatorConfiguration.class, BackendServicesClient.class})
class BackendServicesClientResponseErrorHandlerTest {

    private ResponseActions responseActions;

    private BackendServicesClient backendServicesClient;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        backendServicesClient = new BackendServicesClient(new BackendServicesClientConfiguration("http://localhost"), restTemplate);
        responseActions = MockRestServiceServer.createServer(restTemplate).expect(method(HttpMethod.GET));
    }

    @Test
    public void handle4xx_ReturnException() {
        responseActions.andRespond(withUnauthorizedRequest());
        catchThrowableOfType(() -> backendServicesClient.getPricing(Locale.US.getCountry()), ApiException.class);
    }

    @Test
    public void handle5xx_DoesNotReturnException() {
        responseActions.andRespond(withBadRequest());
        assertThatCode(() -> backendServicesClient.getPricing(Locale.US.getCountry())).doesNotThrowAnyException();
    }
}
