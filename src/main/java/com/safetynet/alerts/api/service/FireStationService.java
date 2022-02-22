package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.AlertsDataSource;
import com.safetynet.alerts.api.model.FireStation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
/**
 * Implementation of {@link com.safetynet.alerts.api.service.IFireStationService} to get,
 * delete or save a fire station mapping from/to a datasource.
 */
public class FireStationService implements IFireStationService {

    @Autowired
    private AlertsDataSource dataSource;

    /**
     * Get a fire station mapping from a datasource.
     *
     * @param address address to witch the fire station is mapped.
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
     * Delete a fire station mapping.
     *
     * @param address address to witch the fire station is mapped.
     *
     * @throws IllegalArgumentException if no fire station is mapped to the given address.
     *
     */
    @Override
    public void deleteFireStation(String address) {
        FireStation fireStation;
        Optional<FireStation> firesStationResult = getFireStation(address);
        if(firesStationResult.isPresent()){
            fireStation = firesStationResult.get();
            dataSource.getData().getFirestations().remove(fireStation);
        } else {
            throw new IllegalArgumentException("No fire station is mapped to address: " + address);
        }
    }
    /**
     * Save a fire station into a datasource. If a fire station is already mapped to the given
     * address, then its number is updated. Else it is created and added to the datasource.
     *
     * @param fireStationToSave fire station to save.
     *
     */
    @Override
    public void saveFireStation(FireStation fireStationToSave) {
        FireStation fireStation;
        Optional<FireStation> firesStationResult = getFireStation(fireStationToSave.getAddress());
        if (firesStationResult.isPresent()) {
            fireStation = firesStationResult.get();
            fireStation.setStation(fireStationToSave.getStation());
        } else {
            fireStation = new FireStation(fireStationToSave);
            dataSource.getData().getFirestations().add(fireStation);
        }
    }
}
