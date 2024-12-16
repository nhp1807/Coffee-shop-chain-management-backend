package com.example.coffee_shop_chain_management.response;

import com.example.coffee_shop_chain_management.entity.Timesheet;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryResponse {
    Long employeeID;
    String chatID;
    String email;
    Double totalSalary;
    Double totalShifts;
    Double shiftSalary;
    List<TimesheetResponse> timesheets;
}
