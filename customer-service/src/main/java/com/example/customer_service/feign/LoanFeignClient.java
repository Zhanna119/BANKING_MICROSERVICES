package com.example.customer_service.feign;

import com.example.loan_service.model.Loan;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/*@FeignClient(name = "loan-service")
public interface LoanFeignClient {
    @GetMapping("/api/loans/getById/{id}")
    ResponseEntity<Loan> getLoanById(@PathVariable("id") Long id);

}*/
