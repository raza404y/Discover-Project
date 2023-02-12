package com.example.inshta.Models;

public class messageModel {

    String uId , message , receiverid;
    long time;

    public messageModel(String uId, String message, long time) {
        this.uId = uId;
        this.message = message;
        this.time = time;
    }

    public messageModel() {
    }

    public messageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
