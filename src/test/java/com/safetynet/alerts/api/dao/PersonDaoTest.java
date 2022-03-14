package com.safetynet.alerts.api.dao;

import com.safetynet.alerts.api.StubbedData;
import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.Person;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonDaoTest {

    private IAlertsDataSource.Data stubbedData;
    private IPersonDao personDao;
    @Mock
    private IAlertsDataSource dataSource;

    @BeforeEach
    void initializeTest() throws ParseException {
        personDao = new PersonDao(dataSource);
        stubbedData = StubbedData.get();
    }

    @Test
    void getPersonExistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        Optional<Person> person = personDao.getPerson("Clive","Ferguson");
        //CHECK
        assertTrue(person.isPresent());
        assertEquals("97451", person.get().getZip());
        assertEquals("841-874-6741", person.get().getPhone());
    }

    @Test
    void getPersonNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        Optional<Person> person = personDao.getPerson("Clive_","Ferguson");
        //CHECK
        assertTrue(person.isEmpty());
    }

    @Test
    void deletePersonExistent() throws DataNotFoundException {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        Person person = stubbedData.getPersons().get(14); //Reginold Walker
        //ACT
        personDao.deletePerson("Reginold","Walker");
        //CHECK
        assertThat(stubbedData.getPersons()).doesNotContain(person);
    }

    @Test
    void deletePersonNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        assertThrows(DataNotFoundException.class,() -> personDao.deletePerson("ReginAld","Walker"));
    }


    @Test
    void updatePersonExistent() throws DataNotFoundException {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //PRE CHECK
        Person personToUpdate = stubbedData.getPersons().get(4);//Felicia Boyd
        assertThat(personToUpdate.getFirstName()).isEqualTo("Felicia");
        assertThat(personToUpdate.getEmail()).isEqualTo("jaboyd@email.com");
        //ACT
        Person person = new Person("Felicia","Boyd", "10 Rue Eiffel", "Paris", "75000","0145804125", "felicia.boyd@parismail.com");
        Person updatedPerson = personDao.updatePerson(person);
        //CHECK
        assertThat(personToUpdate.getAddress()).isEqualTo("10 Rue Eiffel");
        assertThat(personToUpdate.getCity()).isEqualTo("Paris");
        assertThat(personToUpdate.getZip()).isEqualTo("75000");
        assertThat(personToUpdate.getPhone()).isEqualTo("0145804125");
        assertThat(personToUpdate.getEmail()).isEqualTo("felicia.boyd@parismail.com");
        assertEquals(personToUpdate, updatedPerson);
    }

    @Test
    void updatePersonNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        Person personToUpdate = new Person("Helicia","Boyd", "10 Rue Eiffel", "Paris", "75000","0145804125", "felicia.boyd@parismail.com");
        assertThrows(DataNotFoundException.class,() ->personDao.updatePerson(personToUpdate));
    }

    @Test
    void createPersonExistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        Person person = new Person("Felicia","Boyd", "10 Rue Eiffel", "Paris", "75000","0145804125", "felicia.boyd@parismail.com");
        assertThrows(DataAlreadyExistsException.class,()->personDao.createPerson(person));
    }

    @Test
    void createPersonNonexistent() throws DataAlreadyExistsException {
        List<Person> persons = stubbedData.getPersons();
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //PRECHECK
        assertThat(persons.stream().count()).isEqualTo(23);
        //ACT
        Person personToCreate =  new Person("Robert","Dupont", "10 Rue Eiffel", "Paris", "75000","0145804125", "felicia.boyd@parismail.com");
        Person personCreated = personDao.createPerson(personToCreate);
        //CHECK
        assertThat(persons.stream().count()).isEqualTo(24);
        assertThat(persons.get(23).getFirstName()).isEqualTo("Robert");
        assertThat(persons.get(23).getLastName()).isEqualTo("Dupont");
        assertThat(personCreated).isEqualTo(persons.get(23));
    }

    @Test
    void getPersonsByAddressExistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        List<Person> persons = personDao.getPersonsByAddress("892 Downing Ct");
        //CHECK
        assertFalse(persons.isEmpty());
        assertThat(persons)
                .extracting(
                        Person::getFirstName,
                        Person::getLastName,
                        Person::getPhone,
                        Person::getEmail
                        )
                .containsExactly(
                        Tuple.tuple("Sophia", "Zemicks","841-874-7878","soph@email.com"),
                        Tuple.tuple("Warren", "Zemicks","841-874-7512","ward@email.com"),
                        Tuple.tuple("Zach", "Zemicks","841-874-7512","zarc@email.com"));
    }

    @Test
    void getPersonsByAddressNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        List<Person> persons = personDao.getPersonsByAddress("Unknown address");
        //CHECK
        assertTrue(persons.isEmpty());
    }

    @Test
    void getPersonsByCityExistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        List<Person> persons = personDao.getPersonsByCity("Paris");
        //CHECK
        assertFalse(persons.isEmpty());
        assertThat(persons)
                .extracting(
                        Person::getFirstName,
                        Person::getLastName,
                        Person::getEmail
                )
                .containsExactly(
                        Tuple.tuple("Brian", "Stelzer","bstel@email.com"),
                        Tuple.tuple("Shawna", "Stelzer","ssanw@email.com"),
                        Tuple.tuple("Kendrik", "Stelzer","bstel@email.com"));
    }

    @Test
    void getPersonsByCityNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        List<Person> persons = personDao.getPersonsByCity("Niort");
        //CHECK
        assertTrue(persons.isEmpty());
    }

    @Test
    void getPersonsOneExistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        List<Person> persons = personDao.getPersons("Zach", "Zemicks");
        //CHECK
        assertFalse(persons.isEmpty());
        assertThat(persons)
                .extracting(
                        Person::getFirstName,
                        Person::getLastName,
                        Person::getEmail
                )
                .containsExactly(
                        Tuple.tuple("Zach", "Zemicks","zarc@email.com"));
    }

    @Test
    void getPersonsNonexistent() {
        //STUB
        when(dataSource.getData()).thenReturn(stubbedData);
        //ACT
        List<Person> persons = personDao.getPersons("Pierre","Paul");
        //CHECK
        assertTrue(persons.isEmpty());
    }

}