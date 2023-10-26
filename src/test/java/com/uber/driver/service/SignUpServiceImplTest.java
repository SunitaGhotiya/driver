package com.uber.driver.service;

import com.uber.driver.exception.AuthenticationException;
import com.uber.driver.model.UserOtpAuthentication;
import com.uber.driver.model.UserToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SignUpServiceImplTest {

    @InjectMocks
    private SignUpServiceImpl signUpService;

    @Mock
    private OtpService otpService;

    @Mock
    private SendOtpService sendOtpService;

    @Mock
    private JwtService jwtService;

    @Captor
    ArgumentCaptor<String> phoneNumberCaptor;

    @Captor
    ArgumentCaptor<Integer> otpCaptor;

    private String phoneNumber;

    private UserOtpAuthentication userOtpAuthentication;

    @Before
    public void init(){
        phoneNumber = "9876543210";
        userOtpAuthentication = new UserOtpAuthentication("123",2111);
    }

    @Test
    public void generateAndSendOTP() {
        Mockito.when(otpService.generateOTP(Mockito.anyString())).thenReturn(userOtpAuthentication);
        UserOtpAuthentication userOtpAuthenticationResponse = signUpService.generateAndSendOTP(phoneNumber);

        verify(sendOtpService).sendOTP(phoneNumberCaptor.capture(), otpCaptor.capture());

        Assert.assertEquals(phoneNumber, phoneNumberCaptor.getValue());
        Assert.assertEquals(userOtpAuthentication.getOtp(), otpCaptor.getValue().intValue());
        Assert.assertEquals(userOtpAuthentication, userOtpAuthenticationResponse);

    }

    @Test
    public void validateOTPAndGenerateToken_validOtp() {
        UserToken userToken = new UserToken(phoneNumber, "token");
        Mockito.when(otpService.validateOTP(Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(jwtService.generateToken(Mockito.anyString())).thenReturn(userToken);

        Assert.assertEquals(userToken, signUpService.validateOTPAndGenerateToken(phoneNumber, 1234));
    }

    @Test(expected = AuthenticationException.class)
    public void validateOTPAndGenerateToken_InvalidOtp() {
        Mockito.when(otpService.validateOTP(Mockito.anyString(), Mockito.anyInt())).thenReturn(false);
        signUpService.validateOTPAndGenerateToken(phoneNumber, 1234);
    }
}
