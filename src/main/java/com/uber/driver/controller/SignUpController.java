package com.uber.driver.controller;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.model.PhoneNumberAuthenticationToken;
import com.uber.driver.model.UserOtpAuthentication;
import com.uber.driver.model.UserToken;
import com.uber.driver.service.SignUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @PostMapping("/generateOtp")
    public ResponseEntity<UserOtpAuthentication> generateOtp(@RequestParam String phoneNumber){
        log.info("Request Received to generate OTP for mobileNumber : {}", phoneNumber);
        UserOtpAuthentication userOtp = signUpService.generateAndSendOTP(phoneNumber);
        //Should not use in production
        log.debug("OTP : {} generated successfully for mobileNumber : {}", userOtp.getOtp(), phoneNumber);
        return ResponseEntity.ok(userOtp);
    }

    @PostMapping("/validateOtpAndGenerateToken")
    public ResponseEntity<UserToken> validateOtp(@RequestParam String phoneNumber, @RequestParam int otp){
        log.info("Request Received to validate OTP : {} for mobileNumber : {} and generate token.", otp, phoneNumber);
        UserToken userToken = signUpService.validateOTPAndGenerateToken(phoneNumber, otp);
        //Should not use in production
        log.debug("Token : {} generated  for mobileNumber : {} and generate token.", userToken.getToken(), phoneNumber);
        return ResponseEntity.ok(userToken);
    }

}
