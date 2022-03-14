package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MedicalRecordServiceITest {

    @Autowired
    IAlertsDataSource alertsDataSource;
    @Autowired
    IMedicalRecordService medicalRecordService;

    private MedicalRecord medicalRecordToSave;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.FRENCH);


    @BeforeEach
    public void loadData() throws IOException {
        alertsDataSource.load("data-test.json");

        medicalRecordToSave = new MedicalRecord();
        medicalRecordToSave.setFirstName("xxxx");
        medicalRecordToSave.setLastName("yyyy");
        medicalRecordToSave.setBirthdate(new Date());
        medicalRecordToSave.setMedications(Arrays.asList("med1:200mg", "med2:100mg"));
        medicalRecordToSave.setAllergies(Arrays.asList("allergy1", "allergy2", "allergy3"));
    }


    @Test
    void deleteMedicalRecordNullFirstName() {
        assertThrows(DataNotFoundException.class,() -> medicalRecordService.deleteMedicalRecord(null,"Boyd"));
    }
/*
    @Test
    void deleteMedicalRecordNullLastName() {
        assertThrows(IllegalArgumentException.class,() -> medicalRecordService.deleteMedicalRecord("Jacob",null));
    }

    @Test
    void deleteMedicalRecordUnknownFirstName() {
        assertThrows(IllegalArgumentException.class,() -> medicalRecordService.deleteMedicalRecord("Unkonwn","Boyd"));
    }

    @Test
    void deleteMedicalRecordUnknownLastName() {
        assertThrows(IllegalArgumentException.class,() -> medicalRecordService.deleteMedicalRecord("Jacob","unknown"));
    }

    @Test
    void deleteMedicalRecordOk() {
        //check number of medical records
        assertEquals(4, alertsDataSource.getData().getMedicalrecords().stream().count());

        //Act
        assertDoesNotThrow(()-> medicalRecordService.deleteMedicalRecord("Jacob", "Boyd"));

        //check number of medical records has decreased
        assertEquals(3, alertsDataSource.getData().getMedicalrecords().stream().count());
        //try to delete person another time shall throw exception
        assertThrows(IllegalArgumentException.class,() -> medicalRecordService.deleteMedicalRecord("Jacob", "Boyd"));

    }

    @Test
    void saveMedicalRecordAddNew() {
        medicalRecordToSave.setFirstName("Roger");
        medicalRecordToSave.setLastName("Moore");

        //check count before
        assertEquals(4, alertsDataSource.getData().getMedicalrecords().stream().count());
        //Act
        assertDoesNotThrow(()-> medicalRecordService.createMedicalRecord(medicalRecordToSave));
        //check count after
        MedicalRecord addedMedicalRecord =   alertsDataSource.getData().getMedicalrecords().get(4);
        assertEquals(5, alertsDataSource.getData().getMedicalrecords().stream().count());
        assertEquals("Roger", addedMedicalRecord.getFirstName());
        assertEquals("Moore", addedMedicalRecord.getLastName());
        assertEquals(simpleDateFormat.format(new Date()), simpleDateFormat.format(addedMedicalRecord.getBirthdate()));
        assertIterableEquals(new ArrayList<String>(Arrays.asList("med1:200mg", "med2:100mg")), addedMedicalRecord.getMedications());
        assertIterableEquals(new ArrayList<String>(Arrays.asList("allergy1", "allergy2", "allergy3")), addedMedicalRecord.getAllergies());
    }

    @Test
    void saveMedicalRecordUpdateExisting() {
        medicalRecordToSave.setFirstName("Tenley");
        medicalRecordToSave.setLastName("Boyd");

        //check count before
        assertEquals(4, alertsDataSource.getData().getMedicalrecords().stream().count());
        //Act
        assertDoesNotThrow(()-> medicalRecordService.updateMedicalRecord(medicalRecordToSave));
        //check count after
        MedicalRecord updatedMedicalRecord =   alertsDataSource.getData().getMedicalrecords().get(2);
        assertEquals(4, alertsDataSource.getData().getMedicalrecords().stream().count());
        assertEquals("Tenley",updatedMedicalRecord.getFirstName());
        assertEquals("Boyd", updatedMedicalRecord.getLastName());
        assertEquals(simpleDateFormat.format(new Date()), simpleDateFormat.format(updatedMedicalRecord.getBirthdate()));
        assertIterableEquals(new ArrayList<String>(Arrays.asList("med1:200mg", "med2:100mg")), updatedMedicalRecord.getMedications());
        assertIterableEquals(new ArrayList<String>(Arrays.asList("allergy1", "allergy2", "allergy3")), updatedMedicalRecord.getAllergies());
    }

 */
}