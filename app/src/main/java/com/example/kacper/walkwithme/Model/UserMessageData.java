package com.example.kacper.walkwithme.Model;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class UserMessageData {
    private int msgId;
    private int senderId;
    private int receiverId;
    private String status;
    private String msgTime;
    private String message;

    public UserMessageData(int msgId, int senderId, int receiverId, String status, String msgTime, String message) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
        this.msgTime = msgTime;
        this.message = message;
    }

    public UserMessageData() {
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
