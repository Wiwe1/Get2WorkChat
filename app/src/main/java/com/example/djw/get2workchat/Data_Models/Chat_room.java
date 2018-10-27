package com.example.djw.get2workchat.Data_Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chat_room {
    private String id;
    private String name;
    private HashMap<String,Object> UserIds ;


    public Chat_room(String id, String name, HashMap<String,Object> userIds) {
        this.id = id;
        this.name = name;
        UserIds = userIds;
    }

public Chat_room(){

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
