package com.example.loan_service.controller;

import com.example.loan_service.business.service.LoanService;
import com.example.loan_service.model.Loan;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerTest {
    @MockBean
    private LoanService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    public static final String URL = "/api/loans";
    public static final String URL2 = URL + "/all";
    public static final String URL3 = URL + "/getById";
    public static final String URL4 = URL + "/edit";
    public static final String URL5 = URL + "/save";
    public static final String URL6 = URL + "/delete";

    private Loan loan = new Loan(1L,
            1L,
            new BigDecimal(10000.00),
            LocalDate.of(2020, 01, 01),
            new BigDecimal(1000.00),
            null);

    private List<Loan> loanList() {
        List<Loan> list = new ArrayList<>();
        list.add( new Loan(1L,
                1L,
                new BigDecimal(10000.00),
                LocalDate.of(2020, 01, 01),
                new BigDecimal(1000.00),
                null));
        list.add( new Loan(2L,
                1L,
                new BigDecimal(20000.00),
                LocalDate.of(2020, 01, 01),
                new BigDecimal(1000.00),
                null));
return list;
    }
    @Test
    void testGetAllLoans_Successful() throws Exception {
        when(service.getAllLoans()).thenReturn(loanList());
        mockMvc.perform(get(URL2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(loanList().size())))
                .andExpect(jsonPath("$[0].id").value(is(loanList().get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].customerId").value(is(loanList().get(0).getCustomerId().intValue())))
                .andExpect(jsonPath("$[0].loanAmount").value(is(loanList().get(0).getLoanAmount().intValue())))
                .andExpect(jsonPath("$[0].dueDate").value(is(loanList().get(0).getDueDate().toString())))
                .andExpect(jsonPath("$[0].loanDebt").value(is(loanList().get(0).getLoanDebt().intValue())))
                .andExpect(jsonPath("$[0].customerLoanPaymentIds").value(is(loanList().get(0).getCustomerLoanPaymentIds())))
                .andExpect(jsonPath("$[1].id").value(is(loanList().get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].customerId").value(is(loanList().get(1).getCustomerId().intValue())))
                .andExpect(jsonPath("$[1].loanAmount").value(is(loanList().get(1).getLoanAmount().intValue())))
                .andExpect(jsonPath("$[1].dueDate").value(is(loanList().get(1).getDueDate().toString())))
                .andExpect(jsonPath("$[1].loanDebt").value(is(loanList().get(1).getLoanDebt().intValue())))
                .andExpect(jsonPath("$[1].customerLoanPaymentIds").value(is(loanList().get(1).getCustomerLoanPaymentIds())));
        verify(service, times(1)).getAllLoans();
    }

    @Test
    void testGetAllLoans_EmptyList() throws Exception {
        List<Loan> emptyList = new ArrayList<>();
        when(service.getAllLoans()).thenReturn(emptyList);
        mockMvc.perform(get(URL2))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getAllLoans();
    }

    @Test
    void testGetAllLoans_EmptyList_NotFound() throws Exception {
        when(service.getAllLoans()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(URL2))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
        verify(service, times(1)).getAllLoans();

    }

    @Test
    void testGetLoanById_Successful() throws Exception {
        when(service.getLoanById(1L)).thenReturn(Optional.of(loan));
        mockMvc.perform(get(URL3 + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(loanList().get(0).getId().intValue())))
                .andExpect(jsonPath("$.customerId").value(is(loanList().get(0).getCustomerId().intValue())))
                .andExpect(jsonPath("$.loanAmount").value(is(loanList().get(0).getLoanAmount().intValue())))
                .andExpect(jsonPath("$.dueDate").value(is(loanList().get(0).getDueDate().toString())))
                .andExpect(jsonPath("$.loanDebt").value(is(loanList().get(0).getLoanDebt().intValue())))
                .andExpect(jsonPath("$.customerLoanPaymentIds").value(is(loanList().get(0).getCustomerLoanPaymentIds())));
        verify(service, times(1)).getLoanById(1L);
    }

    @Test
    void testGetLoanById_NotExistingId() throws Exception {
        when(service.getLoanById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get(URL3 + "/99"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getLoanById(99L);
    }

    @Test
    void testUpdateLoan_Successful() throws Exception {
        when(service.editLoan(1L, loan)).thenReturn(Optional.ofNullable(loan));
        mockMvc.perform(put(URL4 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(loan.getId().intValue())))
                .andExpect(jsonPath("$.customerId").value(is(loan.getCustomerId().intValue())))
                .andExpect(jsonPath("$.loanAmount").value(is(loan.getLoanAmount().intValue())))
                .andExpect(jsonPath("$.dueDate").value(is(loan.getDueDate().toString())))
                .andExpect(jsonPath("$.loanDebt").value(is(loan.getLoanDebt().intValue())))
                .andExpect(jsonPath("$.customerLoanPaymentIds").value(is(loan.getCustomerLoanPaymentIds())));
        verify(service, times(1)).editLoan(1L, loan);
    }

    @Test
    void testUpdateLoan_MismatchedId() throws Exception {
        when(service.editLoan(1L, loan)).thenReturn(Optional.ofNullable(loan));
        mockMvc.perform(put(URL4 + "/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateLoan_NotFound() throws Exception {
        when(service.editLoan(1L, loan)).thenReturn(Optional.empty());
        mockMvc.perform(put(URL4 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isNotFound());
        verify(service, times(1)).editLoan(1L, loan);
    }

    @Test
    void testSaveLoan_Successful() throws Exception {
        when(service.saveLoan(loan)).thenReturn(loan);
        mockMvc.perform(post(URL5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(loan.getId().intValue())))
                .andExpect(jsonPath("$.customerId").value(is(loan.getCustomerId().intValue())))
                .andExpect(jsonPath("$.loanAmount").value(is(loan.getLoanAmount().intValue())))
                .andExpect(jsonPath("$.dueDate").value(is(loan.getDueDate().toString())))
                .andExpect(jsonPath("$.loanDebt").value(is(loan.getLoanDebt().intValue())))
                .andExpect(jsonPath("$.customerLoanPaymentIds").value(is(loan.getCustomerLoanPaymentIds())));
        verify(service, times(1)).saveLoan(loan);
    }

    @Test
    void testSaveLoan_ExistingId() throws Exception {
        Loan existingLoan = new Loan();
        existingLoan.setId(1L);
        when(service.getLoanById(loan.getId())).thenReturn(Optional.of(existingLoan));
        mockMvc.perform(post(URL5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isUnprocessableEntity());
        verify(service, times(1)).saveLoan(loan);
    }

    @Test
    void testDeleteLoanById_Successful() throws Exception{
        when(service.getLoanById(1L)).thenReturn(Optional.of(loan));
        mockMvc.perform(delete(URL6 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteLoanById(1L);
    }

    @Test
    void testDeleteLoanById_ReturnBadRequest() throws Exception{
        when(service.getLoanById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete(URL6 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllLoansByCustomerId_Found() throws Exception {
        Long customerId = 1L;
        Loan loan1 = new Loan();
        Loan loan2 = new Loan();
        List<Loan> loanList = Arrays.asList(loan1, loan2);
        when(service.getAllLoansByCustomerId(customerId)).thenReturn(loanList);
        mockMvc.perform(get(URL + "/id/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loanList)));
    }
   @Test
    public void testGetAllLoansByCustomerId_NotFound() throws Exception {
        Long customerId = 1L;
        when(service.getAllLoansByCustomerId(customerId)).thenReturn(Collections.emptyList());
        mockMvc.perform(get(URL + "/id/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}