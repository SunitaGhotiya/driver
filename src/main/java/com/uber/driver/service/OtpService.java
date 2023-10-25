package com.uber.driver.service;

import com.uber.driver.model.UserOtpAuthentication;

public interface OtpService {
    UserOtpAuthentication generateOTP(String phoneNumber);
    boolean validateOTP(String phoneNumber, int otp);
}
