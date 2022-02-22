package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.AlertsDataSource;
import com.safetynet.alerts.api.model.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
/**
 * Implementation of {@link com.safetynet.alerts.api.service.IMedicalRecordService} to get,
 * delete or save a person's medical record from/to a datasource.
 */
public class MedicalRecordService implements IMedicalRecordService {

    @Autowired
    private AlertsDataSource dataSource;

    /**
     * Get a person's medical record from a datasource.
     *
     * @param firstName first name of the person.
     * @param lastName last name of the person.
     *
     * @return the medical record if found.
     */
    @Override
    public Optional<MedicalRecord> getMedicalRecord(String firstName, String lastName) {
        List<MedicalRecord> medicalRecords = dataSource.getData().getMedicalrecords();
        Optional<MedicalRecord> medicalRecordResult =
                medicalRecords.stream()
                            .filter(m -> m.getFirstName().equalsIgnoreCase(firstName) &&
                                         m.getLastName().equalsIgnoreCase(lastName))
                            .findFirst();
        return medicalRecordResult;
    }
    /**
     * Delete a person's medical record from a datasource.
     *
     * @param firstName first name of the person.
     * @param lastName last name of the person.
     *
     * @throws IllegalArgumentException if medical record does not exist in the datasource. (No medical record
     * belonging to the given person has been found).
     *
     */
    @Override
    public void deleteMedicalRecord(String firstName, String lastName) {
        MedicalRecord medicalRecord;
        Optional<MedicalRecord> medicalRecordResult = getMedicalRecord(firstName, lastName);
        if(medicalRecordResult.isPresent()){
            medicalRecord = medicalRecordResult.get();
            dataSource.getData().getMedicalrecords().remove(medicalRecord);
        } else {
            throw new IllegalArgumentException("No medical record found for " + firstName + " " + lastName );
        }
    }
    /**
     * Save a person's medical record into a datasource. If a medical record already exists for the person
     * in the datasource (with same first/last names), it is updated. Else it is created
     * and added to the datasource.
     *
     * @param medicalRecordToSave medical record to save.
     *
     */
    @Override
    public void saveMedicalRecord(MedicalRecord medicalRecordToSave) {
        MedicalRecord medicalRecord;
        Optional<MedicalRecord> medicalRecordResult = getMedicalRecord(medicalRecordToSave.getFirstName(), medicalRecordToSave.getLastName());
        if(medicalRecordResult.isPresent()){
            medicalRecord = medicalRecordResult.get();
            medicalRecord.setBirthdate(medicalRecordToSave.getBirthdate());
            medicalRecord.setMedications(medicalRecordToSave.getMedications());
            medicalRecord.setAllergies(medicalRecordToSave.getAllergies());
        } else {
            medicalRecord = new MedicalRecord(medicalRecordToSave);
            dataSource.getData().getMedicalrecords().add(medicalRecord);
        }
    }
}
