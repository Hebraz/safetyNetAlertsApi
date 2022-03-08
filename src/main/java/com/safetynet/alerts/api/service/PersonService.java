package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.dto.ChildAlertDto;
import com.safetynet.alerts.api.model.dto.FireDto;
import com.safetynet.alerts.api.service.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.service.exception.DataIllegalValueException;
import com.safetynet.alerts.api.service.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link com.safetynet.alerts.api.service.IPersonService} to get,
 * delete or save a person from/to a datasource.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class PersonService implements IPersonService {

    private final IAlertsDataSource dataSource;
    private final IMedicalRecordService medicalRecordService;
    private final IFireStationService fireStationService;
    /**
     * Get a person from a datasource.
     *
     * @param firstName first name of the person to get.
     * @param lastName last name of the person to get.
     *
     * @return the person if found.
     */
    @Override
    public Optional<Person> getPerson(final String firstName, final String lastName){
        List<Person> persons = dataSource.getData().getPersons();
        Optional<Person> personResult = persons.stream()
                                            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) &&
                                                        p.getLastName().equalsIgnoreCase(lastName))
                                            .findFirst();
        return personResult;
    }
    /**
     * Delete a person from a datasource.
     *
     * @param firstName first name of the person to delete.
     * @param lastName last name of the person to delete.
     *
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * given firstName and lastName has been found).
     *
     */
    @Override
    public void deletePerson(final String firstName, final String lastName) throws DataNotFoundException {
        Person person;
        Optional<Person> personResult = getPerson(firstName, lastName);
        if(personResult.isPresent()){
            person = personResult.get();
            dataSource.getData().getPersons().remove(person);
        } else {
            throw new DataNotFoundException("Person " + firstName + " " + lastName);
        }
    }
    /**
     * Update an existing person into a datasource.
     *
     * @param personToUpdate person to update.
     *
     * @return updated person.
     *
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * same firstName and lastName has been found).
     *
     */
    @Override
    public Person updatePerson(Person personToUpdate) throws DataNotFoundException {
        Person person;
        Optional<Person> personResult = getPerson(personToUpdate.getFirstName(), personToUpdate.getLastName());
        if(personResult.isPresent()){
            person = personResult.get();
            person.setAddress(personToUpdate.getAddress());
            person.setCity(personToUpdate.getCity());
            person.setZip(personToUpdate.getZip());
            person.setEmail(personToUpdate.getEmail());
            person.setPhone(personToUpdate.getPhone());
        } else {
            throw new DataNotFoundException("Person " + personToUpdate.getFirstName() + " " + personToUpdate.getLastName());
        }
        return person;
    }

    /**
     * Add a new a person into a datasource.
     * 
     * @param personToCreate person to add.
     *
     * @return added person.
     *
     * @throws DataAlreadyExistsException if the person already exist in the datasource. (person with
     * same firstName and lastName has been found).
     *
     */
    @Override
    public Person createPerson(Person personToCreate) throws DataAlreadyExistsException {
        Person person;
        Optional<Person> personResult = getPerson(personToCreate.getFirstName(), personToCreate.getLastName());
        if(personResult.isEmpty()){
            person = new Person(personToCreate);
            dataSource.getData().getPersons().add(person);
        } else {
            throw new DataAlreadyExistsException("Person " + personToCreate.getFirstName() + " " + personToCreate.getLastName());
        }
        return person;
    }

    /**
     * Get a list of persons that live to a given address.
     *
     * @param address the address.
     *
     * @return list of Person object.
     */
    @Override
    public List<Person> getPersonsByAddress(String address){
        List<Person> persons = dataSource.getData().getPersons();
        return persons.stream()
                .filter(p -> p.getAddress().equals(address))
                .collect(Collectors.toList());
    }

    /**
     * Get a list of children that live to a given address.
     *
     * @param address the address
     *
     * @return a {@link com.safetynet.alerts.api.model.dto.ChildAlertDto} object
     *
     */
    public ChildAlertDto getChildren(String address){
        ChildAlertDto children = new ChildAlertDto();
        List<Person> persons = this.getPersonsByAddress(address);
        for(Person p : persons){
            try{
                long personAge = medicalRecordService.getPersonAge(p.getFirstName(),p.getLastName());
                if(personAge > 18) {
                    ChildAlertDto.Adult adult = new ChildAlertDto.Adult();
                    adult.setFirstName(p.getFirstName());
                    adult.setLastName(p.getLastName());
                    children.getAdults().add(adult);
                } else {
                    ChildAlertDto.Child child = new ChildAlertDto.Child();
                    child.setFirstName(p.getFirstName());
                    child.setLastName(p.getLastName());
                    child.setAge(personAge);
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
        FireDto fireDto = new FireDto();
        List<Person> persons = this.getPersonsByAddress(address);
        for(Person p : persons){
            String firstName = p.getFirstName();
            String lastName = p.getLastName();
            Long age;

            FireDto.Person fireDtoPerson = new FireDto.Person();
            fireDtoPerson.setFirstName(firstName);
            fireDtoPerson.setLastName(lastName);
            fireDtoPerson.setPhoneNumber(p.getPhone());

            try{
                age = medicalRecordService.getPersonAge(firstName, lastName);
                fireDtoPerson.setAge(age);
            } catch (DataNotFoundException | DataIllegalValueException e ) {
                log.error("Age of " + firstName + " " + lastName + " cannot be computed : " + e.getMessage());
                //in order to provide this person, even if its age cannot be computed, do not propagate the exceptions
                //and force the age of the person to 0
                fireDtoPerson.setAge(0);
            }

            FireDto.MedicalRecord fireDtoMedicalRecord = new FireDto.MedicalRecord();
            Optional<MedicalRecord> medicalRecordResult = medicalRecordService.getMedicalRecord(firstName,lastName);
            if(medicalRecordResult.isPresent()){
                MedicalRecord medicalRecord = medicalRecordResult.get();
                fireDtoMedicalRecord.getMedications().addAll(medicalRecord.getMedications());
                fireDtoMedicalRecord.getAllergies().addAll(medicalRecord.getAllergies());
            }

            fireDtoPerson.setFiredPersonMedicalRecord(fireDtoMedicalRecord);
            fireDto.getPersons().add(fireDtoPerson);
        }

        try {
            fireDto.setStationNumber(fireStationService.getFireStationNumber(address));
        } catch (DataNotFoundException e){
            log.error("No fire station at address " + address + " : " + e.getMessage());
            //in order to provide the list of persons, even if no fire station have been found,
            // Do not propagate the exception and force fire station to 0
            fireDto.setStationNumber(0);
        }

        return fireDto;
    }
}
