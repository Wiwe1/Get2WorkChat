package com.example.djw.get2workchat.Data_Models;

public class Message {

   // private String senderName;
    private  String id;
    private String senderId;
    private String chatroom_id;
    private String message;
    private Long sent;

    public Message(String id, String senderId, String chatroom_id, String message, Long sent) {
        this.id = id;
        this.senderId = senderId;
        this.chatroom_id = chatroom_id;
        this.message = message;
        this.sent = sent;
    }

    public  Message(){


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getChatroom_id() {
        return chatroom_id;
    }

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSent() {
        return sent;
    }

    public void setSent(Long sent) {
        this.sent = sent;
    }


    //  private String timestamp;
    //private String url;
   // private String image;



}

