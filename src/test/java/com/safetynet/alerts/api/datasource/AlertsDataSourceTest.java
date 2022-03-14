package com.safetynet.alerts.api.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AlertsDataSourceTest {

    private AlertsDataSource alertsDataSource;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.FRENCH);

    @BeforeEach
    void initTest(){
        alertsDataSource = new AlertsDataSource();
    }

    @Test
    void loadNullParameter() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> alertsDataSource.load(null));
    }

    @Test
    void loadBlankParameter() throws IOException {
        assertThrows(IOException.class, () -> alertsDataSource.load(""));
    }

    @Test
    void loadNonexistentFile() throws IOException {
        assertThrows(IOException.class, () -> alertsDataSource.load("nonexistent.json"));
    }

    @Test
    void loadDataFileOk() throws IOException {
        alertsDataSource.load("data-test.json");

        //check
        IAlertsDataSource.Data data = alertsDataSource.getData();
        assertNotNull(data);
        //check persons
        assertEquals("Lily", data.getPersons().get(0).getFirstName());
        assertEquals("lily@email.com", data.getPersons().get(0).getEmail());
        //check fire station
        assertEquals("1509 Culver St", data.getFirestations().get(0).getAddress());
        assertEquals(3, data.getFirestations().get(0).getStation());
        //check medical record
        assertEquals("Roger", data.getMedicalrecords().get(3).getFirstName());
        assertEquals("09/06/2017", simpleDateFormat.format(data.getMedicalrecords().get(3).getBirthdate()));
    }
}