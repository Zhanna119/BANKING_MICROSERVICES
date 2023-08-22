package com.example.creditCard_service.business.service;

import com.example.creditCard_service.model.CreditCard;

import java.util.List;
import java.util.Optional;

public interface CreditCardService {
    List<CreditCard> getAllCreditCards();
    Optional<CreditCard> getCreditCardById(Long id);
    Optional<CreditCard> editCreditCard(Long id, CreditCard updatedCreditCard);
    CreditCard saveCreditCard(CreditCard creditCard);
    void deleteCreditCardById(Long id);
    List<CreditCard> getAllCreditCardsByCustomerId(Long customerId);
}
