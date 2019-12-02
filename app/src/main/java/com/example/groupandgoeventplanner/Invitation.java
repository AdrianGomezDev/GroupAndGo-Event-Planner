package com.example.groupandgoeventplanner;

public class Invitation {
    private String groupName;
    private String hostName;
    private boolean status;
    public Invitation(){}

    public Invitation(String groupName, String hostName, boolean status) {
        this.groupName = groupName;
        this.hostName = hostName;
        this.status = status;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean getStatus() {
        return status;
    }

    public String getHostName() {
        return hostName;
    }

    public void updateStatus(boolean newStatus) {
        this.status = newStatus;
    }
}
