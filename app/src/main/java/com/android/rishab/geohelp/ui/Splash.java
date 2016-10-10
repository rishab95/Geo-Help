package com.android.rishab.geohelp.ui;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.rishab.geohelp.R;
import com.android.rishab.geohelp.ui.login.LoginActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class Splash extends Activity {


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9200;

    boolean res = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //  checkPlayServices();



        if(Build.VERSION.SDK_INT >  Build.VERSION_CODES.LOLLIPOP_MR1) {

            if ((ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)

                    || (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

                    || (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)

                    || (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)

                    || (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED)


                    ) {

                String[] perms = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED
                };
                ActivityCompat.requestPermissions(this, perms, 10);

                return;
            } else {
                new mytask().execute();
            }
        }
        else
        {
            new mytask().execute();
        }

    }


    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        res = false;

        for(int i=0; i < grantResults.length; i++)
        {

            Log.e("firetest", String.valueOf(grantResults[i]));

            if(grantResults[i] != 0)
            {
                break;
            }
            else
            {
                res = true;
            }


        }

        if(res)
        {

            new mytask().execute();
        }
        else
        {
            Toast.makeText(this, "App will not work without permissions", Toast.LENGTH_LONG).show();
        }

    }



    private class mytask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent in = new Intent(Splash.this, LoginActivity.class);
            startActivity(in);
            finish();

        }
    }




}
