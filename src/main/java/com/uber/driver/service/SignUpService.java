package com.uber.driver.service;

import com.uber.driver.model.UserOtpAuthentication;
import com.uber.driver.model.UserToken;

public interface SignUpService {
    UserOtpAuthentication generateAndSendOTP(String phoneNumber);
    UserToken validateOTPAndGenerateToken(String phoneNumber, int otp);
}
