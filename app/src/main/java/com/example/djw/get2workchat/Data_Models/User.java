package com.example.djw.get2workchat.Data_Models;

import java.util.List;

public class User {


    private  String userName;
    private String email;
    private String phone_number;
    private String profession;
    private String profilePicturePath;
    private String engaged_chats;


    public User(String userName, String email, String phone_number, String profession, String profilePicturePath, String engaged_chats) {

            this.userName = userName;
            this.email = email;
            this.phone_number = phone_number;
            this.profession = profession;
            this.profilePicturePath = profilePicturePath;
            this.engaged_chats = engaged_chats;


        }
    public User() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }



    public String getEngaged_chats()
    {
        return engaged_chats;
    }

    public void setEngaged_chats(String engaged_chats) {

        this.engaged_chats = engaged_chats;
    }

}
