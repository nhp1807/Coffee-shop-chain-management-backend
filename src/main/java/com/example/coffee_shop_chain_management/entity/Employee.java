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
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long employeeID;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "dob", nullable = false)
    Date dob;

    @Column(name = "phone", nullable = false)
    String phone;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "address", nullable = false)
    String address;

    @Column(name = "telegram_id", nullable = false)
    Integer telegramID;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    Branch branch;

    // Getters and setters
}
