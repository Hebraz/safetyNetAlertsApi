package com.safetynet.alerts.api.controller.uitls;

import org.springframework.http.HttpStatus;

public interface IRequestLogger {

    void logRequest(String request);

    void logResponseSuccess(HttpStatus httpStatus, String response);

    void logResponseFailure(HttpStatus httpStatus,String response);
}
