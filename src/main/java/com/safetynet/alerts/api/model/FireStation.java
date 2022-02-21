package com.safetynet.alerts.api.model;

/**
 * Fire station entity
 *
 */
public class FireStation {
    private String address;
    private int station;

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
