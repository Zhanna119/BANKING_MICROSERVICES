package com.example.account_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Account and Account Payment services")
                        .description("Account service used to get, create, edit, save and delete customer accounts. " +
                                "Transaction service used to get customer transactions.")
                        .version("1.0"));
    }
}
