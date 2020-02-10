package com.example.scavengerhuntapp;


public class Broadcast {
    static final String KEY_BROADCASTS = "broadcasts";
    static final String KEY_BROADCAST_ID = "broadcastID";
    static final String KEY_MESSAGE = "message";


    private String broadcastID;
    private String message;

    public Broadcast(){ }

    public Broadcast(String broadcastID, String message) {
        this.broadcastID = broadcastID;
        this.message = message;
    }

    public String getBroadcastID() {
        return broadcastID;
    }

    public String getMessage() {
        return message;
    }
}
