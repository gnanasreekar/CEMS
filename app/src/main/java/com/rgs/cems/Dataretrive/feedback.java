package com.rgs.cems.Dataretrive;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgs.cems.R;

public class feedback extends AppCompatActivity {
    EditText emailet;
    EditText feedbacket;
    EditText nameet;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Feedback");

        emailet = findViewById(R.id.feedback_mail);
        feedbacket = findViewById(R.id.feedback);
        nameet = findViewById(R.id.feedback_name);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
        uid = sharedPreferences.getString("name" , null);




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameet.getText().toString();
                String email = emailet.getText().toString();
                String feedback = feedbacket.getText().toString();
                //Checking edit text if its empty
                if (name.isEmpty()) {
                    nameet.setError("Provide your Name");
                    nameet.requestFocus();
                } else if (email.isEmpty()) {
                    emailet.setError("Provide your Email!");
                    emailet.requestFocus();
                } else if (feedback.isEmpty()) {
                    feedbacket.setError("Fill the Field");
                    feedbacket.requestFocus();
                } else {
                    //Uploading Data to FBDB
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feedback/" + uid);
                    databaseReference.child("Name").setValue(name);
                    databaseReference.child("Email").setValue(email);
                    databaseReference.child("Feedback").setValue(feedback);
                    Snackbar.make(view, "Thanks for your feedback! ;-)", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}
