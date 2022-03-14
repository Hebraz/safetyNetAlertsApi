package com.safetynet.alerts.api.dao;

import com.safetynet.alerts.api.StubbedData;
import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Calendar.DECEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import java.text.ParseException;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalRecordDaoTest {
    private IAlertsDataSource.Data stubbedData;
    private IMedicalRecordDao medicalRecordDao;
    @Mock
    private IAlertsDataSource dataSource;

    @BeforeEach
    void initializeTest() throws ParseException {
        medicalRecordDao = new MedicalRecordDao(dataSource);
        stubbedData = StubbedData.get();
    }

    @Test
    void getMedicalRecordExistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        Optional<MedicalRecord> medicalRecord = medicalRecordDao.getMedicalRecord("Eric", "Cadigan");
        //CHECK
        assertTrue(medicalRecord.isPresent());
        assertThat(medicalRecord.get().getMedications()).containsExactly("tradoxidine:400mg");
    }

    @Test
    void getMedicalRecordNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        Optional<MedicalRecord> medicalRecord = medicalRecordDao.getMedicalRecord("Erica", "Cadigan");
        //CHECK
        assertTrue(medicalRecord.isEmpty());
    }

    @Test
    void deleteMedicalRecordExistent() throws DataNotFoundException {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        MedicalRecord medicalRecord = stubbedData.getMedicalrecords().get(5); //Jonanathan Marrack
        //PRECHECK
        assertThat(stubbedData.getMedicalrecords()).contains(medicalRecord);
        //ACT
        medicalRecordDao.deleteMedicalRecord("Jonanathan", "Marrack");
        //CHECK
        assertThat(stubbedData.getMedicalrecords()).doesNotContain(medicalRecord);
    }
    @Test
    void deleteMedicalRecordNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        assertThrows(DataNotFoundException.class,() ->  medicalRecordDao.deleteMedicalRecord("Erica", "Cadigan"));
    }

    @Test
    void updateMedicalRecordExistent() throws DataNotFoundException {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //PRE CHECK

        //ACT
        MedicalRecord medicalRecord = new MedicalRecord("Roger","Boyd",new Date(1231213), List.of("Aspirine:200mg","thradox:700mg" ), List.of("peanut","poller"));
        MedicalRecord updatedMedicalRecord = medicalRecordDao.updateMedicalRecord(medicalRecord);
        //CHECK
        MedicalRecord medicalRecordToUpgate = stubbedData.getMedicalrecords().get(3);
        assertThat(medicalRecordToUpgate)
                .extracting(
                    MedicalRecord::getFirstName,
                    MedicalRecord::getLastName,
                    MedicalRecord::getBirthdate,
                    MedicalRecord::getMedications,
                    MedicalRecord::getAllergies)
                .containsExactly(
                        "Roger",
                        "Boyd",
                        new Date(1231213),
                        List.of("Aspirine:200mg","thradox:700mg" ),
                        List.of("peanut","poller")
                );
        assertEquals(medicalRecordToUpgate , updatedMedicalRecord);
    }


    @Test
    void updateMedicalRecordNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        MedicalRecord medicalRecord = new MedicalRecord("Eric","Cadigun",new Date(), List.of("Aspirine:200mg"), List.of());
        //ACT
        assertThrows(DataNotFoundException.class,() -> medicalRecordDao.updateMedicalRecord(medicalRecord));
    }

    @Test
    void createMedicalRecordExistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        MedicalRecord medicalRecord = new MedicalRecord("Eric","Cadigan",new Date(), List.of("Aspirine:200mg"), List.of());
        assertThrows(DataAlreadyExistsException.class,() -> medicalRecordDao.createMedicalRecord(medicalRecord));
    }

    @Test
    void createMedicalRecordNonexistent() throws DataAlreadyExistsException {
        List<MedicalRecord> medicalRecords = stubbedData.getMedicalrecords();
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //PRECHECK
        assertThat(medicalRecords.stream().count()).isEqualTo(23);
        //ACT
        MedicalRecord medicalRecord = new MedicalRecord("Pierre","Roger",new Date(12315648), List.of("Aspirine:200mg","thradox:700mg" ), List.of("peanut","poller"));
        MedicalRecord createdMediaRecord = medicalRecordDao.createMedicalRecord(medicalRecord);
        //CHECK
        assertThat(medicalRecords.stream().count()).isEqualTo(24);
        assertThat(medicalRecords.get(23))
                .extracting(
                        MedicalRecord::getFirstName,
                        MedicalRecord::getLastName,
                        MedicalRecord::getBirthdate,
                        MedicalRecord::getMedications,
                        MedicalRecord::getAllergies)
                .containsExactly(
                        "Pierre",
                        "Roger",
                        new Date(12315648),
                        List.of("Aspirine:200mg","thradox:700mg" ),
                        List.of("peanut","poller")
                );
        assertEquals(createdMediaRecord , medicalRecords.get(23));
    }

    @Test
    void getPersonBirthdateExistent() throws DataNotFoundException {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        Date birth = medicalRecordDao.getPersonBirthdate("Brian", "Stelzer");
        //CHECK
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birth);
        assertThat(calendar.get(Calendar.YEAR)).isEqualTo(1975);
        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(DECEMBER);
        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(6);
    }

    @Test
    void getPersonBirthdateNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        assertThrows(DataNotFoundException.class,() -> medicalRecordDao.getPersonBirthdate("BrianI", "Stelzer"));
    }
}