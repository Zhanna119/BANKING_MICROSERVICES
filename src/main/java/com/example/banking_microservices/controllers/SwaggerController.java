package com.example.banking_microservices.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SwaggerController {

    @GetMapping("/all")
    public String all() {
        return "all";
    }
}
