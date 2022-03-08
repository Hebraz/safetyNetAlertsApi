package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.dao.IFireStationDao;
import com.safetynet.alerts.api.dao.IMedicalRecordDao;
import com.safetynet.alerts.api.dao.IPersonDao;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.FireStationPersonsDto;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataIllegalValueException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.utils.Age;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        Date personBirthdate;
        final List<String> fireStationAddresses = fireStationDao.getAddresses(stationNumber);
        if(! fireStationAddresses.isEmpty()){

            FireStationPersonsDto fireStationPersonsDto = new FireStationPersonsDto();
            int numberOfAdults = 0;
            int numberOfChildren = 0;

            for(String fireStationAddress : fireStationAddresses){
                List<Person> persons = personDao.getPersonsByAddress(fireStationAddress);
                if(! persons.isEmpty()){
                    List<FireStationPersonsDto.FireStationPerson> fireStationPersons =
                        persons.stream().map(p -> {
                            FireStationPersonsDto.FireStationPerson fireStationPerson = new FireStationPersonsDto.FireStationPerson();
                            fireStationPerson.setFirstName(p.getFirstName());
                            fireStationPerson.setLastName(p.getLastName());
                            fireStationPerson.setAddress(p.getAddress());
                            fireStationPerson.setPhone(p.getPhone());
                            return fireStationPerson;
                        }).collect(Collectors.toList());

                    for(Person p : persons){
                        try{
                            personBirthdate = medicalRecordDao.getPersonBirthdate(p.getFirstName(),p.getLastName());
                            if(Age.isAdult(personBirthdate)) {
                                numberOfAdults++;
                            } else {
                                numberOfChildren++;
                            }
                        } catch (DataIllegalValueException | DataNotFoundException e) {
                            log.error("Failed to get the age of " + p.getFirstName() + " " + p.getLastName() + ": " + e.getMessage());
                        }
                    }
                    fireStationPersonsDto.getPersons().addAll(fireStationPersons);
                }
            }
            fireStationPersonsDto.setNumberOfAdults(numberOfAdults);
            fireStationPersonsDto.setNumberOfChildren(numberOfChildren);
            return fireStationPersonsDto;
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
                List<Person> persons = personDao.getPersonsByAddress(fireStationAddress);
                persons.stream().forEach(p -> phones.add(p.getPhone()));
            }
            return phones;
        }else {
            throw new DataNotFoundException("Fire station number " + stationNumber);
        }
    }
}
