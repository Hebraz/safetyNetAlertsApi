package com.safetynet.alerts.api.service;

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
     * @throws IllegalArgumentException if the person does not exist in the datasource. (No person with
     * given firstName and lastName has been found).
     *
     */
    public void deletePerson(final String firstName, final String lastName);
    /**
     * Save a person into a datasource. If the person already exists in the
     * datasource (with same first/last names), it is updated. Else it is created
     * and added to the datasource.
     *
     * @param personToSave person to save.
     *
     */
    public void savePerson(Person personToSave);
}
