package com.example.loan_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfigLoan {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Loan and Loan Payment services")
                        .description("Loan service used to get, create, edit, save and delete customer loans. " +
                                "Loan payments service used to get loan payments.")
                        .version("1.0"));
    }
}





