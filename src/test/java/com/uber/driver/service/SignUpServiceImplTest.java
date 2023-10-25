//package com.uber.driver.service;
//
//import com.uber.driver.constant.DriverConstants;
//import com.uber.driver.enums.DriverComplianceStatus;
//import com.uber.driver.model.DriverDocument;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.*;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//import static org.mockito.Mockito.verify;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SignUpServiceImplTest {
//
//    @InjectMocks
//    private SignUpServiceImpl signUpService;
//
//    @Mock
//    private OtpService otpService;
//
//    @Mock
//    private SendOtpService sendOtpService;
//
//    @Mock
//    private JwtService jwtService;
//
//    @Captor
//    ArgumentCaptor<String> phoneNumberCaptor;
//
//    @Captor
//    ArgumentCaptor<Integer> otpCaptor;
//
//    private String phoneNumber = "987654321";
//    @Test
//    public void generateAndSendOTP() {
//        Mockito.when(otpService.generateOTP(Mockito.anyString())).thenReturn(1234);
//        String otpGenerateResponse = signUpService.generateAndSendOTP(phoneNumber);
//
//        verify(sendOtpService).sendOTP(phoneNumberCaptor.capture(), otpCaptor.capture());
//
//        Assert.assertEquals(phoneNumber, phoneNumberCaptor.getValue());
//        Assert.assertEquals(1234, otpCaptor.getValue().intValue());
//        Assert.assertEquals(DriverConstants.OTP_GENERATED_SUCCESS, otpGenerateResponse);
//
//    }
//
//    @Test
//    public void validateOTPAndGenerateToken_validOtp() {
//        Mockito.when(otpService.validateOTP(Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
//        Mockito.when(jwtService.generateToken(Mockito.anyString())).thenReturn("token");
//
//        Assert.assertEquals("token", signUpService.validateOTPAndGenerateToken(phoneNumber, 1234));
//    }
//
//    @Test
//    public void validateOTPAndGenerateToken_InvalidOtp() {
//        Mockito.when(otpService.validateOTP(Mockito.anyString(), Mockito.anyInt())).thenReturn(false);
//        Assert.assertEquals(DriverConstants.OTP_INVALID, signUpService.validateOTPAndGenerateToken(phoneNumber, 1234));
//
//    }
//}
