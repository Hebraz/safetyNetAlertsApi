package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.dao.IFireStationDao;
import com.safetynet.alerts.api.dao.IMedicalRecordDao;
import com.safetynet.alerts.api.dao.IPersonDao;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.FireStationPersonsDto;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.dto.FloodDto;
import com.safetynet.alerts.api.model.dto.PersonDto;
import com.safetynet.alerts.api.service.dtomapper.IDtoMapper;
import com.safetynet.alerts.api.utils.Age;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link IFireStationService} to get,
 * delete or save a fire station mapping from/to a datasource.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class FireStationService implements IFireStationService {

    private final IFireStationDao fireStationDao;
    private final IPersonDao personDao;
    private final IMedicalRecordDao medicalRecordDao;
    private final IDtoMapper<Person,PersonDto> personDtoMapper;
    /**
     * Delete a fire station mapping.
     *
     * @param address address to which the fire station is mapped.
     * @throws DataNotFoundException if no fire station is mapped to the given address.
     *
     */
    @Override
    public void deleteFireStation(String address) throws DataNotFoundException {
        fireStationDao.deleteFireStation(address);
    }
    /**
     * Update a fire station into a datasource.
     *
     * @param fireStationToUpdate fire station to update.
     * @return updated fire station.
     * @throws DataNotFoundException if fire station to update does not exist : no fire station at the given address.
     */
    @Override
    public FireStation updateFireStation(FireStation fireStationToUpdate) throws DataNotFoundException {
        return fireStationDao.updateFireStation(fireStationToUpdate);
    }

    /**
     * Create a fire station into a datasource.
     *
     * @param fireStationToCreate fire station to create.
     * @return created fire station.
     * @throws DataAlreadyExistsException if fire station to create already exists at the given address.
     */
    public FireStation createFireStation(FireStation fireStationToCreate) throws DataAlreadyExistsException {
        return fireStationDao.createFireStation(fireStationToCreate);
    }

    /**
     * Get the list of persons that depends on that fire station.
     *
     * @param stationNumber the number of the fire station
     * @return a FireStationPersonsDto object
     * @throws DataNotFoundException if no fire station with number 'stationNumber' exists in datasource
     */
    public FireStationPersonsDto getPersons(Integer stationNumber) throws DataNotFoundException {
        int numberOfAdults = 0;
        int numberOfChildren = 0;
        final List<PersonDto> personDtos= new ArrayList<>();

        final List<String> fireStationAddresses = fireStationDao.getAddresses(stationNumber);
        if(! fireStationAddresses.isEmpty()){
            /*For each address covered by the fire station, get all persons (Dto) that lives at that address
            and add them to the personDtos list*/
            for(String fireStationAddress : fireStationAddresses){
                personDtos.addAll(
                        personDao.getPersonsByAddress(fireStationAddress)
                                .stream()
                                .map(p -> personDtoMapper.mapToDto(p))
                                .collect(Collectors.toList()));
            }
            numberOfAdults = (int)personDtos.stream()
                    .filter(p -> Objects.nonNull(p.getAge()) && Age.isAdult(p.getAge()))
                    .count();
            numberOfChildren = (int)personDtos.stream()
                    .filter(p -> Objects.nonNull(p.getAge()) && !Age.isAdult(p.getAge()))
                    .count();
            return new FireStationPersonsDto(personDtos, numberOfAdults,numberOfChildren);
        } else {
            throw new DataNotFoundException("Fire station number " + stationNumber);
        }
    }

    /**
     * Get the list of phone numbers of people that depends on the given fire station.
     *
     * @param stationNumber the number of the fire station
     * @return a list of phone numbers
     * @throws DataNotFoundException if no fire station with number 'stationNumber' exists in datasource
     */
    @Override
    public List<String> getPhones(Integer stationNumber) throws DataNotFoundException {
        List<String> phones = new ArrayList<>();
        final List<String> fireStationAddresses = fireStationDao.getAddresses(stationNumber);
        if (!fireStationAddresses.isEmpty()) {
            for (String fireStationAddress : fireStationAddresses) {
                //Get all personDtos that lives at current address and add their phone to the phones list
                phones.addAll(
                        personDao.getPersonsByAddress(fireStationAddress)
                                .stream()
                                .map(p -> p.getPhone())
                                .distinct()
                                .collect(Collectors.toList()));
            }
            return phones.stream().distinct().collect(Collectors.toList());
        }else {
            throw new DataNotFoundException("Fire station number " + stationNumber);
        }
    }

    /**
     * For each given fire station, get the list of homes that depends on it,
     * Home is defined by a list of persons that leave at same address, their medical record.
     *
     * @param stations list of station numbers
     * @retun List of object {@link FloodDto}
     */
    @Override
    public  List<FloodDto> getFloodHomes(List<Integer> stations) {
        List<PersonDto> personDtos;
        List<FloodDto> floodDtos = new ArrayList<>();

        for(Integer stationNumber : stations){
            for(String address : fireStationDao.getAddresses(stationNumber))
            {
                personDtos = personDao.getPersonsByAddress(address)
                        .stream()
                        .map(p -> personDtoMapper.mapToDto(p))
                        .collect(Collectors.toList());

                floodDtos.add(new FloodDto(address,personDtos));
            }
        }
        return floodDtos;
    }
}
