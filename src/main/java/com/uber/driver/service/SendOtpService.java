package com.uber.driver.service;

public interface SendOtpService {
    void sendOTP(String phoneNumber, int otp);
}
