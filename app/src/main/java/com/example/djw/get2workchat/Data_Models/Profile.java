package com.example.djw.get2workchat.Data_Models;

public class Profile {

    private String  name;
    private String  mail;
    private String  phoneNum;
    private int     profilepic;


    public Profile(String name, String mail, String phoneNum, int profilepic) {
        this.name = name;
        this.mail = mail;
        this.phoneNum = phoneNum;
        this.profilepic = profilepic;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(int profilepic) {
        this.profilepic = profilepic;
    }
}
