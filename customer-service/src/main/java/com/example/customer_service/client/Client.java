package com.example.customer_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class Client {

    @Autowired
    private WebClient webClient;

    /**
     * Checks if a customer with the given ID exists from CustomerService endpoint.
     *
     * @param customerId The customer id to check.
     * @return ResponseEntity containing info about if accounts of customer was found by id.
     */
    private static final String baseUrlAccounts = "http://localhost:9632/api/accounts";

    public ResponseEntity<Object> getAccountsByCustomerId(String customerId) {
        return WebClient.create(baseUrlAccounts)
                .get()
                .uri("/id/{customerId}", customerId)
                .retrieve()
                .toEntity(Object.class)
                .block();
    }
}
