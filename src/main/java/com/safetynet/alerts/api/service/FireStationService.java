package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.model.dto.FireStationPersonsDto;
import com.safetynet.alerts.api.service.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.service.exception.DataIllegalValueException;
import com.safetynet.alerts.api.service.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.FireStation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link com.safetynet.alerts.api.service.IFireStationService} to get,
 * delete or save a fire station mapping from/to a datasource.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class FireStationService implements IFireStationService {

    private final IAlertsDataSource dataSource;
    private final IPersonService personService;
    private final IMedicalRecordService medicalRecordService;
    /**
     * Get a fire station mapping from a datasource.
     *
     * @param address address to which the fire station is mapped.
     *
     * @return the fire station mapping if found.
     */
    @Override
    public Optional<FireStation> getFireStation(String address) {
        List<FireStation> fireStations = dataSource.getData().getFirestations();
        Optional<FireStation> firesStationResult = fireStations.stream()
                                                        .filter(f -> f.getAddress().equalsIgnoreCase(address))
                                                        .findFirst();
        return firesStationResult;
    }

    /**
     * Get the list of addresses covered by one fire station.
     *
     * @param stationNumber the number of the fire station
     *
     * @return a list of addresses, may be empty
     *
     */
     public  List<FireStation> getAddresses(Integer stationNumber){
         List<FireStation> fireStations = dataSource.getData().getFirestations();
         return fireStations.stream()
                            .filter(f -> stationNumber.equals(f.getStation()))
                            .collect(Collectors.toList());
     }

    /**
     * Delete a fire station mapping.
     *
     * @param address address to which the fire station is mapped.
     *
     * @throws DataNotFoundException if no fire station is mapped to the given address.
     *
     */
    @Override
    public void deleteFireStation(String address) throws DataNotFoundException {
        FireStation fireStation;
        Optional<FireStation> firesStationResult = getFireStation(address);
        if(firesStationResult.isPresent()){
            fireStation = firesStationResult.get();
            dataSource.getData().getFirestations().remove(fireStation);
        } else {
            throw new DataNotFoundException("Fire station at " + address);
        }
    }
    /**
     * Update a fire station into a datasource.
     *
     * @param fireStationToUpdate fire station to update.
     *
     * @return updated fire station.
     *
     *  @throws DataNotFoundException if fire station to update does not exist : no fire station at the given address.
     */
    @Override
    public FireStation updateFireStation(FireStation fireStationToUpdate) throws DataNotFoundException {
        FireStation fireStation;
        Optional<FireStation> firesStationResult = getFireStation(fireStationToUpdate.getAddress());
        if (firesStationResult.isPresent()) {
            fireStation = firesStationResult.get();
            fireStation.setStation(fireStationToUpdate.getStation());
            return fireStation;
        } else {
            throw new DataNotFoundException("Fire station at " + fireStationToUpdate.getAddress());
        }
    }

    /**
     * Create a fire station into a datasource.
     *
     * @param fireStationToCreate fire station to create.
     *
     * @return created fire station.
     *
     *  @throws DataAlreadyExistsException if fire station to create already exists at the given address.
     */
    public FireStation createFireStation(FireStation fireStationToCreate) throws DataAlreadyExistsException {
        FireStation fireStation;
        Optional<FireStation> firesStationResult = getFireStation(fireStationToCreate.getAddress());
        if (firesStationResult.isEmpty()) {
            fireStation = new FireStation(fireStationToCreate);
            dataSource.getData().getFirestations().add(fireStation);
            return fireStation;
        } else {
            throw new DataAlreadyExistsException("Fire station at " + fireStationToCreate.getAddress());
        }
    }

    /**
     * Get the list of persons that depends on that fire station.
     *
     * @param stationNumber the number of the fire station
     *
     * @return a FireStationPersonsDto object
     *
     * @throws DataNotFoundException if no fire station with number 'stationNumber' exists in datasource
     */
    public FireStationPersonsDto getPersons(Integer stationNumber) throws DataNotFoundException {
        final List<FireStation> fireStations = this.getAddresses(stationNumber);
        if(! fireStations.isEmpty()){

            FireStationPersonsDto fireStationPersonsDto = new FireStationPersonsDto();
            int numberOfAdults = 0;
            int numberOfChildren = 0;

            for(FireStation fireStation : fireStations){
                List<Person> persons = personService.getPersonsByAddress(fireStation.getAddress());
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
                            long personAge = medicalRecordService.getPersonAge(p.getFirstName(),p.getLastName());
                            if(personAge > 18) {
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
            throw new DataNotFoundException("Fire station of number " + stationNumber);
        }
    }
}
