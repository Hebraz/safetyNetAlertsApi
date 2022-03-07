package com.safetynet.alerts.api.controller;

import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.exception.ServiceException;
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

/**
 *  Fire station endpoint
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FireStationController {
    private final IFireStationService fireStationService;
    private final IRequestLogger requestLogger;

    /**
     * Delete - Delete a fire station  mapping.
     *
     * @param address -  address to witch the fire station is mapped.
     *
     * @return  HTTP response with :
     *            - empty Body
     *            - Http status code set to "204-No Content" if the mapping have been deleted.
     *
     * @throws DataNotFoundException if no fire station is mapped to the given address in datasource
     */
    @DeleteMapping("/firestation/{address}")
    public ResponseEntity<String> deleteFireStation(@PathVariable("address") final String address) {
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
     * Create - Add a new fire station mapping to datasource.
     *
     * @param fireStation - An object FireStation
     *
     * @return  HTTP response with :
     *              Body : the created FireStation object
     *              Http status code : "201-Created" if fire station mapping have been created
     *
     * @throws DataAlreadyExistsException if a fire station is already mapped to the given address
     */
    @PostMapping("/firestation")
    public ResponseEntity<FireStation> createFireStation(@RequestBody FireStation fireStation) {
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
     * Update - Update an existing fire station mapping
     * @param fireStation An object FireStation
     *
     * HTTP response with :
     *              Body : the updated FireStation object
     *              Http status code : "200-Ok" if fire station mapping have been updated.
     *
     * @throws DataNotFoundException if no fire station is mapped to given address in datasource
     */
    @PutMapping("/firestation")
    public  ResponseEntity<FireStation>  updateFireStation(@RequestBody FireStation fireStation) {
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
}
