package com.safetynet.alerts.api.utils;

import com.safetynet.alerts.api.exception.DataIllegalValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AgeTest {

    private final double NB_MS_BY_YEAR = 31556925260.6688;
    private IAgeUtil ageUtil;
    @BeforeEach
    void initializeTest(){
            ageUtil = new AgeUtil();
    }
    @ParameterizedTest(name="age = ''{0}''")
    @ValueSource(doubles = {0.5, 56.2, 199.9})
    void computeFromBirthdateBitrhBeforeNow(double expectedAge) throws DataIllegalValueException {
        Date now = new Date();
        Date birthDateBeforeNow = new Date(now.getTime() - (long)(expectedAge*NB_MS_BY_YEAR) );
        Integer age = ageUtil.computeFromBirthdate(birthDateBeforeNow);

        assertEquals((int)expectedAge, age);
    }

    @ParameterizedTest(name="age = ''{0}''")
    @ValueSource(doubles = {0, -1})
    void computeFromBirthdateBitrhAFterNow(double age) {
        Date now = new Date();
        Date birthDateAfterNow = new Date(now.getTime() - (long)(age*NB_MS_BY_YEAR) );
        assertThrows(DataIllegalValueException.class, () -> ageUtil.computeFromBirthdate(birthDateAfterNow));
    }

    @Test
    void isAdultTrue() {
        assertTrue(ageUtil.isAdult(19));
        assertTrue(ageUtil.isAdult(Integer.MAX_VALUE));
    }

    @Test
    void isAdultFalse() {
        assertFalse(ageUtil.isAdult(18));
        assertFalse(ageUtil.isAdult(0));
        assertFalse(ageUtil.isAdult(Integer.MIN_VALUE));
    }
}