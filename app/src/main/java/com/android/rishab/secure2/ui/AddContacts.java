package com.android.rishab.secure2.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.rishab.secure2.R;
import com.android.rishab.secure2.models.contacts;
import com.android.rishab.secure2.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddContacts extends AppCompatActivity implements View.OnClickListener {

    Button get, push;
    TextView showcon, cName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showcon = (TextView)findViewById(R.id.show_contact);
        cName = (TextView)findViewById(R.id.con_name);
        get = (Button)findViewById(R.id.get_contact);
        push = (Button)findViewById(R.id.push_contact);

        push.setOnClickListener(this);
        get.setOnClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    private void loadContact()
    {
        Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(in, 10);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String contactId, name, cname = "";
        String ph = "", cNumber;

        if (requestCode == 10) {
            if (resultCode == 1) {


                Uri contactData = data.getData();
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                if (cursor.moveToFirst()) {


                    String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phones = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null);
                        phones.moveToFirst();
                        cNumber = phones.getString(phones.getColumnIndex("data_1"));
                        System.out.println("number is:" + cNumber);
                        showcon.setText(cNumber);
                    }


                }
            }
        }

    }



    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.get_contact:
                loadContact();

                break;


        }

    }

    private void pushContact(final String name, final String mobNo){



        FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
        DatabaseReference myRef;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        myRef = database.getReference(Constants.FIREBASE_LOCATION_USERS).child(uid);
        final DatabaseReference ContactRef = myRef.child(Constants.FIREBASE_LOCATION_CONTACTS);
        DatabaseReference getConId = ContactRef.push();

        final String cid = getConId.getKey();

        final DatabaseReference mRef = ContactRef.child(cid);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    contacts newCont = new contacts(name, mobNo);
                    mRef.setValue(newCont);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
