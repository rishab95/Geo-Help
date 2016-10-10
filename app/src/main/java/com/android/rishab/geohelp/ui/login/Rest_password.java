package com.android.rishab.geohelp.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.rishab.geohelp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Rest_password extends AppCompatActivity implements View.OnClickListener {

    EditText get_email;
    Button reset_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);


        get_email = (EditText)findViewById(R.id.edit_set_email);
        reset_password = (Button)findViewById(R.id.rest_pwd);


        reset_password.setOnClickListener(this);







    }

    DialogInterface.OnClickListener ResetListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface7, int choice) {

            switch (choice){
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        String emailAddress = get_email.getText().toString();
                        if(emailAddress != null){
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.sendPasswordResetEmail(emailAddress)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent loginintent =new Intent(getApplicationContext(), LoginActivity.class);
                                                startActivity(loginintent);

                                            }
                                        }
                                    });

                        }


                    }catch (Exception e){
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {

        if(get_email.getText().toString().isEmpty()){
            Toast.makeText(Rest_password.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        }else {

            AlertDialog.Builder builder7 = new AlertDialog.Builder(this);
            builder7.setMessage(R.string.reset_string)
                    .setPositiveButton("OK", ResetListener).show();
        }

    }
}
