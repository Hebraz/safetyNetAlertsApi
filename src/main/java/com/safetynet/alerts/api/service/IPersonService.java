package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.model.dto.ChildAlertDto;
import com.safetynet.alerts.api.model.dto.FireDto;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.PersonInfoDto;

import java.util.List;
import java.util.Optional;

/**
 * Get, delete or save a person from/to a datasource.
 */
public interface IPersonService {
     /**
     * Delete a person from a datasource.
     *
     * @param firstName first name of the person to delete.
     * @param lastName last name of the person to delete.
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * given firstName and lastName has been found).
     *
     */
    public void deletePerson(final String firstName, final String lastName) throws DataNotFoundException;
    /**
     * Update an existing person into a datasource.
     *
     * @param personToUpdate person to update.
     * @return updated person.
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * same firstName and lastName has been found).
     *
     */
    public Person updatePerson(Person personToUpdate) throws DataNotFoundException;
    /**
     * Add a new a person into a datasource.
     *
     * @param personToCreate person to add.
     * @return added person.
     * @throws DataAlreadyExistsException if the person already exist in the datasource. (person with
     * same firstName and lastName has been found).
     *
     */
    public Person createPerson(Person personToCreate) throws DataAlreadyExistsException;
    /**
     * Get a list of children that live to a given address.
     *
     * @param address the address
     * @return a {@link com.safetynet.alerts.api.model.dto.ChildAlertDto} object
     */
    public ChildAlertDto getChildren(String address);
    /**
     * Get the list of persons that live at given address, their medical record and the associated fire station.
     *
     * @param address address where the fire is
     * @return a {@link com.safetynet.alerts.api.model.dto.FireDto} object
     */
    FireDto getFiredPersons(String address);
    /**
     * Get person information
     *
     * @param firstName - The first name of the person to delete
     * @param lastName - The last name of the person to delete
     * @retun an object {@link com.safetynet.alerts.api.model.dto.PersonInfoDto}
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * same firstName and lastName has been found).
     */
    PersonInfoDto getPersonInfo(String firstName, String lastName) throws DataNotFoundException;
}
