package com.rgs.cems;

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

public class feedback extends AppCompatActivity {
    EditText email;
    EditText feedback;
    EditText name;
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

        email = findViewById(R.id.feedback_mail);
        feedback = findViewById(R.id.feedback);
        name = findViewById(R.id.feedback_name);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
        uid = sharedPreferences.getString("name" , null);
        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show(); //TODO: Remove Toast
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback_todb();
                Snackbar.make(view, "Thanks for your feedback! ;-)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void feedback_todb() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feedback/" + uid);
        databaseReference.child("Name").setValue(name.getText().toString());
        databaseReference.child("Email").setValue(email.getText().toString());
        databaseReference.child("Feedback").setValue(feedback.getText().toString());
        Toast.makeText(this, "ThankYou For the feedback"  , Toast.LENGTH_SHORT).show();
    }

}
