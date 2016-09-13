package com.android.rishab.geohelp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jagdeep on 17/07/16.
 */
public class VolumeChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            int newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
            int oldVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0);
            if (newVolume != oldVolume) {
               // Toast.makeText(context, "Vol", Toast.LENGTH_SHORT).show();

                Log.e("PowerTest", "Volume");
                /*
                Intent i = new Intent();
                i.setClass(context, YourActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
           */
            }
        }
    }
}