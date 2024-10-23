package com.example.coffee_shop_chain_management.entity;

import com.example.coffee_shop_chain_management.enums.OTPType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    Long createdAt;

    @Column(name = "expired_at")
    Long expiredAt;

    @Column(name = "is_used")
    Boolean isUsed;

    @Column(name = "used_at")
    Long usedAt;

    @Column(name = "otp_type")
    OTPType otpType;

    public OTP(int otp, String email, OTPType otpType) {
        this.otpCode = otp;
        this.email = email;
        this.createdAt = System.currentTimeMillis();
        this.expiredAt = System.currentTimeMillis() + 300000;
        this.isUsed = false;
        this.usedAt = null;
        this.otpType = otpType;
    }
}
