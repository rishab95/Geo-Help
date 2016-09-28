package com.android.rishab.geohelp.utils;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.android.rishab.geohelp.R;

/**
 * Created by rishab on 28/9/16.
 */
public class Prefactivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_setting);

    }
}
