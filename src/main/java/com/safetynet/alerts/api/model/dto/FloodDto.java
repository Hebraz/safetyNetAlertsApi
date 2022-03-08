package com.safetynet.alerts.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FloodDto {
    private List<FireStation> fireStations;

    public FloodDto(){
        fireStations = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class FireStation{
        private int stationNumber;
        private List<Home> homes;

        public FireStation(){
            homes = new ArrayList<>();
        }
    }

    @Getter
    @Setter
    public static class Home {
        private String address;
        private List<Person> persons;

        public Home(){
            persons = new ArrayList<>();
        }

    }

    @Getter
    @Setter
    public static class Person{
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private long age;
        private MedicalRecord medicalRecord;
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
