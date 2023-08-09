package com.example.customer_service.config;


import com.example.account_service.model.Account;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class WebClientBuilder {
    private static final String baseUrl = "http://localhost:9632/api/accounts";

    public static Flux<Account> getAccountsByCustomerId(String customerId) {
        Flux<Account> accounts = WebClient.create(baseUrl)
                .get()
                .uri("/id/" + customerId)
                .retrieve()
                .bodyToFlux(Account.class);
        return accounts;
    }
}
