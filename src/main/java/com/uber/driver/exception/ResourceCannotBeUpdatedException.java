package com.uber.driver.exception;

public class ResourceCannotBeUpdatedException extends RuntimeException {

    public ResourceCannotBeUpdatedException(String errorMessage){
        super(errorMessage);
    }

}
