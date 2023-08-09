package com.example.account_service.business.service.impl;

import com.example.account_service.business.mappers.AccountActivityMapper;
import com.example.account_service.business.repository.AccountActivityRepository;
import com.example.account_service.business.repository.model.AccountActivityDAO;
import com.example.account_service.model.AccountActivity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountActivityServiceImplTest {
    @Mock
    private AccountActivityRepository repository;

    @Mock
    private AccountActivityMapper mapper;

    @InjectMocks
    private AccountActivityServiceImpl service;

    private AccountActivityDAO accountActivityDAO;
    private AccountActivity accountActivity;
    private AccountActivityDAO oldAccountActivityDAOEntry;
    private List<AccountActivityDAO> accountActivityDAOList;

    @BeforeEach
    public void init() {
        accountActivityDAO = createAccountActivityDAO();
        accountActivity = createAccountActivity();
        accountActivityDAOList = createAccountActivityDAOList(accountActivityDAO);
        oldAccountActivityDAOEntry = createOldAccountDAOEntry();
    }

    @Test
    public void testGetAllAccountActivities_Successful() {
        when(repository.findAll()).thenReturn(accountActivityDAOList);
        when(mapper.mapFromDao(accountActivityDAO)).thenReturn(accountActivity);
        List<AccountActivity> list = service.getAllTransactions();
        assertEquals(3, list.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetAllAccountActivities_Empty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<AccountActivity> result = service.getAllTransactions();
        verify(repository, times(1)).findAll();
        assertTrue(result.isEmpty());
    }


    @Test
    public void testGetTransactionsByDate_NotFound() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        LocalDate searchDate = LocalDate.of(2020, 01, 01);
        List<AccountActivity> result = service.getTransactionsByDate(searchDate);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
        verify(mapper, never()).mapFromDao(any());
    }

    @Test
    public void testGetTransactionsByAccountId_WithValidAccountId_ReturnsAccountActivity() {
        Long accountId = 123L;
        AccountActivityDAO AccountActivityDao = new AccountActivityDAO();
        AccountActivity expectedActivity = new AccountActivity();
        when(repository.findById(accountId)).thenReturn(Optional.of(accountActivityDAO));
        when(mapper.mapFromDao(accountActivityDAO)).thenReturn(expectedActivity);
        Optional<AccountActivity> result = service.getTransactionsByAccountId(accountId);
        verify(repository, times(1)).findById(accountId);
        verify(mapper, times(1)).mapFromDao(accountActivityDAO);
        assertEquals(Optional.of(expectedActivity), result);
    }

    @Test
    public void testGetTransactionsByAccountId_WithInvalidAccountId_ReturnsEmptyOptional() {
        Long invalidAccountId = 456L;
        when(repository.findById(invalidAccountId)).thenReturn(Optional.empty());
        Optional<AccountActivity> result = service.getTransactionsByAccountId(invalidAccountId);
        verify(repository, times(1)).findById(invalidAccountId);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetTransactionsByDate() {
        when(repository.findAll()).thenReturn(accountActivityDAOList);
        when(mapper.mapFromDao(any())).thenReturn(accountActivity);
        LocalDate searchDate = LocalDate.of(2020, 01, 01);
        List<AccountActivity> result = service.getTransactionsByDate(searchDate);
        assertEquals(3, result.size());
        verify(repository, times(1)).findAll();
        verify(mapper, times(3)).mapFromDao(any());
    }



    private List<AccountActivityDAO> createAccountActivityDAOList(AccountActivityDAO accountActivityDAO) {
        List<AccountActivityDAO> list = new ArrayList<>();
        list.add(accountActivityDAO);
        list.add(accountActivityDAO);
        list.add(accountActivityDAO);
        return list;
    }

    private AccountActivityDAO createOldAccountDAOEntry() {
        return new AccountActivityDAO(
                1L,
                null,
                new BigDecimal(10000.00),
                LocalDate.of(2020, 01, 01),
                "Description"
        );
    }

    private AccountActivityDAO createAccountActivityDAO() {
        return new AccountActivityDAO(
                1L,
                null,
                new BigDecimal(1000.00),
                LocalDate.of(2020, 01, 01),
                "Description"
        );
    }

    private AccountActivity createAccountActivity() {
        return new AccountActivity(
                1L,
                null,
                new BigDecimal(1000.00),
                LocalDate.of(2020, 01, 01),
                "Description"
        );
    }

}
