package com.safetynet.alerts.api.service.dtomapper;

import com.safetynet.alerts.api.dao.IMedicalRecordDao;
import com.safetynet.alerts.api.exception.DataIllegalValueException;
import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.MedicalRecordDto;
import com.safetynet.alerts.api.model.dto.PersonDto;
import com.safetynet.alerts.api.utils.IAgeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;


@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
/**
 * Person entity object to PersonDto object Mapper
 * @param <Person> Person entity class
 * @param <PersonDto> PersonDto class
 */
public class PersonDtoMapper implements IDtoMapper<Person, PersonDto> {

    private final IMedicalRecordDao medicalRecordDao;
    private final IDtoMapper<MedicalRecord, MedicalRecordDto> medicalRecordDtoIDtoMapper;
    private final IAgeUtil ageUtil;


    /**
     * Map a Person object to a PersonDto object
     * @param p a Person object
     * @return a PersonDto object
     */
    @Override
    public PersonDto mapToDto(Person p) {
        Date birthdate;
        Integer age = null;
        MedicalRecordDto medicalRecordDto = null;

        /*get the medical record of the person*/
        Optional<MedicalRecord> medicalRecordResult = medicalRecordDao.getMedicalRecord(p.getFirstName(),p.getLastName());

        if(medicalRecordResult.isPresent()){
            medicalRecordDto = medicalRecordDtoIDtoMapper.mapToDto(medicalRecordResult.get());

            /*Compute age of the person according to its birthdate from the medical record*/
            try{
                age = ageUtil.computeFromBirthdate(medicalRecordDto.getBirthdate());
            } catch (DataIllegalValueException e) {
                log.error("Failed to get the age of " + p.getFirstName() + " " + p.getLastName() + ": " + e.getMessage());
            }
        } else {
            log.error("Failed to get the medical record of " + p.getFirstName() + " " + p.getLastName());
        }

        return new PersonDto(p.getFirstName(),p.getLastName(), p.getAddress(), p.getCity(),p.getZip(),
                p.getPhone(),p.getEmail(),age, medicalRecordDto);
    }
}
