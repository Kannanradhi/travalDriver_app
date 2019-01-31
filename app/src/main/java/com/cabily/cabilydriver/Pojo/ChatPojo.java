package com.cabily.cabilydriver.Pojo;

/**
 * Created by Prem Kumar and Anitha on 1/29/2016.
 */
public class ChatPojo {
    private String message, time, type;
    private String status;
    int _id;


    int chatRideId;

    // Empty constructor
    public ChatPojo() {

    }

    // constructor
    public ChatPojo(int id, String message, String time, String type, String status) {
        this._id = id;
        this.message = message;
        this.time = time;
        this.type = type;
        this.status = status;


    }

    // constructor
    public ChatPojo(String message, String type, String time, String status, int rideId) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.status = status;
        this.chatRideId = rideId;

    }

    public int getChatRideId() {
        return chatRideId;
    }

    public void setChatRideId(int chatRideId) {
        this.chatRideId = chatRideId;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
