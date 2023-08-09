package com.example.account_service.business.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.example.account_service.business.mappers.AccountMapper;
import com.example.account_service.business.repository.AccountRepository;
import com.example.account_service.business.repository.model.AccountDAO;
import com.example.account_service.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository repository;

    @Mock
    private AccountMapper mapper;

    @InjectMocks
    private AccountServiceImpl service;

    private AccountDAO accountDAO;
    private Account account;
    private AccountDAO oldAccountDAOEntry;
    private List<AccountDAO> accountDAOList;

    @BeforeEach
    public void init() {
        accountDAO = createAccountDAO();
        account = createAccount();
        accountDAOList = createAccountDAOList(accountDAO);
        oldAccountDAOEntry = createOldAccountDAOEntry();

    }

    @Test
    public void testGetAllAccounts_Successful() {
        when(repository.findAll()).thenReturn(accountDAOList);
        when(mapper.mapFromDao(accountDAO)).thenReturn(account);
        List<Account> list = service.getAllAccounts();
        assertEquals(3, list.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testGetAllAccounts_Empty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<Account> result = service.getAllAccounts();
        verify(repository, times(1)).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAccountsById_Successful() {
        when(repository.findById(1L)).thenReturn(Optional.of(accountDAO));
        when(mapper.mapFromDao(accountDAO)).thenReturn(account);
        Optional<Account> actualResult = service.getAccountById(account.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(account, actualResult.get());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void testGetAccountsById_NotExistingId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        Optional<Account> result = service.getAccountById(99L);
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(anyLong());
    }


    @Test
    public void testEditAccount_Successful() {
        when(repository.existsById(1L)).thenReturn(true);
        when(mapper.mapToDao(account)).thenReturn(accountDAO);
        when(repository.save(accountDAO)).thenReturn(oldAccountDAOEntry);
        when(mapper.mapFromDao(oldAccountDAOEntry)).thenReturn(account);
        Optional<Account> result = service.editAccount(1L, account);
        assertEquals(Optional.of(account), result);
        verify(repository, times(1)).existsById(1L);
        verify(mapper, times(1)).mapToDao(account);
        verify(repository, times(1)).save(accountDAO);
        verify(mapper, times(1)).mapFromDao(oldAccountDAOEntry);
    }

    @Test
    public void testEditAccount_NotExistingId() {
        when(repository.existsById(99L)).thenReturn(false);
        Optional<Account> result = service.editAccount(99L, account);
        assertFalse(result.isPresent());
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).save(any());
    }

    @Test
    public void testSaveAccount_Successful() {
        when(mapper.mapToDao(account)).thenReturn(accountDAO);
        when(repository.save(accountDAO)).thenReturn(accountDAO);
        when(mapper.mapFromDao(accountDAO)).thenReturn(account);
        Account savedAccount = service.saveAccount(account);
        verify(mapper, times(1)).mapToDao(account);
        verify(repository, times(1)).save(accountDAO);
        verify(mapper, times(1)).mapFromDao(accountDAO);
    }

    @Test
    public void testSaveAccount_DuplicateCustomer() {
        when(repository.findAll()).thenReturn(Collections.singletonList(accountDAO));
        try {
            service.saveAccount(account);
            fail("Expected IllegalArgumentException to be thrown, but nothing was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Account with the same IBAN and currency already exists", e.getMessage());
        }
        verify(repository, times(1)).findAll();
        verify(repository, never()).save(any());
        verify(mapper, never()).mapFromDao(any());
        verify(mapper, never()).mapToDao(any());
    }


    @Test
    public void testDeleteAccountById_Successful() {
        service.deleteAccountById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAccountById_NonExistentCustomer() {
        doNothing().when(repository).deleteById(anyLong());
        service.deleteAccountById(99L);
        verify(repository, times(1)).deleteById(99L);
    }

    private List<AccountDAO> createAccountDAOList(AccountDAO accountDAO) {
        List<AccountDAO> list = new ArrayList<>();
        list.add(accountDAO);
        list.add(accountDAO);
        list.add(accountDAO);
        return list;
    }

    private AccountDAO createOldAccountDAOEntry() {
        return new AccountDAO(
                1L,
                1L,
                "IBAN",
                new BigDecimal(1000.00),
                "USD",
                null
        );
    }

    private AccountDAO createAccountDAO() {
        return new AccountDAO(
                1L,
                1L,
                "IBAN",
                new BigDecimal(1000.00),
                "USD",
                null
        );
    }

    private Account createAccount() {
        return new Account(
                1L,
                1L,
                "IBAN",
                new BigDecimal(1000.00),
                "USD",
                null
        );
    }
}