package com.example.account_service.business.repository;

import com.example.account_service.business.repository.model.AccountDAO;
import com.example.account_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountDAO, Long> {
    //List<Account> getAllAccountsByCustomerId(Long customerId);
}

