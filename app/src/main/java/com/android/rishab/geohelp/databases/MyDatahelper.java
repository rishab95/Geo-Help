package com.android.rishab.geohelp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.rishab.geohelp.utils.Constants;

/**
 * Created by rishab on 25/7/16.
 */
public class MyDatahelper extends SQLiteOpenHelper {

    public MyDatahelper(Context context) {
        super(context, Constants.DATABASE, null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table location_user(lid integer primary key autoincrement, latitude text, longitude text, Time text);");
        db.execSQL("Create table contacts (cid integer primary key autoincrement, name text, mobile text);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists "+ Constants.table_location);

        onCreate(db);

    }

    public  boolean addCon(String name, String mob){

        SQLiteDatabase db1 = this.getWritableDatabase();

        ContentValues cv1 = new ContentValues();

        cv1.put(Constants.mName, name);
        cv1.put(Constants.mMob, mob);

        long r1 = db1.insert(Constants.table_contacts, null, cv1);

        if(r1 >= 0){
            return true;
        }
        else
        {
            return false;
        }

    }

    public void deleteRecord(String mob){

        SQLiteDatabase dbdel = this.getWritableDatabase();

        ContentValues cvdel = new ContentValues();

        String whereclause  = "mobile = " + mob  ;

        try {
            dbdel.delete("contacts", whereclause, null);
        }catch (SQLException e){

        }
        dbdel.close();

    }


    public boolean addRecord(String lat, String lon, String time){


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(Constants.currentLatitude, lat);
        cv.put(Constants.currentLongitude, lon);
        cv.put(Constants.timestamp, time);

        long r = db.insert(Constants.table_location, null, cv);

        db.close();

        if(r >= 0)
            return true;
        else
            return false;

    }

    public Cursor viewAll(String table){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(table, null, null, null, null, null, null);

       // db.close();

        return c;

    }




}
