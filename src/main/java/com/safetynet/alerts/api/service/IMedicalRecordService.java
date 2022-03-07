package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.model.MedicalRecord;

import java.util.Optional;

import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.service.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.service.exception.DataIllegalValueException;
import com.safetynet.alerts.api.service.exception.DataNotFoundException;
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
     * @throws DataNotFoundException if medical record does not exist in the datasource. (No medical record
     * belonging to the given person has been found).
     *
     */
    public void deleteMedicalRecord(final String firstName, final String lastName) throws DataNotFoundException;
    /**
     * Update an existing person's medical record into a datasource.
     *
     * @param medicalRecordToUpdate medical record to update.
     *
     * @return updated medical record.
     *
     * @throws DataNotFoundException if medical record does not exist in the datasource. (No medical record
     *           belonging to the given person has been found).
     */
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecordToUpdate) throws DataNotFoundException;
    /**
     * Create a person's medical record into a datasource.
     *
     * @param medicalRecordToCreate medical record to create.
     *
     * @return created medical record.
     *
     * @throws DataAlreadyExistsException if medical record already exists in datasource
     */
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecordToCreate) throws DataAlreadyExistsException;
    /**
     * Get the age of a person.
     *
     * @param firstName first name of the person.
     * @param lastName last name of the person.
     * @return age of the person
     * @throws DataNotFoundException if medical record of the person does not exist in the datasource.
     */
    public long getPersonAge(String firstName, String lastName) throws DataNotFoundException, DataIllegalValueException;
}
