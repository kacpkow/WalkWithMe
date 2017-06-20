package com.example.kacper.walkwithme.Model;

/**
 * Created by kacper on 2017-06-09.
 */

public class NotificationData {
    private int notification_id;
    private int userId;
    private String status;
    private String type;
    private int eventId;
    private UserProfileData sender;

    public NotificationData() {
    }

    public NotificationData(int notification_id, int userId, String status, String type, int eventId, UserProfileData sendre) {
        this.notification_id = notification_id;
        this.userId = userId;
        this.status = status;
        this.type = type;
        this.eventId = eventId;
        this.sender = sendre;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public UserProfileData getSender() {
        return sender;
    }

    public void setSender(UserProfileData sender) {
        this.sender = sender;
    }
}
