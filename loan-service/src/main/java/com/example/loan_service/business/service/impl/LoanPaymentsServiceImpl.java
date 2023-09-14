package com.example.loan_service.business.service.impl;

import com.example.loan_service.business.mappers.LoanPaymentMapper;
import com.example.loan_service.business.repository.LoanPaymentRepository;
import com.example.loan_service.business.repository.model.LoanPaymentDAO;
import com.example.loan_service.business.service.LoanPaymentService;
import com.example.loan_service.model.LoanPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoanPaymentsServiceImpl implements LoanPaymentService {

    //todo почему полями добавлены? лучше выбрать что-то одно конструктор или поля во всем микросервисе
    @Autowired
    LoanPaymentRepository repository;

    @Autowired
    LoanPaymentMapper mapper;

    @Override
    public List<LoanPayment> getAllLoanPayments() {
        log.info("Looking for all loans payments, returning list");
        List<LoanPayment> listOfDao = repository.findAll()
                .stream()
                .map(mapper::mapFromDAO)
                .collect(Collectors.toList());
        log.info("Returning list with size: {}", listOfDao.size());
        return listOfDao;
    }

    @Override
    public List<LoanPayment> getLoansByDate(LocalDate date) {

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getCredentials();
        log.info("Looking for loans by passing in date, returning list");
        List<LoanPaymentDAO> listOfDao = repository.findAll(); //todo а если у нас 5 миллиардом записей? findByDate
        //todo лучше переписать на Criteria https://www.baeldung.com/spring-data-criteria-queries
        List<LoanPayment> resultList = new ArrayList<>();
        for (LoanPaymentDAO dao : listOfDao) {
            LoanPayment loanPayment = mapper.mapFromDAO(dao);
            if(dao.getLoanPaymentDate().equals(date)) {
                resultList.add(loanPayment);
            }
        }

        //todo пора писать подобным образом
        List<LoanPayment> collect = repository.findAll().stream()
                .map(mapper::mapFromDAO)
                .filter(dao -> dao.getLoanPaymentDate().equals(date))
                .collect(Collectors.toList());


        log.info("Returning list with size: {}", resultList.size());
        return resultList;
    }
}
