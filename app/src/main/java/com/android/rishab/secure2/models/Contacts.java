package com.android.rishab.secure2.models;

/**
 * Created by admin on 13-07-2016.
 */
public class Contacts {

    String Name;
    int mobile_no;

    public Contacts(){

    }

    public Contacts(String Name, int mobile_no){
        this.Name = Name;
        this.mobile_no = mobile_no;
    }


    public String getName() { return Name;  }

    public int getMobile_no() { return mobile_no; }

}
