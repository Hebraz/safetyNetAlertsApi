package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.datasource.AlertsDataSource;
import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FireStationServiceTest {

    @Autowired
    IAlertsDataSource alertsDataSource;
    @Autowired
    IFireStationService fireStationService;

    @BeforeEach
    public void loadData() throws IOException {
        alertsDataSource.load("data-test.json");
    }

    @Test
    void getFireStationUnknownAddress() throws IOException {
        Optional<FireStation> fireStation;
        fireStation = fireStationService.getFireStation("645 Gershwin Cir");

        assertFalse(fireStation.isPresent());
    }

    @Test
    void getFireStationNullAddress() throws IOException {
        Optional<FireStation> fireStation;
        fireStation = fireStationService.getFireStation(null);

        assertFalse(fireStation.isPresent());
    }

    @Test
    void getFireStationKnownAddress() throws IOException {
        Optional<FireStation> fireStation;
        fireStation = fireStationService.getFireStation("834 Binoc Ave");

        assertTrue(fireStation.isPresent());
        assertEquals(3, fireStation.get().getStation());
        assertEquals("834 Binoc Ave", fireStation.get().getAddress());
    }

    @Test
    void deleteFireStationUnknownAddress() throws IOException {
        assertThrows(IllegalArgumentException.class,() -> fireStationService.deleteFireStation("834 Binoc Av"));
    }

    @Test
    void deleteFireStationNullAddress() throws IOException {
        assertThrows(IllegalArgumentException.class,() -> fireStationService.deleteFireStation(null));
    }

    @Test
    void deleteFireStationKnown() throws IOException {
       //check number of firestation
        assertEquals(4, alertsDataSource.getData().getFirestations().stream().count());

        //Act
        assertDoesNotThrow(()-> fireStationService.deleteFireStation("834 Binoc Ave"));

        //check number of firestation has decreased
        assertEquals(3, alertsDataSource.getData().getFirestations().stream().count());
        //try to delete station another time shall throw exception
        assertThrows(IllegalArgumentException.class,() -> fireStationService.deleteFireStation("834 Binoc Ave"));
    }

    @Test
    void saveFireStationAddNewStation(){
        //check number of firestation
        assertEquals(4, alertsDataSource.getData().getFirestations().stream().count());

        FireStation fireStation = new FireStation();
        fireStation.setAddress("547 Rue General de Gaulle");
        fireStation.setStation(145);

        //Act
        assertDoesNotThrow(()-> fireStationService.saveFireStation(fireStation));

        //check
        assertEquals(5, alertsDataSource.getData().getFirestations().stream().count());
        assertEquals(145, alertsDataSource.getData().getFirestations().get(4).getStation());
        assertEquals("547 Rue General de Gaulle", alertsDataSource.getData().getFirestations().get(4).getAddress());
    }

    @Test
    void saveFireStationUpdateExistingStation(){
        //check number of firestation
        assertEquals(4, alertsDataSource.getData().getFirestations().stream().count());
        assertEquals(2, alertsDataSource.getData().getFirestations().get(1).getStation());
        assertEquals("29 15th St", alertsDataSource.getData().getFirestations().get(1).getAddress());

        FireStation fireStation = new FireStation();
        fireStation.setAddress("29 15th St");
        fireStation.setStation(15);

        //Act
        assertDoesNotThrow(()-> fireStationService.saveFireStation(fireStation));

        //check
        assertEquals(4, alertsDataSource.getData().getFirestations().stream().count());
        assertEquals(15, alertsDataSource.getData().getFirestations().get(1).getStation());
        assertEquals("29 15th St", alertsDataSource.getData().getFirestations().get(1).getAddress());
    }
}