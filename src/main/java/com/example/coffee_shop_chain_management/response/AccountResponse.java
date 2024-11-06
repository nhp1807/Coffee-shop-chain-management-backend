package com.example.coffee_shop_chain_management.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    Long accountID;
    String username;
    String role;
    String chatID;
    String email;
    Long branchID;
}
