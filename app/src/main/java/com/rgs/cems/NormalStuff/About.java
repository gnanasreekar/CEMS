package com.rgs.cems.NormalStuff;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgs.cems.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class About extends AppCompatActivity {
    int dpb;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");
        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);


        {

            //RGS profile
            ImageView profile_rgs = findViewById(R.id.profile_rgs);
            profile_rgs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(About.this, Myinfo.class));
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
                            "mailto", "gnanasreekar@pm.me", null));
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
                            "mailto", "lankamounica@pm.me", null));
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
                            "mailto", "vivekkolasani45@gmail.com", null));
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
                            "mailto", "Saisumanth532@gmail.com", null));
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });

            //Github_gopi
            ImageView github_gopi = findViewById(R.id.github_gp);
            github_gopi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/kornegopi123"))); //TODO: gopi github
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
                            "mailto", "kornegopi5a2@gmail.com", null));
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });

        }

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        TextView uid = findViewById(R.id.about_uid);
        uid.setText("UID: " + sharedPreferences.getString("uid","Not aval"));
Dpb();
    }

    public void Dpb() {
        Date d = new Date();
        CharSequence s = DateFormat.format("dd MM yyyy ", d.getTime());
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        String dateBeforeString = "26 12 2018";
        String dateAfterString = (String) s;
        try {
            Date dateBefore = myFormat.parse(dateBeforeString);
            Date dateAfter = myFormat.parse(dateAfterString);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            float daysBetween = (difference / (1000 * 60 * 60 * 24));
            dpb = (int) daysBetween + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    static int i = 0;

    public void onClick(View view) {
        i++;
        if (i == 15) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Easter/" + sharedPreferences.getString("uid", "No data"));
            databaseReference.child("Name").setValue(sharedPreferences.getString("name", "No data"));
            Date d = new Date();
            CharSequence s  = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());
            databaseReference.child("Date").setValue(s);
            showdpbDialog();
            i = 0;
        }
    }

    public void showdpbDialog()     {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dpy);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView dp = dialog.findViewById(R.id.dayspast);
        TextView context = dialog.findViewById(R.id.contentew);
        dp.setText(String.valueOf(dpb) + "  Days");
        context.setText(dpb + " days past and many more to go!");

        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
