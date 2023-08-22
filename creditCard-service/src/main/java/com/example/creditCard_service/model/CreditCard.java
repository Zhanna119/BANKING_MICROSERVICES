package com.example.creditCard_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model of creditCards")
public class CreditCard {
    @ApiModelProperty(notes = "The unique ID for the CreditCard")
    private Long id;

    @ApiModelProperty(notes = "Customer ID")
    private Long customerId;

    @ApiModelProperty(notes = "CreditCard number")
    private String cardNumber;

    @ApiModelProperty(notes = "CreditCard expire date")
    @NotNull
    private LocalDate expireDate;

    @ApiModelProperty(notes = "CreditCard limit")
    @PositiveOrZero
    private BigDecimal cardLimit;

    @ApiModelProperty(notes = "CreditCard available limit")
    @PositiveOrZero
    private BigDecimal availableLimit;

    @ApiModelProperty(notes = "CreditCard debt")
    @PositiveOrZero
    private BigDecimal loanDebt;

    @ApiModelProperty(notes = "CreditCard minimum payment amount")
    @PositiveOrZero
    private BigDecimal minPaymentAmount;

    @ApiModelProperty(notes = "List of customer creditCard ids")
    private List<Long> customerCreditCardPaymentIds;

}
