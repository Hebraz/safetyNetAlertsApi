package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.dto.FireStationPersonsDto;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.dto.FloodDto;

import java.util.List;
import java.util.Optional;

/**
 * Get, delete or save a fire station mapping from/to a datasource.
 */
public interface IFireStationService {
    /**
     * Delete a fire station mapping.
     *
     * @param address address to which the fire station is mapped.
     * @throws DataNotFoundException if no fire station is mapped to the given address.
     */
    public void deleteFireStation(final String address) throws DataNotFoundException;
    /**
     * Update a fire station into a datasource.
     *
     * @param fireStationToUpdate fire station to update.
     * @return updated fire station.
     *  @throws DataNotFoundException if fire station to update does not exist : no fire station at the given address.
     */
    public FireStation updateFireStation(FireStation fireStationToUpdate) throws DataNotFoundException;
    /**
     * Create a fire station into a datasource.
     *
     * @param fireStationToCreate fire station to create.
     * @return created fire station.
     *  @throws DataAlreadyExistsException if fire station to create already exists at the given address.
     */
    public FireStation createFireStation(FireStation fireStationToCreate) throws DataAlreadyExistsException;
    /**
     * Get the list of persons that depends on that fire station.
     *
     * @param stationNumber the number of the fire station
     * @return a FireStationPersonsDto object
     * @throws DataNotFoundException if no fire station with number 'stationNumber' exists in datasource
     */
    FireStationPersonsDto getPersons(Integer stationNumber) throws DataNotFoundException;

    /**
     * Get the list of phone numbers of people that depends on the given fire station.
     *
     * @param stationNumber the number of the fire station
     * @return a list of phone numbers
     * @throws DataNotFoundException if no fire station with number 'stationNumber' exists in datasource
     */
    List<String> getPhones(Integer stationNumber) throws DataNotFoundException;
    /**
     * For each given fire station, get the list of homes that depends on it,
     * Home is defined by a list of persons that leave at same address, their medical record.
     *
     * @param stations list of station numbers
     * @retun an object {@link com.safetynet.alerts.api.model.dto.FloodDto}
     */
    FloodDto getFloodHomes(List<Integer> stations);
}

