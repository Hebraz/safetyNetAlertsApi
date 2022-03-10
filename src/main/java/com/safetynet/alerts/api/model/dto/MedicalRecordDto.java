package com.safetynet.alerts.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonFilter("MedicalRecordDtoFilter")
public class MedicalRecordDto {
    private Date birthdate;
    private List<String> medications;
    private List<String> allergies;

    public MedicalRecordDto(Date birthdate, List<String> medications, List<String> allergies){
        this.birthdate = new Date(birthdate.getTime());
        this.medications = new ArrayList<>(medications);
        this.allergies = new ArrayList<>(allergies);
    }
}
