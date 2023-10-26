package com.uber.driver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;
import java.io.Serializable;

@RedisHash(timeToLive = 18000L)
@AllArgsConstructor
@Getter
@Setter
public class UserToken implements Serializable {
    @Id //for redis
    private String phoneNumber;
    private String token;
}
