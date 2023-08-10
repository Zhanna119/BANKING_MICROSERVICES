package com.example.customer_service.feign;

import com.example.loan_service.model.Loan;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "eureka-loan-service")
public interface LoanFeignClient {
    @GetMapping("/api/loans/id/{id}")
    ResponseEntity<List<Loan>> getLoanById(@PathVariable("id") Long id);
}
