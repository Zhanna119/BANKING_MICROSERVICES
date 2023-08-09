package com.example.account_service.business.service.impl;

import com.example.account_service.business.mappers.AccountActivityMapper;
import com.example.account_service.business.repository.AccountActivityRepository;
import com.example.account_service.business.repository.model.AccountActivityDAO;
import com.example.account_service.business.service.AccountActivityService;
import com.example.account_service.model.AccountActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountActivityServiceImpl implements AccountActivityService {
    @Autowired
    AccountActivityRepository repository;

    @Autowired
    AccountActivityMapper mapper;

    @Override
    public List<AccountActivity> getAllTransactions() {
        log.info("Looking for all transactions, returning list");
        List<AccountActivity> listOfDao = repository.findAll()
                .stream()
                .map(mapper::mapFromDao)
                .collect(Collectors.toList());
        log.info("Returning list with size: {}", listOfDao.size());
        return listOfDao;
        }

    @Override
    public List<AccountActivity> getTransactionsByDate(LocalDate date) {
        log.info("Looking for transactions by passing in date, returning list");
        List<AccountActivityDAO> listOfDao = repository.findAll();
        List<AccountActivity> resultList = new ArrayList<>();
        for (AccountActivityDAO dao : listOfDao) {
            AccountActivity accountActivity = mapper.mapFromDao(dao);
            if(dao.getTransactionDate().equals(date)) {
                resultList.add(accountActivity);
            }
        }
        log.info("Returning list with size: {}", resultList.size());
        return resultList;
    }

    @Override
    public Optional<AccountActivity> getTransactionsByAccountId(Long accountId) {
        return repository
                .findById(accountId)
                .flatMap(accountActivity -> Optional.ofNullable(mapper.mapFromDao(accountActivity)));
    }

}
