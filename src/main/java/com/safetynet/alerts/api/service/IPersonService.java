package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.Person;

import java.util.Optional;

/**
 * Get, delete or save a person from/to a datasource.
 */
public interface IPersonService {
    /**
     * Get a person from a datasource.
     *
     * @param firstName first name of the person to get.
     * @param lastName last name of the person to get.
     *
     * @return the person if found.
     */
    public Optional<Person> getPerson(final String firstName, final String lastName);
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
    public void deletePerson(final String firstName, final String lastName);
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
    public Person updatePerson(Person personToUpdate);
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
    public Person createPerson(Person personToCreate);
}
