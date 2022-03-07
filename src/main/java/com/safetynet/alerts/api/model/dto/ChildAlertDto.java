package com.safetynet.alerts.api.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChildAlertDto {
    private List<ChildAlertDto.Child> children;
    private List<ChildAlertDto.Adult> adults;

    public ChildAlertDto() {
        children = new ArrayList<>();
        adults = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Child{
        private String firstName;
        private String lastName;
        private long age;
        private String phone;
    }

    @Getter
    @Setter
    public static class Adult{
        private String firstName;
        private String lastName;
    }
}

