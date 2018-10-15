package com.example.djw.get2workchat.Data_Models;

import java.util.List;

public class User {


    private  String userName,
                    email,
                    phone_number,
                    profession,
                    profilePicturePath;
    public User() {
    }
public User(String userName, String email, String phone_number, String profession, String profilePicturePath) {
            this.userName = userName;
            this.email = email;
            this.phone_number = phone_number;
            this.profession = profession;
            this.profilePicturePath = profilePicturePath;
        }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProfression() {
        return profession;
    }

    public void setProfression(String profression) {
        this.profession = profression;
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
}
