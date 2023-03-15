package com.fedex.aggregator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedex.aggregator.controller.AggregationController;
import com.fedex.aggregator.service.Aggregation;
import com.fedex.aggregator.service.ShipmentProductType;
import com.fedex.aggregator.service.TrackStatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Locale;

import static com.fedex.aggregator.AggregatorTestUtils.getRandomOrderNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AggregationController.class)
@Import(AggregatorTestConfig.class)
class AggregatorControllerSpringBootTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        Mockito.when(restTemplate.getForObject(any(URI.class), eq(BigDecimal.class))).thenReturn(new BigDecimal("10"));
        Mockito.when(restTemplate.getForObject(any(URI.class), eq(TrackStatusType.class))).thenReturn(TrackStatusType.COLLECTED);
        Mockito.when(restTemplate.getForObject(any(URI.class), eq(ShipmentProductType[].class))).thenReturn(new ShipmentProductType[]{ShipmentProductType.ENVELOPE});
    }

    @Test
    void getAggregation_assertValues() throws Exception {
        String shipmentOrderNumber = getRandomOrderNumber();
        String trackOrderNumber = getRandomOrderNumber();
        String pricingCountryCode = Locale.CANADA.getCountry();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/aggregation")
                .param("shipmentsOrderNumbers", shipmentOrderNumber)
                .param("trackOrderNumbers", trackOrderNumber)
                .param("pricingCountryCodes", pricingCountryCode)
        ).andExpect(status().isOk()).andReturn();

        Aggregation model = extractModel(result);

        assertThat(model.shipments()).isNotNull().hasSize(1);
        assertThat(model.track()).isNotNull().hasSize(1);
        assertThat(model.pricing()).isNotNull().hasSize(1);

        assertThat(model.shipments()).containsKey(shipmentOrderNumber);
        assertThat(model.track()).containsKey(trackOrderNumber);
        assertThat(model.pricing()).containsKey(pricingCountryCode);
    }

    @Test
    void getAggregation_assertEmptyParams() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/aggregation")
                .param("shipmentsOrderNumbers", getRandomOrderNumber(), getRandomOrderNumber())
                .param("trackOrderNumbers", getRandomOrderNumber(), getRandomOrderNumber())
                .param("pricingCountryCodes", Locale.CANADA.getCountry(), Locale.US.getCountry())

        ).andExpect(status().isOk()).andReturn();

        Aggregation model = extractModel(result);
        assertThat(model.shipments()).isNotNull().hasSize(2);
        assertThat(model.track()).isNotNull().hasSize(2);
        assertThat(model.pricing()).isNotNull().hasSize(2);
    }

    @Test
    void getAggregation_assertMultivaluedParams() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/aggregation")).andExpect(status().isOk()).andReturn();

        Aggregation model = extractModel(result);
        assertThat(model.shipments()).isNotNull().isEmpty();
        assertThat(model.track()).isNotNull().isEmpty();
        assertThat(model.pricing()).isNotNull().isEmpty();
    }

    private Aggregation extractModel(MvcResult result) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.readValue(result.getResponse().getContentAsString(), Aggregation.class);
    }
}
