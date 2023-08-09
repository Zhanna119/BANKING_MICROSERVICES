package com.example.customer_service.config;

import com.example.account_service.model.Account;
import com.example.loan_service.model.Loan;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class WebCustomer {
    private static final String baseUrlAccounts = "http://localhost:9632/api/accounts";
    private static final String getBaseUrlLoans = "http://localhost:9633/api/loans";

    public static Flux<Account> getAccountsByCustomerId(String customerId) {
        Flux<Account> accounts = WebClient.create(baseUrlAccounts)
                .get()
                .uri("/id/" + customerId)
                .retrieve()
                .bodyToFlux(Account.class);
        return accounts;
    }

    public static Flux<Loan> getLoansByCustomerId(String customerId) {
        Flux<Loan> loans = WebClient.create(getBaseUrlLoans)
                .get()
                .uri("/getById/" + customerId)
                .retrieve()
                .bodyToFlux(Loan.class);
        return loans;
    }
}


