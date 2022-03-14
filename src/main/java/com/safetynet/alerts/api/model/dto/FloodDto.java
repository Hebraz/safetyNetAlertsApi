package com.safetynet.alerts.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FloodDto {
    private String address;
    private List<PersonDto> persons;

    public FloodDto(String address, List<PersonDto> persons) {
        this.address = address;
        this.persons = persons;
    }
}
