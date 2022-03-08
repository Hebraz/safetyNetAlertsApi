package com.safetynet.alerts.api.dao;

import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.Person;

import java.util.List;
import java.util.Optional;

public interface IPersonDao {
    /**
     * Get a person from a datasource.
     *
     * @param firstName first name of the person to get.
     * @param lastName last name of the person to get.
     * @return the person if found.
     */
    Optional<Person> getPerson(final String firstName, final String lastName);
    /**
     * Delete a person from a datasource.
     *
     * @param firstName first name of the person to delete.
     * @param lastName last name of the person to delete.
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * given firstName and lastName has been found).
     *
     */
    void deletePerson(final String firstName, final String lastName) throws DataNotFoundException;
    /**
     * Update an existing person into a datasource.
     *
     * @param personToUpdate person to update.
     * @return updated person.
     * @throws DataNotFoundException if the person does not exist in the datasource. (No person with
     * same firstName and lastName has been found).
     *
     */
    Person updatePerson(Person personToUpdate) throws DataNotFoundException;
    /**
     * Add a new a person into a datasource.
     *
     * @param personToCreate person to add.
     * @return added person.
     * @throws DataAlreadyExistsException if the person already exist in the datasource. (person with
     * same firstName and lastName has been found).
     *
     */
    Person createPerson(Person personToCreate) throws DataAlreadyExistsException;
    /**
     * Get a list of persons that live at a given address.
     *
     * @param address the address.
     * @return list of Person object.
     */
    List<Person> getPersonsByAddress(String address);
    /**
     * Get a list of persons that live in a given city.
     *
     * @param city the city name.
     * @return list of Person object.
     */
    List<Person> getPersonsByCity(String city);
}
