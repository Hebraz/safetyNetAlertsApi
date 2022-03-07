package com.safetynet.alerts.api.service;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.FireStation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Implementation of {@link com.safetynet.alerts.api.service.IFireStationService} to get,
 * delete or save a fire station mapping from/to a datasource.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FireStationService implements IFireStationService {

    private final IAlertsDataSource dataSource;

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
     * @throws DataNotFoundException if no fire station is mapped to the given address.
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
    public FireStation updateFireStation(FireStation fireStationToUpdate) {
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
    public FireStation createFireStation(FireStation fireStationToCreate) {
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
}
