package com.example.coffee_shop_chain_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountID;

    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "role", nullable = false)
    String role;

    @Column(name = "chatID")
    Integer chatID;

    @Column(name = "email")
    String email;

    @OneToOne(mappedBy = "account")
    @JoinColumn(name = "branch_id", nullable = false)
    Branch branch;

    // Getters and setters
}

