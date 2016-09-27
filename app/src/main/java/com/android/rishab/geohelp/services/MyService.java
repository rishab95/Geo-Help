
package com.android.rishab.geohelp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.android.rishab.geohelp.databases.MyDatahelper;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by admin on 14-07-2016.
 */
public class MyService extends GcmTaskService {

    String mlatitude, mlongitude;
    String mDate;
    String my_Date;
    LocationManager locationManager;
    Location mCurrentLocation;

    DateFormat currentDate;
    public static final String MyPrefrences = "MyPrefs" ;
    public static final String LocationKey = "LastLocation" ;

    public Uri builtUri;

    MyDatahelper md1;

    SharedPreferences sharedPreferences;
    String LocUrl = "";
    String Sendtoloc;


    @Override
    public int onRunTask(TaskParams taskParams) {

        switch (taskParams.getTag()) {
            case "TaskSingle":

                Log.e("gcmService", "Task1 Running");
                getLastKnownLocation();

                return GcmNetworkManager.RESULT_SUCCESS;

            default:
                return GcmNetworkManager.RESULT_FAILURE;
        }
    }

    public void getLastKnownLocation() {
        Location lastKnownGPSLocation;
        Location lastKnownNetworkLocation;

        String gpsLocationProvider = LocationManager.GPS_PROVIDER;
        String networkLocationProvider = LocationManager.NETWORK_PROVIDER;

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            lastKnownNetworkLocation = locationManager.getLastKnownLocation(networkLocationProvider);
            lastKnownGPSLocation = locationManager.getLastKnownLocation(gpsLocationProvider);

            if (lastKnownGPSLocation != null) {
                Log.e("gcm", "lastKnownGPSLocation is used.");
                this.mCurrentLocation = lastKnownGPSLocation;
                if(isNetworkAvailable()) {
                    Location myLocation = this.mCurrentLocation;
                    my_Date = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
                    //setLocationOnServer(myLocation, my_Date);
                    intodb(myLocation, my_Date);



                }
            } else if (lastKnownNetworkLocation != null) {
                Log.e("gcm", "lastKnownNetworkLocation is used.");
                this.mCurrentLocation = lastKnownNetworkLocation;
                if(isNetworkAvailable()) {
                    Location myLocation = this.mCurrentLocation;
                    my_Date = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
                    // setLocationOnServer(myLocation, my_Date);
                    intodb(myLocation, my_Date);
                }
            } else {
                Log.e("gcm", "lastLocation is not known.");

            }


        } catch (SecurityException sex) {
            Log.e("gcm", "Location permission is not granted!");
        }

    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isConnected();
    }

    public void intodb(Location myLocation, String myDate){

        String x = String.valueOf(myLocation.getLatitude());
        String y = String.valueOf(myLocation.getLongitude());

        myLoc lc1 = new myLoc();

        lc1.execute(x, y, myDate);


    }



    public class myLoc extends AsyncTask<String, Void, Void>{



        @Override
        protected Void doInBackground(String... params) {

            //mLat , mlong, Date
            mlatitude = params[0];
            mlongitude = params[1];
            mDate = params[2];



            md1 = new MyDatahelper(MyService.this);

            boolean b1 = md1.addRecord(mlatitude, mlongitude, mDate);

            if(b1) {

                Log.e("Lat", mlatitude);
                Log.e("Lon", mlongitude);
                Log.e("date", mDate);

            }
            return null;
        }

        @Override
        public void onPostExecute(Void aVoid) {


            //sharedPreferences = getSharedPreferences(MyPrefrences,Context.MODE_PRIVATE);

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyService.this);
            //https://www.google.com/maps?z=12&t=m&q=38.9419+78.3020
            final String BASE_URL = "http://maps.google.com/maps?z=12&t=m";
            final String QUERY_PARAM = "q";

            builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, mlatitude + mlongitude)
                    .build();
            LocUrl = builtUri.toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("LastLocation", LocUrl);
            editor.commit();
            Log.e("Loc Url", LocUrl);
            Toast.makeText(MyService.this,LocUrl, Toast.LENGTH_SHORT).show();
            Toast.makeText(MyService.this,"Thanks", Toast.LENGTH_SHORT).show();

            //Sendtoloc = sharedPreferences.getString(LocationKey,LocUrl);
            //Log.e("Sendtolc", Sendtoloc);




            Toast.makeText(MyService.this, "Data Added", Toast.LENGTH_SHORT).show();
            Toast.makeText(MyService.this, mlatitude + " " + mlongitude , Toast.LENGTH_SHORT).show();
            Toast.makeText(MyService.this, mDate, Toast.LENGTH_SHORT).show();

            super.onPostExecute(aVoid);
        }
    }

/*
    private void setLocationOnServer(Location  myloc, String mydate)  {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Log.e("gcm","Trying to put location on server");


        mlatitude= String.valueOf(myloc.getLatitude());
        mlongitude = String.valueOf(myloc.getLongitude());
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        final DatabaseReference userCurrloc;


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();


        myRef = database.getReference(Constants.FIREBASE_LOCATION_USERS).child(uid);
        final DatabaseReference locationRef = myRef.child(Constants.FIREBASE_MYLOCATION);


        userCurrloc = locationRef.child(my_Date);
        userCurrloc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    location newLoc = new location(mlatitude, mlongitude, my_Date);
                    userCurrloc.setValue(newLoc);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }  */

}