package com.android.rishab.geohelp.models;

/**
 * Created by admin on 15-07-2016.
 */
public class location{

    String latitude;
    String longitude;
    String time_stamp;

    public location() {
    }

    public location(String latitude, String longitude, String time_stamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time_stamp = time_stamp;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
