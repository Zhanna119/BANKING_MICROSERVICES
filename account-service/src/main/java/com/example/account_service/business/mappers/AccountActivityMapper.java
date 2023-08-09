package com.example.account_service.business.mappers;

import com.example.account_service.business.repository.model.AccountActivityDAO;
import com.example.account_service.model.AccountActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountActivityMapper {

    @Mapping(target = "accountId", source = "account.id")
    AccountActivity mapFromDao(AccountActivityDAO accountActivityDAO);

    @Mapping(target = "account.id", source = "accountId")
    AccountActivityDAO mapToDao(AccountActivity accountActivity);
}



