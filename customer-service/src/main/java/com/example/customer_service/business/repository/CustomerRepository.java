package com.example.customer_service.business.repository;

import com.example.customer_service.business.repository.model.CustomerDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerDAO, Long> {
}

