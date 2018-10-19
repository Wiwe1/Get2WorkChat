package com.example.djw.get2workchat.Data_Models;

import java.util.ArrayList;
import java.util.List;

public class Chat_room {
    public String id;
    public String name;
    public List<String> UserIds = new ArrayList<String>();


    public Chat_room(String id, String name, List<String> userIds) {
        this.id = id;
        this.name = name;
        UserIds = userIds;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
