package com.example.djw.get2workchat.Data_Models;

public class Message {


    private String Message;
    private String timestamp;
    private User sender;
    private String url;
    private String image;


    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String SendMessage(String Message,String Url,User sender,String Timestamp){

        return "Hej";
    }



}

