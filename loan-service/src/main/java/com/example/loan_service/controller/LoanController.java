package com.example.loan_service.controller;

import com.example.loan_service.business.repository.LoanRepository;
import com.example.loan_service.business.service.LoanService;
import com.example.loan_service.model.Loan;
import com.example.loan_service.swagger.DescriptionVariables;
import com.example.loan_service.swagger.HTMLResponseMessages;
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


@Api(tags = DescriptionVariables.LOAN)
@Slf4j
@RestController
@RequestMapping("api/loans")
public class LoanController {
    @Autowired
    LoanService service;

    @Autowired
    LoanRepository repository;

    @GetMapping("/all")
    @ApiOperation(value = "Finds all loans list",
                  notes = "Returns all loans from the database",
                  response = Loan.class)
    @ApiResponses(value = {
           /* @ApiResponse(code = 200, message = "${HTTP_200}"),
            @ApiResponse(code = 404, message = "${HTTP_404}"),
            @ApiResponse(code = 500, message = "${HTTP_500}")*/
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> list = service.getAllLoans();
        if(list.isEmpty()) {
            log.warn("Loan list is not found");
            return ResponseEntity.notFound().build();
        } else {
            log.info("Loans found, with list size: {}", list.size());
            return ResponseEntity.ok(list);
        }
    }

    @GetMapping("getById/{id}")
    @ApiOperation(value = "Finds loan by id",
                  notes = "Provide an id to search for a specific loan",
                  response = Loan.class)
    @ApiResponses(value = {
            //@ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<Loan> getLoanById(
            @ApiParam(value = "id of the loan", required = true)
            @NonNull @PathVariable("id") Long id) {
        Optional<Loan> loan = service.getLoanById(id);
        if(loan.isPresent()) {
            log.info("Found loan with id {}", id);
            return ResponseEntity.ok(loan.get());
        }
        else {
            log.warn("Loan with id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/edit/{id}")
    @ApiOperation(value = "Changes loan data entry with given id",
            notes = "Provide an id to search for a specific loan in the database",
            response = Loan.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 422, message = HTMLResponseMessages.HTTP_422),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    ResponseEntity<Loan> updateLoan(
            @ApiParam(value = "Id of the loan", required = true)
            @NonNull @PathVariable("id") Long id,
            @Valid @RequestBody Loan loan) {
        if(!loan.getId().equals(id)) {
            log.warn("Given id {} does not match the request body or does not exists", id);
            return ResponseEntity.badRequest().build();
        }
        Optional<Loan> loanOptional = service.editLoan(id, loan);
        if(!loanOptional.isPresent()) {
            log.warn("Loan with given id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Loan with id {} updated", id);
        return ResponseEntity.ok(loanOptional.get());
    }

    @PostMapping("/save")
    @ApiOperation(value = "Saves new loan in database",
            notes = "Saves loan if it's valid",
            response = Loan.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 422, message = HTMLResponseMessages.HTTP_409),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    ResponseEntity<Loan> saveLoan(@Valid @RequestBody Loan loan) {
        Loan data = service.saveLoan(loan);
        if(service.getLoanById(loan.getId()).isPresent()) {
            log.warn("Loan with this id already exists");
            return ResponseEntity.unprocessableEntity().build();
        }
        log.info("Loan entry saved");
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Deletes loan entry with given id",
            notes = "Provide an id to delete loan from database",
            response = Loan.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<Loan> deleteLoanById(
            @ApiParam(value = "id of the loan", required = true)
            @NonNull @PathVariable("id") Long id) {
        Optional<Loan> loan = service.getLoanById(id);
        if(loan.isEmpty()){
            log.warn("Loan with id {} is not found", id);
            return ResponseEntity.badRequest().build();
        }
        log.info("Loan with id {} is deleted", id);
        service.deleteLoanById(id);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("customerId/{customerId}")
    @ApiOperation(value = "Finds loans by customer id",
            notes = "Provide a customer id to search all customer loans",
            response = Loan.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<Loan>> getAllLoansByCustomerId(
            @ApiParam(value = "Customer id", required = true)
            @NonNull @PathVariable("customerId") Long customerId) {
        List<Loan> loans = service.getAllLoansByCustomerId(customerId);
        if (!loans.isEmpty()) {
            log.info("Found all loans with customer id {}", customerId);
            return ResponseEntity.ok(loans);
        } else {
            log.warn("Loans with customer id {} is not found", customerId);
            return ResponseEntity.notFound().build();
        }
    }

}

