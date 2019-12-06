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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/gnana-sreekar/")));
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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/lanka-mounica-37a946178/")));
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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/krishna-vivek-kolasani-74b2a715a/")));
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

        //Github_sumanth
        ImageView github_sumanth = findViewById(R.id.github_sunny);
        github_sumanth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/Vivek-Kolasani"))); //TODO: sunny github
            }
        });

        //Linkedin_sumant
        ImageView linkedin_sumanth = findViewById(R.id.linkedin_sunny);
        linkedin_sumanth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/sunny-sumanth-571236152/")));
            }
        });

        //Mail_sumanth
        ImageButton mail_sumanth = findViewById(R.id.mail_sunny);
        mail_sumanth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","Saisumanth532@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        //Github_gopi
        ImageView github_gopi = findViewById(R.id.github_gp);
        github_gopi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/Vivek-Kolasani"))); //TODO: gopi github
            }
        });

        //Linkedin_gopi
        ImageView linkedin_gopi = findViewById(R.id.linkedin_gp);
        linkedin_gopi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/sunny-sumanth-571236152/")));  //TODO: gopi linkedin
            }
        });

        //Mail_gopi
        ImageButton mail_gopi = findViewById(R.id.mail_gp);     //TODO: gopi mail
        mail_gopi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","Saisumanth532@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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
