package com.android.rishab.geohelp.models;

import java.util.List;

/*
 * Created by admin on 10-07-2016.
 */
public class User {
    private String uname;
    private String email;
    private List<contacts> cList;

    public User() {

    }

    public User(String uname, String email) {
        this.uname = uname;
        this.email = email;
    }

    public String getUname() {
        return uname;
    }

    public void setcList(List<contacts> cList) {
        this.cList = cList;
    }

    public List<contacts> getcList() {

        return cList;
    }

    public String getEmail() {
        return email;
    }


}


