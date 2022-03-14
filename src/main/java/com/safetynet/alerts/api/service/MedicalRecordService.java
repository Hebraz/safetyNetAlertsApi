package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.dao.IMedicalRecordDao;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.MedicalRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link IMedicalRecordService} to get,
 * delete or save a person's medical record from/to a datasource.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalRecordService implements IMedicalRecordService {

    private final IMedicalRecordDao medicalRecordDao;

    /**
     * Delete a person's medical record from a datasource.
     *
     * @param firstName first name of the person.
     * @param lastName last name of the person.
     * @throws DataNotFoundException if medical record does not exist in the datasource. (No medical record
     * belonging to the given person has been found).
     *
     */
    @Override
    public void deleteMedicalRecord(String firstName, String lastName) throws DataNotFoundException {
        medicalRecordDao.deleteMedicalRecord(firstName,lastName);
    }
    /**
     * Update an existing person's medical record into a datasource.
     *
     * @param medicalRecordToUpdate medical record to update.
     * @return updated medical record
     * @throws DataNotFoundException if medical record does not exist in the datasource. (No medical record
     *           belonging to the given person has been found).
     */
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecordToUpdate) throws DataNotFoundException {
        return medicalRecordDao.updateMedicalRecord(medicalRecordToUpdate);
    }
    /**
     * Create a person's medical record into a datasource.
     *
     * @param medicalRecordToCreate medical record to create.
     * @return created medical record.
     * @throws DataAlreadyExistsException if medical record already exists in datasource.
     */
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecordToCreate) throws DataAlreadyExistsException {
        return medicalRecordDao.createMedicalRecord(medicalRecordToCreate);
    }


}
