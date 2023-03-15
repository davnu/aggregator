# Aggregator App Ready!

### How to run the app
This is a Spring Boot 3 application. To run it, navigate to the main directory and run the command:
`./gradlew bootRun`

### Aggregator api examples
Here are some examples of how to use the API:

`curl "http://localhost:8080/aggregation?trackOrderNumbers=123456789"`

`curl "http://localhost:8080/aggregation?shipmentsOrderNumbers=123456789&trackOrderNumbers=123456789&pricingCountryCodes=CA"`

`curl "http://localhost:8080/aggregation?shipmentsOrderNumbers=123456789,123456789&trackOrderNumbers=123456789,123456789&pricingCountryCodes=CA,NL"`
