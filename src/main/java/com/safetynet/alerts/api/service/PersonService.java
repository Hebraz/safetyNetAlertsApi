package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.dao.IFireStationDao;
import com.safetynet.alerts.api.dao.IPersonDao;
import com.safetynet.alerts.api.model.dto.ChildAlertDto;
import com.safetynet.alerts.api.model.dto.FireDto;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.PersonDto;
import com.safetynet.alerts.api.service.dtomapper.IDtoMapper;
import com.safetynet.alerts.api.utils.Age;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private final IFireStationDao fireStationDao;
    private final IDtoMapper<Person,PersonDto> personDtoMapper;
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
        List<PersonDto> adults = new ArrayList<>();
        List<PersonDto> children = new ArrayList<>();

        List<Person> persons = personDao.getPersonsByAddress(address);

        adults.addAll(
                persons.stream()
                        .map(p -> personDtoMapper.mapToDto(p))
                        .filter(p -> Objects.nonNull(p.getAge()) && Age.isAdult(p.getAge()))
                        .collect(Collectors.toList()));

        children.addAll(
                persons.stream()
                        .map(p -> personDtoMapper.mapToDto(p))
                        .filter(p -> Objects.nonNull(p.getAge()) && !Age.isAdult(p.getAge()))
                        .collect(Collectors.toList()));

        return new ChildAlertDto(children,adults);
    }

    /**
     * Get the list of persons that live at given address, their medical record and the associated fire station.
     *
     * @param address address where the fire is
     * @return a {@link FireDto} object
     */
    @Override
    public FireDto getFiredPersons(String address) throws DataNotFoundException {
        int stationNumber=0;
        List<PersonDto> personDtos;

        personDtos = personDao.getPersonsByAddress(address)
                .stream()
                .map(p -> personDtoMapper.mapToDto(p))
                .collect(Collectors.toList());

        try {
            stationNumber = fireStationDao.getFireStationNumber(address);
        } catch (DataNotFoundException e){
            log.error("No fire station at address " + address + " : " + e.getMessage());
            //if no person live at this address, throw an exception
            throw new DataNotFoundException("Persons that live at address " + address);
        }
        return new FireDto(stationNumber,personDtos);
    }

    /**
     * Get person information
     *
     * @param firstName - The first name of the person to delete
     * @param lastName - The last name of the person to delete
     * @retun a list of {@link com.safetynet.alerts.api.model.dto.PersonDto}
     */
    @Override
    public List<PersonDto> getPersonInfo(String firstName, String lastName) {
        return personDao.getPersons(firstName, lastName)
                .stream()
                .map(p -> personDtoMapper.mapToDto(p))
                .collect(Collectors.toList());
    }
    /**
     * Get email of people who live in a given city
     *
     * @param city - The city name
     * @retun a list of emails.
     */
    @Override
    public List<String> getEmailsByCity(String city){
        return personDao.getPersonsByCity(city)
                .stream()
                .map(p->p.getEmail())
                .distinct()
                .collect(Collectors.toList());
    }
}
