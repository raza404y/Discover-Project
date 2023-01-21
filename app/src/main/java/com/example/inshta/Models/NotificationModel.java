package com.example.inshta.Models;

public class NotificationModel {

    int notificationProfilePic;
    String notificationText , time;

    public NotificationModel(int notificationProfilePic, String notificationText, String time) {
        this.notificationProfilePic = notificationProfilePic;
        this.notificationText = notificationText;
        this.time = time;
    }

    public int getNotificationProfilePic() {
        return notificationProfilePic;
    }

    public void setNotificationProfilePic(int notificationProfilePic) {
        this.notificationProfilePic = notificationProfilePic;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
