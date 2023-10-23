package com.uber.driver.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService{

    public static final HashMap<String, Integer> OTP_CACHE = new HashMap<>();

    @Override
    public int generateOTP(String phoneNumber) {
        int otp = 1000 + new Random().nextInt(999);
        OTP_CACHE.put(phoneNumber, otp);
        return otp;
    }

    @Override
    public boolean validateOTP(String phoneNumber, int otp) {
        boolean isValidOtp = OTP_CACHE.containsKey(phoneNumber) && otp == OTP_CACHE.get(phoneNumber);
        return isValidOtp;
    }

}
