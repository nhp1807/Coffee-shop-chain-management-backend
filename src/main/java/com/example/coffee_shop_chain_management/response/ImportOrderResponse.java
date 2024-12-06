package com.example.coffee_shop_chain_management.response;

import com.example.coffee_shop_chain_management.dto.DetailImportOrderDTO;
import com.example.coffee_shop_chain_management.entity.DetailImportOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    Long supplierID;
    Long branchID;
    Boolean status;
    List<DetailImportOrderResponse> detailImportOrders;
}
