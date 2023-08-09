package com.example.account_service.business.service;

import com.example.account_service.business.repository.model.AccountDAO;
import com.example.account_service.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllAccounts();
    Optional<Account> getAccountById(Long id);
    Optional<Account> editAccount(Long id, Account updatedAccount);
    Account saveAccount(Account account);
    void deleteAccountById(Long id);
    List<Account> getAccountsByCustomerId(Long customerId);
}
