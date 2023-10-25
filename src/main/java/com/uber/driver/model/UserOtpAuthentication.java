package com.uber.driver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import org.springframework.data.annotation.Id;

@RedisHash
@AllArgsConstructor
@Getter
@Setter
public class UserOtpAuthentication implements Serializable {
    @Id //for redis
    private String phoneNumber;
    private int otp;
}
