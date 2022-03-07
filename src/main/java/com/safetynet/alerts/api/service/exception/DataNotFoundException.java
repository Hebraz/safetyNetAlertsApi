package com.safetynet.alerts.api.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to throw when trying to access (get/delete/update) a
 * data that do not exist.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends ServiceException{
    public DataNotFoundException(String dataName){
        super("Data '" + dataName + "' does not exist !");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
