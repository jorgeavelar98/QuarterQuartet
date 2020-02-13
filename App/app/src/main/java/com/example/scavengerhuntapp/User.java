package com.example.scavengerhuntapp;


import java.util.HashMap;
import java.util.Map;

public class User {
    static final String KEY_ORGANIZERS = "organizers";
    static final String KEY_PLAYERS = "players";
    static final String KEY_HUNTS = "hunts";
    static final String KEY_TEAM_NAME = "teamName";

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
