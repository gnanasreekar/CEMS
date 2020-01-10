package com.rgs.cems;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rgs.cems.Auth.Login;
import com.rgs.cems.Dataretrive.FirebaseHandler;
import com.rgs.cems.Dataretrive.Report;
import com.rgs.cems.Dataretrive.feedback;
import com.rgs.cems.NormalStuff.About;
import com.roger.catloadinglibrary.CatLoadingView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    NavigationView navView;
    DrawerLayout drawerLayout;
    LinearLayout warninglayout;
    TextView nav_namec, nav_emailc, today_powerusage_tv, months_powerusage_tv, today_cost, month_cost, generator_usagetv, date_tv, warnings;
    CheckBox temp_status;
    int dpb, flag = 0;
    Integer TEC, Todayscos;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    String generatorusage;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    private LinearLayout generatorLayout;
    static MainActivity instance;
    CountDownTimer mCountDownTimer;
    FirebaseHandler firebaseHandler = new FirebaseHandler();
    View parent_view;
    CatLoadingView mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;


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
            generatorLayout = (LinearLayout) findViewById(R.id.generator_layout);
            warnings = findViewById(R.id.warnings);
            warninglayout = findViewById(R.id.warning_layout);
            parent_view = findViewById(android.R.id.content);
            parent_view = findViewById(R.id.main);

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
                    new Getdata(getApplicationContext());
                    Snackbar.make(view, "Refreshing", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        date_tv.setText(sharedPreferences.getString("DATE" + 1, "0"));

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
        if (isNetworkAvailable()) {
            Log.d("Internet Status", "On line");
        } else {
            showCustomDialog();
            Log.d("Internet Status", "Off line");
        }

        httpCall();
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
                warningcheck();
            }
        });

    }

    private void ShowIntro(String title, String text, int viewId, final int type) {

        new GuideView.Builder(this)
                .setTitle(title)
                .setContentText(text)
                .setTargetView((LinearLayout) findViewById(viewId))
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

    public void TEC() {

        try {

            TEC = numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 0, "1")).intValue();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Todayscos = TEC * 7;
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


//        ValueAnimator Todayscost = ValueAnimator.ofInt(0, Todayscos);
//        Todayscost.setDuration(1500);
//        Todayscost.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            public void onAnimationUpdate(ValueAnimator animation) {
//                today_cost.setText("₹ " + animation.getAnimatedValue().toString());
//            }
//        });
//        Todayscost.start();

        String date = sharedPreferences.getString("DATE" + 0, "Not aval");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Values/Totalpower/" + date);
        databaseReference.child("Total Power used").setValue(TEC + " Units");


        Log.d("TEC", String.valueOf(TEC));

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
            Log.d("Datebw", String.valueOf(dpb));
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
        Log.d("Firebase DB_Name_Login", fb_name_main);
        Log.d("Firebase DB_Email_Login", fb_email_main);
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
//            super.onBackPressed();
            showexitDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            mView = new CatLoadingView();
            mView.show(getSupportFragmentManager(), "");
            warningcheck();
            return true;
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
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                //  System.exit(0);
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

    public void onClick(View view) {
        i++;
        if (i == 15) {
            ///showdpbDialog();
            showdpbDialog();
            i = 0;
        }
    }

    public void showdpbDialog() {
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

    public void showtempmonthssDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.edit);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final EditText editText = dialog.findViewById(R.id.edittext_powernumber);
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                months_powerusage_tv.setText(editText.getText().toString() + " Units");

                int finalValue = Integer.parseInt(editText.getText().toString());
                float temp = (float) (finalValue * 5.43);
                month_cost.setText(String.valueOf(temp));
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void changestatus(MenuItem item) {
        Toast.makeText(this, "ewefsfd", Toast.LENGTH_SHORT).show();
    }

    public void changeactivity(View view) {
        startActivity(new Intent(MainActivity.this, DetailsDisplay.class));
    }

    public void httpCall() {
        generatorusage = getString(R.string.URL) + "generatortotal";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, generatorusage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]")) {
                            Toast.makeText(MainActivity.this, "Generator Data not available", Toast.LENGTH_SHORT).show();
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
                Log.e("Volley Error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });
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

    private void warninginfo() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_event);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        final TextView meter2 = (TextView) dialog.findViewById(R.id.meter2);
        final TextView meter3 = (TextView) dialog.findViewById(R.id.meter3);
        final TextView meter4 = (TextView) dialog.findViewById(R.id.meter4);
        final TextView meter5 = (TextView) dialog.findViewById(R.id.meter5);
        final TextView meter6 = (TextView) dialog.findViewById(R.id.meter6);

        if (sharedPreferences.getInt("warning1", 0) == 1) {
            meter2.setText("Data Not Available");
            meter2.setTextColor(Color.RED);
        } else {
            meter2.setText("Data Available");
            meter2.setTextColor(getResources().getColor(R.color.green_700));
        }

        if (sharedPreferences.getInt("warning2", 0) == 1) {
            meter3.setText("Data Not Available");
            meter3.setTextColor(Color.RED);
        } else {
            meter3.setText("Data Available");
            meter3.setTextColor(getResources().getColor(R.color.green_700));
        }

        if (sharedPreferences.getInt("warning3", 0) == 1) {
            meter4.setText("Data Not Available");
            meter4.setTextColor(Color.RED);
        } else {
            meter4.setText("Data Available");
            meter4.setTextColor(getResources().getColor(R.color.green_700));
        }

        if (sharedPreferences.getInt("warning4", 0) == 1) {
            meter5.setText("Data Not Available");
            meter5.setTextColor(Color.RED);
        } else {
            meter5.setText("Data Available");
            meter5.setTextColor(getResources().getColor(R.color.green_700));
        }

        if (sharedPreferences.getInt("warning5", 0) == 1) {
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

        String URL_ptot1 = getString(R.string.URL) + "ptottoday2";
        String URL_ptot2 = getString(R.string.URL) + "ptottoday3";
        String URL_ptot3 = getString(R.string.URL) + "ptottoday4";
        String URL_ptot4 = getString(R.string.URL) + "ptottoday5";
        String URL_ptot5 = getString(R.string.URL) + "ptottoday6";
        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL_ptot1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (response.equals("[]")) {
                            editor.putInt("warning1", 1);
                            editor.apply();
                            Log.d("Warnings", "Num 1 no data aval");
                        } else {
                            editor.putInt("warning1", 0);
                            editor.apply();
                            Log.d("Warnings", "Num 1 no data aval");
                        }
                        Log.d("warninig1", response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URL_ptot2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (response.equals("[]")) {
                            editor.putInt("warning2", 1);
                            editor.apply();
                            Log.d("Warnings", "Num 2 no data aval");
                        } else {
                            editor.putInt("warning2", 0);
                            editor.apply();
                            Log.d("Warnings", "Num 1 no data aval");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });

        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, URL_ptot3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (response.equals("[]")) {
                            editor.putInt("warning3", 1);
                            editor.apply();
                            Log.d("Warnings", "Num 3 no data aval");
                        } else {
                            editor.putInt("warning3", 0);
                            editor.apply();
                            Log.d("Warnings", "Num 1 no data aval");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });

        StringRequest stringRequest4 = new StringRequest(Request.Method.GET, URL_ptot4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (response.equals("[]")) {
                            editor.putInt("warning4", 1);
                            editor.apply();
                            Log.d("Warnings", "Num 4 no data aval");
                        } else {
                            editor.putInt("warning4", 0);
                            editor.apply();
                            Log.d("Warnings", "Num 1 no data aval");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });

        StringRequest stringRequest5 = new StringRequest(Request.Method.GET, URL_ptot5,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (response.equals("[]")) {
                            editor.putInt("warning5", 1);
                            editor.apply();
                            Log.d("Warnings", "Num 5 no data aval");
                        } else {
                            editor.putInt("warning5", 0);

                            Log.d("Warnings", "Num 1 no data aval");
                        }

                        Log.d("TEEEMMMp", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley Status code", String.valueOf(networkResponse.statusCode));
                }
            }
        });
        queue.add(stringRequest1);
        queue.add(stringRequest2);
        queue.add(stringRequest3);
        queue.add(stringRequest4);
        queue.add(stringRequest5);

        Log.d("Checkwaring", "compleer");
        mCountDownTimer = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mView.dismiss();
                warning();
            }
        }.start();
    }

}

