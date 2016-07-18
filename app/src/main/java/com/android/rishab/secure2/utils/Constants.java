package com.android.rishab.secure2.utils;

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
    public static String KEY_LOCATION_ID = "lid";


    public static String ONE_TASK = "TaskSingle";
    public static String MULTI_TASK = "TaskMulti";
}
