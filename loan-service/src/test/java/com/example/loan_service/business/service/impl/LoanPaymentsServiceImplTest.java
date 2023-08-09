package com.example.loan_service.business.service.impl;

import com.example.loan_service.business.mappers.LoanPaymentMapper;
import com.example.loan_service.business.repository.LoanPaymentRepository;
import com.example.loan_service.business.repository.model.LoanPaymentDAO;
import com.example.loan_service.model.LoanPayment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanPaymentsServiceImplTest {
    @Mock
    private LoanPaymentRepository repository;

    @Mock
    private LoanPaymentMapper mapper;

    @InjectMocks
    private LoanPaymentsServiceImpl service;

    private LoanPayment loanPayment =
            new LoanPayment(
                    1L,
                    null,
                    LocalDate.of(2020, 01, 01),
                    new BigDecimal(10000.00));

    private LoanPaymentDAO loanPaymentDAO =
            new LoanPaymentDAO(
                    1L,
                    null,
                    LocalDate.of(2020, 01, 01),
                    new BigDecimal(10000.00));

    private List<LoanPaymentDAO> loanPaymentDAOList = Arrays.asList(loanPaymentDAO, loanPaymentDAO, loanPaymentDAO);

    @Test
    void testGetAllLoanPayments_Successful() {
        when(repository.findAll()).thenReturn(loanPaymentDAOList);
        when(mapper.mapFromDao(loanPaymentDAO)).thenReturn(loanPayment);
        List<LoanPayment> result = service.getAllLoanPayments();
        assertEquals(3, result.size());
        assertEquals(loanPayment.getId(), result.get(0).getId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllLoanPayments_EmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<LoanPayment> result = service.getAllLoanPayments();
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetLoansByDate_Successful() {
        when(repository.findAll()).thenReturn(loanPaymentDAOList);
        when(mapper.mapFromDao(loanPaymentDAO)).thenReturn(loanPayment);
        LocalDate date = LocalDate.of(2020, 01, 01);
        List<LoanPayment> result = service.getLoansByDate(date);
        assertEquals(3, result.size());
        verify(repository, times(1)).findAll();
        verify(mapper, times(3)).mapFromDao(loanPaymentDAO);
    }

    @Test
    void testGetLoansByDate_Empty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        LocalDate date = LocalDate.of(2020, 01, 01);
        List<LoanPayment> result = service.getLoansByDate(date);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }
}