package com.uber.driver.service;

public interface SignUpService {
    String generateAndSendOTP(String phoneNumber);
    String validateOTPAndGenerateToken(String phoneNumber, int otp);
}
