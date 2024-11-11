package com.example.coffee_shop_chain_management.entity;

import com.example.coffee_shop_chain_management.enums.OTPType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "otp")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "otp_code")
    int otpCode;

    @Column(name = "email")
    String email;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "expired_at")
    LocalDateTime expiredAt;

    @Column(name = "is_used")
    Boolean isUsed;

    @Column(name = "used_at")
    LocalDateTime usedAt;

    @Column(name = "otp_type")
    OTPType otpType;

    public OTP(int otp, String email, OTPType otpType) {
        this.otpCode = otp;
        this.email = email;
//        this.createdAt = System.currentTimeMillis();
//        this.expiredAt = System.currentTimeMillis() + 300000;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
        this.isUsed = false;
        this.usedAt = null;
        this.otpType = otpType;
    }
}
