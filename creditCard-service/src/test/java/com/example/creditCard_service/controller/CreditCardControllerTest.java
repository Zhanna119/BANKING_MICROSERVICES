package com.example.creditCard_service.controller;

import com.example.creditCard_service.business.service.CreditCardService;
import com.example.creditCard_service.model.CreditCard;
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
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CreditCardControllerTest {
    @MockBean
    private CreditCardService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    public static final String URL = "/api/creditCards";
    public static final String URL2 = URL + "/all";
    public static final String URL3 = URL + "/getById";
    public static final String URL4 = URL + "/edit";
    public static final String URL5 = URL + "/save";
    public static final String URL6 = URL + "/delete";
    public static final String URL7 = URL + "/customerId";

    private CreditCard creditCard = new CreditCard(
            1L,
            1L,
            "123",
            LocalDate.of(2020, 01, 01),
            new BigDecimal(10000.00),
            new BigDecimal(10000.00),
            new BigDecimal(10000.00),
            new BigDecimal(10000.00),
            null);

    private List<CreditCard> creditCardsList() {
        List<CreditCard> list = new ArrayList<>();
        list.add( new CreditCard(1L,
                1L,
                "123",
                LocalDate.of(2020, 01, 01),
                new BigDecimal(10000.00),
                new BigDecimal(10000.00),
                new BigDecimal(10000.00),
                new BigDecimal(10000.00),
                null));
        list.add( new CreditCard(2L,
                1L,
                "123",
                LocalDate.of(2020, 01, 01),
                new BigDecimal(20000.00),
                new BigDecimal(10000.00),
                new BigDecimal(10000.00),
                new BigDecimal(2000.00),
                null));
        return list;
    }
    @Test
    void testGetAllCreditCards_Successful() throws Exception {
        when(service.getAllCreditCards()).thenReturn(creditCardsList());
        mockMvc.perform(get(URL2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(creditCardsList().size())))
                .andExpect(jsonPath("$[0].id").value(is(creditCardsList().get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].customerId").value(is(creditCardsList().get(0).getCustomerId().intValue())))
                .andExpect(jsonPath("$[0].cardLimit").value(is(creditCardsList().get(0).getCardLimit().intValue())))
                .andExpect(jsonPath("$[0].availableLimit").value(is(creditCardsList().get(0).getAvailableLimit().intValue())))
                .andExpect(jsonPath("$[0].loanDebt").value(is(creditCardsList().get(0).getLoanDebt().intValue())))
                .andExpect(jsonPath("$[0].minPaymentAmount").value(is(creditCardsList().get(0).getMinPaymentAmount().intValue())))
                .andExpect(jsonPath("$[0].customerCreditCardPaymentIds").value(is(creditCardsList().get(0).getCustomerCreditCardPaymentIds())))
                .andExpect(jsonPath("$[1].id").value(is(creditCardsList().get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].customerId").value(is(creditCardsList().get(1).getCustomerId().intValue())))
                .andExpect(jsonPath("$[1].cardLimit").value(is(creditCardsList().get(1).getCardLimit().intValue())))
                .andExpect(jsonPath("$[1].availableLimit").value(is(creditCardsList().get(1).getAvailableLimit().intValue())))
                .andExpect(jsonPath("$[1].loanDebt").value(is(creditCardsList().get(1).getLoanDebt().intValue())))
                .andExpect(jsonPath("$[1].minPaymentAmount").value(is(creditCardsList().get(1).getMinPaymentAmount().intValue())))
                .andExpect(jsonPath("$[1].customerCreditCardPaymentIds").value(is(creditCardsList().get(1).getCustomerCreditCardPaymentIds())));
        verify(service, times(1)).getAllCreditCards();
    }

    @Test
    void testGetAllCreditCards_EmptyList() throws Exception {
        List<CreditCard> emptyList = new ArrayList<>();
        when(service.getAllCreditCards()).thenReturn(emptyList);
        mockMvc.perform(get(URL2))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getAllCreditCards();
    }

    @Test
    void testGetAllCreditCards_EmptyList_NotFound() throws Exception {
        when(service.getAllCreditCards()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(URL2))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
        verify(service, times(1)).getAllCreditCards();
    }

    @Test
    void testGetCreditCardById_Successful() throws Exception {
        when(service.getCreditCardById(1L)).thenReturn(Optional.of(creditCard));
        mockMvc.perform(get(URL3 + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(creditCardsList().get(0).getId().intValue())))
                .andExpect(jsonPath("$.customerId").value(is(creditCardsList().get(0).getCustomerId().intValue())))
                .andExpect(jsonPath("$.cardLimit").value(is(creditCardsList().get(0).getCardLimit().intValue())))
                .andExpect(jsonPath("$.availableLimit").value(is(creditCardsList().get(0).getAvailableLimit().intValue())))
                .andExpect(jsonPath("$.loanDebt").value(is(creditCardsList().get(0).getLoanDebt().intValue())))
                .andExpect(jsonPath("$.minPaymentAmount").value(is(creditCardsList().get(0).getMinPaymentAmount().intValue())))
                .andExpect(jsonPath("$.customerCreditCardPaymentIds").value(is(creditCardsList().get(0).getCustomerCreditCardPaymentIds())));
        verify(service, times(1)).getCreditCardById(1L);
    }

    @Test
    void testGetCreditCardById_NotExistingId() throws Exception {
        when(service.getCreditCardById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get(URL3 + "/99"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getCreditCardById(99L);
    }

    @Test
    void testUpdateCreditCard_Successful() throws Exception {
        when(service.editCreditCard(1L, creditCard)).thenReturn(Optional.ofNullable(creditCard));
        mockMvc.perform(put(URL4 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(creditCard.getId().intValue())))
                .andExpect(jsonPath("$.customerId").value(is(creditCard.getCustomerId().intValue())))
                .andExpect(jsonPath("$.cardLimit").value(is(creditCard.getCardLimit().intValue())))
                .andExpect(jsonPath("$.availableLimit").value(is(creditCard.getAvailableLimit().intValue())))
                .andExpect(jsonPath("$.loanDebt").value(is(creditCard.getLoanDebt().intValue())))
                .andExpect(jsonPath("$.minPaymentAmount").value(is(creditCard.getMinPaymentAmount().intValue())))
                .andExpect(jsonPath("$.customerCreditCardPaymentIds").value(is(creditCard.getCustomerCreditCardPaymentIds())));
        verify(service, times(1)).editCreditCard(1L, creditCard);
    }


    @Test
    void testUpdateCreditCard_MismatchedId() throws Exception {
        when(service.editCreditCard(1L, creditCard)).thenReturn(Optional.ofNullable(creditCard));
        mockMvc.perform(put(URL4 + "/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testUpdateCreditCard_NotFound() throws Exception {
        when(service.editCreditCard(1L, creditCard)).thenReturn(Optional.empty());
        mockMvc.perform(put(URL4 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isNotFound());
        verify(service, times(1)).editCreditCard(1L, creditCard);
    }

    @Test
    void testSaveCreditCard_Successful() throws Exception {
        when(service.saveCreditCard(creditCard)).thenReturn(creditCard  );
        mockMvc.perform(post(URL5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(is(creditCard.getId().intValue())))
                .andExpect(jsonPath("$.customerId").value(is(creditCard.getCustomerId().intValue())))
                .andExpect(jsonPath("$.cardLimit").value(is(creditCard.getCardLimit().intValue())))
                .andExpect(jsonPath("$.availableLimit").value(is(creditCard.getAvailableLimit().intValue())))
                .andExpect(jsonPath("$.loanDebt").value(is(creditCard.getLoanDebt().intValue())))
                .andExpect(jsonPath("$.minPaymentAmount").value(is(creditCard.getMinPaymentAmount().intValue())))
                .andExpect(jsonPath("$.customerCreditCardPaymentIds").value(is(creditCard.getCustomerCreditCardPaymentIds())));
        verify(service, times(1)).saveCreditCard(creditCard);
    }


    @Test
    void testSaveCreditCard_ExistingId() throws Exception {
        CreditCard existingLoan = new CreditCard();
        existingLoan.setId(1L);
        when(service.getCreditCardById(creditCard.getId())).thenReturn(Optional.of(existingLoan));
        mockMvc.perform(post(URL5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isUnprocessableEntity());
        verify(service, times(1)).saveCreditCard(creditCard);
    }

    @Test
    void testDeleteCreditCardById_Successful() throws Exception{
        when(service.getCreditCardById(1L)).thenReturn(Optional.of(creditCard));
        mockMvc.perform(delete(URL6 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteCreditCardById(1L);
    }

    @Test
    void testDeleteCreditCardById_ReturnBadRequest() throws Exception{
        when(service.getCreditCardById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete(URL6 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creditCard)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllCreditCardsByCustomerId_Found() throws Exception {
        Long customerId = 1L;
        CreditCard creditCard1 = new CreditCard();
        CreditCard creditCard2 = new CreditCard();
        List<CreditCard> accounts = Arrays.asList(creditCard1, creditCard2);
        when(service.getAllCreditCardsByCustomerId(customerId)).thenReturn(creditCardsList());
        mockMvc.perform(get(URL7 + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(creditCardsList())));
    }
    @Test
    public void testGetAllCreditCardsByCustomerId_NotFound() throws Exception {
        Long customerId = 1L;
        when(service.getAllCreditCardsByCustomerId(customerId)).thenReturn(Collections.emptyList());
        mockMvc.perform(get(URL7 + "/1" )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}