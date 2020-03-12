package com.rgs.cems.Auth;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/



import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgs.cems.MainActivity;
import com.rgs.cems.R;

import java.util.Date;


public class Signup extends AppCompatActivity {
    public EditText emailId;
    public EditText password;
    public EditText username;
    Button buttom_signup;
    TextView signIn;
    FirebaseAuth firebaseAuth;
    LinearLayout lyt_progress,signup;
    String name;
    CharSequence s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        setTitle("SignUp");

        firebaseAuth = FirebaseAuth.getInstance();

        {
        emailId = findViewById(R.id.email_signup);
        username = findViewById(R.id.username_signup);
        password = findViewById(R.id.password_signup);
        buttom_signup = findViewById(R.id.button_signup);
        signIn = findViewById(R.id.signin_signup);
        lyt_progress = (LinearLayout) findViewById(R.id.signup_loading);
        signup = findViewById(R.id.signup_layout);
        lyt_progress.setVisibility(View.GONE);
        
    }

        buttom_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyt_progress.setVisibility(View.VISIBLE);
                lyt_progress.setAlpha(1.0f);
                signup.setVisibility(View.GONE);
                final String emailID = emailId.getText().toString();
                String paswd = password.getText().toString();
                name = username.getText().toString();

                if (emailID.isEmpty()) {
                    username.setError("Set your Username");
                    username.requestFocus();
                } else if (name.isEmpty()) {
                    emailId.setError("Provide your Email first!");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    password.setError("Set your password");
                    password.requestFocus();
                } else if (!(emailID.isEmpty() && paswd.isEmpty() && name.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Signup.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                //Date
                                Date d = new Date();
                                s  = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());

                                //Storing data to display in the Nav bar and in the app
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("uid" , firebaseAuth.getUid());
                                editor.putString("name" , name);
                                editor.putString("email" , emailID);
                                editor.apply();

                                //To save data in Firebase Database
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + firebaseAuth.getUid());
                                databaseReference.child("Name").setValue(name);
                                databaseReference.child("Email").setValue(emailID);
                                databaseReference.child("UID").setValue(firebaseAuth.getUid());
                                databaseReference.child("Date").setValue(s);
                                databaseReference.child("V1").setValue(0);
                                databaseReference.child("V2").setValue(0);

                                CountDownTimer mCountDownTimer = new CountDownTimer(7000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }
                                    public void onFinish() {
                                        showaccountcreatedDialog();
                                    }
                                }.start();


                            }
                        }
                    });
                } else {
                    Toast.makeText(Signup.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this, Login.class));
            }
        });

        //For interent dialog
        if(!isNetworkAvailable()){
            showCustomDialog();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

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

    public void showaccountcreatedDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.acc_confirmed);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AuthRequest/" + firebaseAuth.getUid());
                databaseReference.child("Name").setValue(name);
                databaseReference.child("Date").setValue(s);
                dialog.dismiss();
                startActivity(new Intent(Signup.this, Login.class));
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}

