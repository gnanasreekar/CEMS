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

        //Github_M
        ImageView github_mounica = findViewById(R.id.github_lm);
        github_mounica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/LankaMounica")));
            }
        });

        //Linkedin_mounica
        ImageView linkedin_mounica = findViewById(R.id.linkedin_lm);
        linkedin_mounica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/in/lanka-mounica-37a946178/")));
            }
        });

        //Mail_rgs
        ImageButton mail_mounica = findViewById(R.id.mail_lm);
        mail_mounica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","lankamounica@pm.me", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        //Github_VK
        ImageView github_vk = findViewById(R.id.github_vk);
        github_vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/Vivek-Kolasani")));
            }
        });

        //Linkedin_mounica
        ImageView linkedin_vk = findViewById(R.id.linkedin_vk);
        linkedin_vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/in/krishna-vivek-kolasani-74b2a715a/")));
            }
        });

        //Mail_rgs
        ImageButton mail_vk = findViewById(R.id.mail_vk);
        mail_vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","vivekkolasani45@gmail.com", null));
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
