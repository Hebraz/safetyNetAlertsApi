package com.safetynet.alerts.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@JsonFilter("PersonDtoFilter")
public class PersonDto {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
    @Nullable
    private Integer age;
    private MedicalRecordDto medicalRecord;

    public PersonDto(String firstName, String lastName, String address, String city, String zip, String phone, String email, Integer age, MedicalRecordDto medicalRecord) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.medicalRecord = medicalRecord;
    }
}
