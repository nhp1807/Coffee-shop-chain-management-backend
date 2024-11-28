package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.OTP;
import com.example.coffee_shop_chain_management.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OTPService {
    @Autowired
    private OTPRepository otpRepository;

    public List<OTP> getAllOTP(){
        return otpRepository.findAll();
    }

    public OTP createOTP(OTP otp){
        return otpRepository.save(otp);
    }

    public OTP getOTPById(Long id){
        return otpRepository.findById(id).orElse(null);
    }

    public OTP getOTPByEmail(String email){
        return otpRepository.findByEmail(email);
    }

    public void deleteOTPById(Long id){
        otpRepository.deleteById(id);
    }

    public void updateOTP(OTP otp){
        otpRepository.save(otp);
    }
}
