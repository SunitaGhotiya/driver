package com.uber.driver.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendOtpServiceImpl implements SendOtpService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String formPhoneNumber;

    @Override
    public void sendOTP(String phoneNumber, int otp) {
        log.info("Send OTP for phoneNumber : {}", phoneNumber);
        Twilio.init(accountSid, authToken);
        Message.creator(new PhoneNumber("+91"+phoneNumber), new PhoneNumber(formPhoneNumber), String.valueOf(otp)).create();
        log.info("OTP sent Successfully !");
    }
}
