package com.android.rishab.secure2.models;

/**
 * Created by admin on 10-07-2016.
 */
public class User {
    private String uname;
    private String email;

    public User() {
    }

    public User(String uname, String email) {
        this.uname = uname;
        this.email = email;
    }

    public String getUname() {
        return uname;
    }

    public String getEmail() {
        return email;
    }
}


