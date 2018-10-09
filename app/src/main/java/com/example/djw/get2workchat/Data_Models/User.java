package com.example.djw.get2workchat.Data_Models;

import java.util.List;

public class User {


    private  String userName;
   private  String email;
    public User() {

    }

    public User(String userName, String Email ){
       this.userName = userName;
       this.email = Email;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }




}
