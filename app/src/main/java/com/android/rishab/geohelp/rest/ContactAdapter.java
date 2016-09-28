package com.android.rishab.geohelp.rest;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.rishab.geohelp.R;
import com.android.rishab.geohelp.models.contacts;
import com.android.rishab.geohelp.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by rishab on 14/9/16.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    View mView;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();


    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = currentUser.getUid();


    DatabaseReference ContactRef = database.getReference(Constants.FIREBASE_LOCATION_USERS).child(uid);

    DatabaseReference contempReference  = ContactRef.child(Constants.FIREBASE_LOCATION_CONTACTS);





    private List<contacts> contactList;

    public ContactAdapter(List<contacts> contactList)
    {
        this.contactList = contactList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView ContactName, ContactPhone;
        public MyViewHolder(View itemView) {
            super(itemView);
            ContactName = (TextView)itemView.findViewById(R.id.contactname);
            ContactPhone = (TextView)itemView.findViewById(R.id.contactphone);
        }
    }



    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_card,parent,false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.MyViewHolder holder, int position) {
        final contacts contact = contactList.get(position);
        holder.ContactName.setText(contact.getMycontact_name());
        holder.ContactPhone.setText(contact.getMymobile_no());
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.e("Click", "YESSSSSSS");

                final String mobile = contact.getMymobile_no();
                Log.e("Mob", mobile);
                deleteContact(mobile);





            }
        });
    }



    DialogInterface.OnClickListener deleteListeneralert = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int choice) {

            switch (choice){
                case DialogInterface.BUTTON_POSITIVE:
                    try {


                    }catch (Exception e){
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }

        }
    };



    private void deleteContact(final String mob){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference GetContactRef;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        GetContactRef = database.getReference(Constants.FIREBASE_LOCATION_USERS).child(uid);

        final DatabaseReference contempReference  = GetContactRef.child(Constants.FIREBASE_LOCATION_CONTACTS);

        final DatabaseReference deleteRef = contempReference.child(mob);

        deleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("Remove", "Yes Removed Yuhu");
            }
        });

        deleteRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
        });





    }


    @Override
    public int getItemCount() {

        return contactList.size();
    }
}
