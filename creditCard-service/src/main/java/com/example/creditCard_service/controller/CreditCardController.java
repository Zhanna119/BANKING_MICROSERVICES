package com.example.creditCard_service.controller;

import com.example.creditCard_service.model.CreditCard;
import com.example.creditCard_service.config.DescriptionVariables;
import com.example.creditCard_service.config.HTMLResponseMessages;
import com.example.creditCard_service.business.repository.CreditCardRepository;
import com.example.creditCard_service.business.service.CreditCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@Api(tags = DescriptionVariables.CREDIT_CARD)
@Slf4j
@RestController
@RequestMapping("api/creditCards")
public class CreditCardController {
    @Autowired
    CreditCardService service;

    @Autowired
    CreditCardRepository repository;

    @GetMapping("/all")
    @ApiOperation(value = "Finds all credit cards list",
                  notes = "Returns all credit cards from the database",
                  response = CreditCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<CreditCard>> getAllCreditCards() {
        List<CreditCard> list = service.getAllCreditCards();
        if(list.isEmpty()) {
            log.warn("Credit card list is not found");
            return ResponseEntity.notFound().build();
        } else {
            log.info("Credit cards found, with list size: {}", list.size());
            return ResponseEntity.ok(list);
        }
    }

    @GetMapping("getById/{id}")
    @ApiOperation(value = "Finds credit cards by id",
                  notes = "Provide an id to search for a specific credit card",
                  response = CreditCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<CreditCard> getCreditCardById(
            @ApiParam(value = "id of the credit card", required = true)
            @NonNull @PathVariable("id") Long id) {
        Optional<CreditCard> creditCard = service.getCreditCardById(id);
        if(creditCard.isPresent()) {
            log.info("Found credit card with id {}", id);
            return ResponseEntity.ok(creditCard.get());
        }
        else {
            log.warn("Credit card with id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/edit/{id}")
    @ApiOperation(value = "Changes credit card data entry with given id",
            notes = "Provide an id to search for a specific credit card in the database",
            response = CreditCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 422, message = HTMLResponseMessages.HTTP_422),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    ResponseEntity<CreditCard> updateCreditCard(
            @ApiParam(value = "Id of the credit card", required = true)
            @NonNull @PathVariable("id") Long id,
            @Valid @RequestBody CreditCard creditCard) {
        if(!creditCard.getId().equals(id)) {
            log.warn("Given id {} does not match the request body or does not exists", id);
            return ResponseEntity.badRequest().build();
        }
        Optional<CreditCard> creditCardOptional = service.editCreditCard(id, creditCard);
        if(!creditCardOptional.isPresent()) {
            log.warn("Credit card with given id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Credit card with id {} updated", id);
        return ResponseEntity.ok(creditCardOptional.get());
    }

    @PostMapping("/save")
    @ApiOperation(value = "Saves new credit card in database",
            notes = "Saves credit card if it's valid",
            response = CreditCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 422, message = HTMLResponseMessages.HTTP_409),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    ResponseEntity<CreditCard> saveCreditCard(@Valid @RequestBody CreditCard creditCard) {
        CreditCard data = service.saveCreditCard(creditCard);
        if(service.getCreditCardById(creditCard.getId()).isPresent()) {
            log.warn("Credit card with this id already exists");
            return ResponseEntity.unprocessableEntity().build();
        }
        log.info("Credit card entry saved");
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Deletes credit card entry with given id",
            notes = "Provide an id to delete credit card from database",
            response = CreditCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<CreditCard> deleteCreditCardById(
            @ApiParam(value = "id of the credit card", required = true)
            @NonNull @PathVariable("id") Long id) {
        Optional<CreditCard> creditCard = service.getCreditCardById(id);
        if(creditCard.isEmpty()){
            log.warn("Credit card with id {} is not found", id);
            return ResponseEntity.badRequest().build();
        }
        log.info("Credit card with id {} is deleted", id);
        service.deleteCreditCardById(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("customerId/{customerId}")
    @ApiOperation(value = "Finds all customer credit cards by customer id",
            notes = "Provide a customer id to search all customer credit cards",
            response = CreditCard.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<CreditCard>> getAllCreditCardsByCustomerId(
            @ApiParam(value = "Customer id", required = true)
            @NonNull @PathVariable("customerId") Long customerId) {
        List<CreditCard> creditCards = service.getAllCreditCardsByCustomerId(customerId);
        if (!creditCards.isEmpty()) {
            log.info("Found credit cards with customer id {}", customerId);
            return ResponseEntity.ok(creditCards);
        } else {
            log.warn("Credit cards with customer id {} is not found", customerId);
            return ResponseEntity.notFound().build();
        }
    }
}

