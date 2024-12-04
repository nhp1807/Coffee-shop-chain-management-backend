package com.example.coffee_shop_chain_management.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateExportOrderDTO {
    String paymentMethod;
    Long employeeID;
    Long branchID;
    List<DetailExportOrderDTO> detailExportOrders;
}
