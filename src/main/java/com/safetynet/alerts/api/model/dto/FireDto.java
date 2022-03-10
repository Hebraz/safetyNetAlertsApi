package com.safetynet.alerts.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FireDto {
    private int stationNumber;
    private List<PersonDto> persons;

    public FireDto(){
        persons = new ArrayList<>();
    }

    public FireDto(int stationNumber, List<PersonDto> persons) {
        this.stationNumber = stationNumber;
        this.persons = persons;
    }
}
