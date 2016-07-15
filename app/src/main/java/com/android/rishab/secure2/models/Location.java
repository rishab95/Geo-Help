package com.android.rishab.secure2.models;

/**
 * Created by admin on 15-07-2016.
 */
public class location{

    String latitude;
    String longitude;

    public location() {
    }

    public location(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
