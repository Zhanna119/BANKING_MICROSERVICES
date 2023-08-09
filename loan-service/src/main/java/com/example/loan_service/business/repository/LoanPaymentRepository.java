package com.example.loan_service.business.repository;


import com.example.loan_service.business.repository.model.LoanPaymentDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanPaymentRepository extends JpaRepository<LoanPaymentDAO, Long> {
}

