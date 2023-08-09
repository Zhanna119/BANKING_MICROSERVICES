package com.example.account_service.business.repository;

import com.example.account_service.business.repository.model.AccountActivityDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountActivityRepository extends JpaRepository<AccountActivityDAO, Long> {
}

