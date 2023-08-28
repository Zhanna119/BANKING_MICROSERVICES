package com.example.account_service.business.repository;

import com.example.account_service.business.repository.model.AccountDAO;
import com.example.account_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountDAO, Long> {
}

