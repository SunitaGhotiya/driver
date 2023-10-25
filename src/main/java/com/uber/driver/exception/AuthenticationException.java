package com.uber.driver.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String errorMessage){
        super(errorMessage);
    }

}
