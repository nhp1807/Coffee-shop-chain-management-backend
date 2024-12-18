package com.example.coffee_shop_chain_management.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportOrderDataResponse {
    Long productID;
    String productName;
    Integer quantity;
    Double price;
    Double total;
    LocalDateTime date;
}
