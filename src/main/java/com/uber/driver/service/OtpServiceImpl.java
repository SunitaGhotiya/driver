package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.exception.AuthenticationException;
import com.uber.driver.model.UserOtpAuthentication;
import com.uber.driver.reposiotry.UserOtpAuthenticationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
public class OtpServiceImpl implements OtpService{

    @Autowired
    private UserOtpAuthenticationRepository userOtpAuthenticationRepository;

    @Override
    public UserOtpAuthentication generateOTP(String phoneNumber) {
        log.info("Generating OTP for phoneNumber : {}", phoneNumber);
        int otp = 1000 + new Random().nextInt(999);
        UserOtpAuthentication userOtpAuthentication = new UserOtpAuthentication(phoneNumber, otp);
        userOtpAuthenticationRepository.save(userOtpAuthentication);
        log.info("OTP generated and saved in cache successfully for phoneNumber : {}", phoneNumber);
        return userOtpAuthentication;
    }

    @Override
    public boolean validateOTP(String phoneNumber, int otp) {
        log.info("Validating OTP for phoneNumber : {}", phoneNumber);
        UserOtpAuthentication userOtpAuthentication = userOtpAuthenticationRepository.findById(phoneNumber).orElse(null);
        if(Objects.nonNull(userOtpAuthentication) && otp == userOtpAuthentication.getOtp()){
            userOtpAuthenticationRepository.deleteById(phoneNumber);
            log.info("OTP Validated and removed from cache for phoneNumber : {}", phoneNumber);
            return true;
        }
        log.info("Invalid OTP for phoneNumber : {}", phoneNumber);
        return false;
    }

}
