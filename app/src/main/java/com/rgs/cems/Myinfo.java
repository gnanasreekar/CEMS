package com.rgs.cems;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rgs.cems.Firebase.feedback;

public class Myinfo extends AppCompatActivity {
    FloatingActionButton feedbackMyinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        feedbackMyinfo = findViewById(R.id.feedback_myinfo);
        setSupportActionBar(toolbar);

        final TextView myClickableUrl =  findViewById(R.id.linkdin);
        myClickableUrl.setText(getString(R.string.lindin));
        Linkify.addLinks(myClickableUrl, Linkify.WEB_URLS);

        final TextView myClickableUrlgt =  findViewById(R.id.github);
        myClickableUrlgt.setText(getString(R.string.github_www_github_com_gnanasreekar));
        Linkify.addLinks(myClickableUrlgt, Linkify.WEB_URLS);

        final TextView myClickableUrlYT =  findViewById(R.id.youtube);
        myClickableUrlYT.setText(getString(R.string.yotube));
        Linkify.addLinks(myClickableUrlYT, Linkify.WEB_URLS);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "😁😁😁😁😁😁😁😁😁", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        feedbackMyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Myinfo.this, feedback.class));
            }
        });
    }
}
