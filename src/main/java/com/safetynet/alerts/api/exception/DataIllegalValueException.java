package com.safetynet.alerts.api.exception;

import org.springframework.http.HttpStatus;

public class DataIllegalValueException extends ServiceException{
    public DataIllegalValueException(String dataName, String value){
        super("Illegal value '"+ value + "' for data '" + dataName + "'.");
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }
}