package com.safetynet.alerts.api.utils;

import com.safetynet.alerts.api.exception.DataIllegalValueException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
@Component
public class AgeUtil implements IAgeUtil {
    private static final int CHILD_AGE_LIMIT = 18;

    public int computeFromBirthdate(Date birthdate) throws DataIllegalValueException {
        LocalDate birthDateLocalDate = birthdate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate now = LocalDate.now();
        if(birthDateLocalDate.isBefore(now)){
            return Period.between(birthDateLocalDate, now).getYears();
        } else {
            throw new DataIllegalValueException("Birthdate if after now(" + now.toString() + ")", birthDateLocalDate.toString());
        }
    }

    public boolean isAdult(int age)  {
        if(age > CHILD_AGE_LIMIT){
            return true;
        } else {
            return false;
        }
    }
}
