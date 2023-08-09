package com.example.loan_service.controller;

import com.example.loan_service.business.repository.LoanPaymentRepository;
import com.example.loan_service.business.service.LoanPaymentService;
import com.example.loan_service.model.Loan;
import com.example.loan_service.model.LoanPayment;
import com.example.loan_service.swagger.DescriptionVariables;
import com.example.loan_service.swagger.HTMLResponseMessages;
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

@Api(tags = DescriptionVariables.LOAN_PAYMENT)
@Slf4j
@RestController
@RequestMapping("api/loanPayments")
public class LoanPaymentController {
    @Autowired
    LoanPaymentService service;

    @Autowired
    LoanPaymentRepository repository;


    @GetMapping("/all")
    @ApiOperation(value = "Finds all loan payments list",
            notes = "Returns all loan payments from the database",
            response = Loan.class)
    @ApiResponses(value = {
           /* @ApiResponse(code = 200, message = "${HTTP_200}"),
            @ApiResponse(code = 404, message = "${HTTP_404}"),
            @ApiResponse(code = 500, message = "${HTTP_500}")*/
            @ApiResponse(code = 200, message = "HTTP_200"),
            //@ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<LoanPayment>> getAllLoanPayments() {
        List<LoanPayment> list = service.getAllLoanPayments();
        if(list.isEmpty()) {
            log.warn("Loan payment list is not found");
            return ResponseEntity.notFound().build();
        } else {
            log.info("Loan payments found, with list size: {}", list.size());
            return ResponseEntity.ok(list);
        }
    }

    @GetMapping("/date")
    @ApiOperation(value = "Finds payments by passing in date",
            notes = "This API endpoint retrieves the payments based on the provided date (date should be in the format 'YYYY-MM-DD')",
            response = LoanPayment.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<LoanPayment>> getLoanPaymentsByDate(
            @RequestParam(value = "date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        List<LoanPayment> resultList = service.getLoansByDate(date);
        if (date == null) {
            log.warn("Date cannot be empty");
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        if (date.isAfter(currentDate)) {
            log.warn("The provided date is in the future");
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        if (resultList.isEmpty()) {
            log.warn("There are no payments on the specified date");
            return ResponseEntity.notFound().build();
        }
        log.info("Returning list of payments");
        return ResponseEntity.ok(resultList);
    }

}
