package com.example.swagger.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SwaggerController {

    @GetMapping("/all")
    public String all() {
        return "all";
    }
}
