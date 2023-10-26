package com.uber.driver.service;

import com.uber.driver.exception.BadRequestException;
import com.uber.driver.exception.ResourceNotFoundException;
import com.uber.driver.model.UserOtpAuthentication;
import com.uber.driver.reposiotry.UserOtpAuthenticationRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class OtpServiceImplTest{

    @InjectMocks
    private OtpServiceImpl otpService;

    @Mock
    private UserOtpAuthenticationRepository userOtpAuthenticationRepository;

    private String phoneNumber = "9588975483";

    @Test
    public void generateOTP_validPhoneNumber() {
        Assert.assertNotEquals(0, otpService.generateOTP(phoneNumber));
    }

    @Test(expected = BadRequestException.class)
    public void generateOTP_invalidPhoneNumberFormat() {
        Assert.assertNotEquals(0, otpService.generateOTP("abcd"));
    }

    @Test(expected = BadRequestException.class)
    public void generateOTP_invalidPhoneNumber() {
        Assert.assertNotEquals(0, otpService.generateOTP("0101010101"));
    }


    @Test
    public void validateOTP_ValidOtp() {
        Mockito.when(userOtpAuthenticationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(new UserOtpAuthentication(phoneNumber, 1234)));
        Assert.assertTrue(otpService.validateOTP(phoneNumber, 1234));
    }

    @Test
    public void validateOTP_NoOtp() {
        Mockito.when(userOtpAuthenticationRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Assert.assertFalse(otpService.validateOTP(phoneNumber, 1234));
    }

    @Test
    public void validateOTP_InValidOtp() {
        Mockito.when(userOtpAuthenticationRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(new UserOtpAuthentication(phoneNumber, 1234)));
        Assert.assertFalse(otpService.validateOTP(phoneNumber, 1212));
    }

}
