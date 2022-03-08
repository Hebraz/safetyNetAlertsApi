package com.safetynet.alerts.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class PersonInfoDto {

        private String firstName;
        private String lastName;
        private String address;
        private String city;
        private String zip;
        private String email;
        private long age;
        private MedicalRecord medicalRecord;

    @Getter
    @Setter
    public static class MedicalRecord{
        private List<String> medications;
        private List<String> allergies;

        public MedicalRecord(){
            medications = new ArrayList<>();
            allergies = new ArrayList<>();
        }
    }
}
