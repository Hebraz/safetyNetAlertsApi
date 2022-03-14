package com.safetynet.alerts.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safetynet.alerts.api.Json;
import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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
import java.util.Date;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerTest {
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
    void deletePersonExistent() throws Exception {
        String firstName = "Sophia";
        String lastName = "Zemicks";

        //Check person exists before deleting it
        Optional<Person> person = dataSource.getData()
                .getPersons().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(person.isPresent());

        //ACT
        mockMvc.perform(delete("/person/"+firstName+"/"+lastName))
                .andExpect(status().isNoContent());

        //CHECK
        person = dataSource.getData()
                .getPersons().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(person.isEmpty());
    }

    @Test
    void deletePersonNonexistent() throws Exception {
        String firstName = "Sophi";
        String lastName = "Zemicks";

        //Check person does not exist before deleting it
        Optional<Person> person = dataSource.getData()
                .getPersons().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(person.isEmpty());

        //ACT
        mockMvc.perform(delete("/person/"+firstName+"/"+lastName))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPersonExistent() throws Exception {
        String firstName = "Sophia";
        String lastName = "Zemicks";

        //Check person exists before creating it
        Optional<Person> person = dataSource.getData()
                .getPersons().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(person.isPresent());

        //ACT
        mockMvc.perform(post("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.fromObject(new Person(firstName, lastName, "1 Rd Sky","Paris","15489","0145154871","fsf@jios.com"))))
                        .andExpect(status().isConflict());
    }

    @Test
    void createPersonNonexistent() throws Exception {
        String firstName = "Pierre";
        String lastName = "Paul";

        //Check person does not exist before creating it
        Optional<Person> person = dataSource.getData()
                .getPersons().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(person.isEmpty());

        //ACT
        mockMvc.perform(post("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.fromObject(new Person(firstName, lastName, "1 Rd Sky","Paris","15489","0145154871","fsf@jios.com"))))
                .andExpect(status().isCreated());

        //CHECK
        person = dataSource.getData()
                .getPersons().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(person.isPresent());
        Person createdPerson = person.get();
        assertThat(createdPerson.getAddress()).isEqualTo("1 Rd Sky");
        assertThat(createdPerson.getEmail()).isEqualTo("fsf@jios.com");
    }

    @Test
    void updatePersonExistent() throws Exception {
        String firstName = "Sophia";
        String lastName = "Zemicks";

        //Check person exists before updating it
        Optional<Person> person = dataSource.getData()
                .getPersons().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(person.isPresent());

        //ACT
        mockMvc.perform(put("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.fromObject(new Person(firstName, lastName, "1 Rd Sky","Paris","15489","0145154871","fsf@jios.com"))))
                .andExpect(status().isOk());

        //CHECK
        person = dataSource.getData()
                .getPersons().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(person.isPresent());
        Person createdPerson = person.get();
        assertThat(createdPerson.getAddress()).isEqualTo("1 Rd Sky");
        assertThat(createdPerson.getEmail()).isEqualTo("fsf@jios.com");
    }

    @Test
    void updatePersonNonexistent() throws Exception {
        String firstName = "Pierre";
        String lastName = "Paul";

        //Check person does not exist before updating it
        Optional<Person> person = dataSource.getData()
                .getPersons().stream()
                .filter(s -> s.getFirstName().equalsIgnoreCase(firstName) &&  s.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        assertTrue(person.isEmpty());

        //ACT
        mockMvc.perform(put("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Json.fromObject(new Person(firstName, lastName, "1 Rd Sky","Paris","15489","0145154871","fsf@jios.com"))))
                .andExpect(status().isNotFound());

    }

    @Test
    void getChildrenAtAddress() throws Exception {
        mockMvc.perform(get("/childAlert?address=1509 Culver St"))
                .andExpect(status().isOk())
                //check children
                .andExpect(jsonPath("$.children", hasSize(2)))
                //check first child
                .andExpect(jsonPath("$.children.[0].firstName", is("Tenley")))
                .andExpect(jsonPath("$.children.[0].lastName", is("Boyd")))
                .andExpect(jsonPath("$.children.[0].age", is(10)))
                .andExpect(jsonPath("$.children.[0].address").doesNotExist())
                .andExpect(jsonPath("$.children.[0].city").doesNotExist())
                .andExpect(jsonPath("$.children.[0].zip").doesNotExist())
                .andExpect(jsonPath("$.children.[0].phone").doesNotExist())
                .andExpect(jsonPath("$.children.[0].email").doesNotExist())
                .andExpect(jsonPath("$.children.[0].medicalRecord").doesNotExist())
                //check last child
                .andExpect(jsonPath("$.children.[1].firstName", is("Roger")))
                .andExpect(jsonPath("$.children.[1].lastName", is("Boyd")))
                .andExpect(jsonPath("$.children.[1].age", is(4)))
                //check adults
                .andExpect(jsonPath("$.adults", hasSize(3)))
                //check first adult
                .andExpect(jsonPath("$.adults.[0].firstName", is("John")))
                .andExpect(jsonPath("$.adults.[0].lastName", is("Boyd")))
                .andExpect(jsonPath("$.adults.[0].address").doesNotExist())
                .andExpect(jsonPath("$.adults.[0].city").doesNotExist())
                .andExpect(jsonPath("$.adults.[0].zip").doesNotExist())
                .andExpect(jsonPath("$.adults.[0].phone").doesNotExist())
                .andExpect(jsonPath("$.adults.[0].email").doesNotExist())
                .andExpect(jsonPath("$.adults.[0].age").doesNotExist())
                .andExpect(jsonPath("$.adults.[0].medicalRecord").doesNotExist())
                //check last adult
                .andExpect(jsonPath("$.adults.[2].firstName", is("Felicia")))
                .andExpect(jsonPath("$.adults.[2].lastName", is("Boyd")));
    }

    @Test
    void getChildrenAtAddressNoChild() throws Exception {
        mockMvc.perform(get("/childAlert?address=48 Townings Dr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getFiredPersons() throws Exception {
        mockMvc.perform(get("/fire?address=947 E. Rose Dr"))
                .andExpect(status().isOk())
                //check station number
                .andExpect(jsonPath("$.stationNumber", is(1)))
                //check  persons
                .andExpect(jsonPath("$.persons", hasSize(3)))
                .andExpect(jsonPath("$.persons.[0].firstName", is("Brian")))
                .andExpect(jsonPath("$.persons.[0].lastName", is("Stelzer")))
                .andExpect(jsonPath("$.persons.[0].phone",  is("841-874-7784")))
                .andExpect(jsonPath("$.persons.[0].age", is(46)))
                .andExpect(jsonPath("$.persons.[0].medicalRecord.medications",  hasSize(2)))
                .andExpect(jsonPath("$.persons.[0].medicalRecord.medications.[0]", is("ibupurin:200mg")))
                .andExpect(jsonPath("$.persons.[0].medicalRecord.medications.[1]", is("hydrapermazol:400mg")))
                .andExpect(jsonPath("$.persons.[0].medicalRecord.allergies",  hasSize(1)))
                .andExpect(jsonPath("$.persons.[0].medicalRecord.allergies.[0]", is("nillacilan")))
                .andExpect(jsonPath("$.persons.[0].medicalRecord.birthdate").doesNotExist())
                .andExpect(jsonPath("$.persons.[0].address").doesNotExist())
                .andExpect(jsonPath("$.persons.[0].city").doesNotExist())
                .andExpect(jsonPath("$.persons.[0].zip").doesNotExist())
                .andExpect(jsonPath("$.persons.[0].email").doesNotExist())
                //check last persons
                .andExpect(jsonPath("$.persons.[2].firstName", is("Kendrik")))
                .andExpect(jsonPath("$.persons.[2].lastName", is("Stelzer")));
    }

    @Test
    void getPersonInfo() throws Exception {
        mockMvc.perform(get("/personInfo?firstName=Sophia&lastName=Zemicks"))
                .andExpect(status().isOk())
                //check  persons
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Sophia")))
                .andExpect(jsonPath("$[0].lastName", is("Zemicks")))
                .andExpect(jsonPath("$[0].address", is("892 Downing Ct")))
                .andExpect(jsonPath("$[0].city", is("Culver")))
                .andExpect(jsonPath("$[0].zip", is("97451")))
                .andExpect(jsonPath("$[0].age", is(34)))
                .andExpect(jsonPath("$[0].email", is("soph@email.com")))
                .andExpect(jsonPath("$[0].medicalRecord.medications",  hasSize(4)))
                .andExpect(jsonPath("$[0].medicalRecord.medications.[0]", is("aznol:60mg")))
                .andExpect(jsonPath("$[0].medicalRecord.medications.[1]", is( "hydrapermazol:900mg")))
                .andExpect(jsonPath("$[0].medicalRecord.medications.[2]", is( "pharmacol:5000mg")))
                .andExpect(jsonPath("$[0].medicalRecord.medications.[3]", is( "terazine:500mg")))
                .andExpect(jsonPath("$[0].medicalRecord.allergies",  hasSize(3)))
                .andExpect(jsonPath("$[0].medicalRecord.allergies.[0]", is("peanut")))
                .andExpect(jsonPath("$[0].medicalRecord.allergies.[1]", is("shellfish")))
                .andExpect(jsonPath("$[0].medicalRecord.allergies.[2]", is("aznol")))
                .andExpect(jsonPath("$[0].medicalRecord.birthdate").doesNotExist())
                .andExpect(jsonPath("$[0].phone").doesNotExist());
    }

    @Test
    void testGetCommunityEmail() throws Exception {
        mockMvc.perform(get("/communityEmail?city=Culver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(15)))
                .andExpect(jsonPath("$", hasItem("jaboyd@email.com")))
                .andExpect(jsonPath("$", hasItem("drk@email.com")))
                .andExpect(jsonPath("$", hasItem("tenz@email.com")))
                .andExpect(jsonPath("$", hasItem("tcoop@ymail.com")))
                .andExpect(jsonPath("$", hasItem("lily@email.com")))
                .andExpect(jsonPath("$", hasItem("soph@email.com")))
                .andExpect(jsonPath("$", hasItem("ward@email.com")))
                .andExpect(jsonPath("$", hasItem("zarc@email.com")))
                .andExpect(jsonPath("$", hasItem("reg@email.com")))
                .andExpect(jsonPath("$", hasItem("jpeter@email.com")))
                .andExpect(jsonPath("$", hasItem("aly@imail.com")))
                .andExpect(jsonPath("$", hasItem("bstel@email.com")))
                .andExpect(jsonPath("$", hasItem("ssanw@email.com")))
                .andExpect(jsonPath("$", hasItem("clivfd@ymail.com")))
                .andExpect(jsonPath("$", hasItem("gramps@email.com")));
    }
}