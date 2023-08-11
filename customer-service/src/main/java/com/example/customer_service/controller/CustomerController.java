package com.example.customer_service.controller;

import com.example.account_service.model.Account;
import com.example.customer_service.business.repository.CustomerRepository;
import com.example.customer_service.business.service.CustomerService;
import com.example.customer_service.config.WebCustomer;
import com.example.customer_service.feign.LoanFeignClient;
import com.example.customer_service.model.Customer;
import com.example.customer_service.swagger.DescriptionVariables;
import com.example.customer_service.swagger.HTMLResponseMessages;
import com.example.loan_service.model.Loan;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Api(tags = DescriptionVariables.CUSTOMER)
@Slf4j
@RestController
@RequestMapping("api/customers")
public class CustomerController {
    @Autowired
    CustomerService service;

    @Autowired
    CustomerRepository repository;

    private LoanFeignClient loanFeignClient;

    public CustomerController(LoanFeignClient loanFeignClient) {

        this.loanFeignClient = loanFeignClient;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Finds all customers list",
                  notes = "Returns all customers from the database",
                  response = Customer.class)
    @ApiResponses(value = {
            /*@ApiResponse(code = 200, message = "${HTTP_200}"),
            @ApiResponse(code = 404, message = "${HTTP_404}"),
            @ApiResponse(code = 500, message = "${HTTP_500}")*/
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> list = service.getAllCustomers();
        if(list.isEmpty()) {
            log.info("Customers list is not found");
            return ResponseEntity.notFound().build();
        } else {
            log.info("Customers found, with list size: {}", list.size());
            return ResponseEntity.ok(list);
        }
    }

    @GetMapping("getById/{id}")
    @ApiOperation(value = "Finds customer by id",
                  notes = "Provide an id to search for a specific customer",
                  response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<Customer> getCustomerById(
            @ApiParam(value = "id of the customer", required = true)
            @NonNull @PathVariable("id") Long id) {
        Optional<Customer> customer = service.getCustomerById(id);
        if(customer.isPresent()) {
            log.info("Found customer with id {}", id);
            return ResponseEntity.ok(customer.get());
        }
        else {
            log.info("Customer with id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/edit/{id}")
    @ApiOperation(value = "Changes customer data entry with given id",
            notes = "Provide an id to search for a specific customer in the database",
            response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 422, message = HTMLResponseMessages.HTTP_422),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    ResponseEntity<Customer> updateCustomer(
            @ApiParam(value = "Id of the customer", required = true)
            @NonNull @PathVariable("id") Long id,
            @Valid @RequestBody Customer customer) {
        if(!customer.getId().equals(id)) {
            log.warn("Given id {} does not match the request body or does not exists", id);
            return ResponseEntity.badRequest().build();
        }
        Optional<Customer> customerOptional = service.editCustomer(id, customer);
        if(!customerOptional.isPresent()) {
            log.warn("Customer with given id {} is not found", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Customer with id {} updated", id);
        return ResponseEntity.ok(customerOptional.get());
    }

    @PostMapping("/save")
    @ApiOperation(value = "Saves new customer in database",
            notes = "Saves customer if it's valid",
            response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 422, message = HTMLResponseMessages.HTTP_409),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    ResponseEntity<Customer> saveCustomer(@Valid @RequestBody Customer customer) {
        Customer data = service.saveCustomer(customer);
        if(service.getCustomerById(customer.getId()).isPresent()) {
            log.warn("Customer with this id already exists");
            return ResponseEntity.unprocessableEntity().build();
        }
        log.info("Customer entry saved");
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Deletes customer entry with given id",
            notes = "Provide an id to delete customer from database",
            response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<Customer> deleteCustomerById(
            @ApiParam(value = "id of the customer", required = true)
            @NonNull @PathVariable("id") Long id) {
        Optional<Customer> customer = service.getCustomerById(id);
        if(customer.isEmpty()){
            log.warn("Customer with id {} is not found", id);
            return ResponseEntity.badRequest().build();
        }
        log.info("Customer with id {} is deleted", id);
        service.deleteCustomerById(id);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/{customerId}/accounts")
    @ApiOperation(value = "Searches for all customer accounts with given customer id",
            notes = "Provide an id to find all customer accounts from database",
            response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public Mono<ResponseEntity<Flux<Account>>> getAccountsByCustomerId(@PathVariable String customerId) {
        log.info("Searching for all customer accounts");
        Long customerIdAsLong = Long.valueOf(customerId);
        if (!service.isCustomerPresent(customerIdAsLong)) {
            log.warn("Customer with id {} is not found", customerId);
            return Mono.just(ResponseEntity.notFound().build());
        }
        log.info("Returning list of all customer {} accounts", customerId);
        return Mono.just(ResponseEntity.ok(WebCustomer.getAccountsByCustomerId(customerId)));
    }


    @GetMapping("/{customerId}/loans")
    @ApiOperation(value = "Searches for all customer loans with given customer id",
            notes = "Provide an id to find all customer loans from database",
            response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public Flux<Loan> getLoansByCustomerId(@PathVariable String customerId) {
        log.info("Searching for all loans of customer {}", customerId);
        return WebCustomer.getLoansByCustomerId(customerId);
    }


    @GetMapping("/{customerId}/loansFeign")
    @ApiOperation(value = "Finds customer loans with given customer id",
            notes = "Provide an customer id to find all customer loans",
            response = Customer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    public ResponseEntity<List<Loan>> getLoanById(@PathVariable Long customerId) {
        log.info("Searching for all loans of customer {}", customerId);
        if(!service.isCustomerPresent(customerId)){
            log.warn("Customer with id {} is not found", customerId);
            return ResponseEntity.notFound().build();
        }
        ResponseEntity<List<Loan>> loans = loanFeignClient.getLoanById(customerId);
        if (loans == null) {
            log.warn("Server Error", customerId);
            return ResponseEntity.badRequest().build();
        }
        log.info("Returning all customer with id {}, loans", customerId);
        return loans;
    }
}

