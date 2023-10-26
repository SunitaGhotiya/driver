package com.uber.driver.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String errorMessage){
        super(errorMessage);
    }

}