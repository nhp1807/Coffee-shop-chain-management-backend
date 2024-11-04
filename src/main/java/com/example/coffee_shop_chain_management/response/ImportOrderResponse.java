package com.example.coffee_shop_chain_management.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImportOrderResponse {
    Long importID;
    Double total;
    String paymentMethod;
    String date;
    Long supplierId;
    Long branchId;
}
