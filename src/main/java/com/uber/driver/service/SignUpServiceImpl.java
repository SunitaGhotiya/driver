package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.exception.AuthenticationException;
import com.uber.driver.model.UserOtpAuthentication;
import com.uber.driver.model.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignUpServiceImpl implements SignUpService{

    @Autowired
    private OtpService otpService;

    @Autowired
    private SendOtpService sendOtpService;

    @Autowired
    private JwtService jwtService;

    @Override
    public UserOtpAuthentication generateAndSendOTP(String phoneNumber) {
        UserOtpAuthentication userOtpAuthentication = otpService.generateOTP(phoneNumber);
        sendOtpService.sendOTP(phoneNumber, userOtpAuthentication.getOtp());
        return userOtpAuthentication;
    }

    @Override
    public UserToken validateOTPAndGenerateToken(String phoneNumber, int otp) {
        boolean isValidOtp = otpService.validateOTP(phoneNumber, otp);
        if(isValidOtp)
            return jwtService.generateToken(phoneNumber);
        else
            throw new AuthenticationException(DriverConstants.INVALID_OTP);
    }
}
