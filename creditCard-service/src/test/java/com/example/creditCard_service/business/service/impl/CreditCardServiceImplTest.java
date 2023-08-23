package com.example.creditCard_service.business.service.impl;

import com.example.creditCard_service.business.mappers.CreditCardMapper;
import com.example.creditCard_service.business.repository.CreditCardRepository;
import com.example.creditCard_service.business.repository.model.CreditCardDAO;
import com.example.creditCard_service.model.CreditCard;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CreditCardServiceImplTest {
    @Mock
    private CreditCardRepository repository;

    @Mock
    private CreditCardMapper mapper;

    @InjectMocks
    private CreditCardServiceImpl service;

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

    private CreditCardDAO creditCardDAO = new CreditCardDAO(
            1L,
            1L,
            "123",
            LocalDate.of(2020, 01, 01),
            new BigDecimal(10000.00),
            new BigDecimal(10000.00),
            new BigDecimal(10000.00),
            new BigDecimal(10000.00),
            null);

    private CreditCardDAO oldCreditCardDAO = new CreditCardDAO(
            99L,
            1L,
            "123",
            LocalDate.of(2020, 01, 01),
            new BigDecimal(10000.00),
            new BigDecimal(10000.00),
            new BigDecimal(10000.00),
            new BigDecimal(10000.00),
            null);

    private List<CreditCardDAO> creditCardDAOList =
            Arrays.asList(creditCardDAO, creditCardDAO, creditCardDAO);

    @Test
    void testGetAllCreditCards_Successful() {
        when(repository.findAll()).thenReturn(creditCardDAOList);
        when(mapper.mapFromDao(creditCardDAO)).thenReturn(creditCard);
        List<CreditCard> result = service.getAllCreditCards();
        assertEquals(3, result.size());
        assertEquals(creditCard.getId(), result.get(0).getId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllCreditCards_EmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<CreditCard> result = service.getAllCreditCards();
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetCreditCardById() {
        when(repository.findById(1L)).thenReturn(Optional.of(creditCardDAO));
        when(mapper.mapFromDao(creditCardDAO)).thenReturn(creditCard);
        Optional<CreditCard> result = service.getCreditCardById(1L);
        assertEquals(creditCard.getId(), result.get().getId());
        assertTrue(result.isPresent());
        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).mapFromDao(creditCardDAO);
    }

    @Test
    void testGetCreditCardById_Empty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        Optional<CreditCard> result = service.getCreditCardById(99L);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findById(99L);

    }

    @Test
    void testEditCreditCard_Successful() {
        when(repository.existsById(1L)).thenReturn(true);
        when(mapper.mapToDao(creditCard)).thenReturn(creditCardDAO);
        when(repository.save(creditCardDAO)).thenReturn(oldCreditCardDAO);
        when(mapper.mapFromDao(oldCreditCardDAO)).thenReturn(creditCard);
        Optional<CreditCard> result = service.editCreditCard(1L, creditCard);
        assertEquals(Optional.of(creditCard), result);
        verify(repository, times(1)).existsById(1L);
        verify(mapper, times(1)).mapToDao(creditCard);
        verify(repository, times(1)).save(creditCardDAO);
        verify(mapper, times(1)).mapFromDao(oldCreditCardDAO);
    }

    @Test
    void testEditCreditCard_NotExistingId() {
        when(repository.existsById(99L)).thenReturn(false);
        Optional<CreditCard> result = service.editCreditCard(99L, creditCard);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).existsById(99L);
    }


    @Test
    void testSaveCreditCard_Successful() {
        when(mapper.mapToDao(creditCard)).thenReturn(creditCardDAO);
        when(repository.save(creditCardDAO)).thenReturn(creditCardDAO);
        when(mapper.mapFromDao(creditCardDAO)).thenReturn(creditCard);
        CreditCard saveCreditCard = service.saveCreditCard(creditCard);
        assertEquals(creditCard, saveCreditCard);
        verify(mapper, times(1)).mapToDao(creditCard);
        verify(repository, times(1)).save(creditCardDAO);
        verify(mapper, times(1)).mapFromDao(creditCardDAO);
    }


    @Test
    void testSaveLoanWithDuplicateId() {
        when(repository.findAll()).thenReturn(Collections.singletonList(creditCardDAO));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.saveCreditCard(creditCard);
        });
        assertEquals("Credit with the same id already exists", exception.getMessage());
    }

    @Test
    void testDeleteCreditCardById_Successful() {
        service.deleteCreditCardById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCreditCardById_NotFound() {
        doNothing().when(repository).deleteById(anyLong());
        service.deleteCreditCardById(99L);
        verify(repository, times(1)).deleteById(99L);
    }

    @Test
    public void testGetAccountsByCustomerId_Successful() {
        Long customerId = 1L; // Customer ID to search for
        CreditCardDAO creditCardDAO1 = new CreditCardDAO();
        creditCardDAO1.setCustomerId(customerId);
        CreditCardDAO creditCardDAO2 = new CreditCardDAO();
        creditCardDAO2.setCustomerId(customerId);
        CreditCard creditCard1 = new CreditCard();
        CreditCard creditCard2 = new CreditCard();
        List<CreditCardDAO> creditCardDAOList1 = Arrays.asList(creditCardDAO1, creditCardDAO2);
        when(repository.findAll()).thenReturn(creditCardDAOList1);
        when(mapper.mapFromDao(creditCardDAO1)).thenReturn(creditCard1);
        when(mapper.mapFromDao(creditCardDAO2)).thenReturn(creditCard2);
        List<CreditCard> result = service.getAllCreditCardsByCustomerId(customerId);
        assertEquals(2, result.size());
        assertEquals(creditCard1, result.get(0));
        assertEquals(creditCard2, result.get(1));
    }

    @Test
    public void testGetAccountsByCustomerId_NoAccounts() {
        Long customerId = 1L;
        List<CreditCard> result = service.getAllCreditCardsByCustomerId(customerId);
        assertEquals(0, result.size());
    }

}