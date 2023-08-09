package com.example.loan_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Model of loans")
public class Loan {
    @ApiModelProperty(notes = "The unique ID for the Loan")
    private Long id;

    @ApiModelProperty(notes = "Customer ID")
    private Long customerId;

    @ApiModelProperty(notes = "Loan amount")
    private BigDecimal loanAmount;

    @ApiModelProperty(notes = "Loan due date")
    @NotNull
    private LocalDate dueDate;

    @ApiModelProperty(notes = "Loan debt")
    private BigDecimal loanDebt;

    @ApiModelProperty(notes = "List of customer loan payment ids")
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> customerLoanPaymentIds;

    public Loan(Long id, BigDecimal loanAmount, LocalDate dueDate, BigDecimal loanDebt) {
        this.id = id;
        this.loanAmount = loanAmount;
        this.dueDate = dueDate;
        this.loanDebt = loanDebt;
    }
}
