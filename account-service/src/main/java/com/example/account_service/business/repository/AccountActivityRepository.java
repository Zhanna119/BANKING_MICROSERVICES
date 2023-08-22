package com.example.account_service.business.repository;

import com.example.account_service.business.repository.model.AccountActivityDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountActivityRepository extends JpaRepository<AccountActivityDAO, Long> {
}

