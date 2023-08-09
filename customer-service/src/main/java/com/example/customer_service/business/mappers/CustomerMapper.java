package com.example.customer_service.business.mappers;

import com.example.account_service.model.Account;
import com.example.customer_service.business.repository.model.CustomerDAO;
import com.example.customer_service.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer mapFromDao(CustomerDAO customerDAO);

    CustomerDAO mapToDao(Customer customer);

    @Mapping(source = "accounts", target = "customerAccountIds")
    Customer mapToDtoWithAccounts(CustomerDAO customerDAO, List<Account> accounts);

    default List<Long> mapAccountsToIds(List<Account> accounts) {
        if (accounts == null) {
            return null;
        }
        return accounts.stream()
                .map(Account::getId)
                .collect(Collectors.toList());
    }
}


