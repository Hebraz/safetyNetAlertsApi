package com.safetynet.alerts.api.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Fire station entity
 *
 */
@FieldDefaults(makeFinal = false, level = AccessLevel.PRIVATE)
public class FireStation {
    String address;
    int station;

    public FireStation(){}

    public FireStation(FireStation fireStation){
        this.station = fireStation.station;
        this.address = fireStation.address;
    }

    public FireStation(String address, int station) {
        this.address = address;
        this.station = station;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

}
