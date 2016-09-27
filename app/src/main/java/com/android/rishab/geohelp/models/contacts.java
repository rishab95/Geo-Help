package com.android.rishab.geohelp.models;

/**
 * Created by admin on 13-07-2016.
 */
public class contacts {

    String mycontact_name;
    String mymobile_no;


    public contacts(){
    }

    public void setMycontact_name(String mycontact_name) {
        this.mycontact_name = mycontact_name;
    }

    public void setMymobile_no(String mymobile_no) {
        this.mymobile_no = mymobile_no;
    }

    public contacts(String mycontactname, String mymobile_no){
        this.mycontact_name = mycontactname;
        this.mymobile_no = mymobile_no;
    }

    public String getMycontact_name() {
        return mycontact_name;
    }

    public String getMymobile_no() {
        return mymobile_no;
    }


}

