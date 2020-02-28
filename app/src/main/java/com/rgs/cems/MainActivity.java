package com.rgs.cems;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rgs.cems.Auth.Login;
import com.rgs.cems.Charts.Comparechart;
import com.rgs.cems.Charts.PreviousUsage;
import com.rgs.cems.Charts.Previousdate;
import com.rgs.cems.Dataretrive.Report;
import com.rgs.cems.Dataretrive.feedback;
import com.rgs.cems.Justclasses.Dialogs;
import com.rgs.cems.NormalStuff.About;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    NavigationView navView;
    DrawerLayout drawerLayout;
    LinearLayout warninglayout;
    TextView nav_namec, nav_emailc, today_powerusage_tv, months_powerusage_tv, today_cost, month_cost, generator_usagetv, date_tv, warnings, generator_today,costfortodat;
    int dpb, flag = 0, gen = 0, val =0;
    Integer TEC;
    float Todayscos, Version;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    String generatorusage, Date;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    static MainActivity instance;
    CountDownTimer mCountDownTimer;
    View parent_view;
    long date_ship_millis;
    RelativeLayout nav_layout;
    Menu menuList;
    MenuItem item;
    DatabaseReference databaseReference;
    CharSequence s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        {

            toolbar = findViewById(R.id.toolbar);
            fab = findViewById(R.id.fab);
            navView = findViewById(R.id.nav_view);
            drawerLayout = findViewById(R.id.drawer_layout);
            setSupportActionBar(toolbar);
            today_powerusage_tv = findViewById(R.id.today_power_usage);
            months_powerusage_tv = findViewById(R.id.months_powerusage_tmep);
            today_cost = (TextView) findViewById(R.id.today_cost);
            month_cost = (TextView) findViewById(R.id.month_cost);
            generator_usagetv = findViewById(R.id.generator_usage);
            date_tv = findViewById(R.id.date_main);
            warnings = findViewById(R.id.warnings);
            warninglayout = findViewById(R.id.warning_layout);
            parent_view = findViewById(R.id.main);
            generator_today = findViewById(R.id.generator_usage_today);
            nav_layout = findViewById(R.id.nav_layout);
            costfortodat = findViewById(R.id.costfortoday);
        }

        {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            navView.setNavigationItemSelectedListener(this);

            FloatingActionButton fab = findViewById(R.id.fabmain);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    todaysusage();
                    Toasty.info(instance, "Refreshing", Toast.LENGTH_SHORT, true).show();
                }
            });
        }

        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        if (!sharedPreferences.getBoolean("firstTime", false)) {
            showintroDialog();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }

        //Displaying names in Nav Bar
        navviewdata();

        //DPBdialoginfo
        Dpb();

        //Checking for Internet
        if (!isNetworkAvailable()) {
            showCustomDialog();
        }

        generator_usage();
        TEC();

        mCountDownTimer = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                warning();
            }
        }.start();

        warninglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                Toasty.info(instance, "Please Wait..", Toast.LENGTH_SHORT, true).show();
                warningcheck();
            }
        });

        databaseReference.child("Version").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (item != null) {
                        item.setVisible(false);
                    }
                    Version = numberFormat.parse(dataSnapshot.getValue().toString()).floatValue();
                    mCountDownTimer = new CountDownTimer(1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            try {
                                update(Version);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Date d = new Date();
        s  = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Last_used_on/" + sharedPreferences.getString("uid","Not aval"));
        databaseReference.child("Name").setValue(sharedPreferences.getString("name","Not aval"));
        databaseReference.child("Email").setValue(sharedPreferences.getString("email","Not aval"));
        databaseReference.child("Date").setValue(s);

    }

    private void update(float version) throws ParseException {

        if (version > numberFormat.parse(getString(R.string.versionName)).floatValue()) {
            Snackbar.make(parent_view, "Update available!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Download", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse("https://appdistribution.firebase.dev/app_distro/auth/sign_in");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }).show();
            if (item != null) {
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                item.setVisible(true);
            }
        } else {
            if (item != null) {
                item.setVisible(false);
            }
        }

    }

    private void ShowIntro(String title, String text, int viewId, final int type) {

        new GuideView.Builder(this)
                .setTitle(title)
                .setContentText(text)
                .setTargetView(findViewById(viewId))
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .setDismissType(GuideView.DismissType.anywhere) //optional - default dismissible by TargetView
                .setGuideListener(new GuideView.GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        if (type == 1) {
                            ShowIntro("Month's Usage", "Amount of Units consumed this month", R.id.months_layout, 2);
                        } else if (type == 2) {
                            ShowIntro("Status", "Warnings and Errors will be displayed here", R.id.warning_error, 3);

                        } else if (type == 3) {
                            ShowIntro("Today's USage", "Amount of Units consumed today", R.id.todays, 4);
                        } else if (type == 4) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("showcase", false);
                        }
                    }
                })
                .build()
                .show();
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @SuppressLint("SetTextI18n")
    public void TEC() {

        try {

            TEC = numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 0, "1")).intValue();

        } catch (ParseException e) {
            e.printStackTrace();
        }


        Todayscos = TEC * sharedPreferences.getFloat("cost" , (float) 7.65);
        costfortodat.setText("Rs. "+ sharedPreferences.getFloat("cost" , 7) +"/Unit");

        ValueAnimator animator = ValueAnimator.ofInt(0, TEC);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                today_powerusage_tv.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        animator.start();

        DecimalFormat decim = new DecimalFormat("#,###.##");
        today_cost.setText("₹ " + decim.format(Todayscos));

        generator_today.setText(sharedPreferences.getString("Energy Consumed" + 5, "1") + " Units");

        Calendar calendar = Calendar.getInstance();
        date_ship_millis = calendar.getTimeInMillis();
        String date = sharedPreferences.getString("DATE" + 1, "Please refresh");
        date_tv.setText(date);

        if (!date.equals(getFormattedDateSimple(date_ship_millis))){
            Toasty.info(instance, "Please Refresh", Toast.LENGTH_SHORT, true).show();
        }
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
        ValueAnimator animator = ValueAnimator.ofInt(0, dpb);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                months_powerusage_tv.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        animator.start();
    }

    public void navviewdata() {
        View nav_view = navView.getHeaderView(0);
        nav_emailc = nav_view.findViewById(R.id.nav_email);
        nav_namec = nav_view.findViewById(R.id.nav_name);
        String fb_name_main = sharedPreferences.getString("name", "NO data found");
        String fb_email_main = sharedPreferences.getString("email", "NO data found");
        nav_namec.setText(fb_name_main);
        nav_emailc.setText(fb_email_main);
        setTitle("Hi, " + fb_name_main);
    }

    //Checking for internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //No internet dialog
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();l
            showexitDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menuList = menu;
        item = menuList.findItem(R.id.updateaval);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.warningcheck) {
            flag = 1;
            Toasty.info(instance, "Please Wait..", Toast.LENGTH_SHORT, true).show();
            warningcheck();
            return true;
        } else if(id == R.id.updateaval){
            Uri uri = Uri.parse("https://appdistribution.firebase.dev/app_distro/auth/sign_in"); 
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://drive.google.com/open?id=1oBPYNUW_p_LufNtp1FFYE29VtsOlG1i2");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_compare) {
            startActivity(new Intent(MainActivity.this, Comparechart.class));
        } else if (id == R.id.nav_previous) {
            startActivity(new Intent(MainActivity.this, PreviousUsage.class));
        } else if (id == R.id.dev_info) {
            startActivity(new Intent(MainActivity.this, About.class));
        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(MainActivity.this, feedback.class));
        } else if (id == R.id.nav_report) {
            startActivity(new Intent(MainActivity.this, Report.class));
        } else if (id == R.id.nav_signout) {
            showsignoutDialog();
        } else if (id == R.id.nav_compare_entire) {
            startActivity(new Intent(MainActivity.this, Previousdate.class));
        } else if (id == R.id.nav_changepass){
            changepass();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changepass() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.forgotpass);
        dialog.setCancelable(true);

        final EditText email = dialog.findViewById(R.id.passemail);
        email.setText(sharedPreferences.getString("email","Not aval"));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(instance, "Please Check your mail!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(instance, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public void showintroDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.intro);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowIntro("Generator Output", "The Power output from the Generator", R.id.generator_layout, 1);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void showsignoutDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.signout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    static int i = 0;

    public void falcon(View view){
        i++;
        if(i == 7)
        {
            startActivity(new Intent(MainActivity.this , falcon.class));
        }
    }

    public void onClick_nav(View view) {
        i++;

        if (i == 2) {
            Toast.makeText(instance, "Your are one click away from entering Admin panel", Toast.LENGTH_SHORT).show();
        } else if (i == 3) {
            if(sharedPreferences.getString("admin" , "0").equals("1")){
                startActivity(new Intent(MainActivity.this , Adminactivity.class));
            } else {
                Toasty.error(instance, "You do not have admin rights", Toast.LENGTH_SHORT, true).show();
                notauthdialog();}
            i = 0;
        }
    }

    public void notauthdialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.notpermited);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AdminAccessRequest/" + sharedPreferences.getString("uid","Not aval"));
                databaseReference.child("Name").setValue(sharedPreferences.getString("name", "NO data found"));
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void changeactivity(View view) {
        startActivity(new Intent(MainActivity.this, DetailsDisplay.class));
    }

    public void generator_usage() {
        generatorusage = getString(R.string.URL) + "generatortotal";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, generatorusage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")) {
                            Toasty.warning(instance, "Generator Data not available", Toast.LENGTH_SHORT, true).show();
                        }

                        try {
                            int gen = numberFormat.parse(response).intValue();
                            ValueAnimator animator = ValueAnimator.ofInt(0, gen);
                            animator.setDuration(1500);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    generator_usagetv.setText(animation.getAnimatedValue().toString() + " Units");
                                }
                            });
                            animator.start();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // enjoy your response
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                generator_usage();
                Toast.makeText(MainActivity.this, error.toString()+" Main 1", LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void warning() {
        int warningcount = 0;
        if (sharedPreferences.getInt("warning1", 0) == 1) {
            warningcount++;
        }

        if (sharedPreferences.getInt("warning2", 0) == 1) {
            warningcount++;
        }

        if (sharedPreferences.getInt("warning3", 0) == 1) {
            warningcount++;
        }

        if (sharedPreferences.getInt("warning4", 0) == 1) {
            warningcount++;
        }

        if (sharedPreferences.getInt("warning5", 0) == 1) {
            warningcount++;
        }

        ValueAnimator animator = ValueAnimator.ofInt(0, warningcount);
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("SetTextI18n")
            public void onAnimationUpdate(ValueAnimator animation) {
                warnings.setText(animation.getAnimatedValue().toString() + " Warnings");
            }
        });
        animator.start();

        if (warningcount > 0) {

            warninglayout.setBackgroundColor(getResources().getColor(R.color.red_300));

            warningstatus();
        } else {
            if (flag == 1) {
                warninginfo();
                flag = 0;
            }
        }


    }

    public void warningstatus() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.warninig_status);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warninginfo();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @SuppressLint("SetTextI18n")
    private void warninginfo() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_event);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        final TextView meter2 =  dialog.findViewById(R.id.meter2);
        final TextView meter3 =  dialog.findViewById(R.id.meter3);
        final TextView meter4 =  dialog.findViewById(R.id.meter4);
        final TextView meter5 =  dialog.findViewById(R.id.meter5);
        final TextView meter6 =  dialog.findViewById(R.id.meter6);

        if (sharedPreferences.getInt("warning2", 0) == 1) {
            meter2.setText("Data Not Available");
            meter2.setTextColor(Color.RED);
        } else {
            meter2.setText("Data Available");
            meter2.setTextColor(getResources().getColor(R.color.green_700));
        }

        if (sharedPreferences.getInt("warning3", 0) == 1) {
            meter3.setText("Data Not Available");
            meter3.setTextColor(Color.RED);
        } else {
            meter3.setText("Data Available");
            meter3.setTextColor(getResources().getColor(R.color.green_700));
        }

        if (sharedPreferences.getInt("warning4", 0) == 1) {
            meter4.setText("Data Not Available");
            meter4.setTextColor(Color.RED);
        } else {
            meter4.setText("Data Available");
            meter4.setTextColor(getResources().getColor(R.color.green_700));
        }

        if (sharedPreferences.getInt("warning5", 0) == 1) {
            meter5.setText("Data Not Available");
            meter5.setTextColor(Color.RED);
        } else {
            meter5.setText("Data Available");
            meter5.setTextColor(getResources().getColor(R.color.green_700));
        }

        if (sharedPreferences.getInt("warning6", 0) == 1) {
            meter6.setText("Data Not Available");
            meter6.setTextColor(Color.RED);
        } else {
            meter6.setText("Data Available");
            meter6.setTextColor(getResources().getColor(R.color.green_700));
        }

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void warningcheck() {

        String warn = getString(R.string.URL) + "todaysusage";
        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, warn,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("[]")){
                            new Dialogs(MainActivity.this , 2);
                        } else {
                            JSONArray json = null;
                            try {
                                json = new JSONArray(response);
                                for(int i=0;i<json.length();i++){
                                    JSONObject e = json.getJSONObject(i);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Date =  e.getString("DATE");
                                    String EC = e.getString("Energy Consumed");
                                    String MID = e.getString("Meter ID");

                                    if (EC.equals("0.000")) {
                                        editor.putInt("warning" + MID, 1);
                                        editor.apply();
                                        Log.d("Warningshss" + MID , MID + "data not aval    warning"+MID);
                                    } else if (!EC.equals("0.000")){
                                        editor.putInt("warning" + MID, 0);
                                        editor.apply();
                                        Log.d("Warningshss" + MID , MID + "data aval    warning"+MID);
                                    }

                                }

                            } catch (JSONException e) {
                                Log.d("Json exception fb" , e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        warninginfo();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString()+" Main 2", LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);

    }

    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newFormat.format(new Date(dateTime));
    }

    public void todaysusage(){
        RequestQueue queue;
        String url =  getString(R.string.URL) + "todaysusage";
        queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("[]")) {

                            new Dialogs(MainActivity.this , 2);

                        } else {
                            Log.d("dateVolley" , response);
                            Calendar calendar = Calendar.getInstance();
                            date_ship_millis = calendar.getTimeInMillis();
                            JSONArray json = null;
                            try {
                                json = new JSONArray(response);
                                for(int i=0;i<json.length();i++){
                                    JSONObject e = json.getJSONObject(i);

                                    sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Date =  e.getString("DATE");
                                    String EC = e.getString("Energy Consumed");
                                    String MID = e.getString("Meter ID");
                                    gen = gen +  numberFormat.parse(EC).intValue();

                                    if (EC.equals("0.000")) {
                                        editor.putInt("warning" + MID, 1);
                                        editor.apply();
                                        Log.d("Warningshss" + MID , MID + "data not aval    warning"+MID);
                                    } else {
                                        editor.putInt("warning" + MID, 0);
                                        editor.apply();
                                        Log.d("Warningshss" + MID , MID + "data aval    warning"+MID);
                                    }

                                    editor.putInt("TEC" , gen);
                                    editor.putString("DATE" +val ,Date);
                                    editor.putString("Energy Consumed" + val, EC);
                                    editor.putString("Meter ID" + val , MID);
                                    editor.putInt("Jsonlength" , json.length());
                                    editor.apply();
                                    val++;
                                }

                                if (!Date.equals(getFormattedDateSimple(date_ship_millis))){
                                    new Dialogs(MainActivity.getInstance(), 1);
                                }

                                mCountDownTimer = new CountDownTimer(2000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    public void onFinish() {
                                        TEC();
                                    }
                                }.start();
                            } catch (JSONException e) {
                                Log.d("Json exception fb" , e.getMessage());
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString()+" Main Handler", LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}

