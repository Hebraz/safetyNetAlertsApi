package com.safetynet.alerts.api.controller;

import com.safetynet.alerts.api.exception.DataAlreadyExistsException;
import com.safetynet.alerts.api.exception.DataNotFoundException;
import com.safetynet.alerts.api.exception.ServiceException;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.service.IPersonService;
import com.safetynet.alerts.api.utils.IRequestLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

/**
 *  Person endpoint
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonController {

    private final IPersonService personService;
    private final IRequestLogger requestLogger;

    /**
     * Delete - Delete a person.
     *
     * @param firstName - The first name of the person to delete
     * @param lastName - The last name of the person to delete
     *
     * @return  HTTP response with :
     *            - empty Body
     *            - Http status code set to "204-No Content" if person have been deleted.
     *
     * @throws DataNotFoundException if no person with the same first name and last name
     *                                exists in datasource
     */
    @DeleteMapping("/person/{firstName}/{lastName}")
    public ResponseEntity<String> deletePerson(@PathVariable("firstName") final String firstName,
                                               @PathVariable("lastName") final String lastName) {
        requestLogger.logRequest("DELETE /person/"+firstName+"/"+lastName);
        try{
            personService.deletePerson(firstName, lastName);
            requestLogger.logResponseSuccess(HttpStatus.NO_CONTENT, null);
            return ResponseEntity.noContent().build();
        } catch (DataNotFoundException e){
            requestLogger.logResponseFailure(e.getHttpStatus(), e.getMessage());
            throw e;
        }
    }

    /**
     * Create - Add a new person.
     *
     * @param person An object Person
     *
     * @return  HTTP response with :
     *              Body : the created Person object.
     *              Http status code : "201-Created" if person have been created.
     *
     * @throws DataAlreadyExistsException if a person with the same first name and last name
     *                                      already exists in datasource
     */
    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        requestLogger.logRequest("POST /person/"+ person.getFirstName()+"/"+person.getLastName());
        try{
            Person createdPerson = personService.createPerson(person);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{firstName}/{lastName}")
                    .buildAndExpand(createdPerson.getFirstName(),createdPerson.getLastName())
                    .toUri();
            requestLogger.logResponseSuccess(HttpStatus.CREATED,null);
            return ResponseEntity.created(location).build();
        } catch (DataAlreadyExistsException e){
            requestLogger.logResponseFailure(e.getHttpStatus() ,e.getMessage());
            throw e;
        }
    }

    /**
     * Update - Update an existing person.
     *
     * @param person An object Person
     *
     * @return  HTTP response with :
     *              Body : the updated Person object.
     *              Http status code :  "200-Ok" if person have been updated.
     *
     * @throws DataNotFoundException if no person with the same first name and last name
     *                                exists in datasource
     */
    @PutMapping("/person")
    public  ResponseEntity<Person>  updatePerson(@RequestBody Person person) {
        requestLogger.logRequest("PUT /person/"+ person.getFirstName()+"/"+person.getLastName());
        try  {
            personService.updatePerson(person);
            requestLogger.logResponseSuccess(HttpStatus.OK,null);
            return ResponseEntity.ok().build();
        } catch (DataNotFoundException e){
            requestLogger.logResponseFailure(e.getHttpStatus() ,e.getMessage());
            throw e;
        }
    }
}
