package com.safetynet.alerts.api.model.dto;

import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.Person;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FireStationPersonsDto {
    private List<FireStationPerson> persons;
    private Integer numberOfAdults;
    private Integer numberOfChildren;

    public FireStationPersonsDto() {
        persons = new ArrayList<>();
    }
    @Getter
    @Setter
    public static class FireStationPerson{
        private String firstName;
        private String lastName;
        private String address;
        private String phone;
    }
}
