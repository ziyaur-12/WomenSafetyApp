package com.example.womensafetyapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Contact {
    public String id;
    public String name;
    public String phoneNumber;

    public Contact() {
        // Default constructor required for Firebase
    }

    public Contact(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Explicit getters for Java compatibility if needed, 
    // though public fields work too.
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
}
