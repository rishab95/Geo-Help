package com.android.rishab.geohelp.utils;

import android.content.Context;
import android.widget.Toast;
/**
 * Created by rishab on 6/9/16.
 */
public class utils {
    public static void showToast(Context context,String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
