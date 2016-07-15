package com.android.rishab.secure2.models;

/**
 * Created by admin on 13-07-2016.
 */
public class contacts {

    String Name;
    String mobile_no;

    public contacts(){

    }

    public contacts(String Name, String mobile_no){
        this.Name = Name;
        this.mobile_no = mobile_no;
    }


    public String getName() { return Name;  }

    public String getMobile_no() { return mobile_no; }

}
