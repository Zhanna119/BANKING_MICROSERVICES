package com.example.creditCard_service.business.repository;


import com.example.creditCard_service.business.repository.model.CreditCardPaymentDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardPaymentRepository extends JpaRepository<CreditCardPaymentDAO, Long> {
}

