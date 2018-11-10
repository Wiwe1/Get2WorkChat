package com.example.djw.get2workchat.Data_Models;

public class Message {

    // private String senderName;
    private String id;
    private String sender_id;
    private String chat_room_id;
    private String message;
    private String type;
    private Long sent;
    private Long message_number;

    public Message(String id, String sender_id, String chat_room_id, String message, String type, Long sent, Long message_number) {
        this.id = id;
        this.sender_id = sender_id;
        this.chat_room_id = chat_room_id;
        this.message = message;
        this.sent = sent;
        this.type = type;
        this.message_number = message_number;
    }

    public Message() {


        //  private String timestamp;
        //private String url;
        // private String image;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getChat_room_id() {
        return chat_room_id;
    }

    public void setChat_room_id(String chat_room_id) {
        this.chat_room_id = chat_room_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSent() {
        return sent;
    }

    public void setSent(Long sent) {
        this.sent = sent;
    }


    public Long getMessage_number() {
        return message_number;
    }

    public void setMessage_number(Long message_number) {
        this.message_number = message_number;
    }
}