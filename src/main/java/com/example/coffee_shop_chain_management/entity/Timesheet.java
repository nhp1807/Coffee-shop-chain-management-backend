package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "timesheet")
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long timesheetID;

    @Column(name = "date", nullable = false)
    Date date;

    @Column(name = "shift", nullable = false)
    String shift;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    Employee employee;

    // Getters and setters
}
