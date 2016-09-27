package com.android.rishab.geohelp.ui;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.rishab.geohelp.R;
import com.android.rishab.geohelp.services.CustomService;
import com.android.rishab.geohelp.services.MyService;
import com.android.rishab.geohelp.services.UpdateService;
import com.android.rishab.geohelp.ui.login.LoginActivity;
import com.firebase.client.Firebase;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.firebase.auth.FirebaseAuth;

//import android.location.LocationListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth.AuthStateListener mAuthListener;

    GcmNetworkManager myManager, gcmNetworkManager;


    ImageView img1;

    Button Start, Stop;

    //Switch service_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        Start = (Button)findViewById(R.id.start_service);
        Stop = (Button)findViewById(R.id.stop_service);
        Start.setOnClickListener(this);
        Stop.setOnClickListener(this);

        img1 = (ImageView)findViewById(R.id.imageView2);

        img1.setOnClickListener(this);

        myManager = GcmNetworkManager.getInstance(this);

        gcmNetworkManager = GcmNetworkManager.getInstance(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Window window1 = getWindow();

        window1.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        window1.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock("abc");

        lock.disableKeyguard();

        Intent in = new Intent(this, UpdateService.class);
        startService(in);

    }

    private void startSendingSMS()
    {

        OneoffTask task = new OneoffTask.Builder()
                .setService(CustomService.class)
                .setTag("Messagetask")
                .setExecutionWindow(0L, 60L)
                .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                .build();

        gcmNetworkManager.schedule(task);

    }

    DialogInterface.OnClickListener startListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int choice) {

            switch (choice){
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        startTask();
                        Toast.makeText(MainActivity.this, "Service Started", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }

        }
    };


    DialogInterface.OnClickListener stopListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int choice) {

            switch (choice){
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        cancelAllTask();
                        Toast.makeText(MainActivity.this, "Service Stopped", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }

        }
    };

    DialogInterface.OnClickListener MessageListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int choice) {

            switch (choice){
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        startSendingSMS();
                        Toast.makeText(MainActivity.this, "Message Sending Started", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }

        }
    };




    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.imageView2:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setMessage("Do you want start sending messages?").setPositiveButton("YES",MessageListener)
                        .setNegativeButton("NO",MessageListener).show();
                break;
            case R.id.start_service:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Do you want start fetching location?").setPositiveButton("YES",startListener)
                        .setNegativeButton("NO",startListener).show();

            case R.id.stop_service:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage("Do you want stop  fetching location?").setPositiveButton("YES",stopListener)
                        .setNegativeButton("NO",stopListener).show();

        }

    }




    BroadcastReceiver mybroadcast = new BroadcastReceiver() {
        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("[BroadcastReceiver]", "MyReceiver");

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.i("[BroadcastReceiver]", "Screen ON");
            }
            else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i("[BroadcastReceiver]", "Screen OFF");
            }

        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.logout){
            logout();
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.home) {
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
            // Handle the camera action
        } else if (id == R.id.contacts) {
            Intent in1 = new Intent(this, ShowContacts.class);
            startActivity(in1);

        } else if (id == R.id.messages) {


        } else if (id == R.id.my_location) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void takeUserToLoginScreenOnUnAuth() {
        /* Move user to LoginActivity, and remove the backstack */
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    protected void logout() {
        FirebaseAuth.getInstance().signOut();
        takeUserToLoginScreenOnUnAuth();
    }

    private void startTask()
    {

        Task task1 =  new PeriodicTask.Builder()
                .setService(MyService.class)
                .setPeriod(3)
                .setFlex(1)
                .setPersisted(true)
                .setTag("TaskSingle")
                .build();

        myManager.schedule(task1);
    }


    private void cancelAllTask()
    {
        myManager.cancelAllTasks(MyService.class);
    }

    private void cancelTask(String tag)
    {
        myManager.cancelTask(tag, MyService.class);
    }

}