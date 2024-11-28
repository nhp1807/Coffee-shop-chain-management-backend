package com.example.coffee_shop_chain_management.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTimesheetDTO {
    LocalDateTime date;
    String shift;
    Long employeeId;
}
