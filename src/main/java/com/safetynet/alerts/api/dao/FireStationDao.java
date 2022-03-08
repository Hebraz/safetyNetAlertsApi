package com.safetynet.alerts.api.dao;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FireStationDao implements IFireStationDao{

    private final IAlertsDataSource dataSource;

    /**
     * Get a fire station mapping from a datasource.
     *
     * @param address address to which the fire station is mapped.
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
     * @return a list of addresses, may be empty
     *
     */
    @Override
    public  List<String> getAddresses(Integer stationNumber){
        List<FireStation> fireStations = dataSource.getData().getFirestations();
        return fireStations.stream()
                .filter(f -> stationNumber.equals(f.getStation()))
                .map(f -> f.getAddress())
                .collect(Collectors.toList());
    }

    /**
     * Delete a fire station mapping.
     *
     * @param address address to which the fire station is mapped.
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
     * @return updated fire station.
     * @throws DataNotFoundException if fire station to update does not exist : no fire station at the given address.
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
     * @return created fire station.
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
     * Get a fire station number by address.
     *
     * @param address address to which the fire station is mapped.
     * @return the fire station number
     * @throws DataNotFoundException if no fire station at given address exists in datasource
     */
    @Override
    public int getFireStationNumber(String address) throws DataNotFoundException {
        Optional<FireStation> fireStationResult = this.getFireStation(address);
        if(fireStationResult.isPresent()){
            return fireStationResult.get().getStation();
        } else {
            throw new DataNotFoundException("Fire station at address " + address);
        }
    }

}
