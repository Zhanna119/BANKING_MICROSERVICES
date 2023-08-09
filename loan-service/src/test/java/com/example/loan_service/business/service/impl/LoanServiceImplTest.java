package com.example.loan_service.business.service.impl;

import com.example.loan_service.business.mappers.LoanMapper;
import com.example.loan_service.business.repository.LoanRepository;
import com.example.loan_service.business.repository.model.LoanDAO;
import com.example.loan_service.model.Loan;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {
    @Mock
    private LoanRepository repository;

    @Mock
    private LoanMapper mapper;

    @InjectMocks
    private LoanServiceImpl service;

    private LoanDAO loanDAO = new LoanDAO(1L,
                                1L,
                                new BigDecimal(10000.00),
                                LocalDate.of(2020, 01, 01),
                                new BigDecimal(1000.00),
            null);

    private Loan loan = new Loan(1L,
            1L,
            new BigDecimal(10000.00),
            LocalDate.of(2020, 01, 01),
            new BigDecimal(1000.00),
            null);


    private LoanDAO oldLoanDAO = new LoanDAO(1L,
            1L,
            new BigDecimal(10000.00),
            LocalDate.of(2020, 01, 01),
            new BigDecimal(1000.00),
            null);
    private List<LoanDAO> loanDAOList = Arrays.asList(loanDAO, loanDAO, loanDAO);


    @Test
     void testGetAllLoans_Successful() {
     when(repository.findAll()).thenReturn(loanDAOList);
     when(mapper.mapFromDao(loanDAO)).thenReturn(loan);
     List<Loan> list = service.getAllLoans();
     assertEquals(3, list.size());
     assertEquals(loan.getId(), list.get(0).getId());
     verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllLoans_EmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<Loan> result = service.getAllLoans();
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetLoanById_Successful() {
        when(repository.findById(1L)).thenReturn(Optional.of(loanDAO));
        when(mapper.mapFromDao(loanDAO)).thenReturn(loan);
        Optional<Loan> result = service.getLoanById(1L);
        assertEquals(loan, result.get());
        assertTrue(result.isPresent());
        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).mapFromDao(loanDAO);
    }

    @Test
    void testGetLoanById_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        Optional<Loan> result = service.getLoanById(99L);
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void testEditLoan_Successful() {
        when(repository.existsById(1L)).thenReturn(true);
        when(mapper.mapToDao(loan)).thenReturn(loanDAO);
        when(repository.save(loanDAO)).thenReturn(oldLoanDAO);
        when(mapper.mapFromDao(oldLoanDAO)).thenReturn(loan);
        Optional<Loan> result = service.editLoan(1L, loan);
        assertEquals(Optional.of(loan), result);
        verify(repository, times(1)).existsById(1L);
        verify(mapper, times(1)).mapToDao(loan);
        verify(repository, times(1)).save(loanDAO);
        verify(mapper, times(1)).mapFromDao(loanDAO);
    }

    @Test
    void testEditLoan_Empty() {
        when(repository.existsById(1L)).thenReturn(false);
        Optional<Loan> result = service.editLoan(1L, loan);
        assertFalse(result.isPresent());
        verify(repository, times(1)).existsById(1L);
    }

    @Test
    void testSaveLoan_Successful() {
        when(mapper.mapToDao(loan)).thenReturn(loanDAO);
        when(repository.save(loanDAO)).thenReturn(loanDAO);
        when(mapper.mapFromDao(loanDAO)).thenReturn(loan);
        Loan savedLoan = service.saveLoan(loan);
        assertEquals(loan, savedLoan);
        verify(mapper, times(1)).mapToDao(loan);
        verify(repository, times(1)).save(loanDAO);
        verify(mapper,times(1)).mapFromDao(loanDAO);
    }

    @Test
    void testSaveLoanWithDuplicateId() {
        when(repository.findAll()).thenReturn(Collections.singletonList(loanDAO));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.saveLoan(loan);
        });
        assertEquals("Loan with the same id already exists", exception.getMessage());
    }

    @Test
    void testDeleteLoanById_Successful() {
        service.deleteLoanById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteLoanById_NotExistingId() {
        doNothing().when(repository).deleteById(anyLong());
        service.deleteLoanById(99L);
        verify(repository, times(1)).deleteById(99L);
    }
}
