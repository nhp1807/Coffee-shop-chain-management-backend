package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    @Query("SELECT otp FROM OTP otp WHERE otp.email = ?1 AND otp.isUsed = false ORDER BY otp.createdAt DESC LIMIT 1")
    OTP findByEmail(String email);
}
