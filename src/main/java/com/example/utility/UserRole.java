package com.example.utility;

public enum UserRole {
    ORGANIZER("organizer"),
    COMPETITOR("competitor"),
    ADMIN("administrator");
    public final String label;
    UserRole(String label){
        this.label = label;
    }


}
