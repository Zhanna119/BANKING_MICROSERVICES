package com.example.loan_service.business.mappers;

import com.example.loan_service.business.repository.model.LoanPaymentDAO;
import com.example.loan_service.model.LoanPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface LoanPaymentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "loanDAO.id", target = "loanId")
    @Mapping(source = "loanPaymentDate", target = "loanPaymentDate")
    @Mapping(source = "paymentAmount", target = "paymentAmount")
    LoanPayment mapFromDAO(LoanPaymentDAO loanPaymentDAO);

    @Mapping(source = "id", target = "id")
    @Mapping(target = "loanDAO", ignore = true) // We ignore it because DAO layer will handle it
    @Mapping(source = "loanPaymentDate", target = "loanPaymentDate")
    @Mapping(source = "paymentAmount", target = "paymentAmount")
    LoanPaymentDAO mapToDAO(LoanPayment loanPayment);
}
