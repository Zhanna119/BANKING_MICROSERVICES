package com.example.creditCard_service.business.repository;


import com.example.creditCard_service.business.repository.model.CreditCardDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCardDAO, Long> {
}


