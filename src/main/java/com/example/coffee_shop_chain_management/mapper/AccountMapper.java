package com.example.coffee_shop_chain_management.mapper;

import com.example.coffee_shop_chain_management.dto.CreateAccountDTO;
import com.example.coffee_shop_chain_management.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(CreateAccountDTO createAccountDTO);
}
