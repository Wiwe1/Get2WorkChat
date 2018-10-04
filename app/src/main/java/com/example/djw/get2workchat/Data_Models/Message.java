package com.example.djw.get2workchat.Data_Models;

public class Message {


    private String Message;
    private String timestamp;
    private String senderId;
    private String url;
    private String image;
    private String Chatroom_id;


    public Message(String message, String timestamp, String senderId, String url, String image, String chatroom_id) {
        this.Message = message;
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.url = url;
        this.image = image;
        this.Chatroom_id = chatroom_id;
    }


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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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

    public String getChatroom_id() {
        return Chatroom_id;
    }

    public void setChatroom_id(String chatroom_id) {
        Chatroom_id = chatroom_id;
    }
}

