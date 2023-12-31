package com.example.account_service.controller;

import com.example.account_service.business.service.AccountService;
import com.example.account_service.business.service.impl.AccountServiceImpl;
import com.example.account_service.model.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @MockBean
    private AccountServiceImpl service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String URL = "/api/accounts";
    public static final String URL2 = URL + "/all";
    public static final String URL3 = URL + "/getById";
    public static final String URL4 = URL + "/edit";
    public static final String URL5 = URL + "/save";
    public static final String URL6 = URL + "/delete";

    private Account account;

    private List<Account> mockedData;

    @BeforeEach
    public void init() {
        account = createAccount();
        mockedData = createMockedListAccount();
    }

    @Test
    void testGetAllAccounts_Successful() throws Exception {
        when(service.getAllAccounts()).thenReturn(mockedData);
        mockMvc.perform(get(URL2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(mockedData.size())))
                .andExpect(jsonPath("$[0].id").value(is(mockedData.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].customerId").value(is(mockedData.get(0).getCustomerId().intValue())))
                .andExpect(jsonPath("$[0].iban").value(is(mockedData.get(0).getIban())))
                .andExpect(jsonPath("$[0].currentBalance").value(is(mockedData.get(0).getCurrentBalance().intValue())))
                .andExpect(jsonPath("$[0].currency").value(is(mockedData.get(0).getCurrency())))
                .andExpect(jsonPath("$[1].id").value(is(mockedData.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].customerId").value(is(mockedData.get(0).getCustomerId().intValue())))
                .andExpect(jsonPath("$[1].iban").value(is(mockedData.get(1).getIban())))
                .andExpect(jsonPath("$[1].currentBalance").value(is(mockedData.get(1).getCurrentBalance().intValue())))
                .andExpect(jsonPath("$[1].currency").value(is(mockedData.get(1).getCurrency())));
        verify(service, times(1)).getAllAccounts();
    }

    @Test
    public void testGetAllCustomers_EmptyList() throws Exception {
        List<Account> emptyList = new ArrayList<>();
        when(service.getAllAccounts()).thenReturn(emptyList);
        mockMvc.perform(get(URL2))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllAccounts4_EmptyList() throws Exception {
        when(service.getAllAccounts()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
        verify(service, times(1)).getAllAccounts();
    }



    @Test
    void testGetAccountById_Successful() throws Exception {
        when(service.getAccountById(1L)).thenReturn(Optional.of(account));
        mockMvc.perform(get(URL3 + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.iban", is("IBAN")))
                .andExpect(jsonPath("$.currentBalance", is(1000.00)))
                .andExpect(jsonPath("$.currency", is("Currency")));
        verify(service, times(1)).getAccountById(1L);
    }

    @Test
    void testGetAccountById_NotExistingId() throws Exception {
        when(service.getAccountById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get(URL3 + "/99"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getAccountById(99L);
    }

    @Test
    void testSaveAccount_Successful() throws Exception {
        when(service.saveAccount(account)).thenReturn(account);
        mockMvc.perform(post(URL5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.iban", is("IBAN")))
                .andExpect(jsonPath("$.currentBalance", is(1000.00)))
                .andExpect(jsonPath("$.currency", is("Currency")));
        verify(service, times(1)).saveAccount(account);
    }

    /*@Test
    void testSaveAccount_AccountAlreadyExists() throws Exception {
        // Create a mock account and ID
        Account account = new Account();
        account.setId(100L);

        // Mock the behavior of the service to return an Optional containing the account
        when(service.getAccountById(account.getId())).thenReturn(Optional.of(account));

        // Perform the POST request to save the account
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isUnprocessableEntity()); // Expect a 422 Unprocessable Entity status
    }*/


    @Test
    void testUpdateAccount_Successful() throws Exception {
        when(service.editAccount(1L, account)).thenReturn(Optional.ofNullable(account));
        mockMvc.perform(put(URL4 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.iban", is("IBAN")))
                .andExpect(jsonPath("$.currentBalance", is(1000.00)))
                .andExpect(jsonPath("$.currency", is("Currency")));
        verify(service, times(1)).editAccount(1L, account);
    }

    @Test
    void testUpdateAccount_Failed() throws Exception {
        when(service.editAccount(2L, account)).thenReturn(Optional.ofNullable(account));
        mockMvc.perform(put(URL4 + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).editAccount(anyLong(), any());
    }

    @Test
    void testUpdateAccount_NotFound() throws Exception {
        when(service.editAccount(1L, account)).thenReturn(Optional.empty());
        mockMvc.perform(put(URL4 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isNotFound());
        verify(service, times(1)).editAccount(1L, account);
    }


    @Test
    void testDeleteAccountById_Successful() throws Exception{
        when(service.getAccountById(1L)).thenReturn(Optional.of(account));
        mockMvc.perform(delete(URL6 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteAccountById(1L);
    }

    @Test
    void testDeleteAccountById_ReturnBadRequest() throws Exception{
        when(service.getAccountById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete(URL6 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllAccountByCustomerId_Found() throws Exception {
        Long customerId = 1L;
        Account account1 = new Account();
        Account account2 = new Account();
        List<Account> accounts = Arrays.asList(account1, account2);
        when(service.getAccountsByCustomerId(customerId)).thenReturn(createMockedListAccount());
        mockMvc.perform(get(URL + "/id/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createMockedListAccount())));
    }
    @Test
    public void testGetAllAccountByCustomerId_NotFound() throws Exception {
        Long customerId = 1L;
        when(service.getAccountsByCustomerId(customerId)).thenReturn(Collections.emptyList());
        mockMvc.perform(get(URL + "/id/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    private Account createAccount() {
        return new Account(
                1L,
                1L,
                "IBAN",
                new BigDecimal("1000.00"),
                "Currency",
                null
        );
    }

    private List<Account> createMockedListAccount() {
        List<Account> list = new ArrayList<>();
        list.add(new Account(
                1L,
                1L,
                "IBAN",
                new BigDecimal(1000.00),
                "Currency",
                null));
        list.add((new Account(
                2L,
                1L,
                "IBAN2",
                new BigDecimal(2000.00),
                "Currency2",
                null)));
        return list;
    }
}



