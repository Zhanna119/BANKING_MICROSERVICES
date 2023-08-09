package com.example.loan_service.business.repository.model;

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
@Table(name = "loans")
public class LoanDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "loan_amount", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal loanAmount;

    @Column(name = "loan_due_date")
    private LocalDate dueDate;

    @Column(name = "loan_debt", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal loanDebt;

    @OneToMany(mappedBy = "loanDAO", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanPaymentDAO> customerLoanPaymentIds;

}

