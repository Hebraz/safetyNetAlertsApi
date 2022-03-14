package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PersonServiceITest {

    @Autowired
    IAlertsDataSource alertsDataSource;
    @Autowired
    IPersonService personService;

    private Person personToSave;

    @BeforeEach
    public void loadData() throws IOException {
        alertsDataSource.load("data-test.json");

        personToSave = new Person();
        personToSave.setFirstName("xxxx");
        personToSave.setLastName("yyyy");
        personToSave.setEmail("sophia.anotherone@toto.com");
        personToSave.setPhone("454-554-545");
        personToSave.setAddress("78 Rue Pierre brossolette");
        personToSave.setCity("Paris");
        personToSave.setZip("75000");
    }


    @Test
    void deletePersonNullFirstName() {
        assertThrows(DataNotFoundException.class,() -> personService.deletePerson(null,"Zemicks"));
    }
    /*
    @Test
    void deletePersonNullLastName(){
        assertThrows(DataNotFoundException.class,() -> personService.deletePerson("Sophia",null));
    }
    @Test
    void deletePersonUnknownFirstName() {
        assertThrows(DataNotFoundException.class,() -> personService.deletePerson("Sophie","Zemicks"));
    }
    @Test
    void deletePersonUnknownLastName() {
        assertThrows(DataNotFoundException.class,() -> personService.deletePerson("Sophia","Zemick"));
    }
    @Test
    void deletePersonOk() {
        //check number of persons
        assertEquals(7, alertsDataSource.getData().getPersons().stream().count());

        //Act
        assertDoesNotThrow(()-> personService.deletePerson("Sophia", "Zemicks"));

        //check number of persons has decreased
        assertEquals(6,  alertsDataSource.getData().getPersons().stream().count());
        //try to delete person another time shall throw exception
        assertThrows(DataNotFoundException.class,() -> personService.deletePerson("Sophia", "Zemicks"));
    }

    @Test
    void savePersonAddNewWithSameFirstName() {
        personToSave.setFirstName("Sophia");
        personToSave.setLastName("AnotherOne");

        //check count before
        assertEquals(7, alertsDataSource.getData().getPersons().stream().count());
        //Act
        assertDoesNotThrow(()-> personService.createPerson(personToSave));
        //check count after
        Person addedPerson = alertsDataSource.getData().getPersons().get(7);
        assertEquals(8, alertsDataSource.getData().getPersons().stream().count());
        assertEquals("Sophia", addedPerson.getFirstName());
        assertEquals("AnotherOne", addedPerson.getLastName());
        assertEquals("sophia.anotherone@toto.com", addedPerson.getEmail());
        assertEquals("454-554-545", addedPerson.getPhone());
        assertEquals("78 Rue Pierre brossolette", addedPerson.getAddress());
        assertEquals("Paris", addedPerson.getCity());
        assertEquals("75000", addedPerson.getZip());
    }

    @Test
    void savePersonAddNewWithSameLastName() {
        personToSave.setFirstName("SophiaSister");
        personToSave.setLastName("Zemicks");

        //check count before
        assertEquals(7, alertsDataSource.getData().getPersons().stream().count());
        //Act
        assertDoesNotThrow(()-> personService.createPerson(personToSave));
        //check count after
        Person addedPerson = alertsDataSource.getData().getPersons().get(7);
        assertEquals(8, alertsDataSource.getData().getPersons().stream().count());
        assertEquals("SophiaSister",addedPerson.getFirstName());
        assertEquals("Zemicks", addedPerson.getLastName());
        assertEquals("sophia.anotherone@toto.com", addedPerson.getEmail());
        assertEquals("454-554-545", addedPerson.getPhone());
        assertEquals("78 Rue Pierre brossolette", addedPerson.getAddress());
        assertEquals("Paris", addedPerson.getCity());
        assertEquals("75000", addedPerson.getZip());
    }

    @Test
    void savePersonUpdateExistingPerson() {
        personToSave.setFirstName("Sophia");
        personToSave.setLastName("Zemicks");

        //check count before
        assertEquals(7, alertsDataSource.getData().getPersons().stream().count());
        //Act
        assertDoesNotThrow(()-> personService.updatePerson(personToSave));
        //check count after
        Person updatedPerson = alertsDataSource.getData().getPersons().get(1);
        assertEquals(7, alertsDataSource.getData().getPersons().stream().count());
        assertEquals("Sophia", updatedPerson.getFirstName());
        assertEquals("Zemicks", updatedPerson.getLastName());
        assertEquals("sophia.anotherone@toto.com", updatedPerson.getEmail());
        assertEquals("454-554-545", updatedPerson.getPhone());
        assertEquals("78 Rue Pierre brossolette", updatedPerson.getAddress());
        assertEquals("Paris", updatedPerson.getCity());
        assertEquals("75000", updatedPerson.getZip());

    }*/
}