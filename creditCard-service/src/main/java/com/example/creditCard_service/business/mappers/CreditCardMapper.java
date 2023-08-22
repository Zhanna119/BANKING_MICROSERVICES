package com.example.creditCard_service.business.mappers;

import com.example.creditCard_service.business.repository.model.CreditCardDAO;
import com.example.creditCard_service.business.repository.model.CreditCardPaymentDAO;
import com.example.creditCard_service.model.CreditCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {

    CreditCardMapper INSTANCE = Mappers.getMapper(CreditCardMapper.class);

    @Mapping(target = "customerCreditCardPaymentIds", source = "customerCreditCardPaymentIds")
    CreditCard mapFromDao(CreditCardDAO creditCardDAO);

    @Mapping(target = "customerCreditCardPaymentIds", source = "customerCreditCardPaymentIds")
    CreditCardDAO mapToDao(CreditCard creditCard);

    @Mapping(target = "customerId", source = "creditCardDAO.customerId")
    CreditCard mapWithCustomerId(CreditCardDAO creditCardDAO);


    default List<Long> mapCreditCardPaymentDAOsToLong(List<CreditCardPaymentDAO> paymentDAOs) {
        return paymentDAOs.stream()
                .map(CreditCardPaymentDAO::getId)
                .collect(Collectors.toList());
    }

    default List<CreditCardPaymentDAO> mapLongsToCreditCardPaymentDAOs(List<Long> ids) {
        return ids.stream()
                .map(id -> {
                    CreditCardPaymentDAO paymentDAO = new CreditCardPaymentDAO();
                    paymentDAO.setId(id);
                    return paymentDAO;
                })
                .collect(Collectors.toList());
    }
}

