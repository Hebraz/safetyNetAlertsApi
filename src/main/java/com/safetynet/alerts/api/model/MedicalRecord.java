package com.safetynet.alerts.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Medical record entity
 */
public class MedicalRecord {
    private String firstName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date birthdate;
    private List<String> medications;
    private List<String> allergies;

    public MedicalRecord(){}

    public MedicalRecord(MedicalRecord medicalRecord){
        this.firstName = medicalRecord.firstName;
        this.lastName = medicalRecord.lastName;
        this.birthdate = new Date(medicalRecord.birthdate.getTime());
        this.medications = new ArrayList<>(medicalRecord.medications);
        this.allergies = new ArrayList<>(medicalRecord.allergies);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}
