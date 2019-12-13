package com.rgs.cems.Auth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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
import com.rgs.cems.MainActivity;
import com.rgs.cems.R;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import is.arontibo.library.ElasticDownloadView;


public class Login extends AppCompatActivity {

    public EditText login_username, login_password;
    Button button_login;
    TextView signup;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;
    static public String fb_name, fb_uid , fb_email , fb_flag;
    ProgressDialog TempDialog;
    CountDownTimer mCountDownTimer;
    int i=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        login_username = findViewById(R.id.username);
        login_password = findViewById(R.id.password);
        button_login = findViewById(R.id.button_login);
        signup = findViewById(R.id.signup);
        final String fbuid = firebaseAuth.getUid();

        TempDialog = new ProgressDialog(Login.this);
        TempDialog.setMessage("Please wait...");
        TempDialog.setCancelable(false);
        TempDialog.setProgress(i);
        TempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        TempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(Login.this, "Please wait until login", Toast.LENGTH_SHORT).show();
                    Log.d("Redirect" , "This happened from LOGIN authstate listner");
                    TempDialog.show();

                    new Firebaseretrive().execute();
                } else {
                    firebaseAuth.removeAuthStateListener(authStateListener);
                    Toast.makeText(Login.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Login" , "Login");
                Log.d("Redirect" , "This happned from LOGIN1");
                Intent I = new Intent(Login.this, Signup.class);
                startActivity(I);
                finish();
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = login_username.getText().toString();
                String userPaswd = login_password.getText().toString();
                TempDialog.show();

                if (userEmail.isEmpty()) {
                    login_username.setError("Provide your Email first!");
                    login_username.requestFocus();
                } else if (userPaswd.isEmpty()) {
                    login_password.setError("Enter Password!");
                    login_password.requestFocus();
                } else if (userEmail.isEmpty() && userPaswd.isEmpty()) {
                    Toast.makeText(Login.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPaswd.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Not successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("Redirect" , "This happned from LOGIN normal sign in");

                                new Firebaseretrive().execute();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    button_login.performClick();
                }
                return false;
            }
        });

        //For interent dialog
        if(isNetworkAvailable()){
            Log.d("Internet Status Login" , "On line");
        } else {
            showCustomDialog();
            Log.d("Internet Status Login" , "Off line");
        }




    }

    private class Firebaseretrive extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {

            //Getting data from Firebase Database
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //To make sure the data exist
                    if(dataSnapshot.hasChild("Name") && dataSnapshot.hasChild("Email") && dataSnapshot.hasChild("UID")) {
                        fb_name = dataSnapshot.child("Name").getValue().toString();
                        fb_email = dataSnapshot.child("Email").getValue().toString();
                        fb_uid = dataSnapshot.child("UID").getValue().toString();
                        fb_flag = dataSnapshot.child("V1").getValue().toString();
                        Log.d("Firebase Database" , "data found");

                    } else {
                        fb_name = "NO data found";
                        fb_email = "NO data found";
                        fb_uid = "NO data found";
                        fb_flag = "NO data found";
                        Log.d("Firebase Database" , "No data found");
                    }
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid" , fb_uid);
                    editor.putString("name" , fb_name);
                    editor.putString("email" , fb_email);
                    editor.apply();
                    Log.d("Firebase Name_ALogin" , fb_name);
                    Log.d("Firebase Email_ALogin" , fb_email);
                    Log.d("Firebase UID_ALogin" , fb_uid);

                    Toast.makeText(Login.this, "This works", Toast.LENGTH_SHORT).show();
                    Log.d("WOrks", fb_flag);
                    //TODO too much delay sometimes


                    mCountDownTimer = new CountDownTimer(1000, 1000)
                    {
                        public void onTick(long millisUntilFinished)
                        {
                            TempDialog.setMessage("Please wait..");
                        }

                        public void onFinish()
                        {
                            TempDialog.dismiss();
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }
                    }.start();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException();
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.d("Redirect" , "This happned from LOGIN2");

//                startActivity(new Intent(Login.this, MainActivity.class));
//                finish();


        }
    }

    //To check if internet is avaliable or no
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //No internet Dialog
    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.nonet_warning);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onBackPressed() {
        showexitDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    //Exit Dialog
    private void showexitDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.exitdialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
              //  System.exit(0);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}