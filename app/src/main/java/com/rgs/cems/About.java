package com.rgs.cems;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setTitle("About");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RGS profile
        LinearLayout profile_rgs = findViewById(R.id.profile_rgs);
        profile_rgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(About.this , Myinfo.class));
            }
        });

        //Github_rgs
        ImageView github_rgs = findViewById(R.id.github_rgs);
        github_rgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/gnanasreekar")));
            }
        });

        //Linkedin_rgs
        ImageView linkedin_rgs = findViewById(R.id.linkedin_rgs);
        linkedin_rgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/in/gnana-sreekar/")));
            }
        });

        //Mail_rgs
        ImageButton mail_rgs = findViewById(R.id.mail_rgs);
        mail_rgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","gnanasreekar@pm.me", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
