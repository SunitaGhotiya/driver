package com.uber.driver.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PhoneNumberAuthenticationToken extends AbstractAuthenticationToken {
    private String phoneNumber;

    @Value("${spring.security.key}")
    private String secret;

    public PhoneNumberAuthenticationToken(String phoneNumber){
        super((Collection)null);
        this.phoneNumber = phoneNumber;
    }

    public PhoneNumberAuthenticationToken(String phoneNumber, Collection<GrantedAuthority> authorities){
        super(authorities);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Object getCredentials() {
        return this.phoneNumber;
    }

    @Override
    public Object getPrincipal() {
        return this.secret;
    }
}
