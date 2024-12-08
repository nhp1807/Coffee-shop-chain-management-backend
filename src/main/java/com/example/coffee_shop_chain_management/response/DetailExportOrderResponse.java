package com.example.coffee_shop_chain_management.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetailExportOrderResponse {
    Long productID;
    String name;
    Integer quantity;
    Double price;
    String description;
}
