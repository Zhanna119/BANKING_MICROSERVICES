package com.example.account_service.controller;

import com.example.account_service.business.repository.AccountRepository;
import com.example.account_service.business.service.AccountService;
import com.example.account_service.model.Account;
import com.example.account_service.config.DescriptionVariables;
import com.example.account_service.config.HTMLResponseMessages;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(name = DescriptionVariables.ACCOUNT, description = "Information related to customer accounts")
@Slf4j
@RestController
@RequestMapping("api/accounts")
public class AccountController {
    @Autowired
    AccountService service;

    @Autowired
    AccountRepository repository;

    @GetMapping("/all")
    @Operation(summary = "Finds all accounts list",
                  description = "Returns all accounts from the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> list = service.getAllAccounts();
        if(list.isEmpty()) {
            log.warn("Account list is not found");
            return ResponseEntity.notFound().build();
        } else {
            log.info("Accounts found, with list size: {}", list.size());
            return ResponseEntity.ok(list);
        }
    }

    @GetMapping("getById/{id}")
    @Operation(summary = "Finds account by id",
                  description = "Provide an id to search for a specific account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<Account> getAccountById(
            @ApiParam(value = "id of the account", required = true)
            @NonNull @PathVariable("id") Long id) {
        Optional<Account> account = service.getAccountById(id);
        if(account.isPresent()) {
            log.info("Found account with id {}", id);
            return ResponseEntity.ok(account.get());
        }
        else {
            log.warn("Account with id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/edit/{id}")
    @Operation(summary = "Changes account data entry with given id",
            description = "Provide an id to search for a specific account in the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 422, message = HTMLResponseMessages.HTTP_422),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    ResponseEntity<Account> updateAccount(
            @ApiParam(value = "Id of the account", required = true)
            @NonNull @PathVariable("id") Long id,
            @Valid @RequestBody Account account) {
        if(!account.getId().equals(id)) {
            log.warn("Given id {} does not match the request body or does not exists", id);
            return ResponseEntity.badRequest().build();
        }
        Optional<Account> accountOptional = service.editAccount(id, account);
        if(!accountOptional.isPresent()) {
            log.warn("Account with given id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Account with id {} updated", id);
        return ResponseEntity.ok(accountOptional.get());
    }

    @PostMapping("/save")
    @Operation(summary = "Saves new account in database",
            description = "Saves account if it's valid")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 422, message = HTMLResponseMessages.HTTP_409),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    ResponseEntity<Account> saveAccount(@Valid @RequestBody Account account) {
        Account data = service.saveAccount(account);
        /*if(service.getAccountById(account.getId()).isPresent()) {
            log.warn("Account with this id already exists");
            return ResponseEntity.unprocessableEntity().build();
        }*/
        log.info("Account entry saved");
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deletes account entry with given id",
            description = "Provide an id to delete account from database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<Account> deleteAccountById(
            @ApiParam(value = "id of the account", required = true)
            @NonNull @PathVariable("id") Long id) {
        Optional<Account> account = service.getAccountById(id);
        if(account.isEmpty()){
            log.warn("Account with id {} is not found", id);
            return ResponseEntity.badRequest().build();
        }
        log.info("Account with id {} is deleted", id);
        service.deleteAccountById(id);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("id/{customerId}")
    @Operation(summary = "Finds accounts by customer id",
            description = "Provide a customer id to search all customer accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<Account>> getAllAccountByCustomerId(
            @ApiParam(value = "Customer id", required = true)
            @NonNull @PathVariable("customerId") Long customerId) {
        List<Account> accounts = service.getAccountsByCustomerId(customerId);
        if (!accounts.isEmpty()) {
            log.info("Found accounts with customer id {}", customerId);
            return ResponseEntity.ok(accounts);
        } else {
            log.warn("Account with customer id {} is not found", customerId);
            return ResponseEntity.notFound().build();
        }
    }

}

