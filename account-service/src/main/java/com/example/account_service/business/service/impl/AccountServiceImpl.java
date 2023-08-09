package com.example.account_service.business.service.impl;

import com.example.account_service.business.mappers.AccountMapper;
import com.example.account_service.business.repository.AccountRepository;
import com.example.account_service.business.repository.model.AccountDAO;
import com.example.account_service.business.service.AccountService;
import com.example.account_service.model.Account;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository repository;

    @Autowired
    AccountMapper mapper;


    @Override
    public List<Account> getAllAccounts() {
        log.info("Looking for all accounts, returning list");
        List<Account> listOfDao = repository.findAll()
                .stream()
                .map(mapper::mapFromDao)
                .collect(Collectors.toList());
        log.info("Returning list with size: {}", listOfDao.size());
        return listOfDao;
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        log.info("Looking for account with id {}", id);
        return repository
                .findById(id)
                .flatMap(account -> Optional.ofNullable(mapper.mapFromDao(account)));
    }

    @Override
    public Optional<Account> editAccount(Long id, Account updatedAccount) {
        log.info("Updating account entry");
        if(repository.existsById(id)){
            log.info("Account entry with id {} is updated", id);
            return Optional.ofNullable(mapper.mapFromDao(repository.save(mapper.mapToDao(updatedAccount))));
        }
        log.warn("Account entry with id {} is not updated", id);
        return Optional.empty();
    }


    @Override
    public Account saveAccount(Account account) {
        List<AccountDAO> existingAccounts = repository.findAll();
        for (AccountDAO existingAccount : existingAccounts) {
            if (existingAccount.getIban().equals(account.getIban())
                    && existingAccount.getCurrency().equals(account.getCurrency())) {
                log.warn("Account with the same IBAN and currency already exists");
                throw new IllegalArgumentException("Account with the same IBAN and currency already exists");
            }
        }
        log.info("Saving new Account entry");
        return mapper.mapFromDao(repository.save(mapper.mapToDao(account)));
    }

    @Override
    @Transactional
    public void deleteAccountById(Long id) {
        log.info("Deleting account with id {}", id);
        repository.deleteById(id);
    }

    @Override
    public List<Account> getAccountsByCustomerId(Long customerId) {
        log.info("Looking for all accounts by customer id {}", customerId);
        List<Account> listOfAccounts = new ArrayList<>();
        List<AccountDAO> existingAccounts = repository.findAll();
        for (AccountDAO existingAccount : existingAccounts) {
            if (existingAccount.getCustomerId().equals(customerId)) {
                Account account = mapper.mapFromDao(existingAccount);
                listOfAccounts.add(account);
            }
        }
        log.info("Returning list with size: {}", listOfAccounts.size());
        return listOfAccounts;
    }

}
