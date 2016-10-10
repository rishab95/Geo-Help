package com.android.rishab.geohelp.services;

/**
 * Created by rishab on 25/8/16.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;

import com.android.rishab.geohelp.databases.MyDatahelper;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jagdeep on 13/07/16.
 */
public class CustomService extends GcmTaskService {

    MyService mysrv;

    String msg;
    String toloc;



    SharedPreferences sharedPreferences1;

    String textmsg;

    int i=0;
    List<String> phones, names;


    MyService MyLoc = new MyService();



//    String bsms = "Help me i at ";



    @Override
    public int onRunTask(TaskParams taskParams) {

        switch (taskParams.getTag()) {
            case "Messagetask":
             //   bsms = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("bsms", null);
                Log.e("gcmservice1del", "SmsSendStart");
                loadContacts();
                return GcmNetworkManager.RESULT_SUCCESS;
            default:
                return GcmNetworkManager.RESULT_FAILURE;

        }
    }



    @Override
    public int onStartCommand(Intent intent, int i, int i1) {

        Log.e("gcmservice1del", "Task Start");
        return super.onStartCommand(intent, i, i1);

    }



    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if(contactName == null)
        {
            contactName = "Unknown";
        }
        return contactName;
    }



    private void sendSMS(String phone, String name) {




        SmsManager sm1 = SmsManager.getDefault();

        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(CustomService.this);
        toloc = sharedPreferences1.getString("LastLocation","");
        Log.e("LOCCCCC", toloc);

        textmsg = sharedPreferences1.getString("My Message", "");



        String myLc = (toloc).toString();
        Log.e("TEST",myLc);
        Log.e("TESTING    ", textmsg);
        msg = textmsg +  "I am at " +  myLc;
        Log.e("Text11",msg);


        List<String> messages = sm1.divideMessage("Dear " + name +" " + msg);


        for (String message : messages) {
            /*

            PendingIntent pi = PendingIntent.getBroadcast(this, 0 ,
                    new Intent("com.netmaxtech.smscrm.smssent"), 0);
*/
            Log.e("Message", message);
            Log.e("Phone", phone);
            sm1.sendTextMessage(phone, null, message, null, null);
        }
    }


    BroadcastReceiver br1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = "";
            boolean error = true;

            switch (getResultCode())
            {

                case Activity.RESULT_OK:

                    error = false;
                    status = "Message Sent";

                    break;

                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    status = "Error";
                    break;

                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    status = "No Service";
                    break;

                case SmsManager.RESULT_ERROR_NULL_PDU:
                    status = "No Data";
                    break;

                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    status = "Airplane";
                    break;
            }

            Log.i("SMSCRM", status);

            if(phones.get(i) != null)
            {
                sendSMS(phones.get(i), names.get(i));
                i++;
            }

        }
    };

    private void loadContacts()
    {
        //Log.i("Error", "Record Not found");
        phones = new ArrayList<String>();
        names = new ArrayList<String>();

        MyDatahelper dh1 = new MyDatahelper(getApplicationContext());

        Cursor c = dh1.viewAll("contacts");

        if(c.getCount() < 0 )
        {
            Log.e("Error", "Record Not found");
            return;
        }

        while (c.moveToNext())
        {

            String name = c.getString(c.getColumnIndex("name"));
            String ph = c.getString(c.getColumnIndex("mobile"));
            Log.e("Check No", "Contact Phone Number: " + ph);
            Log.e("Check Name", "Contact Phone Number: " + name);

            phones.add(ph);
            names.add(name);
        }
        smsLoop(c.getCount());
    }


    private void smsLoop(int c)
    {
        for(i = 0; i< c; i++)
        {
            sendSMS(phones.get(i), names.get(i));
        }
    }
}
