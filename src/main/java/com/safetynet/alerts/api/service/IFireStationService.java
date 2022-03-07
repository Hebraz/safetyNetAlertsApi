package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import java.util.Optional;

/**
 * Get, delete or save a fire station mapping from/to a datasource.
 */
public interface IFireStationService {
    /**
     * Get a fire station mapping from a datasource.
     *
     * @param address address to witch the fire station is mapped.
     *
     * @return the fire station mapping if found.
     */
    public Optional<FireStation> getFireStation(final String address);
    /**
     * Delete a fire station mapping.
     *
     * @param address address to witch the fire station is mapped.
     *
     * @throws DataNotFoundException if no fire station is mapped to the given address.
     *
     */
    public void deleteFireStation(final String address);
    /**
     * Update a fire station into a datasource.
     *
     * @param fireStationToUpdate fire station to update.
     *
     * @return updated fire station.
     *
     *  @throws DataNotFoundException if fire station to update does not exist : no fire station at the given address.
     */
    public FireStation updateFireStation(FireStation fireStationToUpdate);
    /**
     * Create a fire station into a datasource.
     *
     * @param fireStationToCreate fire station to create.
     *
     * @return created fire station.
     *
     *  @throws DataAlreadyExistsException if fire station to create already exists at the given address.
     */
    public FireStation createFireStation(FireStation fireStationToCreate);
}

