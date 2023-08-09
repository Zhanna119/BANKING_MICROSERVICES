package com.example.loan_service.business.service;

import com.example.loan_service.model.LoanPayment;

import java.time.LocalDate;
import java.util.List;

public interface LoanPaymentService {
    List<LoanPayment> getAllLoanPayments();
    List<LoanPayment> getLoansByDate (LocalDate date);
}
