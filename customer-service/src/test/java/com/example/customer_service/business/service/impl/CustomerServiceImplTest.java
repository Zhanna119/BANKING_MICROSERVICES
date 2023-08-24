package com.example.customer_service.business.service.impl;

import com.example.customer_service.business.mappers.CustomerMapper;
import com.example.customer_service.business.repository.CustomerRepository;
import com.example.customer_service.business.repository.model.CustomerDAO;
import com.example.customer_service.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerMapper mapper;

    @Mock(answer = Answers.RETURNS_SELF)
    private WebClient.Builder webClientBuilder;


    @Mock
    private WebClient webClient;

    @InjectMocks
    private CustomerServiceImpl service;

    @BeforeEach
    public void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        customerDAOList = new ArrayList<>(Arrays.asList(new CustomerDAO(), new CustomerDAO(), new CustomerDAO()));
        customerDAO = new CustomerDAO();
        customer = new Customer();
        service = new CustomerServiceImpl(repository, mapper, webClientBuilder);
    }

    private CustomerDAO customerDAO;
    private Customer customer;
    private CustomerDAO oldCustomerDAOEntry;
    private List<CustomerDAO> customerDAOList;

    @Test
    public void testGetAllCustomers_Successful() {
        when(repository.findAll()).thenReturn(customerDAOList);
        when(mapper.mapFromDao(customerDAO)).thenReturn(customer);
        List<Customer> list = service.getAllCustomers();
        assertEquals(3, list.size());
        verify(repository, times(1)).findAll();
    }


    @Test
    public void testGetAllCustomers_Empty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<Customer> result = service.getAllCustomers();
        verify(repository, times(1)).findAll();
        assertTrue(result.isEmpty());
    }



    @Test
    public void testGetCustomersById_Successful() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(customerDAO));
        when(mapper.mapFromDao(customerDAO)).thenReturn(customer);
        Optional<Customer> actualResult = service.getCustomerById(customer.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(customer, actualResult.get());
        verify(repository, times(1)).findById(1L);
    }


    @Test
    public void testGetCustomerById_NotExistingId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        Optional<Customer> result = service.getCustomerById(99L);
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(anyLong());
    }


    @Test
    public void testEditCustomer_Successful() {
        when(repository.existsById(1L)).thenReturn(true);
        when(mapper.mapToDao(customer)).thenReturn(customerDAO);
        when(repository.save(customerDAO)).thenReturn(oldCustomerDAOEntry);
        when(mapper.mapFromDao(oldCustomerDAOEntry)).thenReturn(customer);
        Optional<Customer> result = service.editCustomer(1L, customer);
        assertEquals(Optional.of(customer), result);
        verify(repository, times(1)).existsById(1L);
        verify(mapper, times(1)).mapToDao(customer);
        verify(repository, times(1)).save(customerDAO);
        verify(mapper, times(1)).mapFromDao(oldCustomerDAOEntry);
    }


    @Test
    public void testEditCustomer_NotExistingId() {
        when(repository.existsById(99L)).thenReturn(false);
        Optional<Customer> result = service.editCustomer(99L, customer);
        assertFalse(result.isPresent());
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).save(any());
    }


    @Test
    public void testSaveCustomer_Successful() {
        when(mapper.mapToDao(customer)).thenReturn(customerDAO);
        when(repository.save(customerDAO)).thenReturn(customerDAO);
        when(mapper.mapFromDao(customerDAO)).thenReturn(customer);
        Customer savedCustomer = service.saveCustomer(customer);
        verify(mapper, times(1)).mapToDao(customer);
        verify(repository, times(1)).save(customerDAO);
        verify(mapper, times(1)).mapFromDao(customerDAO);

    }

    @Test
    void testSaveCustomer_DuplicateCustomer() {
        Customer customer = new Customer(1L, "John", "Doe", "12345");
        CustomerDAO customerDAO = new CustomerDAO(1L, "John", "Doe", "12345");
        when(repository.findAll()).thenReturn(Collections.singletonList(customerDAO));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.saveCustomer(customer);
        });
        String expectedMessage = "Customer with the same name, surname, and identity number already exists";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(repository, never()).save(any());
    }

    /*@Test
    void testSaveCustomer_NullAttributesInExistingCustomer() {
        Customer customer = new Customer(1L, "John", "Doe", "12345");
        CustomerDAO customerDAO = new CustomerDAO(null, null, null, null);
        when(repository.findAll()).thenReturn(Collections.singletonList(customerDAO));
        Customer savedCustomer = service.saveCustomer(customer);
        assertNotNull(savedCustomer);
        verify(repository, times(1)).save(any());
    }*/


    @Test
    public void testDeleteCustomerById_Successful() {
        service.deleteCustomerById(1L);
        verify(repository, times(1)).deleteById(1L);
    }


    @Test
    public void testDeleteCustomerById_NonExistentCustomer() {
        doNothing().when(repository).deleteById(anyLong());
        service.deleteCustomerById(99L);
        verify(repository, times(1)).deleteById(99L);
    }

    @Test
    public void testIsCustomerPresent_WhenCustomerDoesNotExist() {
        when(repository.existsById(1L)).thenReturn(false);
        boolean result = service.isCustomerPresent(1L);
        assertFalse(result);
    }

    private List<CustomerDAO> createCustomerDAOList(CustomerDAO customerDAO) {
        List<CustomerDAO> list = new ArrayList<>();
        list.add(customerDAO);
        list.add(customerDAO);
        list.add(customerDAO);
        return list;
    }

    private CustomerDAO createOldCustomerDAOEntry() {
        return new CustomerDAO(
                1L,
                "OldName",
                "OldSurname",
                "OldIdentityNumber"
        );
    }

    private CustomerDAO createCustomerDAO() {
        return new CustomerDAO(
                1L,
                "Name",
                "Surname",
                "IdentityNumber"
        );
    }

    private Customer createCustomer() {
        return new Customer(
                1L,
                "Name",
                "Surname",
                "IdentityNumber"
        );
    }
}