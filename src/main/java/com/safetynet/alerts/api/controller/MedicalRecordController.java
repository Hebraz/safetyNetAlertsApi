package com.safetynet.alerts.api.controller;

import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.exception.ServiceException;
import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.service.IMedicalRecordService;
import com.safetynet.alerts.api.utils.IRequestLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 *  Medical record endpoint
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalRecordController {

    private final IMedicalRecordService medicalRecordService;
    private final IRequestLogger requestLogger;

    /**
     * Delete - Delete a medical record of a person.
     *
     * @param firstName - The first name of the person
     * @param lastName - The last name of the person
     *
     * @return  HTTP response with :
     *            - empty Body
     *            - Http status code set to "204-No Content" if the medical record have been deleted.
     *
     * @throws DataNotFoundException if no medical record for the given person exists in datasource
     */
    @DeleteMapping("/medicalRecord/{firstName}/{lastName}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable("firstName") final String firstName,
                                                        @PathVariable("lastName") final String lastName) {
        requestLogger.logRequest("DELETE /medicalRecord/"+firstName+"/"+lastName);
        try{
            medicalRecordService.deleteMedicalRecord(firstName, lastName);
            requestLogger.logResponseSuccess(HttpStatus.NO_CONTENT, null);
            return ResponseEntity.noContent().build();
        } catch (DataNotFoundException e){
            requestLogger.logResponseFailure(e.getHttpStatus(), e.getMessage());
            throw e;
        }
    }

    /**
     * Create - Add a new medical record.
     *
     * @param medicalRecord An object medicalRecord
     *
     * @return  HTTP response with :
     *            - body : the created medicalRecord object.
     *            - Http status code set to  "201-Created" if medical record have been created.
     *
     * @throws DataAlreadyExistsException if a medical record for the given person already exists
     */
    @PostMapping("/medicalRecord")
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        requestLogger.logRequest("POST /medicalRecord/"+ medicalRecord.getFirstName()+"/"+medicalRecord.getLastName());
        try{
            MedicalRecord createdMedicalRecord = medicalRecordService.createMedicalRecord(medicalRecord);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{firstName}/{lastName}")
                    .buildAndExpand(createdMedicalRecord.getFirstName(),createdMedicalRecord.getLastName())
                    .toUri();
            requestLogger.logResponseSuccess(HttpStatus.CREATED,null);
            return ResponseEntity.created(location).build();
        } catch (DataAlreadyExistsException e){
            requestLogger.logResponseFailure(e.getHttpStatus() ,e.getMessage());
            throw e;
        }
    }

    /**
     * Update - Update an existing medical record.
     *
     * @param medicalRecord An object medicalRecord
     *
     * @return  HTTP response with :
     *            - body : the updated medicalRecord object.
     *            - Http status code set to   "200-Ok" if medical record have been updated.
     *
     * @throws DataNotFoundException if no medical record for the given person exists in datasource
     */
    @PutMapping("/medicalRecord")
    public  ResponseEntity<MedicalRecord>  updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        requestLogger.logRequest("PUT /medicalRecord/"+ medicalRecord.getFirstName()+"/"+medicalRecord.getLastName());
        try  {
            medicalRecordService.updateMedicalRecord(medicalRecord);
            requestLogger.logResponseSuccess(HttpStatus.OK,null);
            return ResponseEntity.ok().build();
        } catch (DataNotFoundException e){
            requestLogger.logResponseFailure(e.getHttpStatus() ,e.getMessage());
            throw e;
        }
    }
}
