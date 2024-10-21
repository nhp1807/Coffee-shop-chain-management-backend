package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "branch")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long branchID;

    @Column(name = "address", nullable = false)
    String address;

    @Column(name = "phone", nullable = false)
    String phone;

    @Column(name = "fax", nullable = false)
    String fax;

    @OneToMany(mappedBy = "branch")
    List<Employee> employees;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    Account account;

    // Getters and setters
}

