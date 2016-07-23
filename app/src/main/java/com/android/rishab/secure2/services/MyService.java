package com.android.rishab.secure2.services;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.Log;

import com.android.rishab.secure2.models.location;
import com.android.rishab.secure2.utils.Constants;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by admin on 14-07-2016.
 */
public class MyService extends GcmTaskService {

    String mlatitude, mlongitude;
    String my_Date;
    LocationManager locationManager;
    Location mCurrentLocation;

    DateFormat currentDate;



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
                    setLocationOnServer(myLocation, my_Date);
                }
            } else if (lastKnownNetworkLocation != null) {
                Log.e("gcm", "lastKnownNetworkLocation is used.");
                this.mCurrentLocation = lastKnownNetworkLocation;
                if(isNetworkAvailable()) {
                    Location myLocation = this.mCurrentLocation;
                    my_Date = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
                    setLocationOnServer(myLocation, my_Date);
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


    private void setLocationOnServer(Location  myloc, String mydate)
    {
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

    }

}



