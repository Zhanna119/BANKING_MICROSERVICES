package com.example.customer_service.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model of customers")
public class Customer {
    private static final String CONTAIN_LETTERS_MESSAGE = "Type must contain only letters";
    private static final String STRING_PATTERN = "^[a-zA-Z\\\\s]*$";


    @ApiModelProperty(notes = "The unique ID for the Customer")
    private Long id;

    @ApiModelProperty(notes = "Customers name")
    @NotEmpty
    @Pattern(regexp = STRING_PATTERN, message = CONTAIN_LETTERS_MESSAGE)
    private String name;

    @ApiModelProperty(notes = "Customers surname")
    @NotEmpty
    @Pattern(regexp = STRING_PATTERN, message = CONTAIN_LETTERS_MESSAGE)
    private String surname;

    @ApiModelProperty(notes = "Customer identity number")
    @NotEmpty
    private String identityNumber;

    @ApiModelProperty(notes = "Customer accounts")
    private List<Long> customerAccountIds;

    @ApiModelProperty(notes = "Customer loans")
    private List<Long> customerLoansIds;

    @ApiModelProperty(notes = "Customer credit cards")
    private List<Long> customerCreditCardIds;

    public Customer(Long id, String name, String surname, String identityNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.identityNumber = identityNumber;
    }
}