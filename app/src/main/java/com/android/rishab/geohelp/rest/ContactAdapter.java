package com.android.rishab.geohelp.rest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.rishab.geohelp.R;
import com.android.rishab.geohelp.models.contacts;

import java.util.List;

/**
 * Created by rishab on 14/9/16.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    View mView;

    private List<contacts> contactList;

    public ContactAdapter(List<contacts> contactList) {
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
        contacts contact = contactList.get(position);
        holder.ContactName.setText(contact.getMycontact_name());
        holder.ContactPhone.setText(contact.getMymobile_no());

    }

    @Override
    public int getItemCount() {

        return contactList.size();
    }
}
