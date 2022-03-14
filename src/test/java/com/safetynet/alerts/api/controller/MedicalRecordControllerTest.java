package com.safetynet.alerts.api.controller;

import com.safetynet.alerts.api.Json;
import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IAlertsDataSource dataSource;

    @Value( "${datasource.filepath}" )
    private String dataSourceFilePath;

    @BeforeEach
    private void reloadDatasource() throws IOException {
        dataSource.load(dataSourceFilePath);
    }

    @Test
    void deleteMedicalRecordExistent() throws Exception {
        String firstName = "Sophia";
        String lastName = "Zemicks";

        //Check medical record exists before deleting it
        Optional<MedicalRecord> medicalRecord = dataSource.getData()
                .getMedicalrecords().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        assertTrue(medicalRecord.isPresent());

        //ACT
        mockMvc.perform(delete("/medicalRecord/"+firstName+"/"+lastName))
                .andExpect(status().isNoContent());

        //CHECK
        medicalRecord = dataSource.getData()
                .getMedicalrecords().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(medicalRecord.isEmpty());
    }

    @Test
    void deleteMedicalRecordNonExistent() throws Exception {
        String firstName = "Sophie";
        String lastName = "Zemicks";

        //Check medical record does not exist
        Optional<MedicalRecord> medicalRecord = dataSource.getData()
                .getMedicalrecords().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        assertTrue(medicalRecord.isEmpty());

        //ACT
        mockMvc.perform(delete("/medicalRecord/"+firstName+"/"+lastName))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMedicalRecordExistent() throws Exception {
        String firstName = "Sophia";
        String lastName = "Zemicks";

        //Check medical record exists before trying to create it
        Optional<MedicalRecord> medicalRecord = dataSource.getData()
                .getMedicalrecords().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        assertTrue(medicalRecord.isPresent());

        //ACT
        mockMvc.perform(post("/medicalRecord/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.fromObject(new MedicalRecord(firstName, lastName, new Date(1231564),null,null))))
                .andExpect(status().isConflict());
    }

    @Test
    void createMedicalRecordNonexistent() throws Exception {
        String firstName = "Pierre";
        String lastName = "Paul";

        //Check medical record does not exist
        Optional<MedicalRecord> medicalRecord = dataSource.getData()
                .getMedicalrecords().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(medicalRecord.isEmpty());

        //ACT
        mockMvc.perform(post("/medicalRecord/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.fromObject(new MedicalRecord(firstName, lastName, new Date(1209600000L), List.of("aspirine:100mg","ibu:50mg"),List.of("pollen","chat")))))
                        .andExpect(status().isCreated());
        //Check medical record exist
        medicalRecord = dataSource.getData()
                .getMedicalrecords().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(medicalRecord.isPresent());
        MedicalRecord medicalRecordCreated = medicalRecord.get();
        assertThat(medicalRecordCreated.getBirthdate().getTime()).isEqualTo(1209600000L);
        assertThat(medicalRecordCreated.getMedications()).containsExactly("aspirine:100mg","ibu:50mg");
        assertThat(medicalRecordCreated.getAllergies()).containsExactly("pollen","chat");
    }

    @Test
    void updateMedicalRecordExistent() throws Exception {
        String firstName = "Sophia";
        String lastName = "Zemicks";

        //Check medical record exists before trying to create it
        Optional<MedicalRecord> medicalRecord = dataSource.getData()
                .getMedicalrecords().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(medicalRecord.isPresent());

        //ACT
        mockMvc.perform(put("/medicalRecord/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.fromObject(new MedicalRecord(firstName, lastName, new Date(1209600000L), List.of("aspirine:100mg","ibu:50mg"),List.of("pollen","chat")))))
                .andExpect(status().isOk());

        //Check medical record has been updated
        medicalRecord = dataSource.getData()
                .getMedicalrecords().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(medicalRecord.isPresent());
        MedicalRecord medicalRecordCreated = medicalRecord.get();
        assertThat(medicalRecordCreated.getBirthdate().getTime()).isEqualTo(1209600000L);
        assertThat(medicalRecordCreated.getMedications()).containsExactly("aspirine:100mg","ibu:50mg");
        assertThat(medicalRecordCreated.getAllergies()).containsExactly("pollen","chat");
    }

    @Test
    void updateMedicalRecordNonexistent() throws Exception {
        String firstName = "Pierre";
        String lastName = "Paul";

        //Check medical record does not exist
        Optional<MedicalRecord> medicalRecord = dataSource.getData()
                .getMedicalrecords().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(medicalRecord.isEmpty());

        //ACT
        mockMvc.perform(put("/medicalRecord/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.fromObject(new MedicalRecord(firstName, lastName, new Date(1231564),null,null))))
                .andExpect(status().isNotFound());
    }
}