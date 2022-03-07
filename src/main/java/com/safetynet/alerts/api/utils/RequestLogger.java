package com.safetynet.alerts.api.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class RequestLogger implements IRequestLogger {

    private String request;

    @Override
    public void logRequest(String request) {
        this.request = request;
        log.info("Request : '" + request + "'");
    }

    @Override
    public void logResponseSuccess(HttpStatus httpStatus, String response) {
        log.info("SUCCES ! Response to request : '" + this.request + "' : " + response);
    }

    @Override
    public void logResponseFailure(HttpStatus httpStatus,String response) {
        log.error("FAILURE ! Response to request : '" + this.request + "' : " + response);
    }
}
