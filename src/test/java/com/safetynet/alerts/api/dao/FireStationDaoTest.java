package com.safetynet.alerts.api.dao;

import com.safetynet.alerts.api.StubbedData;
import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FireStationDaoTest {

    private IAlertsDataSource.Data stubbedData;
    private IFireStationDao fireStationDao;
    @Mock
    private IAlertsDataSource dataSource;

    @BeforeEach
    void initializeTest() throws ParseException {
        fireStationDao = new FireStationDao(dataSource);
        stubbedData = StubbedData.get();
    }

    @Test
    void getFireStationExistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        Optional<FireStation> fireStationResult = fireStationDao.getFireStation("112 Steppes Pl");
        //CHECK
        assertTrue(fireStationResult.isPresent());
        assertEquals(3, fireStationResult.get().getStation());
    }
    @Test
    void getFireStationNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        Optional<FireStation> fireStationResult = fireStationDao.getFireStation("1154 avenue Charles de Gaulle");
        //CHECK
        assertTrue(fireStationResult.isEmpty());
    }

    @Test
    void getAddressesNonexistentStation() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        List<String> addresses = fireStationDao.getAddresses(5);
        //CHECK
        assertTrue(addresses.isEmpty());
    }
    @Test
    void getAddressesExistentStation() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        List<String> addresses = fireStationDao.getAddresses(3);
        //CHECK
        assertFalse(addresses.isEmpty());
        assertThat(addresses)
                .containsExactly(
                        "1509 Culver St",
                        "834 Binoc Ave",
                        "748 Townings Dr",
                        "112 Steppes Pl",
                        "748 Townings Dr");
    }

    @Test
    void deleteFireStationNonexistent() throws DataNotFoundException {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        assertThrows(DataNotFoundException.class,() -> fireStationDao.deleteFireStation("1154 avenue Charles de Gaulle"));
    }

    @Test
    void deleteFireStationExistent() throws DataNotFoundException {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        FireStation fireStationToDelete = stubbedData.getFirestations().get(2); //"834 Binoc Ave" , number 3
        //ACT
        fireStationDao.deleteFireStation("834 Binoc Ave");
        //CHECK
        assertThat(stubbedData.getFirestations()).doesNotContain(fireStationToDelete);
    }

    @Test
    void updateFireStationNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        FireStation fireStationToUpdate = new FireStation("Unknown Address", 3);
          //ACT
        assertThrows(DataNotFoundException.class,() -> fireStationDao.updateFireStation(fireStationToUpdate));
    }

    @Test
    void updateFireStationExistent() throws DataNotFoundException {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //PRE CHECK
        FireStation fireStationToUpdate = stubbedData.getFirestations().get(9);
        assertThat(fireStationToUpdate.getAddress()).isEqualTo("947 E. Rose Dr");
        assertThat(fireStationToUpdate.getStation()).isEqualTo(1);
        //ACT
        FireStation fireStationNewMapping = new FireStation("947 E. Rose Dr", 2);
        FireStation updatedStation = fireStationDao.updateFireStation(fireStationNewMapping);
        //CHECK
        assertThat(fireStationToUpdate.getAddress()).isEqualTo("947 E. Rose Dr");
        assertThat(fireStationToUpdate.getStation()).isEqualTo(2);
        assertEquals(fireStationToUpdate, updatedStation);
    }

    @Test
    void createFireStationAlreadyExist() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        FireStation fireStationToCreate = new FireStation("951 LoneTree Rd", 2);
        assertThrows(DataAlreadyExistsException.class,() -> fireStationDao.createFireStation(fireStationToCreate));
    }

    @Test
    void createFireStationNewStation() throws DataAlreadyExistsException {
        List<FireStation> fireStations = stubbedData.getFirestations();
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //PRECHECK
        assertThat(fireStations.stream().count()).isEqualTo(12);
        //ACT
        FireStation fireStationToCreate = new FireStation("1154 avenue Charles de Gaulle", 2);
        FireStation fireStationCreated = fireStationDao.createFireStation(fireStationToCreate);
        //CHECK
        assertThat(fireStations.stream().count()).isEqualTo(13);
        assertThat(fireStations.get(12).getAddress()).isEqualTo("1154 avenue Charles de Gaulle");
        assertThat(fireStations.get(12).getStation()).isEqualTo(2);
        assertThat(fireStationCreated).isEqualTo(fireStations.get(12));
    }

    @Test
    void getFireStationNumberNonExistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        assertThrows(DataNotFoundException.class,() -> fireStationDao.getFireStationNumber("Unknown address"));
    }

    @Test
    void getFireStationNumberExistent() throws DataNotFoundException {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        int stationNumber = fireStationDao.getFireStationNumber("951 LoneTree Rd");
        //CHECK
        assertEquals(2, stationNumber);
    }
}