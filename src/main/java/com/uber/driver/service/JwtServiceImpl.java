package com.uber.driver.service;

import com.uber.driver.constant.DriverConstants;
import com.uber.driver.enums.DocumentStatus;
import com.uber.driver.enums.DriverComplianceStatus;
import com.uber.driver.exception.AuthenticationException;
import com.uber.driver.model.UserToken;
import com.uber.driver.reposiotry.UserTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtServiceImpl implements JwtService {

    @Value("${spring.security.key}")
    private String secret;

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Override
    public UserToken generateToken(String phoneNumber) {
        log.info("Generate token for phoneNumber : {}", phoneNumber);
        Map<String, Object> claims = new HashMap<>();
        String token =  createToken(claims, phoneNumber);
        UserToken userToken = new UserToken(phoneNumber, token);
        userTokenRepository.save(userToken);
        log.info("Token generated and saved in Cache ");
        return userToken;
    }

    private String createToken(Map<String, Object> claims, String phoneNumber) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Boolean validateToken(String token, String phoneNumber) {
        UserToken userToken = userTokenRepository.findById(phoneNumber)
                .orElseThrow(() -> new AuthenticationException(DriverConstants.INVALID_TOKEN));

        log.info("Checking if token is expired, then remove from cache");
        boolean isTokenExpired = isTokenExpired(token);
        if(isTokenExpired)
            userTokenRepository.deleteById(phoneNumber);

        log.info("Checking if token is same as stored in cache for the phoneNumber :{}" +
                "if the phoneNumber in token is same as stored" +
                "If the token is not expired : Then valid token !", phoneNumber);
        return (token.equals(userToken.getToken()) &&
                userToken.getPhoneNumber().equals(phoneNumber) &&
                !isTokenExpired);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
