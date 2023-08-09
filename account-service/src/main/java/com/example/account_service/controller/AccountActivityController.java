package com.example.account_service.controller;

import com.example.account_service.business.repository.AccountActivityRepository;
import com.example.account_service.business.service.AccountActivityService;
import com.example.account_service.model.AccountActivity;
import com.example.account_service.swagger.DescriptionVariables;
import com.example.account_service.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Api(tags = DescriptionVariables.ACCOUNT_ACTIVITY)
@Slf4j
@RestController
@RequestMapping("api/accountActivity")
public class AccountActivityController {
    @Autowired
    AccountActivityService service;

    @Autowired
    AccountActivityRepository repository;

    @GetMapping("/all")
    @ApiOperation(value = "Finds all transactions list",
            notes = "Returns all transactions from the database",
            response = AccountActivity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<AccountActivity>> getAllTransactions() {
        List<AccountActivity> list = service.getAllTransactions();
        if(list.isEmpty()) {
            log.warn("Transactions are not found");
            return ResponseEntity.notFound().build();
        } else {
            log.info("Transactions found, with list size: {}", list.size());
            return ResponseEntity.ok(list);
        }
    }

    @GetMapping("/date")
    @ApiOperation(value = "Finds transactions by passing in date",
            notes = "This API endpoint retrieves the transactions based on the provided date (date should be in the format 'YYYY-MM-DD')",
            response = AccountActivity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<AccountActivity>> getTransactionsByDate(
            @RequestParam(value = "date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        List<AccountActivity> resultList = service.getTransactionsByDate(date);
        if (date == null) {
            log.warn("Date cannot be empty");
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        if (date.isAfter(currentDate)) {
            log.warn("The provided date is in the future");
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        if (resultList.isEmpty()) {
            log.warn("There are no transactions on the specified date");
            return ResponseEntity.notFound().build();
        }
        log.info("Returning list of transactions");
        return ResponseEntity.ok(resultList);
    }

   /* @GetMapping("/account/{accountId}/transactions")
    @ApiOperation(value = "Finds transactions by account id",
            notes = "Provide an account id to search for all account transactions",
            response = AccountActivity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<Optional<AccountActivity>> getTransactionsByAccountId(
            @PathVariable("accountId") Long accountId) {
        Optional<AccountActivity> accountTransactions = service.getTransactionsByAccountId(accountId);
        if (!accountTransactions.isEmpty()) {
            log.info("Found transactions of account with id {}", accountId);
            return ResponseEntity.ok(accountTransactions);
        } else {
            log.warn("Account with id {} is not found or has no transactions", accountId);
            return ResponseEntity.notFound().build();
        }
    }*/

   /* @GetMapping("/account/{accountId}/activity")
    public ResponseEntity<AccountActivity> getAccountActivityByActivityId(@PathVariable Long activityId, @PathVariable String accountId) {
        Optional<AccountActivity> activityOptional = service.getTransactionsByAccountId(activityId);
        if (activityOptional.isPresent()) {
            return ResponseEntity.ok(activityOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
}
