package com.example.loan_service.business.service.impl;

import com.example.loan_service.business.mappers.LoanPaymentMapper;
import com.example.loan_service.business.repository.LoanPaymentRepository;
import com.example.loan_service.business.repository.model.LoanPaymentDAO;
import com.example.loan_service.business.service.LoanPaymentService;
import com.example.loan_service.model.LoanPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoanPaymentsServiceImpl implements LoanPaymentService {
    @Autowired
    LoanPaymentRepository repository;

    @Autowired
    LoanPaymentMapper mapper;

    @Override
    public List<LoanPayment> getAllLoanPayments() {
        log.info("Looking for all loans payments, returning list");
        List<LoanPayment> listOfDao = repository.findAll()
                .stream()
                .map(mapper::mapFromDao)
                .collect(Collectors.toList());
        log.info("Returning list with size: {}", listOfDao.size());
        return listOfDao;
    }

    @Override
    public List<LoanPayment> getLoansByDate(LocalDate date) {
        log.info("Looking for loans by passing in date, returning list");
        List<LoanPaymentDAO> listOfDao = repository.findAll();
        List<LoanPayment> resultList = new ArrayList<>();
        for (LoanPaymentDAO dao : listOfDao) {
            LoanPayment loanPayment = mapper.mapFromDao(dao);
            if(dao.getLoanPaymentDate().equals(date)) {
                resultList.add(loanPayment);
            }
        }
        log.info("Returning list with size: {}", resultList.size());
        return resultList;
    }
}
