package com.safetynet.alerts.api.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


/**
 * AlertsDataSource class enables to
 *  - load SafetyNet Alerts data from Json file by calling load method.
 *  - give access to that loaded data
 */
@Component
public class AlertsDataSource implements IAlertsDataSource {

    private Data data;

    /**
     * Load SafetyNet Alerts data from Json file given in parameter.
     * Shall be called first before accessing to data through getData method
     *
     * @param dataSourceFilePath path to the json data file.
     */
    public void load(String dataSourceFilePath) throws IOException {
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
