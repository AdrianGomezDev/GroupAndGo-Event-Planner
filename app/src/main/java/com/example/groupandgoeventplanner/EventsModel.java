package com.example.groupandgoeventplanner;

import java.io.Serializable;

public class EventsModel implements Serializable {
    private String name;
    private String locationName;
    private boolean complete;

    public EventsModel() {}

    public EventsModel(String name, String locationName, boolean complete) {
        this.name = name;
        this.locationName = locationName;
        this.complete = complete;

    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}