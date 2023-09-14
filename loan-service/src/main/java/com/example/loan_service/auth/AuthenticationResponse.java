package com.example.loan_service.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

//todo а не хочешь oauth 2.0 ?
public class AuthenticationResponse {

    private String token;
}
