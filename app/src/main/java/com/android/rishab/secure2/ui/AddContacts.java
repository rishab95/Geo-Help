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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;// contacts unique ID
    String MycontactNumber , MycontactName, cname, cno;

    private TextView phone, con_name;

    private EditText phone_edit, message_edit;
    Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        get = (Button)findViewById(R.id.get_contact);



        phone = (TextView)findViewById(R.id.phone);
        con_name = (TextView)findViewById(R.id.name);

        phone_edit = (EditText) findViewById(R.id.enter_phone);
        message_edit = (EditText)findViewById(R.id.enter_messsge);
        b1= (Button)findViewById(R.id.button);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        b1.setOnClickListener(this);
        get.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button:
                sendSMS();
                break;

            case R.id.get_contact:
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
                        REQUEST_CODE_PICK_CONTACTS);
        }

    }


/*    public void onClickSelectContact(View btnSelectContact) {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

*/






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            cname = retrieveContactName();
            cno = retrieveContactNumber();
            pushContact(cname, cno);
        }
    }


    private String retrieveContactNumber() {

        MycontactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);



        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            MycontactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + MycontactNumber);
        phone.setText(MycontactNumber);

        return MycontactNumber;
    }

    private String retrieveContactName() {

        MycontactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            MycontactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + MycontactName);
        con_name.setText(MycontactName);

        return MycontactName;

    }


    private void pushContact(final String ConName, final String mobNo){



        FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
        DatabaseReference myRef;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        myRef = database.getReference(Constants.FIREBASE_LOCATION_USERS).child(uid);
        final DatabaseReference ContactRef = myRef.child(Constants.FIREBASE_LOCATION_CONTACTS);


        DatabaseReference getConId = ContactRef.push();



        final String cid = getConId.getKey();

        final DatabaseReference mRef = ContactRef.child(cid);

        /*mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() == null) {
                    contacts newCont = new contacts(ConName, mobNo);
                    mRef.setValue(newCont);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    contacts newCont = new contacts(ConName, mobNo);
                    mRef.setValue(newCont);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendSMS(){


        final FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
        DatabaseReference GetContactRef;



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        GetContactRef = database.getReference(Constants.FIREBASE_LOCATION_USERS).child(uid);

        final DatabaseReference contempReference  = GetContactRef.child(Constants.FIREBASE_LOCATION_CONTACTS);

        contempReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("There are " + dataSnapshot.getChildrenCount() + " Contacts ");
                for (DataSnapshot ContactSnapshot: dataSnapshot.getChildren()) {
                    contacts getcon = ContactSnapshot.getValue(contacts.class);
                    String check  = String.valueOf(getcon.getMycontact_name() + "  " + getcon.getMymobile_no());
                    Log.e("contact is : ", check );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        if(TextUtils.isEmpty(phone_edit.getText().toString())) {
            return;
        }

        if(TextUtils.isEmpty(message_edit.getText().toString())) {
            return;
        }
    }

}
