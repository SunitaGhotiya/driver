package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpServiceImpl implements SignUpService{

    @Autowired
    private OtpService otpService;

    @Autowired
    private SendOtpService sendOtpService;

    @Autowired
    private JwtService jwtService;

    @Override
    public String generateAndSendOTP(String phoneNumber) {
        int otp = otpService.generateOTP(phoneNumber);
        sendOtpService.sendOTP(phoneNumber, otp);
        return DriverConstants.OTP_GENERATED_SUCCESS;
    }

    @Override
    public String validateOTPAndGenerateToken(String phoneNumber, int otp) {
        boolean isValidOtp = otpService.validateOTP(phoneNumber, otp);
        if(isValidOtp)
            return jwtService.generateToken(phoneNumber);
        else
            return DriverConstants.OTP_INVALID;
    }
}
