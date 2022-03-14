package com.safetynet.alerts.api;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.Person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StubbedData {

    public static IAlertsDataSource.Data get() throws ParseException {
        IAlertsDataSource.Data data = new IAlertsDataSource.Data();
        data.setPersons(
                new ArrayList<>(Arrays.asList(
                        new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                        new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com"),
                        new Person("Tenley", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "tenz@email.com"),
                        new Person("Roger", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                        new Person("Felicia", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6544", "jaboyd@email.com"),
                        new Person("Jonanathan", "Marrack", "29 15th St", "Culver", "97451", "841-874-6513", "drk@email.com"),
                        new Person("Tessa", "Carman", "834 Binoc Ave", "Culver", "97451", "841-874-6512", "tenz@email.com"),
                        new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451", "841-874-6512", "jaboyd@email.com"),
                        new Person("Foster", "Shepard", "748 Townings Dr", "Culver", "97451", "841-874-6544", "jaboyd@email.com"),
                        new Person("Tony", "Cooper", "112 Steppes Pl", "Culver", "97451", "841-874-6874", "tcoop@ymail.com"),
                        new Person("Lily", "Cooper", "489 Manchester St", "Culver", "97451", "841-874-9845", "lily@email.com"),
                        new Person("Sophia", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7878", "soph@email.com"),
                        new Person("Warren", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "ward@email.com"),
                        new Person("Zach", "Zemicks", "892 Downing Ct", "Culver", "97451", "841-874-7512", "zarc@email.com"),
                        new Person("Reginold", "Walker", "908 73rd St", "Culver", "97451", "841-874-8547", "reg@email.com"),
                        new Person("Jamie", "Peters", "908 73rd St", "Culver", "97451", "841-874-7462", "jpeter@email.com"),
                        new Person("Ron", "Peters", "112 Steppes Pl", "Culver", "97451", "841-874-8888", "jpeter@email.com"),
                        new Person("Allison", "Boyd", "112 Steppes Pl", "Culver", "97451", "841-874-9888", "aly@imail.com"),
                        new Person("Brian", "Stelzer", "947 E. Rose Dr", "Paris", "97451", "841-874-7784", "bstel@email.com"),
                        new Person("Shawna", "Stelzer", "947 E. Rose Dr", "Paris", "97451", "841-874-7784", "ssanw@email.com"),
                        new Person("Kendrik", "Stelzer", "947 E. Rose Dr", "Paris", "97451", "841-874-7784", "bstel@email.com"),
                        new Person("Clive", "Ferguson", "48 Townings Dr", "Culver", "97451", "841-874-6741", "clivfd@ymail.com"),
                        new Person("Eric", "Cadigan", "951 LoneTree Rd", "Culver", "97451", "841-874-7458", "gramps@email.com")
                )));
        data.setFirestations(
                new ArrayList<>(Arrays.asList(
                        new FireStation("1509 Culver St", 3),
                        new FireStation("29 15th St", 2),
                        new FireStation("834 Binoc Ave", 3),
                        new FireStation("644 Gershwin Cir", 1),
                        new FireStation("748 Townings Dr", 3),
                        new FireStation("112 Steppes Pl", 3),
                        new FireStation("489 Manchester St", 4),
                        new FireStation("892 Downing Ct", 2),
                        new FireStation("908 73rd St", 1),
                        new FireStation("947 E. Rose Dr", 1),
                        new FireStation("748 Townings Dr", 3),
                        new FireStation("951 LoneTree Rd", 2)
                )
                ));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        data.setMedicalrecords(
                new ArrayList<>(Arrays.asList(
                        new MedicalRecord("John", "Boyd", dateFormat.parse("03/06/1984"), List.of("aznol:350mg", "hydrapermazol:100mg"), List.of("nillacilan")),
                        new MedicalRecord("Jacob", "Boyd", dateFormat.parse("03/06/1989"), List.of("pharmacol:5000mg", "terazine:10mg", "noznazol:250mg"), List.of()),
                        new MedicalRecord("Tenley", "Boyd", dateFormat.parse("02/18/2012"), List.of(), List.of("peanut")),
                        new MedicalRecord("Roger", "Boyd", dateFormat.parse("09/06/2017"), List.of(), List.of()),
                        new MedicalRecord("Felicia", "Boyd", dateFormat.parse("01/08/1986"), List.of("tetracyclaz:650mg"), List.of("xilliathal")),
                        new MedicalRecord("Jonanathan", "Marrack", dateFormat.parse("01/03/1989"), List.of(), List.of()),
                        new MedicalRecord("Tessa", "Carman", dateFormat.parse("02/18/2012"), List.of(), List.of()),
                        new MedicalRecord("Peter", "Duncan", dateFormat.parse("09/06/2000"), List.of(), List.of("shellfish")),
                        new MedicalRecord("Foster", "Shepard", dateFormat.parse("01/08/1980"), List.of(), List.of()),
                        new MedicalRecord("Tony", "Cooper", dateFormat.parse("03/06/1994"), List.of("hydrapermazol:300mg", "dodoxadin:30mg"), List.of("shellfish")),
                        new MedicalRecord("Lily", "Cooper", dateFormat.parse("03/06/1994"), List.of(), List.of()),
                        new MedicalRecord("Sophia", "Zemicks", dateFormat.parse("03/06/1988"), List.of("aznol:60mg", "hydrapermazol:900mg", "pharmacol:5000mg", "terazine:500mg"), List.of("peanut", "shellfish", "aznol")),
                        new MedicalRecord("Warren", "Zemicks", dateFormat.parse("03/06/1985"), List.of(), List.of()),
                        new MedicalRecord("Zach", "Zemicks", dateFormat.parse("03/06/2017"), List.of(), List.of()),
                        new MedicalRecord("Reginold", "Walker", dateFormat.parse("08/30/1979"), List.of("thradox:700mg"), List.of("illisoxian")),
                        new MedicalRecord("Jamie", "Peters", dateFormat.parse("03/06/1982"), List.of(), List.of()),
                        new MedicalRecord("Ron", "Peters", dateFormat.parse("04/06/1965"), List.of(), List.of()),
                        new MedicalRecord("Allison", "Boyd", dateFormat.parse("03/15/1965"), List.of("aznol:200mg"), List.of("nillacilan")),
                        new MedicalRecord("Brian", "Stelzer", dateFormat.parse("12/06/1975"), List.of("ibupurin:200mg", "hydrapermazol:400mg"), List.of("nillacilan")),
                        new MedicalRecord("Shawna", "Stelzer", dateFormat.parse("07/08/1980"), List.of(), List.of()),
                        new MedicalRecord("Kendrik", "Stelzer", dateFormat.parse("03/06/2014"), List.of("noxidian:100mg", "pharmacol:2500mg"), List.of()),
                        new MedicalRecord("Clive", "Ferguson", dateFormat.parse("03/06/1994"), List.of(), List.of()),
                        new MedicalRecord("Eric", "Cadigan", dateFormat.parse("08/06/1945"), List.of("tradoxidine:400mg"), List.of())
                )
                ));
        return data;

    }
}
