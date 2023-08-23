package com.example.creditCard_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class CreditCardOpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Credit card service")
                        .description("Credit card service used to get, create, edit, save and delete customer credit cards.")
                        .version("1.0"));
    }
}