package com.uber.driver.service;

import com.uber.driver.exception.AuthenticationException;
import com.uber.driver.model.UserToken;
import com.uber.driver.reposiotry.UserTokenRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private UserTokenRepository userTokenRepository;

    private UserToken jwtToken;

    private String phoneNumber;

    @Before
    public void init(){
        phoneNumber = "9876543210";
        ReflectionTestUtils.setField(jwtService, "secret", "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437");
        jwtToken = jwtService.generateToken(phoneNumber);
    }

    @Test
    public void generateToken() {
        UserToken jwtToken = jwtService.generateToken(phoneNumber);
        Assert.assertNotNull(jwtToken);
        Assert.assertNotNull(jwtToken.getToken());
    }

    @Test
    public void extractPhoneNumber() {
        String phoneNumberFromToken = jwtService.extractPhoneNumber(jwtToken.getToken());
        Assert.assertEquals(phoneNumber, phoneNumberFromToken);
    }

    @Test
    public void validateToken_validToken() {
        Mockito.when(userTokenRepository.findById(Mockito.anyString())).thenReturn(Optional.of(jwtToken));
        Assert.assertTrue(jwtService.validateToken(jwtToken.getToken(), phoneNumber));
    }

    @Test(expected = AuthenticationException.class)
    public void validateToken_NoTokenInCache() {
        Mockito.when(userTokenRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        Assert.assertFalse(jwtService.validateToken(jwtToken.getToken(), ""));
    }


    @Test
    public void validateToken_TokenIsNotSameAsStoredInCache() {
        Mockito.when(userTokenRepository.findById(Mockito.anyString())).thenReturn(Optional.of(jwtToken));
        Assert.assertFalse(jwtService.validateToken("dummyToken", phoneNumber));
    }

    @Test
    public void validateToken_ExpiredToken() {
        Mockito.when(userTokenRepository.findById(Mockito.anyString())).thenReturn(Optional.of(jwtToken));
        JwtService jwtServiceSpy = Mockito.spy(jwtService);
        Mockito.doReturn(new Date(System.currentTimeMillis() - 1000)).when(jwtServiceSpy).extractExpiration(jwtToken.getToken());

        Assert.assertFalse(jwtServiceSpy.validateToken(jwtToken.getToken(), phoneNumber));
    }

    @Test
    public void extractExpiration() {
        Assert.assertTrue(jwtService.extractExpiration(jwtToken.getToken()).after(new Date(System.currentTimeMillis())));
    }

}
