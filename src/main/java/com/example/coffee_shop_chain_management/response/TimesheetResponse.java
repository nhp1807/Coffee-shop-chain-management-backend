package com.example.coffee_shop_chain_management.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimesheetResponse {
    Long timesheetID;
    Long employeeId;
    LocalDateTime date;
    String shift;
    Long BranchId;
}
