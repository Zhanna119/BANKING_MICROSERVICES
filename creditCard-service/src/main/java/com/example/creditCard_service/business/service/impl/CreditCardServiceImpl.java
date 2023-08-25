package com.example.creditCard_service.business.service.impl;

import com.example.creditCard_service.business.mappers.CreditCardMapper;
import com.example.creditCard_service.business.repository.CreditCardRepository;
import com.example.creditCard_service.business.repository.model.CreditCardDAO;
import com.example.creditCard_service.business.service.CreditCardService;
import com.example.creditCard_service.model.CreditCard;
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
public class CreditCardServiceImpl implements CreditCardService {
    @Autowired
    CreditCardRepository repository;

    @Autowired
    CreditCardMapper mapper;

    @Override
    public List<CreditCard> getAllCreditCards() {
        log.info("Looking for credit cards, returning list");
        List<CreditCard> listOfDao = repository.findAll()
                .stream()
                .map(mapper::mapFromDao)
                .collect(Collectors.toList());
        log.info("Returning list with size: {}", listOfDao.size());
        return listOfDao;
    }

    @Override
    public Optional<CreditCard> getCreditCardById(Long id) {
        log.info("Looking for credit card with id {}", id);
        return repository
                .findById(id)
                .flatMap(creditCard -> Optional.ofNullable(mapper.mapFromDao(creditCard)));
    }


    @Override
    public Optional<CreditCard> editCreditCard(Long id, CreditCard updatedCreditCard) {
        log.info("Updating credit card entry");
        if(repository.existsById(id)){
            log.info("Credit card entry with id {} is updated", id);
            return Optional.ofNullable(mapper.mapFromDao(repository.save(mapper.mapToDao(updatedCreditCard))));
        }
        log.warn("Credit card entry with id {} is not updated", id);
        return Optional.empty();
    }

    @Override
    public CreditCard saveCreditCard(CreditCard creditCard) {
        List<CreditCardDAO> existingCreditCards = repository.findAll();
        for (CreditCardDAO existingCreditCard : existingCreditCards) {
            if (existingCreditCard.getId().equals(creditCard.getId())) {
                log.warn("Credit card with the same id already exists");
                throw new IllegalArgumentException("Credit with the same id already exists");
            }
        }
        log.info("Saving new credit card entry");
        return mapper.mapFromDao(repository.save(mapper.mapToDao(creditCard)));
    }


    @Override
    @Transactional
    public void deleteCreditCardById(Long id) {
        log.info("Deleting credit card with id {}", id);
        repository.deleteById(id);
    }

    @Override
    public List<CreditCard> getAllCreditCardsByCustomerId(Long customerId) {
        log.info("Looking for all customer credit cards by customer id {}", customerId);
        List<CreditCard> newListOfCreditCards = new ArrayList<>();
        List<CreditCardDAO> allCreditCards = repository.findAll();
        for (CreditCardDAO existingCreditCard : allCreditCards) {
            if(existingCreditCard.getCustomerId().equals(customerId)) {
                CreditCard creditCard = mapper.mapFromDao(existingCreditCard);
                newListOfCreditCards.add(creditCard);
            }
        }
        log.info("Returning list with size: {}", newListOfCreditCards.size());
        return newListOfCreditCards;
    }
}
