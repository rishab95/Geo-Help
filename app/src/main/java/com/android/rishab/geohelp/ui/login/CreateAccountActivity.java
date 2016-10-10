package com.android.rishab.geohelp.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rishab.geohelp.R;
import com.android.rishab.geohelp.models.User;
import com.android.rishab.geohelp.utils.Constants;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = CreateAccountActivity.class.getSimpleName();
    private final String FIREBASE_URL = "https://securehome-b0c59.firebaseio.com";
    private FirebaseDatabase database;
    private ProgressDialog mAuthProgressDialog;
    private FirebaseAuth mAuth;
    private Firebase mFirebaseRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mEditTextUsernameCreate, mEditTextEmailCreate, mEditTextPasswordCreate;
    private String mUserName, mUserEmail, mPassword;
    private TextView signIn;

    private Button signin, signup;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase(FIREBASE_URL);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener  = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // User is signed in
                    Log.d(LOG_TAG, "hi");
                } else {
                    // User is signed out
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        initializeScreen();
    }

    public void initializeScreen(){
        mEditTextEmailCreate = (EditText)findViewById(R.id.edit_text_email_create);
        mEditTextUsernameCreate = (EditText)findViewById(R.id.edit_text_username_create);
        mEditTextPasswordCreate = (EditText)findViewById(R.id.edit_text_password_create);

        signin = (Button)findViewById(R.id.button_signin);
        signup = (Button)findViewById(R.id.btn_create_account_final);

        signin.setOnClickListener(this);
        signup.setOnClickListener(this);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        mAuthProgressDialog.setCancelable(false);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_create_account_final:
                onCreateAccountPressed();
                break;

            case R.id.button_signin:
                onSignInPressed();
                break;
        }




    }

    /**
     * Create new account using Firebase email/password provider
     */
    public void onCreateAccountPressed() {
        mUserName = mEditTextUsernameCreate.getText().toString();
        mUserEmail = mEditTextEmailCreate.getText().toString();
        mPassword = mEditTextPasswordCreate.getText().toString();
        //client side checking
        boolean validEmail = isEmailValid(mUserEmail);
        boolean validUserName = isUserNameValid(mUserName);
        boolean validPassword = isPasswordValid(mPassword);
        if (!validEmail || !validUserName || !validPassword) return;

        /**
         * If everything was valid show the progress dialog to indicate that
         * account creation has started
         */
        mAuthProgressDialog.show();

        /**
         * Create new user with specified email and password
         */
        mAuth.createUserWithEmailAndPassword(mUserEmail, mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            String uid = task.getResult().getUser().getUid();
                            createUserInFirebaseHelper(uid);
                            mAuthProgressDialog.dismiss();
                            showToast("Account Created Successfully");
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);

                        }
                        else {
                            Log.w(LOG_TAG, "signInWithEmail", task.getException());
                            mAuthProgressDialog.dismiss();
                            showToast("Authentification Failed");
                        }
                    }
                });
    }

    /**
     * Creates a new user in Firebase from the Java POJO
     */
    private void createUserInFirebaseHelper(final String uid) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_USERS);
        final DatabaseReference userLocation = myRef.child(uid);
        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //no user,create it
                if(dataSnapshot.getValue() == null) {
                    User newUser = new User(mUserName,mUserEmail);
                    userLocation.setValue(newUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, "Error occured" + databaseError.getMessage());

            }
        });

    }

    private boolean isEmailValid(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEditTextEmailCreate.setError(String.format(getString(R.string.error_invalid_email_not_valid),
                    email));
            return false;
        }
        return isGoodEmail;
    }

    private boolean isUserNameValid(String userName) {
        if (userName.equals("")) {
            mEditTextUsernameCreate.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }



        return true;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            mEditTextPasswordCreate.setError(getResources().getString(R.string.error_invalid_password_not_valid));
            return false;
        }
        return true;
    }

    /**
     * Show error toast to users
     */
    private void showToast(String message) {
        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onSignInPressed(){
        Intent in = new Intent(CreateAccountActivity.this,  LoginActivity.class);
        startActivity(in);
    }

}