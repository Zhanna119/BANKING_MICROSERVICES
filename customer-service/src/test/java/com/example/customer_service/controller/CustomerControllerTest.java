package com.example.customer_service.controller;

import com.example.customer_service.business.mappers.CustomerMapper;
import com.example.customer_service.business.service.CustomerService;
import com.example.customer_service.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {
    @MockBean
    private CustomerService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    public static final String URL = "/api/customers";
    public static final String URL2 = URL + "/all";
    public static final String URL3 = URL + "/getById";
    public static final String URL4 = URL + "/edit";
    public static final String URL5 = URL + "/save";
    public static final String URL6 = URL + "/delete";

    private Customer customer;

    private List<Customer> mockedData;

    @BeforeEach
    public void init() {
        customer = createCustomer();
        mockedData = createMockedListCustomer();
    }

    @Test
    void testGetAllCustomers_Successful() throws Exception {
        when(service.getAllCustomers()).thenReturn(mockedData);
        mockMvc.perform(get(URL2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(mockedData.size())))
                .andExpect(jsonPath("$[0].id", is(mockedData.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(mockedData.get(0).getName())))
                .andExpect(jsonPath("$[0].surname", is(mockedData.get(0).getSurname())))
                .andExpect(jsonPath("$[0].identityNumber", is(mockedData.get(0).getIdentityNumber())))
                .andExpect(jsonPath("$[0].customerAccountIds", is(mockedData.get(0).getCustomerAccountIds())))
                .andExpect(jsonPath("$[0].customerLoansIds", is(mockedData.get(0).getCustomerLoansIds())))
                .andExpect(jsonPath("$[0].customerCreditCardIds", is(mockedData.get(0).getCustomerCreditCardIds())))
                .andExpect(jsonPath("$[1].id", is(mockedData.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(mockedData.get(1).getName())))
                .andExpect(jsonPath("$[1].surname", is(mockedData.get(1).getSurname())))
                .andExpect(jsonPath("$[1].identityNumber", is(mockedData.get(1).getIdentityNumber())))
                .andExpect(jsonPath("$[1].customerAccountIds", is(mockedData.get(1).getCustomerAccountIds())))
                .andExpect(jsonPath("$[1].customerLoansIds", is(mockedData.get(1).getCustomerLoansIds())))
                .andExpect(jsonPath("$[1].customerCreditCardIds", is(mockedData.get(1).getCustomerCreditCardIds())));
        verify(service, times(1)).getAllCustomers();
    }

    @Test
    public void testGetAllCustomers_EmptyList() throws Exception {
        List<Customer> emptyList = new ArrayList<>();
        when(service.getAllCustomers()).thenReturn(emptyList);
        mockMvc.perform(get(URL2))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCustomerById_Successful() throws Exception {
        when(service.getCustomerById(1L)).thenReturn(Optional.of(customer));
        mockMvc.perform(get(URL3 + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.surname", is("Surname")))
                .andExpect(jsonPath("$.identityNumber", is("IdentityNumber")));
        verify(service, times(1)).getCustomerById(1L);
    }

    @Test
    void testGetCustomerById_NotExistingId() throws Exception {
        when(service.getCustomerById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get(URL3 + "/99"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getCustomerById(99L);
    }

    @Test
    void testSaveCustomer_Successful() throws Exception {
        when(service.saveCustomer(customer)).thenReturn(customer);
        mockMvc.perform(post(URL5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.surname", is("Surname")))
                .andExpect(jsonPath("$.identityNumber", is("IdentityNumber")))
                .andExpect(jsonPath("$.customerAccountIds").value(nullValue()))
                .andExpect(jsonPath("$.customerLoansIds").value(nullValue()))
                .andExpect(jsonPath("$.customerCreditCardIds").value(nullValue()));
        verify(service, times(1)).saveCustomer(customer);
    }

    @Test
    void testSaveCustomer_CustomerNotExists() throws Exception {
        when(service.getCustomerById(customer.getId())).thenReturn(Optional.of(customer));
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testUpdateCustomer_Successful() throws Exception {
        when(service.editCustomer(1L, customer)).thenReturn(Optional.ofNullable(customer));
        mockMvc.perform(put(URL4 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.surname", is("Surname")))
                .andExpect(jsonPath("$.identityNumber", is("IdentityNumber")))
                .andExpect(jsonPath("$.customerAccountIds").value(nullValue()))
                .andExpect(jsonPath("$.customerLoansIds").value(nullValue()))
                .andExpect(jsonPath("$.customerCreditCardIds").value(nullValue()));
        verify(service, times(1)).editCustomer(1L, customer);
    }

    @Test
    void testUpdateCustomer_Failed() throws Exception {
        when(service.editCustomer(2L, customer)).thenReturn(Optional.ofNullable(customer));
        mockMvc.perform(put(URL4 + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).editCustomer(anyLong(), any());
    }

    @Test
    void testUpdateCustomer_NotFound() throws Exception {
        when(service.editCustomer(1L, customer)).thenReturn(Optional.empty());
        mockMvc.perform(put(URL4 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNotFound());
        verify(service, times(1)).editCustomer(1L, customer);
    }

    @Test
    void testDeleteCustomerById_Successful() throws Exception {
        when(service.getCustomerById(1L)).thenReturn(Optional.of(customer));
        mockMvc.perform(delete(URL6 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk());
        verify(service, times(1)).deleteCustomerById(1L);
    }


    @Test
    void testDeleteCustomerById_ReturnBadRequest() throws Exception {
        when(service.getCustomerById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete(URL6 + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isBadRequest());
    }


    private Customer createCustomer() {
        return new Customer(
                1L,
                "Name",
                "Surname",
                "IdentityNumber",
                null,
                null,
                null);
    }

    private List<Customer> createMockedListCustomer() {
        List<Customer> list = new ArrayList<>();
        list.add(new Customer(
                1L,
                "Name",
                "Surname",
                "IdentityNumber",
                null,
                null,
                null));
        list.add(new Customer(
                2L,
                "Name2",
                "Surname2",
                "IdentityNumber2",
                null,
                null,
                null));
        return list;
    }
}