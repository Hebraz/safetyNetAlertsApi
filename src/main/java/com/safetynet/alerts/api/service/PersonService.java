package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link com.safetynet.alerts.api.service.IPersonService} to get,
 * delete or save a person from/to a datasource.
 */
@Service
public class PersonService implements IPersonService {

    @Autowired
    private IAlertsDataSource dataSource;
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
     * @throws IllegalArgumentException if the person does not exist in the datasource. (No person with
     * given firstName and lastName has been found).
     *
     */
    @Override
    public void deletePerson(final String firstName, final String lastName){
        Person person;
        Optional<Person> personResult = getPerson(firstName, lastName);
        if(personResult.isPresent()){
            person = personResult.get();
            dataSource.getData().getPersons().remove(person);
        } else {
            throw new IllegalArgumentException(firstName + " " + lastName + " does not exist");
        }
    }
    /**
     * Save a person into a datasource. If the person already exists in the
     * datasource (with same first/last names), it is updated. Else it is created
     * and added to the datasource.
     *
     * @param personToSave person to save.
     *
     */
    @Override
    public void savePerson(Person personToSave){
        Person person;
        Optional<Person> personResult = getPerson(personToSave.getFirstName(), personToSave.getLastName());
        if(personResult.isPresent()){
            person = personResult.get();
            person.setAddress(personToSave.getAddress());
            person.setCity(personToSave.getCity());
            person.setZip(personToSave.getZip());
            person.setEmail(personToSave.getEmail());
            person.setPhone(personToSave.getPhone());
        } else {
            person = new Person(personToSave);
            dataSource.getData().getPersons().add(person);
        }
    }
}
