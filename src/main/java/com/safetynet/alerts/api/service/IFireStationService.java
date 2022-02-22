package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.Person;

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
     * @throws IllegalArgumentException if no fire station is mapped to the given address.
     *
     */
    public void deleteFireStation(final String address);
    /**
     * Save a fire station into a datasource. If a fire station is already mapped to the given
     * address, then its number is updated. Else it is created and added to the datasource.
     *
     * @param fireStationToSave fire station to save.
     *
     */
    public void saveFireStation(FireStation fireStationToSave);
}

