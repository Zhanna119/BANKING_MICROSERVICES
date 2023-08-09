package com.example.loan_service.business.service.impl;

import com.example.loan_service.business.mappers.LoanMapper;
import com.example.loan_service.business.repository.LoanRepository;
import com.example.loan_service.business.repository.model.LoanDAO;
import com.example.loan_service.business.service.LoanService;
import com.example.loan_service.model.Loan;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    LoanRepository repository;

    @Autowired
    LoanMapper mapper;

    @Override
    public List<Loan> getAllLoans() {
        log.info("Looking for all loans, returning list");
        List<Loan> listOfDao = repository.findAll()
                .stream()
                .map(mapper::mapFromDao)
                .collect(Collectors.toList());
        log.info("Returning list with size: {}", listOfDao.size());
        return listOfDao;
    }

    @Override
    public Optional<Loan> getLoanById(Long id) {
        log.info("Looking for loan with id {}", id);
        return repository
                .findById(id)
                .flatMap(loan -> Optional.ofNullable(mapper.mapFromDao(loan)));
    }


    @Override
    public Optional<Loan> editLoan(Long id, Loan updatedLoan) {
        log.info("Updating loan entry");
        if(repository.existsById(id)){
            log.info("Loan entry with id {} is updated", id);
            return Optional.ofNullable(mapper.mapFromDao(repository.save(mapper.mapToDao(updatedLoan))));
        }
        log.warn("Loan entry with id {} is not updated", id);
        return Optional.empty();
    }


    @Override
    public Loan saveLoan(Loan loan) {
        List<LoanDAO> existingLoans = repository.findAll();
        for (LoanDAO existingLoan : existingLoans) {
            if (existingLoan.getId().equals(loan.getId())) {
                log.warn("Loan with the same id already exists");
                throw new IllegalArgumentException("Loan with the same id already exists");
            }
        }
        log.info("Saving new loan entry");
        return mapper.mapFromDao(repository.save(mapper.mapToDao(loan)));
    }

    @Override
    @Transactional
    public void deleteLoanById(Long id) {
        log.info("Deleting loan with id {}", id);
        repository.deleteById(id);
    }

    @Override
    public List<Loan> getAllLoansByCustomerId(Long customerId) {
        log.info("Looking for all loans by customer id {}", customerId);

        List<Loan> listOfLoans = repository.findAll().stream() // Преобразуем список в поток
                .filter(existingLoan -> existingLoan.getCustomerId().equals(customerId)) // Фильтруем по customerId
                .map(existingLoan -> mapper.mapFromDao(existingLoan)) // Преобразуем каждый элемент потока с использованием mapper
                .collect(Collectors.toList()); // Собираем результат в новый список

        log.info("Returning list with size: {}", listOfLoans.size());
        return listOfLoans;
    }

}
