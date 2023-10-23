package com.uber.driver.controller;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @PostMapping("/generateOtp")
    public @ResponseBody String generateOtp(@RequestParam String phoneNumber){
        return signUpService.generateAndSendOTP(phoneNumber);
    }

    @PostMapping("/validateOtpAndGenerateToken")
    public @ResponseBody String validateOtp(@RequestParam String phoneNumber, @RequestParam int otp){
        return signUpService.validateOTPAndGenerateToken(phoneNumber, otp);
    }

}
