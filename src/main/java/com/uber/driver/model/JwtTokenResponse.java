package com.uber.driver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class JwtTokenResponse {
    private String phoneNumber;
    private String token;
}
