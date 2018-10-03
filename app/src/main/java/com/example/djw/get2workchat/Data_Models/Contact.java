package com.example.djw.get2workchat.Data_Models;

public class Contact {

    private String name;
    private String phone_num;
    private int photo;

    public Contact(String name, String phone_num, int photo) {
        this.name = name;
        this.phone_num = phone_num;
        this.photo = photo;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }



}
