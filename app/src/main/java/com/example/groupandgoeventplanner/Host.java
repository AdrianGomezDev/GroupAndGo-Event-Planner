package com.example.groupandgoeventplanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Host {
    private String user_name;
    private String email;
    private Map<String, ArrayList<Invitees>> groups;

    public Host() {}

    public Host(String email) {
        //this.user_name = user_name;
        this.email = email;
        groups = new HashMap<>();
    }

    public String get_user_name() {
        return user_name;
    }

    public String get_email(){
        return email;
    }

    public Map<String, ArrayList<Invitees>> get_groups()  {
        return groups;
    }

    public void create_group(String group_name, ArrayList<Invitees> members) {
        //Assume that this group name does not exist yet
        groups.put(group_name, members);
    }

    public void add_member_to_group (Invitees member_email, String group_name) {
        groups.get(group_name).add(member_email);

    }
}
