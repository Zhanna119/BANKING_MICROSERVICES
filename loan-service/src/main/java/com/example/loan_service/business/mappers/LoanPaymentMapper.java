package com.example.loan_service.business.mappers;

import com.example.loan_service.business.repository.model.LoanPaymentDAO;
import com.example.loan_service.model.LoanPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*@Mapper(componentModel = "spring")
public interface LoanPaymentMapper {
    LoanPayment mapFromDao(LoanPaymentDAO loanPaymentDAO);
    LoanPaymentDAO mapToDao(LoanPayment loanPayment);
}*/

@Mapper(componentModel = "spring")
public interface LoanPaymentMapper {

    @Mapping(target = "loanId", source = "loanDAO.id")
    LoanPayment mapFromDao(LoanPaymentDAO loanPaymentDAO);

    @Mapping(target = "loanDAO.id", source = "loanId")
    LoanPaymentDAO mapToDao(LoanPayment loanPayment);
}
