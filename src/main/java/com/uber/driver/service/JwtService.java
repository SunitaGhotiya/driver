package com.uber.driver.service;

import com.uber.driver.model.UserToken;

import java.util.Date;

public interface JwtService {
    UserToken generateToken(String phoneNumber);
    String extractPhoneNumber(String token);
    Boolean validateToken(String token, String phoneNumber);
    Date extractExpiration(String token);
}
