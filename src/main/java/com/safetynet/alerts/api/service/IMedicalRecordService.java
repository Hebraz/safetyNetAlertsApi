package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.Person;

import java.util.Optional;

/**
 * Get, delete or save a person's medical record from/to a datasource.
 */
public interface IMedicalRecordService {
    /**
     * Get a person's medical record from a datasource.
     *
     * @param firstName first name of the person.
     * @param lastName last name of the person.
     *
     * @return the medical record if found.
     */
    public Optional<MedicalRecord> getMedicalRecord(final String firstName, final String lastName);
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
    public void deleteMedicalRecord(final String firstName, final String lastName);
    /**
     * Save a person's medical record into a datasource. If a medical record already exists for the person
     * in the datasource (with same first/last names), it is updated. Else it is created
     * and added to the datasource.
     *
     * @param medicalRecordToSave medical record to save.
     *
     */
    public void saveMedicalRecord(MedicalRecord medicalRecordToSave);
}
