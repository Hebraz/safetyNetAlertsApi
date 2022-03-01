package com.safetynet.alerts.api.datasource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.api.model.FireStation;
import com.safetynet.alerts.api.model.MedicalRecord;
import com.safetynet.alerts.api.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * AlertsDataSource class enables to
 *  - load SafetyNet Alerts data from Json file by calling load method.
 *  - give access to that loaded data
 */
@Component
public class AlertsDataSource implements IAlertsDataSource {

    @Value( "${datasource.filepath}" )
    private String dataSourceFilePath;

    private Data data;

    /**
     * Load SafetyNet Alerts data from Json file defined by
     * "datasource.filepath" application property. Shall be called
     * first before accessing to data through getData method
     */
    public void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File resource = new ClassPathResource(dataSourceFilePath).getFile();
        data = mapper.readValue(resource, Data.class);
    }

    /**
     * Get SafetyNet Alerts data loaded from Json file.
     *
     * @return SafetyNet Alerts data or null if data have not been loaded
     */
    public Data getData() {
        return data;
    }


}
