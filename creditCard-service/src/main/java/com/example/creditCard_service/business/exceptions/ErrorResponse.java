package com.example.creditCard_service.business.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private int errorCode;
}
