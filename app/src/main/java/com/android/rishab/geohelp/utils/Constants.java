package com.android.rishab.geohelp.utils;

import android.widget.Toast;

/**
 * Created by admin on 10-07-2016.
 */
public final class Constants {
    public static String FIREBASE_URL = "https://securehome-b0c59.firebaseio.com";
    public static String FIREBASE_LOCATION_USERS = "users";


    public static String KEY_USER_ID = "uid";

    public static String FIREBASE_LOCATION_CONTACTS = "contacts";
    public static String FIREBASE_URL_USERS = FIREBASE_URL + FIREBASE_LOCATION_USERS;
    public static String FIREBASE_URL_CONTACTS = FIREBASE_URL_USERS +  FIREBASE_LOCATION_CONTACTS;

    public static String FIREBASE_MYLOCATION = "location";
    public static String FIREBASE_URL_MYLOCATION = FIREBASE_URL_USERS + FIREBASE_MYLOCATION;

    public static String KEY_USER_EMAIL = "email";
    
    public static String KEY_CONTACT_ID = "cid";



    public static String ONE_TASK = "TaskSingle";
    public static String MULTI_TASK = "TaskMulti";

    //     Sqlite Helper Names
    public final static String DATABASE = "Mydata";
    public final static String table_location = "Location_user";

    public static String KEY_LOCATION_ID = "lid";
    public final static String currentLatitude = "latitude";
    public final static String currentLongitude = "longitude";
    public final static String timestamp = "Time";



    public final static String table_contacts = "contacts";
    public final static String mName = "name";
    public final static String mMob = "mobile";

    public static final String LocationKey = "LastLocation" ;

    public static final String LocUrl="";






}
