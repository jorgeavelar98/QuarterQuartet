package com.example.scavengerhuntapp.objects;


import java.util.HashMap;
import java.util.Map;

public class User {
    public static final String KEY_PLAYER_TYPE = "playerType";
    public static final String KEY_PLAYERS = "players";
    public static final String KEY_ORGANIZERS = "organizers";
    public static final String KEY_ORGANIZER = "organizer";
    public static final String KEY_PLAYER = "player";
    public static final String KEY_HUNTS = "hunts";

    private String userID;
    private String fullName;
    private String email;
    private String userType;
    private Map<String, Map<String, String>> hunts;

    public User(){
    }

    public User(String userID, String fullName, String email, String userType){
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
        this.hunts = new HashMap<>();
    }

    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public Map<String, Map<String, String>> getHunts() {
        return hunts;
    }

    public void addHunt(String huntID, Map<String, String> huntValues){
        this.hunts.put(huntID, huntValues);
    }
}
