package com.safetynet.alerts.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FireDto {
    private int stationNumber;
    private List<Person> persons;

    public FireDto(){
        persons = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Person{
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private long age;
        private MedicalRecord firedPersonMedicalRecord;
    }

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
