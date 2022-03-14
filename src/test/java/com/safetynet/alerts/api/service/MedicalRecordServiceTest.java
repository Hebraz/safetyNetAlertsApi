package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.dao.IMedicalRecordDao;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    private IMedicalRecordService medicalRecordService;
    @Mock
    private IMedicalRecordDao medicalRecordDao;

    @BeforeEach
    void initializeTest(){
        medicalRecordService = new MedicalRecordService(medicalRecordDao);
    }

    @Test
    void deleteMedicalRecord() throws DataNotFoundException {
        //ACT
        medicalRecordService.deleteMedicalRecord("Pierre","Paul");
        //CHECK
        verify(medicalRecordDao, times(1)).deleteMedicalRecord("Pierre","Paul");
    }

    @Test
    void updateMedicalRecord() throws DataNotFoundException {
        //PREPARE
        MedicalRecord medicalReturnedByDao = new MedicalRecord("Pierre","Paul", new Date(1561561), List.of("Doliprane:500mg","ibuprophene:100mg"), List.of("pollen"));
        when(medicalRecordDao.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalReturnedByDao);
        //ACT
        MedicalRecord medicalToUpdate = new MedicalRecord("Pierre","Paul", new Date(54589), List.of("ibuprophene:100mg"), List.of());
        MedicalRecord updatedMedical = medicalRecordService.updateMedicalRecord(medicalToUpdate);
        //CHECK
        verify(medicalRecordDao, times(1)).updateMedicalRecord(medicalToUpdate);
        assertThat(updatedMedical).isEqualTo(medicalReturnedByDao);
    }

    @Test
    void createMedicalRecord() throws DataAlreadyExistsException {
        //PREPARE
        MedicalRecord medicalReturnedByDao = new MedicalRecord("Pierre","Paul", new Date(1561561), List.of("Doliprane:500mg","ibuprophene:100mg"), List.of("pollen"));
        when(medicalRecordDao.createMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalReturnedByDao);
        //ACT
        MedicalRecord medicalToCreate = new MedicalRecord("Pierre","Paul", new Date(54589), List.of("ibuprophene:100mg"), List.of());
        MedicalRecord createdMedical = medicalRecordService.createMedicalRecord(medicalToCreate);
        //CHECK
        verify(medicalRecordDao, times(1)).createMedicalRecord(medicalToCreate);
        assertThat(createdMedical).isEqualTo(medicalReturnedByDao);
    }
}