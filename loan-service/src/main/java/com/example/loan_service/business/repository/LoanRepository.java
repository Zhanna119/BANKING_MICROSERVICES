package com.example.loan_service.business.repository;

import com.example.loan_service.business.repository.model.LoanDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanDAO, Long> {
}

