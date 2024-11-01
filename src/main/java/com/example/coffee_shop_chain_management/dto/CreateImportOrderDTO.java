package com.example.coffee_shop_chain_management.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateImportOrderDTO {
    Long supplierId;
    Long branchId;
    Double total;
    String paymentMethod;
    Date date;
    List<DetailImportOrderDTO> detailImportOrders;
}
