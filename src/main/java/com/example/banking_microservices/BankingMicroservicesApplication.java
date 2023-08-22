package com.example.banking_microservices;

import com.example.banking_microservices.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SwaggerConfig.class)
@ComponentScan(basePackages = {"com.example.account_service"})
public class BankingMicroservicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingMicroservicesApplication.class, args);
    }

}
