package com.example.djw.get2workchat.Data_Models;

import java.util.List;

public class User {


    private  String userName;
    private List<Contact> lstContact;
    private Profile prof;

    public User() {

    }

    public User(String userName, List<Contact> lstContact, Profile prof) {
        this.userName = userName;
        this.lstContact = lstContact;
        this.prof = prof;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Contact> getLstContact() {
        return lstContact;
    }

    public void setLstContact(List<Contact> lstContact) {
        this.lstContact = lstContact;
    }

    public Profile getProf() {
        return prof;
    }

    public void setProf(Profile prof) {
        this.prof = prof;
    }


}
