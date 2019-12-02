package com.example.groupandgoeventplanner;

import java.util.ArrayList;

public class Invitees {
    private String email;
    private ArrayList<String> groups;
    private String host_name;

    public Invitees() {}

    public Invitees(String email, String host_name) {
        this.email = email;
        this.groups = new ArrayList<>();
        this.host_name = host_name;
    }
    public ArrayList<String> getGroups() {
        return groups;
    }
    public String getHost_name(){
        return host_name;
    }

    public void invitedTo(String group_name) {
        groups.add(group_name);
    }

    public void acceptInvitation(String group_name) {
        //groups.put(group_name, true);
    }

    public boolean getStatus(String group_name) {
        //return groups.get(group_name);
        return true;
    }
    /**
    public void accept() {
        accept_invite = true;
    }
     **/
    public String getInviteeEmail() {
        return email;
    }
}
