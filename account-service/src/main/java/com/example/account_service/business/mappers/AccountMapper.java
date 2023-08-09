package com.example.account_service.business.mappers;

import com.example.account_service.business.repository.model.AccountActivityDAO;
import com.example.account_service.business.repository.model.AccountDAO;
import com.example.account_service.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "customerAccountActivityIds", source = "customerAccountActivityIds")
    Account mapFromDao(AccountDAO accountDAO);

    @Mapping(target = "customerAccountActivityIds", source = "customerAccountActivityIds")
    AccountDAO mapToDao(Account account);

    @Mapping(target = "customerId", source = "accountDAO.customerId")
    Account mapWithCustomerId(AccountDAO accountDAO);

    @Mapping(target = "customerAccountActivityIds", expression = "java(mapAccountActivityDAOs(accountDAO.getCustomerAccountActivityIds()))")
    Account mapFromDaoWithActivityList(AccountDAO accountDAO);

    default AccountActivityDAO mapToAccountActivityDAO(Long id) {
        AccountActivityDAO accountActivityDAO = new AccountActivityDAO();
        accountActivityDAO.setId(id);
        return accountActivityDAO;
    }

    default List<Long> mapAccountActivityDAOs(List<AccountActivityDAO> accountActivityDAOs) {
        return accountActivityDAOs.stream()
                .map(AccountActivityDAO::getId)
                .collect(Collectors.toList());
    }
}




