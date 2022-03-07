package com.safetynet.alerts.api.service.exception;

import org.springframework.http.HttpStatus;

public abstract class ServiceException extends Exception{
    public abstract HttpStatus getHttpStatus();

    ServiceException(String description){
        super(description);
    }
}
