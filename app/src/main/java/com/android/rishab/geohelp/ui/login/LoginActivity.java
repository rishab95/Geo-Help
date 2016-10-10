package com.android.rishab.geohelp.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rishab.geohelp.R;
import com.android.rishab.geohelp.ui.MainActivity;
import com.android.rishab.geohelp.utils.Constants;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;
    private EditText mEditTextEmailInput, mEditTextPasswordInput;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button SignIn, SignUp;
    private String email, password;

    private TextView forget_pwd_txt;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();


                // ...
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //direct to main activity
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    Log.d(LOG_TAG, "hi");
                } else {
                    // User is signed out
                    Log.d(LOG_TAG, "signed_out");
                }
                // ...
            }
        };
        initializeScreen();

        /**
         * Link layout elements from XML and setup progress dialog
         */


        /**
         * Call signInPassword() when user taps "Done" keyboard action
         */
        mEditTextPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    signInPassword();
                }
                return true;
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Override onCreateOptionsMenu to inflate nothing
     *
     * @param menu The menu with which nothing will happen
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    /**
     * Sign in with Password provider when user clicks sign in button
     */


    public void onSignInPressed() {


        email = mEditTextEmailInput.getText().toString();
        password = mEditTextPasswordInput.getText().toString();

        boolean validEmail = isEmailValid(email);
        boolean validPassword = isPasswordValid(password);
        if (!validEmail || !validPassword) return;


        mAuthProgressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            String uid = task.getResult().getUser().getUid();
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor spe = sp.edit();
                            spe.putString(Constants.KEY_USER_ID,uid).apply();

                            Toast.makeText(LoginActivity.this,"Login successfull",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra(Intent.EXTRA_TEXT,uid);
                            startActivity(intent);
                        }
                        else {
                            Log.w(LOG_TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mAuthProgressDialog.cancel();
                        }
                    }
                });

    }

    private boolean isEmailValid(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEditTextEmailInput.setError("Please Enter Email");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            mEditTextPasswordInput.setError("Please Enter Password");
            return false;
        }
        return true;
    }

    /**
     * Open CreateAccountActivity when user taps on "Sign up" TextView
     */
    public void onSignUpPressed() {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    /**
     * Link layout elements from XML and setup the progress dialog
     */
    public void initializeScreen() {
        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);
        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);



        SignIn = (Button)findViewById(R.id.login_with_password);
        SignUp = (Button)findViewById(R.id.btn_signup);

        SignIn.setOnClickListener(this);
        SignUp.setOnClickListener(this);

        forget_pwd_txt = (TextView)findViewById(R.id.forgot_password_text);

        forget_pwd_txt.setOnClickListener(this);



        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Logging In");
        mAuthProgressDialog.setCancelable(false);



    }


    /**
     * Sign in with Password provider (used when user taps "Done" action on keyboard)
     */
    public void signInPassword() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_signup:
                onSignUpPressed();
                break;
            case R.id.login_with_password:
                onSignInPressed();
                break;
            case R.id.forgot_password_text:
                Intent in3 = new Intent(getApplicationContext(), Rest_password.class);
                startActivity(in3);
        }

    }

    /**
     * Helper method that makes sure a user is created if the user
     * logs in with Firebase's email/password provider.
     * @param authData AuthData object returned from onAuthenticated
     */

    //  private void setAuthenticatedUserPasswordProvider(AuthData authData) {
    //  }


    String emailAddress = "user@example.com";



}