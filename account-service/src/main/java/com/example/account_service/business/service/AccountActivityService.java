package com.example.account_service.business.service;

import com.example.account_service.model.AccountActivity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountActivityService {
    List<AccountActivity> getAllTransactions();
    List<AccountActivity> getTransactionsByDate (LocalDate date);
    Optional<AccountActivity> getTransactionsByAccountId(Long accountId);
}
