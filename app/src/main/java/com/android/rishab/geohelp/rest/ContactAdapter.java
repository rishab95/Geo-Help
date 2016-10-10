package com.android.rishab.geohelp.rest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.rishab.geohelp.R;
import com.android.rishab.geohelp.databases.MyDatahelper;
import com.android.rishab.geohelp.models.contacts;
import com.android.rishab.geohelp.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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




    MyDatahelper md2;





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
            md2 = new MyDatahelper(itemView.getContext());
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
                deletelocal(mobile);
            }
        });
    }




    public void deletelocal(String mobile){

        String mbl = String.valueOf(mobile);
        Log.e("to del", mbl);
        md2.deleteRecord(mbl);
        Log.e("delte local", "YES Deleted");

    }



    private void deleteContact(final String mob){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference GetCtactRef;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        GetCtactRef = database.getReference(Constants.FIREBASE_LOCATION_USERS).child(uid);

        final DatabaseReference ctempReference  = GetCtactRef.child(Constants.FIREBASE_LOCATION_CONTACTS);

        final DatabaseReference delRef = ctempReference.child(mob);

        delRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("Remove", "Yes Removed Yuhu");
            }
        });






    }


    @Override
    public int getItemCount() {

        return contactList.size();
    }
}