package com.example.loan_service.business.repository.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loan_payment")
public class LoanPaymentDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_payment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private LoanDAO loanDAO;

    @Column(name = "loan_payment_date")
    @NotNull
    private LocalDate loanPaymentDate;
    @Positive(message = "Payment amount must be a positive number")
    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;
}
