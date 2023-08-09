package com.example.account_service.controller;

import com.example.account_service.business.service.AccountActivityService;
import com.example.account_service.model.AccountActivity;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AccountActivityControllerTest {
    @MockBean
    private AccountActivityService service;
    @Autowired
    private MockMvc mockMvc;

    public static final String URL = "/api/accountActivity";
    public static final String URL2 = URL + "/all";
    public static final String URL3 = URL + "/date";

    private AccountActivity accountActivity;

    private List<AccountActivity> mockedData;

    @BeforeEach
    public void init() {
        accountActivity = createAccountActivity();
        mockedData = createMockedListAccountActivity();
    }

    @Test
    void testGetAllTransactions_Successful() throws Exception {
        when(service.getAllTransactions()).thenReturn(mockedData);
        mockMvc.perform(get(URL2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(mockedData.size())))
                .andExpect(jsonPath("$[0].id").value(is(mockedData.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].transactionAmount").value(is(mockedData.get(0).getTransactionAmount().intValue())))
                .andExpect(jsonPath("$[0].transactionDate").value(is(mockedData.get(0).getTransactionDate().toString())))
                .andExpect(jsonPath("$[0].description").value(is(mockedData.get(0).getDescription())))
                .andExpect(jsonPath("$[1].id").value(is(mockedData.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].transactionAmount").value(is(mockedData.get(1).getTransactionAmount().intValue())))
                .andExpect(jsonPath("$[1].transactionDate").value(is(mockedData.get(1).getTransactionDate().toString())))
                .andExpect(jsonPath("$[1].description").value(is(mockedData.get(1).getDescription())));
        verify(service, times(1)).getAllTransactions();
    }

    @Test
    void testGetAllTransactions_EmptyList() throws Exception {
        List<AccountActivity> emptyList = new ArrayList<>();
        when(service.getAllTransactions()).thenReturn(emptyList);
        mockMvc.perform(get(URL2))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getAllTransactions();
    }


    @Test
    public void testGetTransactionsByDate_SingleDate_Returns200() throws Exception {
        LocalDate date = LocalDate.parse("2022-06-30");
        List<AccountActivity> transactions = Arrays.asList(new AccountActivity(), new AccountActivity());
        when(service.getTransactionsByDate(any(LocalDate.class))).thenReturn(transactions);
        mockMvc.perform(get(URL3).param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(service, times(1)).getTransactionsByDate(argThat(argument ->
                argument.isEqual(date)));
    }

    @Test
    public void testGetTransactionByDate_DateIsNull_Returns400() {
        AccountActivityController controller = new AccountActivityController();
        controller.service = mock(AccountActivityService.class);
        ResponseEntity<List<AccountActivity>> response = controller.getTransactionsByDate(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testGetTransactionsByDate_FutureDate_Returns400() throws Exception {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        mockMvc.perform(get(URL3).param("date", futureDate.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetTransactionsByDate_NoTransactions_Returns404() throws Exception {
        LocalDate dateWithoutTransactions = LocalDate.parse("2022-07-31");
        when(service.getTransactionsByDate(any(LocalDate.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get(URL3).param("date", dateWithoutTransactions.toString()))
                .andExpect(status().isNotFound());
    }


    private AccountActivity createAccountActivity() {
        return new AccountActivity(
                1L,
                null,
                new BigDecimal("1000"),
                LocalDate.of(2020, 01, 01),
                "Description"
        );
    }

    private List<AccountActivity> createMockedListAccountActivity() {
        List<AccountActivity> list = new ArrayList<>();
        list.add(new AccountActivity(
                1L,
                null,
                new BigDecimal("1000"),
                LocalDate.of(2020, 01, 01),
                "Description"));
        list.add((new AccountActivity(
                2L,
                null,
                new BigDecimal("2000"),
                LocalDate.of(2022, 01, 01),
                "Description2")));
        return list;
    }
}

