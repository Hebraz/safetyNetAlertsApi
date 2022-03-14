package com.safetynet.alerts.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FireStationPersonsDto {
    private List<PersonDto> persons;
    private Integer numberOfAdults;
    private Integer numberOfChildren;

    public FireStationPersonsDto(List<PersonDto> persons, Integer numberOfAdults, Integer numberOfChildren) {
        this.persons = persons;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }
}
