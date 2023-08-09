package com.example.account_service.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
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
@ApiModel(description = "Model of customer account activity")
public class AccountActivity {
    @ApiModelProperty(notes = "The unique ID for the account activity")
    private Long id;

    @ApiModelProperty(notes = "Account ID")
    private Long accountId;

    @ApiModelProperty(notes = "Transaction Amount")
    @NotNull
    @Positive(message = "Transaction amount must be a positive number")
    private BigDecimal transactionAmount;

    @ApiModelProperty(notes = "Transaction date")
    @NotNull
    private LocalDate transactionDate;

    @ApiModelProperty(notes = "Transaction transcription")
    @NotEmpty
    private String description;
}


