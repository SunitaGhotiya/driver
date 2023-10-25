package com.uber.driver.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
}
