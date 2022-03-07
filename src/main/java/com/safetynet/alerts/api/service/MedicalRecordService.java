package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.service.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.service.exception.DataIllegalValueException;
import com.safetynet.alerts.api.service.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.MedicalRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * Implementation of {@link com.safetynet.alerts.api.service.IMedicalRecordService} to get,
 * delete or save a person's medical record from/to a datasource.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalRecordService implements IMedicalRecordService {

    private final IAlertsDataSource dataSource;

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
     * @throws DataNotFoundException if medical record does not exist in the datasource. (No medical record
     * belonging to the given person has been found).
     *
     */
    @Override
    public void deleteMedicalRecord(String firstName, String lastName) throws DataNotFoundException {
        MedicalRecord medicalRecord;
        Optional<MedicalRecord> medicalRecordResult = getMedicalRecord(firstName, lastName);
        if(medicalRecordResult.isPresent()){
            medicalRecord = medicalRecordResult.get();
            dataSource.getData().getMedicalrecords().remove(medicalRecord);
        } else {
            throw new DataNotFoundException("Medical record of " + firstName + " " + lastName);
        }
    }
    /**
     * Update an existing person's medical record into a datasource.
     *
     * @param medicalRecordToUpdate medical record to update.
     *
     * @return updated medical record
     *
     * @throws DataNotFoundException if medical record does not exist in the datasource. (No medical record
     *           belonging to the given person has been found).
     */
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecordToUpdate) throws DataNotFoundException {
        MedicalRecord medicalRecord;
        Optional<MedicalRecord> medicalRecordResult = getMedicalRecord(medicalRecordToUpdate.getFirstName(), medicalRecordToUpdate.getLastName());
        if(medicalRecordResult.isPresent()){
            medicalRecord = medicalRecordResult.get();
            medicalRecord.setBirthdate(medicalRecordToUpdate.getBirthdate());
            medicalRecord.setMedications(medicalRecordToUpdate.getMedications());
            medicalRecord.setAllergies(medicalRecordToUpdate.getAllergies());
        } else {
            throw new DataNotFoundException("Medical record of " + medicalRecordToUpdate.getFirstName() + " " + medicalRecordToUpdate.getLastName());
        }
        return medicalRecord;
    }
    /**
     * Create a person's medical record into a datasource.
     *
     * @param medicalRecordToCreate medical record to create.
     *
     * @return created medical record.
     *
     * @throws DataAlreadyExistsException if medical record already exists in datasource.
     */
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecordToCreate) throws DataAlreadyExistsException {
        MedicalRecord medicalRecord;
        Optional<MedicalRecord> medicalRecordResult = getMedicalRecord(medicalRecordToCreate.getFirstName(), medicalRecordToCreate.getLastName());
        if (medicalRecordResult.isEmpty()) {
            medicalRecord = new MedicalRecord(medicalRecordToCreate);
            dataSource.getData().getMedicalrecords().add(medicalRecord);
        } else {
            throw new DataAlreadyExistsException("Medical record of " + medicalRecordToCreate.getFirstName() + " " + medicalRecordToCreate.getLastName());
        }
        return medicalRecord;
    }

    /**
     * Get the age of a person.
     *
     * @param firstName first name of the person.
     * @param lastName last name of the person.
     * @return age of the person
     * @throws DataNotFoundException if medical record of the person does not exist in the datasource,
     * @throws DataIllegalValueException if age of the person cannot be computed.
     */
    @Override
    public long getPersonAge(String firstName, String lastName) throws DataNotFoundException, DataIllegalValueException {
        long birthDateInMs;
        long nowInMs;
        long personAge;
        final long nbMilisecondsByYears = 31556925216L;
        Optional<MedicalRecord> medicalRecordResult = getMedicalRecord(firstName, lastName);
        if(medicalRecordResult.isPresent()){
            MedicalRecord medicalRecord = medicalRecordResult.get();
            birthDateInMs = medicalRecord.getBirthdate().getTime();
            nowInMs = new Date().getTime();
            if(nowInMs >= birthDateInMs) {
                personAge = (nowInMs - birthDateInMs) / nbMilisecondsByYears;
                return personAge;
            } else {
                throw new DataIllegalValueException("Birthdate of " + firstName + " " + lastName, medicalRecord.getBirthdate().toString());
            }
        } else {
            throw new DataNotFoundException("Medical record of " + firstName + " " +lastName);
        }
    }
}
