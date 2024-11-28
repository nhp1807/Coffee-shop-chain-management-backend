package com.example.coffee_shop_chain_management.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExportOrderResponse {
    Long exportID;
    Double total;
    String paymentMethod;
    LocalDateTime date;
    Long employeeID;
    Long branchID;
}
