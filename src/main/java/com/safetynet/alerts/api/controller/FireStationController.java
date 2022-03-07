package com.safetynet.alerts.api.controller;

import com.safetynet.alerts.api.model.dto.FireStationPersonsDto;
import com.safetynet.alerts.api.service.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.service.exception.DataNotFoundException;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.service.IFireStationService;
import com.safetynet.alerts.api.utils.IRequestLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 *  Fire station endpoint
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FireStationController {
    private final IFireStationService fireStationService;
    private final IRequestLogger requestLogger;

    /**
     * Delete a fire station  mapping.
     *
     * @param address -  address to which the fire station is mapped.
     *
     * @return  HTTP response with :
     *            - empty Body
     *            - Http status code set to "204-No Content" if the mapping have been deleted.
     *
     * @throws DataNotFoundException if no fire station is mapped to the given address in datasource
     */
    @DeleteMapping("/firestation/{address}")
    public ResponseEntity<String> deleteFireStation(@PathVariable("address") final String address) throws DataNotFoundException {
        requestLogger.logRequest("DELETE /firestation/"+address);
        try{
            fireStationService.deleteFireStation(address);
            requestLogger.logResponseSuccess(HttpStatus.NO_CONTENT, null);
            return ResponseEntity.noContent().build();
        } catch (DataNotFoundException e){
            requestLogger.logResponseFailure(e.getHttpStatus(), e.getMessage());
            throw e;
        }
    }

    /**
     * Add a new fire station mapping to datasource.
     *
     * @param fireStation - An object FireStation
     *
     * @return  HTTP response with :
     *              empty Body
     *              Http status code : "201-Created" if fire station mapping have been created
     *
     * @throws DataAlreadyExistsException if a fire station is already mapped to the given address
     */
    @PostMapping("/firestation")
    public ResponseEntity<String> createFireStation(@RequestBody FireStation fireStation) throws DataAlreadyExistsException {
        requestLogger.logRequest("POST /firestation/"+ fireStation.getAddress());
        try{
            FireStation createdFireStation = fireStationService.createFireStation(fireStation);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{address}")
                    .buildAndExpand(createdFireStation.getAddress())
                    .toUri();
            requestLogger.logResponseSuccess(HttpStatus.CREATED,null);
            return ResponseEntity.created(location).build();
        } catch (DataAlreadyExistsException e){
            requestLogger.logResponseFailure(e.getHttpStatus() ,e.getMessage());
            throw e;
        }
    }

    /**
     * Update an existing fire station mapping
     * @param fireStation An object FireStation
     *
     * HTTP response with :
     *              empty Body
     *              Http status code : "200-Ok" if fire station mapping have been updated.
     *
     * @throws DataNotFoundException if no fire station is mapped to given address in datasource
     */
    @PutMapping("/firestation")
    public  ResponseEntity<String>  updateFireStation(@RequestBody FireStation fireStation) throws DataNotFoundException {
        requestLogger.logRequest("PUT /firestation/"+ fireStation.getAddress());
        try  {
            fireStationService.updateFireStation(fireStation);
            requestLogger.logResponseSuccess(HttpStatus.OK,null);
            return ResponseEntity.ok().build();
        } catch (DataNotFoundException e){
            requestLogger.logResponseFailure(e.getHttpStatus() ,e.getMessage());
            throw e;
        }
    }

    /**
     * Get the list of persons that depends on the given fire station.
     *
     * @param stationNumber the number of the fire station
     *
     * HTTP response with :
     *              Body : an object {@link com.safetynet.alerts.api.model.dto.FireStationPersonsDto}
     *              Http status code : "200-Ok" .
     *
     * @throws DataNotFoundException if no fire station with number 'stationNumber' exists in datasource
     */
    @GetMapping("/firestation")
    public ResponseEntity<FireStationPersonsDto> getFireStationPersons(@RequestParam Integer stationNumber) throws DataNotFoundException {
        requestLogger.logRequest("GET /firestation?stationNumber="+ stationNumber);
        try{
            FireStationPersonsDto fireStationPersons = fireStationService.getPersons(stationNumber);
            requestLogger.logResponseSuccess(HttpStatus.OK ,"");
            return ResponseEntity.ok(fireStationPersons);
        } catch (DataNotFoundException e){
            requestLogger.logResponseFailure(e.getHttpStatus() ,e.getMessage());
            throw e;
        }
    }

    /**
     * Get the list of phone numbers of people that depend on the given fire station.
     *
     * @param firestation the number of the fire station
     *
     * HTTP response with :
     *              Body : a list of phone numbers
     *              Http status code : "200-Ok" .
     *
     * @throws DataNotFoundException if no fire station with number 'stationNumber' exists in datasource
     */
    @GetMapping("/phoneAlert")
    public ResponseEntity<List<String>> getPhoneAlert(@RequestParam Integer firestation) throws DataNotFoundException {
        requestLogger.logRequest("GET /phoneAlert?firestation="+ firestation);
        try{
            List<String> phones = fireStationService.getPhones(firestation);
            requestLogger.logResponseSuccess(HttpStatus.OK ,"");
            return ResponseEntity.ok(phones);
        } catch (DataNotFoundException e){
            requestLogger.logResponseFailure(e.getHttpStatus() ,e.getMessage());
            throw e;
        }
    }
}
