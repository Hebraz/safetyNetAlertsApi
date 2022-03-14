package com.safetynet.alerts.api.utils;

import com.safetynet.alerts.api.exception.DataIllegalValueException;

import java.util.Date;

public interface IAgeUtil {
    public int computeFromBirthdate(Date birthdate) throws DataIllegalValueException;
    public boolean isAdult(int age) ;
}

