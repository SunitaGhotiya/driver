//package com.uber.driver.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@RunWith(MockitoJUnitRunner.class)
//public class JwtServiceImplTest {
//
//    @InjectMocks
//    private JwtServiceImpl jwtService;
//
//    private String token;
//
//    private String phoneNumber;
//
//    @Before
//    public void init(){
//        phoneNumber = "9876543210";
//        ReflectionTestUtils.setField(jwtService, "secret", "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437");
//        token = jwtService.generateToken(phoneNumber);
//    }
//
//    @Test
//    public void generateToken() {
//        String jwtToken = jwtService.generateToken(phoneNumber);
//        Assert.assertNotNull(jwtToken);
//    }
//
//    @Test
//    public void extractPhoneNumber() {
//        String phoneNumberFromToken = jwtService.extractPhoneNumber(token);
//        Assert.assertEquals(phoneNumber, phoneNumberFromToken);
//    }
//
//    @Test
//    public void validateToken_validToken() {
//        Assert.assertTrue(jwtService.validateToken(token, phoneNumber));
//    }
//
//    @Test
//    public void validateToken_InvalidToken() {
//        Assert.assertFalse(jwtService.validateToken(token, ""));
//    }
//
//    @Test
//    public void validateToken_ExpiredToken() {
//        JwtService jwtServiceSpy = Mockito.spy(jwtService);
//        Mockito.doReturn(new Date(System.currentTimeMillis() - 1000)).when(jwtServiceSpy).extractExpiration(token);
//
//        Assert.assertFalse(jwtServiceSpy.validateToken(token, phoneNumber));
//    }
//
//    @Test
//    public void extractExpiration() {
//        Assert.assertTrue(jwtService.extractExpiration(token).after(new Date(System.currentTimeMillis())));
//    }
//
//}
