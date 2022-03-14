package com.safetynet.alerts.api.service.dtomapper;

import com.safetynet.alerts.api.dao.IMedicalRecordDao;
import com.safetynet.alerts.api.exception.DataIllegalValueException;
import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.MedicalRecordDto;
import com.safetynet.alerts.api.model.dto.PersonDto;
import com.safetynet.alerts.api.utils.IAgeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonDtoMapperTest {

    private IDtoMapper<Person, PersonDto> personDtoMapper;
    private  MedicalRecord medicalRecord;
    private  MedicalRecordDto medicalRecordDto;
    private Person person;

    @Mock
    private  IMedicalRecordDao medicalRecordDao;
    @Mock
    private  IDtoMapper<MedicalRecord, MedicalRecordDto> medicalRecordDtoMapper;
    @Mock
    private  IAgeUtil ageUtil;

    @BeforeEach
    void initializeTest(){
        personDtoMapper = new PersonDtoMapper(medicalRecordDao, medicalRecordDtoMapper, ageUtil);
        person = new Person("Pierre","Paul","100 Av De Gaulle", "Paris", "75000", "0145100203","pierre.paul@gmail.com");
        medicalRecord = new MedicalRecord(
                "Pierre",
                "Paul",
                Date.from(Instant.parse("1980-04-09T00:00:00Z")),
                List.of("aspirine:200mg","ibu:50mg"),
                List.of("lactose","fructose"));
        medicalRecordDto = new MedicalRecordDto(
                Date.from(Instant.parse("1980-04-09T00:00:00Z")),
                List.of("aspirine:200mg","ibu:50mg"),
                List.of("lactose","fructose"));
    }
    @Test
    void mapToDtoNoMedicalRecord() {
        //PREPARE
        when(medicalRecordDao.getMedicalRecord(any(),any())).thenReturn(Optional.empty());

        //ACT
        PersonDto personDto = personDtoMapper.mapToDto(person);

        assertThat(personDto).extracting(
                PersonDto::getFirstName,
                PersonDto::getLastName,
                PersonDto::getAddress,
                PersonDto::getCity,
                PersonDto::getZip,
                PersonDto::getPhone,
                PersonDto::getEmail,
                PersonDto::getAge,
                PersonDto::getMedicalRecord)
                .containsExactly(
                        "Pierre","Paul","100 Av De Gaulle", "Paris", "75000", "0145100203","pierre.paul@gmail.com",null,null
                );
    }

    @Test
    void mapToDtoMedicalFailToComputeAge() throws DataIllegalValueException {
        //PREPARE
       when(medicalRecordDao.getMedicalRecord(any(),any())).thenReturn(Optional.of(medicalRecord));
       when(medicalRecordDtoMapper.mapToDto(medicalRecord)).thenReturn(medicalRecordDto);
       when(ageUtil.computeFromBirthdate(any())).thenThrow(DataIllegalValueException.class);

        //ACT
        PersonDto personDto = personDtoMapper.mapToDto(person);

        assertThat(personDto).extracting(
                        PersonDto::getFirstName,
                        PersonDto::getLastName,
                        PersonDto::getAddress,
                        PersonDto::getCity,
                        PersonDto::getZip,
                        PersonDto::getPhone,
                        PersonDto::getEmail,
                        PersonDto::getAge)
                .containsExactly(
                        "Pierre","Paul","100 Av De Gaulle", "Paris", "75000", "0145100203","pierre.paul@gmail.com",null
                );

        assertThat(personDto.getMedicalRecord()).extracting(
                        m -> m.getBirthdate().toInstant().toString(),
                        MedicalRecordDto::getMedications,
                        MedicalRecordDto::getAllergies)
                .containsExactly(
                        "1980-04-09T00:00:00Z",
                        List.of("aspirine:200mg","ibu:50mg"),
                        List.of("lactose","fructose")
                );
    }

    @Test
    void mapToDtoMedicalComputeAgeOk() throws DataIllegalValueException {
        //PREPARE
        when(medicalRecordDao.getMedicalRecord(any(),any())).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDtoMapper.mapToDto(medicalRecord)).thenReturn(medicalRecordDto);
        when(ageUtil.computeFromBirthdate(any())).thenReturn(10);

        //ACT
        PersonDto personDto = personDtoMapper.mapToDto(person);

        assertThat(personDto).extracting(
                        PersonDto::getFirstName,
                        PersonDto::getLastName,
                        PersonDto::getAddress,
                        PersonDto::getCity,
                        PersonDto::getZip,
                        PersonDto::getPhone,
                        PersonDto::getEmail,
                        PersonDto::getAge)
                .containsExactly(
                        "Pierre","Paul","100 Av De Gaulle", "Paris", "75000", "0145100203","pierre.paul@gmail.com",10
                );

        assertThat(personDto.getMedicalRecord()).extracting(
                        m -> m.getBirthdate().toInstant().toString(),
                        MedicalRecordDto::getMedications,
                        MedicalRecordDto::getAllergies)
                .containsExactly(
                        "1980-04-09T00:00:00Z",
                        List.of("aspirine:200mg","ibu:50mg"),
                        List.of("lactose","fructose")
                );
    }
}