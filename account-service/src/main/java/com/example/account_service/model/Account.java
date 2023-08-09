package com.example.account_service.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model of accounts")
public class Account {
    @ApiModelProperty(notes = "The unique ID for the Account")
    private Long id;

    @ApiModelProperty(notes = "Customer ID")
    private Long customerId;

    @ApiModelProperty(notes = "Account IBAN number")
    @NotEmpty
    private String iban;

    @ApiModelProperty(notes = "Account current balance")
    private BigDecimal currentBalance;

    @ApiModelProperty(notes = "Account currency")
    @NotEmpty
    private String currency;

    @ApiModelProperty(notes = "List of customer transactions ids")
    private List<Long> customerAccountActivityIds;
}

