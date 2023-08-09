package com.example.loan_service.model;

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
@ApiModel(description = "Model of loan payments")
public class LoanPayment {
    @ApiModelProperty(notes = "The unique ID for the loan payment")
    private Long id;
    @ApiModelProperty(notes = "Loan Id")
    private Long loanId;
    @ApiModelProperty(notes = "Loan payment date")
    @NotNull
    private LocalDate loanPaymentDate;
    @ApiModelProperty(notes = "Loan Payment Amount")
    @NotNull
    @Positive(message = "Payment amount must be a positive number")
    private BigDecimal paymentAmount;
}
