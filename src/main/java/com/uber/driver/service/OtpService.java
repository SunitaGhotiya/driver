package com.uber.driver.service;

public interface OtpService {
    int generateOTP(String phoneNumber);
    boolean validateOTP(String phoneNumber, int otp);
}
