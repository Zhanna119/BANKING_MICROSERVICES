package com.example.creditCard_service.business.mappers;

import com.example.creditCard_service.business.repository.model.CreditCardPaymentDAO;
import com.example.creditCard_service.model.CreditCardPayment;
import org.bouncycastle.dvcs.CCPDRequestBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CreditCardPaymentMapper {

    @Mapping(target = "creditCardId", source = "creditCardDAO.id")
    CreditCardPayment mapFromDao(CreditCardPaymentDAO creditCardPaymentDAO);

    @Mapping(target = "creditCardDAO.id", source = "creditCardId")
    CreditCardPaymentDAO mapToDao(CreditCardPayment creditCardPayment);
}
