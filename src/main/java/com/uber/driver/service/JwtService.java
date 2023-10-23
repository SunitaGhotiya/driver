package com.uber.driver.service;

public interface JwtService {
    String generateToken(String phoneNumber);
    String extractPhoneNumber(String token);
    Boolean validateToken(String token, String phoneNumber);
}
