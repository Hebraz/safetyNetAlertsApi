package com.safetynet.alerts.api.dao;

import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IFireStationDao {
    /**
     * Get a fire station mapping from a datasource.
     *
     * @param address address to which the fire station is mapped.
     * @return the fire station mapping if found.
     */
    public Optional<FireStation> getFireStation(final String address);

    /**
     * Get the list of addresses covered by one fire station.
     *
     * @param stationNumber the number of the fire station
     * @return a list of addresses, may be empty
     *
     */
    List<String> getAddresses(Integer stationNumber);

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
     * Get a fire station number by address.
     *
     * @param address address to which the fire station is mapped.
     * @return the fire station number
     * @throws DataNotFoundException if no fire station at given address exists in datasource
     */
    int getFireStationNumber(String address) throws DataNotFoundException;
}
