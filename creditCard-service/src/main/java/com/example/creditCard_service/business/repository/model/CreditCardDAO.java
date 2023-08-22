package com.example.creditCard_service.business.repository.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "creditCards")
public class CreditCardDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creditCard_id")
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "number")
    private String cardNumber;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "creditLimit", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal cardLimit;

    @Column(name = "available_limit", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal availableLimit;

    @Column(name = "debt", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal loanDebt;

    @Column(name = "min_payment_amount", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal minPaymentAmount;

    @OneToMany(mappedBy = "creditCardDAO", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCardPaymentDAO> customerCreditCardPaymentIds;
}

