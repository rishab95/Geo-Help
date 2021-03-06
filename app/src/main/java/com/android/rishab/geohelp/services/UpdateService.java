package com.android.rishab.geohelp.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.rishab.geohelp.ui.MainActivity;

/**
 * Created by Jagdeep on 17/07/16.
 */
public class UpdateService extends Service {

    int count;
    long interval1, interval2, interval3, on, off;
    @Override
    public void onCreate() {
        super.onCreate();
        // register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mybroadcast, filter);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    BroadcastReceiver mybroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.e("PowerTest", "MyReceiver"+ count);
      //      Toast.makeText(context,"MyReceiver"+count,Toast.LENGTH_SHORT).show();

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.e("PowerTest", "Screen ON");

                on = System.currentTimeMillis();

                if(on - off <= 1000)
                {
                 count++;
                }
                else
                {
                    count=0;
                }
            }

            else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.e("PowerTest", "Screen OFF");
                off = System.currentTimeMillis();
                interval1 = off - on;
                if(interval1 <= 1000)
                {
                    count++;
                }
                else
                {
                    count = 0;
                }
                if(count > 2 )
                {
                    Log.e("PowerTest", "Fast Press");
                    count =0;

                    Intent in = new Intent(context, MainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                }
            }
        }
    };
}