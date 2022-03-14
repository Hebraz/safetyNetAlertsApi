package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.dao.IFireStationDao;
import com.safetynet.alerts.api.dao.IPersonDao;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.FireStationPersonsDto;
import com.safetynet.alerts.api.model.dto.FloodDto;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FireStationServiceTest {
    private IFireStationService fireStationService;
    @Mock
    private IPersonDao personDao;
    @Mock
    private IFireStationDao fireStationDao;
    @Mock
    private IDtoMapper<Person, PersonDto> personDtoMapper;
    @Mock
    private IAgeUtil ageUtil;

    private  Map<String, List<Person>> addressPersonsMap;
    private  Map<String, List<PersonDto>> addressPersonDtosMap;
    private final String ADDRESS_1 = "892 Downing Ct";
    private final String ADDRESS_2 = "947 E. Rose Dr";
    private final String ADDRESS_3 = "951 LoneTree Rd";
    private final int AGE_CHILD = 10;
    private final int AGE_ADULT = 20;
    @BeforeEach
    void initializeTest(){
        fireStationService = new FireStationService(fireStationDao,personDao, personDtoMapper, ageUtil);

        addressPersonsMap =   new HashMap<>() {{
            put(ADDRESS_1,
                    new ArrayList<Person>(Arrays.asList(
                            new Person("IamAChild18A", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com"),
                            new Person("IamAdult", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "ward@email.com"),
                            new Person("IamNullAge", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "zarc@email.com"))));
            put(ADDRESS_2,
                    new ArrayList<Person>(Arrays.asList(
                        new Person("Brian", "Stelzer", "947 E. Rose Dr","Culver", "97451","841-874-7512", "bstel@email.com" ),
                        new Person("Shawna", "Stelzer", "947 E. Rose Dr","Culver", "97451", "841-874-7513", "zarc@email.com"),
                        new Person("IamNullAge", "Zemicks", "947 E. Rose Dr","Culver", "97451", "841-874-7514", "zarc@email.com"))));
            put(ADDRESS_3,
                    new ArrayList<Person>(Arrays.asList(
                            new Person("Eric", "Cadigan", "951 LoneTree Rd","Culver", "97451","841-874-7512", "bstel@email.com" ))));

        }};

        addressPersonDtosMap =   new HashMap<>() {{
            put(ADDRESS_1,
                    new ArrayList<PersonDto>(Arrays.asList(
                        new PersonDto("IamAChild18A", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com", AGE_CHILD,null),
                        new PersonDto("IamAdult", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "ward@email.com",AGE_ADULT,null),
                        new PersonDto("IamNullAge", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "zarc@email.com", null,null))));
            put(ADDRESS_2,
                    new ArrayList<PersonDto>(Arrays.asList(
                        new PersonDto("Brian", "Stelzer", "947 E. Rose Dr","Culver", "97451","841-874-7512", "bstel@email.com", AGE_CHILD, null ),
                        new PersonDto("Shawna", "Stelzer", "947 E. Rose Dr","Culver", "97451", "841-874-7513", "zarc@email.com", AGE_CHILD, null ),
                        new PersonDto("Kendrik", "Stelzer", "947 E. Rose Dr","Culver", "97451", "841-874-7514", "zarc@email.com", AGE_ADULT, null ))));
            put(ADDRESS_3,
                    new ArrayList<PersonDto>(Arrays.asList(
                            new PersonDto("Eric", "Cadigan", "951 LoneTree Rd","Culver", "97451","841-874-7512", "bstel@email.com", AGE_ADULT, null ))));

        }};
    }
    @Test
    void deleteFireStation() throws DataNotFoundException {
        //ACT
        fireStationService.deleteFireStation("145 Rue Eilffel");
        //CHECK
        verify(fireStationDao, times(1)).deleteFireStation("145 Rue Eilffel");
    }

    @Test
    void updateFireStation() throws DataNotFoundException {
        //PREPARE
        FireStation stationReturnedByDao = new FireStation("145 Rue Eiffel",1);
        when(fireStationDao.updateFireStation(any(FireStation.class))).thenReturn(stationReturnedByDao);
        //ACT
        FireStation stationToUpdate =new FireStation("145 Rue Eiffel",6);
        FireStation updatedStation = fireStationService.updateFireStation(stationToUpdate);
        //CHECK
        verify(fireStationDao, times(1)).updateFireStation(stationToUpdate);
        assertThat(updatedStation).isEqualTo(stationReturnedByDao);
    }

    @Test
    void createFireStation() throws DataAlreadyExistsException {
        //PREPARE
        FireStation stationReturnedByDao = new FireStation("145 Rue Eiffel",1);
        when(fireStationDao.createFireStation(any(FireStation.class))).thenReturn(stationReturnedByDao);
        //ACT
        FireStation stationToCreate =new FireStation("145 Rue Eiffel",6);
        FireStation createdStation = fireStationService.createFireStation(stationToCreate);
        //CHECK
        verify(fireStationDao, times(1)).createFireStation(stationToCreate);
        assertThat(createdStation).isEqualTo(stationReturnedByDao);
    }

    @Test
    void getPersonsOk() throws DataNotFoundException {
        //PREPARE
        List<String> addresses = List.of(ADDRESS_1,ADDRESS_2);
        when(fireStationDao.getAddresses(any())).thenReturn(addresses);
        for(String address : addresses){
            when(personDao.getPersonsByAddress(address)).thenReturn(addressPersonsMap.get(address));
            List<Person> persons = addressPersonsMap.get(address);
            List<PersonDto> personDtos = addressPersonDtosMap.get(address);
            for(int i = 0; i < persons.size(); i++){
                when(personDtoMapper.mapToDto(persons.get(i))).thenReturn(personDtos.get(i));
            }
        }
        when(ageUtil.isAdult(AGE_CHILD)).thenReturn(false);
        when(ageUtil.isAdult(AGE_ADULT)).thenReturn(true);

        //ACT
        FireStationPersonsDto fireStationPersonsDto = fireStationService.getPersons(88);

        //CHECK
        verify(fireStationDao,times(1)).getAddresses(88);
        verify(personDao,times(addresses.size())).getPersonsByAddress(any());

        //check children
        assertThat(fireStationPersonsDto.getPersons())
                .extracting(PersonDto::getFirstName, PersonDto::getLastName, PersonDto::getAge)
                .containsExactly(
                        Tuple.tuple("IamAChild18A", "Zemicks", AGE_CHILD),
                        Tuple.tuple("IamAdult", "Zemicks",AGE_ADULT),
                        Tuple.tuple("IamNullAge", "Zemicks",null),
                        Tuple.tuple("Brian", "Stelzer",AGE_CHILD),
                        Tuple.tuple("Shawna", "Stelzer", AGE_CHILD),
                        Tuple.tuple("Kendrik", "Stelzer",AGE_ADULT));
        //check adults
        assertThat(fireStationPersonsDto.getNumberOfAdults()).isEqualTo(2);
        assertThat(fireStationPersonsDto.getNumberOfChildren()).isEqualTo(3);
    }
    @Test
    void getPersonsUnknownFireStation() throws DataNotFoundException {
        //PREPARE
        when(fireStationDao.getAddresses(any())).thenReturn(List.of());

        //ACT
        assertThrows(DataNotFoundException.class,() -> fireStationService.getPersons(88));

        //CHECK
        verify(fireStationDao,times(1)).getAddresses(88);
    }

    @Test
    void getPhonesOk() throws DataNotFoundException {
        //PREPARE
        List<String> addresses = List.of(ADDRESS_1,ADDRESS_2);
        when(fireStationDao.getAddresses(any())).thenReturn(List.of(ADDRESS_1,ADDRESS_2));
        for(String address : addresses){
            when(personDao.getPersonsByAddress(address)).thenReturn(addressPersonsMap.get(address));
        }

        //ACT
        List<String> phones = fireStationService.getPhones(88);

        //CHECK
        verify(fireStationDao,times(1)).getAddresses(88);
        verify(personDao,times(addresses.size())).getPersonsByAddress(any());
        //check children
        assertThat(phones)
                .containsExactly(
                        "841-874-7878",
                        "841-874-7512",
                        "841-874-7513",
                        "841-874-7514");
    }

    @Test
    void getPhoneUnknownStation() throws DataNotFoundException {
        //PREPARE
        when(fireStationDao.getAddresses(any())).thenReturn(List.of());

        //ACT
        assertThrows(DataNotFoundException.class,() -> fireStationService.getPhones(88));

        //CHECK
        verify(fireStationDao,times(1)).getAddresses(88);
    }

    @Test
    void getFloodHomes() {
        //PREPARE
        List<Integer> stations = List.of(1,2);

        when(fireStationDao.getAddresses(1)).thenReturn(List.of(ADDRESS_1,ADDRESS_2));
        when(fireStationDao.getAddresses(2)).thenReturn(List.of(ADDRESS_3));

        for(String address : addressPersonsMap.keySet()) {
            when(personDao.getPersonsByAddress(address)).thenReturn(addressPersonsMap.get(address));
            List<Person> persons = addressPersonsMap.get(address);
            List<PersonDto> personDtos = addressPersonDtosMap.get(address);
            for (int i = 0; i < persons.size(); i++) {
                when(personDtoMapper.mapToDto(persons.get(i))).thenReturn(personDtos.get(i));
            }
        }

        //ACT
        List<FloodDto> floodDtos = fireStationService.getFloodHomes(stations);

        //CHECK
        verify(fireStationDao,times(2)).getAddresses(any());

        //check station 1 ADDDRESS_1
        assertThat(floodDtos.get(0).getPersons())
                .extracting(PersonDto::getFirstName, PersonDto::getLastName, PersonDto::getAge)
                .containsExactly(
                        Tuple.tuple("IamAChild18A", "Zemicks", AGE_CHILD),
                        Tuple.tuple("IamAdult", "Zemicks",AGE_ADULT),
                        Tuple.tuple("IamNullAge", "Zemicks",null));
        //check station 1 ADDDRESS_2
        assertThat(floodDtos.get(1).getPersons())
                .extracting(PersonDto::getFirstName, PersonDto::getLastName, PersonDto::getAge)
                .containsExactly(
                        Tuple.tuple("Brian", "Stelzer",AGE_CHILD),
                        Tuple.tuple("Shawna", "Stelzer", AGE_CHILD),
                        Tuple.tuple("Kendrik", "Stelzer",AGE_ADULT));
       //check station 2 ADDDRESS_3
            assertThat(floodDtos.get(2).getPersons())
                    .extracting(PersonDto::getFirstName, PersonDto::getLastName, PersonDto::getAge)
                    .containsExactly(
                            Tuple.tuple("Eric", "Cadigan", AGE_ADULT));
    }
}