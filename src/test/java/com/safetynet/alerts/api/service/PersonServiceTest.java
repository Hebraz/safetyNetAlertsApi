package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.dao.IFireStationDao;
import com.safetynet.alerts.api.dao.IPersonDao;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.ChildAlertDto;
import com.safetynet.alerts.api.model.dto.FireDto;
import com.safetynet.alerts.api.model.dto.PersonDto;
import com.safetynet.alerts.api.service.dtomapper.IDtoMapper;
import com.safetynet.alerts.api.utils.IAgeUtil;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.Assert.assertThrows;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    private IPersonService personService;
    @Mock
    private IPersonDao personDao;
    @Mock
    private IFireStationDao fireStationDao;
    @Mock
    private IDtoMapper<Person, PersonDto> personDtoMapper;
    @Mock
    private IAgeUtil ageUtil;
    @BeforeEach
    void initializeTest(){
        personService = new PersonService(personDao,fireStationDao,personDtoMapper, ageUtil);
    }

    @Test
    void deletePerson() throws DataNotFoundException {
        //ACT
        personService.deletePerson("Pierre","Paul");
        //CHECK
        verify(personDao, times(1)).deletePerson("Pierre","Paul");
    }

    @Test
    void updatePerson() throws DataNotFoundException {
        //PREPARE
        Person personReturnedByDao = new Person("Pierre","Paul", "10 Rue de la Tour", "Tour", "45000","01215645256", "p.boyd@tourmail.com");
        when(personDao.updatePerson(any(Person.class))).thenReturn(personReturnedByDao);
        //ACT
        Person personToUpdate = new Person("Pierre","Paul", "10 Rue Eiffel", "Paris", "75000","0145804125", "felicia.boyd@parismail.com");
        Person updatedPerson = personService.updatePerson(personToUpdate);
        //CHECK
        verify(personDao, times(1)).updatePerson(personToUpdate);
        assertThat(updatedPerson).isEqualTo(personReturnedByDao);
    }

    @Test
    void createPerson() throws DataAlreadyExistsException {
        //PREPARE
        Person personReturnedByDao = new Person("Pierre","Paul", "10 Rue de la Tour", "Tour", "45000","01215645256", "p.boyd@tourmail.com");
        when(personDao.createPerson(any(Person.class))).thenReturn(personReturnedByDao);
        //ACT
        Person personToCreate = new Person("Pierre","Paul", "10 Rue Eiffel", "Paris", "75000","0145804125", "felicia.boyd@parismail.com");
        Person createdPerson = personService.createPerson(personToCreate);
        //CHECK
        verify(personDao, times(1)).createPerson(personToCreate);
        assertThat(createdPerson).isEqualTo(personReturnedByDao);
    }

    @Test
    void getChildren() {
        //PREPARE
        List<Person> persons =   new ArrayList<>(Arrays.asList(
                new Person("IamAChild18A", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com"),
                new Person("IamAChild2A", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com"),
                new Person("IamAdult", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "ward@email.com"),
                new Person("IamNullAge", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "zarc@email.com")));
        List<PersonDto> personDtos =   new ArrayList<>(Arrays.asList(
                new PersonDto("IamAChild18A", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com",18,null),
                new PersonDto("IamAChild2A", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com",2,null),
                new PersonDto("IamAdult", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "ward@email.com",19,null),
                new PersonDto("IamNullAge", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "zarc@email.com",null,null)));

        when(personDao.getPersonsByAddress(any(String.class))).thenReturn(persons);
        for(int i = 0; i < persons.size(); i++){
            when(personDtoMapper.mapToDto(persons.get(i))).thenReturn(personDtos.get(i));
        }
        when(ageUtil.isAdult(18)).thenReturn(false);
        when(ageUtil.isAdult(2)).thenReturn(false);
        when(ageUtil.isAdult(19)).thenReturn(true);
        //ACT
        ChildAlertDto childAlertDto = personService.getChildren("892 Downing Ct");
        //CHECK
        verify(personDao,times(1)).getPersonsByAddress("892 Downing Ct");
        verify(personDtoMapper,times(2*personDtos.size())).mapToDto(any(Person.class));
        //check children
        assertThat(childAlertDto.getChildren())
                .extracting(PersonDto::getFirstName, PersonDto::getLastName, PersonDto::getAge)
                .containsExactly(Tuple.tuple("IamAChild18A", "Zemicks",18), Tuple.tuple("IamAChild2A", "Zemicks",2));
        //check adults
        assertThat(childAlertDto.getAdults())
                .extracting(PersonDto::getFirstName, PersonDto::getLastName, PersonDto::getAge)
                .containsExactly(Tuple.tuple("IamAdult", "Zemicks",19));
    }

    @Test
    void getFiredPersonsNoBodyNoFireStationAtAddress() throws DataNotFoundException {
        //PREPARE
        List<Person> persons =   new ArrayList<>();
        when(personDao.getPersonsByAddress(any(String.class))).thenReturn(persons);
        when(fireStationDao.getFireStationNumber(any(String.class))).thenThrow(DataNotFoundException.class);
        //ACT
        assertThrows(DataNotFoundException.class,  () -> personService.getFiredPersons("892 Downing Ct"));
        //CHECK
        verify(personDao,times(1)).getPersonsByAddress("892 Downing Ct");
        verify(fireStationDao,times(1)).getFireStationNumber("892 Downing Ct");
        verify(personDtoMapper,never()).mapToDto(any(Person.class));
    }

    @Test
    void getFiredPersonsNoFireStationAtAddressButPeopleYes() throws DataNotFoundException {
        //PREPARE
        List<Person> persons =   new ArrayList<>(Arrays.asList(
                new Person("Pierre", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com"),
                new Person("Paul", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com")));
        List<PersonDto> personDtos =   new ArrayList<>(Arrays.asList(
                new PersonDto("Pierre", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com",18,null),
                new PersonDto("Paul", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com",2,null)));

        when(personDao.getPersonsByAddress(any(String.class))).thenReturn(persons);
        for(int i = 0; i < persons.size(); i++){
            when(personDtoMapper.mapToDto(persons.get(i))).thenReturn(personDtos.get(i));
        }
        when(fireStationDao.getFireStationNumber(any(String.class))).thenThrow(DataNotFoundException.class);

        //ACT
        FireDto fireDto = personService.getFiredPersons("892 Downing Ct");
        //CHECK
        verify(personDao,times(1)).getPersonsByAddress("892 Downing Ct");
        verify(fireStationDao,times(1)).getFireStationNumber("892 Downing Ct");
        verify(personDtoMapper,times(personDtos.size())).mapToDto(any(Person.class));
        //check persons
        assertThat(fireDto.getPersons())
                .extracting(PersonDto::getFirstName, PersonDto::getLastName)
                .containsExactly(Tuple.tuple("Pierre", "Zemicks"), Tuple.tuple("Paul", "Zemicks"));
        //check adults
        assertThat(fireDto.getStationNumber()).isEqualTo(0);
    }

    @Test
    void getFiredPersonsAllOk() throws DataNotFoundException {
        //PREPARE
        List<Person> persons =   new ArrayList<>(Arrays.asList(
                new Person("Pierre", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com"),
                new Person("Paul", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com")));
        List<PersonDto> personDtos =   new ArrayList<>(Arrays.asList(
                new PersonDto("Pierre", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com",18,null),
                new PersonDto("Paul", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com",2,null)));
        int stationNumber = 3;
        when(personDao.getPersonsByAddress(any(String.class))).thenReturn(persons);
        for(int i = 0; i < persons.size(); i++){
            when(personDtoMapper.mapToDto(persons.get(i))).thenReturn(personDtos.get(i));
        }
        when(fireStationDao.getFireStationNumber(any(String.class))).thenReturn(stationNumber);

        //ACT
        FireDto fireDto = personService.getFiredPersons("892 Downing Ct");
        //CHECK
        verify(personDao,times(1)).getPersonsByAddress("892 Downing Ct");
        verify(fireStationDao,times(1)).getFireStationNumber("892 Downing Ct");
        verify(personDtoMapper,times(personDtos.size())).mapToDto(any(Person.class));
        //check persons
        assertThat(fireDto.getPersons())
                .extracting(PersonDto::getFirstName, PersonDto::getLastName)
                .containsExactly(Tuple.tuple("Pierre", "Zemicks"), Tuple.tuple("Paul", "Zemicks"));
        //check adults
        assertThat(fireDto.getStationNumber()).isEqualTo(stationNumber);
    }

    @Test
    void getPersonInfo() {
        //PREPARE
        List<Person> persons =   new ArrayList<>(Arrays.asList(
                new Person("Pierre", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com"),
                new Person("Pierre", "Zemicks", "893 Downing Ct", "Paris", "75000", "841-874-7878", "soph@paris.com")));
        List<PersonDto> personDtos =   new ArrayList<>(Arrays.asList(
                new PersonDto("Pierre", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com",18,null),
                new PersonDto("Pierre", "Zemicks", "893 Downing Ct", "Paris", "7000", "841-874-7878", "soph@paris.com",2,null)));

        when(personDao.getPersons(any(), any())).thenReturn(persons);
        for(int i = 0; i < persons.size(); i++){
            when(personDtoMapper.mapToDto(persons.get(i))).thenReturn(personDtos.get(i));
        }

        //ACT
        List<PersonDto> returnedPersonDtos = personService.getPersonInfo("Pierre", "Zemicks");
        //CHECK
        verify(personDao,times(1)).getPersons("Pierre", "Zemicks");
        verify(personDtoMapper,times(personDtos.size())).mapToDto(any(Person.class));
        //check persons
        assertThat(returnedPersonDtos)
                .extracting(PersonDto::getFirstName, PersonDto::getLastName, PersonDto::getEmail)
                .containsExactly(Tuple.tuple("Pierre", "Zemicks","soph@email.com"), Tuple.tuple("Pierre", "Zemicks", "soph@paris.com"));
    }

    @Test
    void getEmailsByCity() {
        //PREPARE
        List<Person> persons =   new ArrayList<>(Arrays.asList(
                new Person("Pierre", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com"),
                new Person("Pierre", "Zemicks", "893 Downing Ct", "Culver", "75000", "841-874-7878", "soph@paris.com")));
        when(personDao.getPersonsByCity(any())).thenReturn(persons);
        //ACT
        List<String> returnedEmails = personService.getEmailsByCity("Culver");
        //CHECK
        verify(personDao,times(1)).getPersonsByCity("Culver");
        //check persons
        assertThat(returnedEmails)
                 .containsExactly("soph@email.com", "soph@paris.com");
    }
}