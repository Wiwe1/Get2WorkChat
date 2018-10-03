package com.example.djw.get2workchat.Data_Models;

import java.util.List;

public class User {

    private  String userName;
    private List<Contact> lstContact;
    private Profile prof;


    public User(String userName, List<Contact> lstContact, Profile prof) {
        this.userName = userName;
        this.lstContact = lstContact;
        this.prof = prof;
    }
}
