package com.uber.driver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverAuthenticationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String phoneNumber;
    private int otp;
    private String token;
}
