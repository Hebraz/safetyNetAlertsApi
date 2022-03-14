package com.safetynet.alerts.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChildAlertDto {
    @JsonFilter("ChildAlertDtoChildrenFilter")
    private List<PersonDto> children;
    @JsonFilter("ChildAlertDtoAdultFilter")
    private List<PersonDto> adults;

    public ChildAlertDto(List<PersonDto> children, List<PersonDto> adults) {
        this.children = children;
        this.adults = adults;
    }
}

