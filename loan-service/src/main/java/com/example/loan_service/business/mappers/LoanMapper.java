package com.example.loan_service.business.mappers;

import com.example.loan_service.business.repository.model.LoanDAO;
import com.example.loan_service.business.repository.model.LoanPaymentDAO;
import com.example.loan_service.model.Loan;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    Loan mapFromDao(LoanDAO loanDAO);

    LoanDAO mapToDao(Loan loan);

    default List<Long> mapToCustomerLoanPaymentIds(List<LoanPaymentDAO> loanPaymentDAOs) {
        return loanPaymentDAOs.stream()
                .map(LoanPaymentDAO::getId)
                .collect(Collectors.toList());
    }

    default LoanPaymentDAO mapToLoanPaymentDAO(Long id) {
        LoanPaymentDAO loanPaymentDAO = new LoanPaymentDAO();
        loanPaymentDAO.setId(id);
        return loanPaymentDAO;
    }
}
