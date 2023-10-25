//package com.uber.driver.service;
//
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Value;
//
//import static org.mockito.Mockito.mockStatic;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SendOtpServiceImplTest {
//
//    @InjectMocks
//    private SendOtpServiceImpl sendOtpService;
//
//    private String accountSid;
//
//    private String authToken;
//
//    private String formPhoneNumber;
//
//    @Test
//    public void sendOTP() {
//
//        try (MockedStatic mocked = mockStatic(Twilio.class)) {
//            sendOtpService.sendOTP("9876543210", 1234);
//            mocked.when(() -> Twilio.init(accountSid, authToken));
//            mocked.verify(() -> Twilio.init(accountSid, authToken));
//        }
//    }
//}
