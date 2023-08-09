package com.example.loan_service.business.service;

import com.example.loan_service.model.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<Loan> getAllLoans();
    Optional<Loan> getLoanById(Long id);
    Optional<Loan> editLoan(Long id, Loan updatedLoan);
    Loan saveLoan(Loan loan);
    void deleteLoanById(Long id);
    List<Loan> getAllLoansByCustomerId(Long customerId);
}
