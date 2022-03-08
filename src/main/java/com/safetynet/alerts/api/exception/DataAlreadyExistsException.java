package com.safetynet.alerts.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to throw when trying to create a data that already exists.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DataAlreadyExistsException extends ServiceException{

    public DataAlreadyExistsException(String dataName){
        super("Data '" + dataName + "' already exists !");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
