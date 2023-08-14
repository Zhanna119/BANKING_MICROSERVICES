package com.example.loan_service.controller;

import com.example.loan_service.business.service.LoanPaymentService;
import com.example.loan_service.model.Loan;
import com.example.loan_service.model.LoanPayment;
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
class LoanPaymentControllerTest {
    @MockBean
    private LoanPaymentService service;
    @Autowired
    private MockMvc mockMvc;

    public static final String URL = "/api/loanPayments";
    public static final String URL2 = URL + "/all";
    public static final String URL3 = URL + "/date";

    private LoanPayment loanPayment = new LoanPayment(
            1L,
            2L,
            LocalDate.of(2020, 01, 01),
            new BigDecimal(10000.00));

    private List<LoanPayment> loanPaymentList() {
        List<LoanPayment> loanPayments = new ArrayList<>();
        loanPayments.add( new LoanPayment(
                1L,
                2L,
                LocalDate.of(2020, 01, 01),
                new BigDecimal(10000.00)));
        loanPayments.add( new LoanPayment(
                1L,
                99L,
                LocalDate.of(2020, 01, 01),
                new BigDecimal(10000.00)));
        return loanPayments;
    }

    @Test
    void testGetAllLoanPayments_Successful() throws Exception {
        when(service.getAllLoanPayments()).thenReturn(loanPaymentList());
        mockMvc.perform(get(URL2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(loanPaymentList().size())))
                .andExpect(jsonPath("$[0].id").value(is(loanPaymentList().get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].loanId").value(is(loanPaymentList().get(0).getLoanId().intValue())))
                .andExpect(jsonPath("$[0].loanPaymentDate").value(is(loanPaymentList().get(0).getLoanPaymentDate().toString())))
                .andExpect(jsonPath("$[0].paymentAmount").value(is(loanPaymentList().get(0).getPaymentAmount().intValue())))
                .andExpect(jsonPath("$[1].id").value(is(loanPaymentList().get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].loanId").value(is(loanPaymentList().get(1).getLoanId().intValue())))
                .andExpect(jsonPath("$[1].loanPaymentDate").value(is(loanPaymentList().get(1).getLoanPaymentDate().toString())))
                .andExpect(jsonPath("$[1].paymentAmount").value(is(loanPaymentList().get(1).getPaymentAmount().intValue())));
        verify(service, times(1)).getAllLoanPayments();
    }

    @Test
    void testGetAllLoanPayments_EmptyList() throws Exception {
        List<LoanPayment> emptyList = new ArrayList<>();
        when(service.getAllLoanPayments()).thenReturn(emptyList);
        mockMvc.perform(get(URL2))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getAllLoanPayments();
    }

    @Test
    void testGetAllLoanPayments_EmptyList_NotFound() throws Exception {
        when(service.getAllLoanPayments()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(URL2))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
        verify(service, times(1)).getAllLoanPayments();
    }

    @Test
    public void testGetLoanPaymentsByDate_Successful() throws Exception {
        LocalDate date = LocalDate.parse("2022-06-30");
        List<LoanPayment> payments = Arrays.asList(new LoanPayment(), new LoanPayment());
        when(service.getLoansByDate(date)).thenReturn(payments);
        mockMvc.perform(get(URL3).param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(service, times(1)).getLoansByDate(date);
    }

    @Test
    public void testGetLoanPaymentsByDate_DateIsNull_Returns400() {
        LoanPaymentController controller = new LoanPaymentController();
        controller.service = mock(LoanPaymentService.class);
        ResponseEntity<List<LoanPayment>> response = controller.getLoanPaymentsByDate(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void testGetLoanPaymentsByDate_FutureDate_Returns400() throws Exception {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        mockMvc.perform(get(URL3).param("date", futureDate.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testGetLoanPaymentsByDate_NoPayments_Returns404() throws Exception {
        LocalDate dateWithoutPayments = LocalDate.parse("2022-07-31");
        when(service.getLoansByDate(any(LocalDate.class))).thenReturn(Collections.emptyList());
        mockMvc.perform(get(URL3).param("date", dateWithoutPayments.toString()))
                .andExpect(status().isNotFound());
    }

}