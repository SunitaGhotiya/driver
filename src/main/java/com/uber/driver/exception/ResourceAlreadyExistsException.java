package com.uber.driver.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }

}
