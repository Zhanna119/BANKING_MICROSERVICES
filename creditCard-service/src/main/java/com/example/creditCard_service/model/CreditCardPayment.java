package com.example.creditCard_service.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model of creditCard payments")
public class CreditCardPayment {
    @ApiModelProperty(notes = "The unique ID for the creditCard payment")
    private Long id;
    @ApiModelProperty(notes = "CreditCard Id")
    private Long creditCardId;
    @ApiModelProperty(notes = "CreditCard Payment Amount")
    @NotNull
    @Positive(message = "CreditCard payment must be a positive number")
    private BigDecimal transactionAmount;
    @ApiModelProperty(notes = "CreditCard payment date")
    @NotNull
    private LocalDate transactionDate;

}
