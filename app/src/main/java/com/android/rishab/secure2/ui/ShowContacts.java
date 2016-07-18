package com.android.rishab.secure2.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.android.rishab.secure2.R;
import com.android.rishab.secure2.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowContacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts);

        ListView contactView = (ListView)findViewById(R.id.contact_list);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_URL_CONTACTS);

    //    mAdapter = new FirebaseList


    }


}
