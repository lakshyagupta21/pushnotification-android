package com.dexter.pushnotificationandroid.models;

public class User {

    public String username;
    public String email;
    public String token;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String token) {
        this.username = username;
        this.email = email;
        this.token = token;
    }

}
// [END blog_user_class]
