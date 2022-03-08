package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.dao.IFireStationDao;
import com.safetynet.alerts.api.dao.IMedicalRecordDao;
import com.safetynet.alerts.api.dao.IPersonDao;
import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.dto.ChildAlertDto;
import com.safetynet.alerts.api.model.dto.FireDto;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataIllegalValueException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.PersonInfoDto;
import com.safetynet.alerts.api.utils.Age;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link IPersonService} to get,
 * delete or save a person from/to a datasource.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class PersonService implements IPersonService {

    private final IPersonDao personDao;
    private final IMedicalRecordDao medicalRecordDao;
    private final IFireStationDao fireStationDao;

    /**
     * Delete a person from a datasource.
     *
     * @param firstName first name of the person to delete.
     * @param lastName last name of the person to delete.
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * given firstName and lastName has been found).
     *
     */
    @Override
    public void deletePerson(final String firstName, final String lastName) throws DataNotFoundException {
        personDao.deletePerson(firstName,lastName);
    }
    /**
     * Update an existing person into a datasource.
     *
     * @param personToUpdate person to update.
     * @return updated person.
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * same firstName and lastName has been found).
     *
     */
    @Override
    public Person updatePerson(Person personToUpdate) throws DataNotFoundException {
        return personDao.updatePerson(personToUpdate);
    }

    /**
     * Add a new a person into a datasource.
     * 
     * @param personToCreate person to add.
     * @return added person.
     * @throws DataAlreadyExistsException if the person already exist in the datasource. (person with
     * same firstName and lastName has been found).
     *
     */
    @Override
    public Person createPerson(Person personToCreate) throws DataAlreadyExistsException {
        return personDao.createPerson(personToCreate);
    }

     /**
     * Get a list of children that live to a given address.
     *
     * @param address the address
     * @return a {@link com.safetynet.alerts.api.model.dto.ChildAlertDto} object
     *
     */
    public ChildAlertDto getChildren(String address){
        Date personBirthdate;
        ChildAlertDto children = new ChildAlertDto();
        List<Person> persons = personDao.getPersonsByAddress(address);
        for(Person p : persons){
            try{
                personBirthdate = medicalRecordDao.getPersonBirthdate(p.getFirstName(),p.getLastName());
                if(Age.isAdult(personBirthdate)) {
                    ChildAlertDto.Adult adult = new ChildAlertDto.Adult();
                    adult.setFirstName(p.getFirstName());
                    adult.setLastName(p.getLastName());
                    children.getAdults().add(adult);
                } else {
                    ChildAlertDto.Child child = new ChildAlertDto.Child();
                    child.setFirstName(p.getFirstName());
                    child.setLastName(p.getLastName());
                    child.setAge(Age.computeAge(personBirthdate));
                    child.setPhone(p.getPhone());
                    children.getChildren().add(child);
                }
            } catch (DataIllegalValueException | DataNotFoundException e) {
                log.error("Failed to get the age of " + p.getFirstName() + " " + p.getLastName() + ": " + e.getMessage());
            }
        }
        return children;
    }

    /**
     * Get the list of persons that live at given address, their medical record and the associated fire station.
     *
     * @param address address where the fire is
     * @return a {@link FireDto} object
     */
    @Override
    public FireDto getFiredPersons(String address)  {
        int age;
        Date personBirthdate;
        FireDto fireDto = new FireDto();
        List<Person> persons = personDao.getPersonsByAddress(address);

        for(Person p : persons){
            String firstName = p.getFirstName();
            String lastName = p.getLastName();
            FireDto.Person fireDtoPerson = new FireDto.Person();
            fireDtoPerson.setFirstName(firstName);
            fireDtoPerson.setLastName(lastName);
            fireDtoPerson.setPhoneNumber(p.getPhone());

            try{
                personBirthdate = medicalRecordDao.getPersonBirthdate(p.getFirstName(),p.getLastName());
                age = Age.computeAge(personBirthdate);
                fireDtoPerson.setAge(age);
            } catch (DataNotFoundException | DataIllegalValueException e ) {
                log.error("Age of " + firstName + " " + lastName + " cannot be computed : " + e.getMessage());
                //in order to provide this person, even if its age cannot be computed, do not propagate the exceptions
                //and force the age of the person to 0
                fireDtoPerson.setAge(0);
            }

            FireDto.MedicalRecord fireDtoMedicalRecord = new FireDto.MedicalRecord();
            Optional<MedicalRecord> medicalRecordResult = medicalRecordDao.getMedicalRecord(firstName,lastName);
            if(medicalRecordResult.isPresent()){
                MedicalRecord medicalRecord = medicalRecordResult.get();
                fireDtoMedicalRecord.getMedications().addAll(medicalRecord.getMedications());
                fireDtoMedicalRecord.getAllergies().addAll(medicalRecord.getAllergies());
            }

            fireDtoPerson.setMedicalRecord(fireDtoMedicalRecord);
            fireDto.getPersons().add(fireDtoPerson);
        }

        try {
            fireDto.setStationNumber(fireStationDao.getFireStationNumber(address));
        } catch (DataNotFoundException e){
            log.error("No fire station at address " + address + " : " + e.getMessage());
            //in order to provide the list of persons, even if no fire station have been found,
            // Do not propagate the exception and force fire station to 0
            fireDto.setStationNumber(0);
        }

        return fireDto;
    }

    /**
     * Get person information
     *
     * @param firstName - The first name of the person to delete
     * @param lastName - The last name of the person to delete
     * @retun an object {@link com.safetynet.alerts.api.model.dto.PersonInfoDto}
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * same firstName and lastName has been found).
     */
    @Override
    public PersonInfoDto getPersonInfo(String firstName, String lastName) throws DataNotFoundException {
        PersonInfoDto personInfoDto = new PersonInfoDto();

        Optional<Person> personResult = personDao.getPerson(firstName, lastName);
        if(personResult.isPresent())
        {
            Person person = personResult.get();
            personInfoDto.setFirstName(firstName);
            personInfoDto.setLastName(lastName);
            personInfoDto.setEmail(person.getEmail());
            personInfoDto.setAddress(person.getAddress());
            personInfoDto.setCity(person.getCity());
            personInfoDto.setZip(person.getZip());

            try{
                Date personBirthdate = medicalRecordDao.getPersonBirthdate(firstName,lastName);
                int age = Age.computeAge(personBirthdate);
                personInfoDto.setAge(age);
            } catch (DataNotFoundException | DataIllegalValueException e ) {
                log.error("Age of " + firstName + " " + lastName + " cannot be computed : " + e.getMessage());
                //in order to provide this person, even if its age cannot be computed, do not propagate the exceptions
                //and force the age of the person to 0
                personInfoDto.setAge(0);
            }

            PersonInfoDto.MedicalRecord medicalRecordDto = new PersonInfoDto.MedicalRecord();
            Optional<MedicalRecord> medicalRecordResult = medicalRecordDao.getMedicalRecord(firstName,lastName);
            if(medicalRecordResult.isPresent()){
                MedicalRecord medicalRecord = medicalRecordResult.get();
                medicalRecordDto.getMedications().addAll(medicalRecord.getMedications());
                medicalRecordDto.getAllergies().addAll(medicalRecord.getAllergies());
            }
            personInfoDto.setMedicalRecord(medicalRecordDto);
            return personInfoDto;
        } else {
            throw new DataNotFoundException("Person " + firstName + " " + lastName);
        }
    }
    /**
     * Get email of people who live in a given city
     *
     * @param city - The city name
     * @retun a list of emails.
     */
    @Override
    public List<String> getEmailsByCity(String city){
        List<Person> persons = personDao.getPersonsByCity(city);
        return persons.stream().map(p->p.getEmail()).collect(Collectors.toList());
    }
}
