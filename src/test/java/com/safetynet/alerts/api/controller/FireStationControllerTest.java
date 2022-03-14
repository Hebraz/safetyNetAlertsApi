package com.safetynet.alerts.api.controller;

import com.safetynet.alerts.api.Json;
import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;
import java.io.IOException;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class FireStationControllerTest {

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
    void deleteFireStationExistent() throws Exception {
        String address = "644 Gershwin Cir";
        //Check firestation mapping exists before deleting it
        Optional<FireStation> fireStation = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStation.isPresent());

        //ACT
        mockMvc.perform(delete("/firestation/"+address))
                .andExpect(status().isNoContent());

        //CHECK
        fireStation = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStation.isEmpty());
    }
    @Test
    void deleteFireStationNonexistent() throws Exception {
        String address = "645 Gershwin Cir";
        //Check firestation mapping exists before deleting it
        Optional<FireStation> fireStation = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStation.isEmpty());

        //ACT
        mockMvc.perform(delete("/firestation/"+address))
                .andExpect(status().isNotFound());
    }

    @Test
    void createFireStationExistent() throws Exception {
        String address = "644 Gershwin Cir";
        //Check firestation mapping exists before trying to add it
        Optional<FireStation> fireStationRes = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStationRes.isPresent());
        assertEquals(1, fireStationRes.get().getStation());

        //ACT
        mockMvc.perform(post("/firestation/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.fromObject(new FireStation(address,3))))
                .andExpect(status().isConflict());

        //CHECK
        //station has not been modified
        fireStationRes = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStationRes.isPresent());
        assertEquals(1, fireStationRes.get().getStation());
    }

    @Test
    void createFireStationNonexistent() throws Exception {
        String address = "145 Rue Eiffel";
        //Check firestation mapping does not exist before trying to add it
        Optional<FireStation> fireStationRes = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStationRes.isEmpty());

        //ACT
        mockMvc.perform(post("/firestation/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.fromObject(new FireStation(address,3))))
                        .andExpect(status().isCreated());

        //CHECK
        //station has been created
        fireStationRes = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStationRes.isPresent());
        assertEquals(3, fireStationRes.get().getStation());
    }

    @Test
    void updateFireStationExistent() throws Exception {
        String address = "644 Gershwin Cir";
        //Check firestation mapping exists before trying to update it
        Optional<FireStation> fireStationRes = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStationRes.isPresent());
        assertEquals(1, fireStationRes.get().getStation());

        //ACT
        mockMvc.perform(put("/firestation/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.fromObject(new FireStation(address,3))))
                .andExpect(status().isOk());

        //CHECK
        //station has been modified
        fireStationRes = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStationRes.isPresent());
        assertEquals(3, fireStationRes.get().getStation());
    }

    @Test
    void updateFireStationNonexistent() throws Exception {
        String address = "145 Rue Eiffel";
        //Check firestation mapping does not exist before trying to update it
        Optional<FireStation> fireStationRes = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStationRes.isEmpty());

        //ACT
        mockMvc.perform(put("/firestation/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.fromObject(new FireStation(address,3))))
                .andExpect(status().isNotFound());

        //CHECK
        //station has not been created
        fireStationRes = dataSource.getData().getFirestations().stream().filter(s -> s.getAddress().equalsIgnoreCase(address)).findFirst();
        assertTrue(fireStationRes.isEmpty());
    }

    @Test
    void getFireStationPersons() throws Exception {
        mockMvc.perform(get("/firestation?stationNumber=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons", hasSize(5)))
                //check first person
                .andExpect(jsonPath("$.persons.[0].firstName", is("Jonanathan")))
                .andExpect(jsonPath("$.persons.[0].lastName", is("Marrack")))
                .andExpect(jsonPath("$.persons.[0].address", is("29 15th St")))
                .andExpect(jsonPath("$.persons.[0].city", is("Culver")))
                .andExpect(jsonPath("$.persons.[0].zip", is("97451")))
                .andExpect(jsonPath("$.persons.[0].phone", is("841-874-6513")))
                .andExpect(jsonPath("$.persons.[0].age").doesNotExist())
                .andExpect(jsonPath("$.persons.[0].email").doesNotExist())
                .andExpect(jsonPath("$.persons.[0].medicalRecord").doesNotExist())
                //check last person
                .andExpect(jsonPath("$.persons.[4].firstName", is("Eric")))
                .andExpect(jsonPath("$.persons.[4].lastName", is("Cadigan")))
                //check nb adults and children
                .andExpect(jsonPath("$.numberOfAdults", is(4)))
                .andExpect(jsonPath("$.numberOfChildren", is(1)));
    }

    @Test
    void getFireStationPersonsFireStationInexistent() throws Exception {
        mockMvc.perform(get("/firestation?stationNumber=5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPhoneAlert() throws Exception {
        mockMvc.perform(get("/phoneAlert?firestation=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$", hasItem("841-874-6512")))
                .andExpect(jsonPath("$", hasItem("841-874-6513")))
                .andExpect(jsonPath("$", hasItem("841-874-6544")))
                .andExpect(jsonPath("$", hasItem("841-874-6874")))
                .andExpect(jsonPath("$", hasItem("841-874-8888")))
                .andExpect(jsonPath("$", hasItem("841-874-9888")));
    }

    @Test
    void getPhoneAlertFireStationInexistent() throws Exception {
        mockMvc.perform(get("/phoneAlert?firestation=5"))
                .andExpect(status().isNotFound());
    }
    @Test
    void getFloodStations() throws Exception {
        mockMvc.perform(get("/flood/stations?stations=3,4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                //check first home
                .andExpect(jsonPath("$[0].address", is("1509 Culver St")))
                .andExpect(jsonPath("$[0].persons", hasSize(5)))
                    //first person
                .andExpect(jsonPath("$[0].persons.[0].firstName",  is("John")))
                .andExpect(jsonPath("$[0].persons.[0].lastName",  is("Boyd")))
                .andExpect(jsonPath("$[0].persons.[0].phone",  is("841-874-6512")))
                .andExpect(jsonPath("$[0].persons.[0].age",  is(38)))
                .andExpect(jsonPath("$[0].persons.[0].medicalRecord.medications",  hasSize(2)))
                .andExpect(jsonPath("$[0].persons.[0].medicalRecord.medications.[0]", is("aznol:350mg")))
                .andExpect(jsonPath("$[0].persons.[0].medicalRecord.medications.[1]", is("hydrapermazol:100mg")))
                .andExpect(jsonPath("$[0].persons.[0].medicalRecord.allergies",  hasSize(1)))
                .andExpect(jsonPath("$[0].persons.[0].medicalRecord.allergies.[0]", is("nillacilan")))
                .andExpect(jsonPath("$[0].persons.[0].medicalRecord.birthdate").doesNotExist())
                .andExpect(jsonPath("$[0].persons.[0].address").doesNotExist())
                .andExpect(jsonPath("$[0].persons.[0].city").doesNotExist())
                .andExpect(jsonPath("$[0].persons.[0].zip").doesNotExist())
                .andExpect(jsonPath("$[0].persons.[0].email").doesNotExist())
                    //last person
                .andExpect(jsonPath("$[0].persons.[4].firstName",  is("Felicia")))
                .andExpect(jsonPath("$[0].persons.[4].lastName",  is("Boyd")))
                //check last home
                .andExpect(jsonPath("$[4].address", is("489 Manchester St")))
                .andExpect(jsonPath("$[4].persons", hasSize(1)))
                .andExpect(jsonPath("$[4].persons.[0].firstName",  is("Lily")))
                .andExpect(jsonPath("$[4].persons.[0].lastName",  is("Cooper")));

    }
}