package com.rgs.cems.Dataretrive;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgs.cems.Justclasses.MyMarkerView;
import com.rgs.cems.R;

public class Report extends AppCompatActivity {

    EditText report;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        report = findViewById(R.id.report);
        setTitle("Report");
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
        uid = sharedPreferences.getString("name" , null);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report_todb();
                Snackbar.make(view, "Thanks for your feedback! ;-)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }

    public void report_todb() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report/" + uid);
        databaseReference.child("Report").setValue(report.getText().toString());
        Toast.makeText(this, "ThankYou For the feedback"  , Toast.LENGTH_SHORT).show();
    }



}
