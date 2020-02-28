package com.rgs.cems.Charts;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rgs.cems.Justclasses.Dialogs;
import com.rgs.cems.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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

import static android.widget.Toast.LENGTH_LONG;

public class Previousdate extends AppCompatActivity {


    private TextView dateTotalpower;
    private TextView dateTotalcost;
    private TextView dateDateactivity;
    private TextView dateSchool;
    private TextView dateSchAcad;
    private TextView dateSchAdmin;
    private TextView dateGenerator;


    private TextView dateGirlshostel;
    private TextView dateAuditorium;
    RequestQueue queue;
    long date_ship_millis, val;
    String URL_ptot, Date, datedialog;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    CollapsingToolbarLayout collapsingToolbarLayout;
    SharedPreferences.Editor editor;
    Integer M1, M2, M3, M4, M5, M6, Todayscos, TEC;
    SharedPreferences sharedPreferences;
    LinearLayout lyt_progress;
  //  ProgressBar progressBar;
    NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previousdate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.date_toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = findViewById(R.id.date_collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Previous Usage");


        nestedScrollView = findViewById(R.id.nested_scroll_view);
        lyt_progress = (LinearLayout) findViewById(R.id.prevdate_loading);
       // progressBar = findViewById(R.id.progress_prevdate);
       // progressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);



        LinearLayout datePtotSchool;
        LinearLayout datePtotSchoolAcad;
        LinearLayout datePtotSchoolAdmin;
        LinearLayout datePtotGirlshostel;
        LinearLayout datePtotAudi;
        {
            dateTotalpower = (TextView) findViewById(R.id.date_totalpower);
            dateTotalcost = (TextView) findViewById(R.id.date_totalcost);
            dateDateactivity = (TextView) findViewById(R.id.date_dateactivity);
            dateSchool = (TextView) findViewById(R.id.date_school);
            dateSchAcad = (TextView) findViewById(R.id.date_sch_acad);
            dateSchAdmin = (TextView) findViewById(R.id.date_sch_admin);
            dateGirlshostel = (TextView) findViewById(R.id.date_girlshostel);
            dateAuditorium = (TextView) findViewById(R.id.date_auditorium);
            dateGenerator = (TextView) findViewById(R.id.date_generator);
            datePtotSchool = (LinearLayout) findViewById(R.id.date_ptot_school);
            datePtotSchoolAcad = (LinearLayout) findViewById(R.id.date_ptot_school_acad);
            datePtotSchoolAdmin = (LinearLayout) findViewById(R.id.date_ptot_school_admin);
            datePtotGirlshostel = (LinearLayout) findViewById(R.id.date_ptot_girlshostel);
            datePtotAudi = (LinearLayout) findViewById(R.id.date_ptot_audi);
        }
        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        editor = sharedPreferences.edit();
        {

            datePtotSchool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Previousdate.this, Ptot_graph.class);
                    intent.putExtra("value", 7);
                    URL_ptot = getString(R.string.URL) + "previoususageptot?date=" + datedialog + "&mid=" + 2;
                    editor.putString("date_block", "School on " + datedialog);
                    editor.putString("date_url", URL_ptot);
                    editor.apply();
                    startActivity(intent);
                }
            });

            datePtotSchoolAcad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Previousdate.this, Ptot_graph.class);
                    intent.putExtra("value", 7);
                    URL_ptot = getString(R.string.URL) + "previoususageptot?date=" + datedialog + "&mid=" + 3;
                    editor.putString("date_block", "School Academic Block on " + datedialog);
                    editor.putString("date_url", URL_ptot);
                    editor.apply();
                    startActivity(intent);
                }
            });

            datePtotSchoolAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Previousdate.this, Ptot_graph.class);
                    intent.putExtra("value", 7);
                    URL_ptot = getString(R.string.URL) + "previoususageptot?date=" + datedialog + "&mid=" + 4;
                    editor.putString("date_block", "School Admin Block on " + datedialog);
                    editor.putString("date_url", URL_ptot);
                    editor.apply();
                    startActivity(intent);
                }
            });

            datePtotGirlshostel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Previousdate.this, Ptot_graph.class);
                    intent.putExtra("value", 7);
                    URL_ptot = getString(R.string.URL) + "previoususageptot?date=" + datedialog + "&mid=" + 5;
                    editor.putString("date_block", "Girls Hostel on " + datedialog);
                    editor.putString("date_url", URL_ptot);
                    editor.apply();
                    startActivity(intent);
                }
            });

            datePtotAudi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Previousdate.this, Ptot_graph.class);
                    intent.putExtra("value", 7);
                    URL_ptot = getString(R.string.URL) + "previoususageptot?date=" + datedialog + "&mid=" + 6;
                    editor.putString("date_block", "Auditorium on " + datedialog);
                    editor.putString("date_url", URL_ptot);
                    editor.apply();
                    startActivity(intent);
                }
            });

        }

        Basedialog();


    }

    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newFormat.format(new Date(dateTime));
    }

    private void Basedialog() {

                final android.icu.util.Calendar calendar = android.icu.util.Calendar.getInstance();
                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(Previousdate.this,R.style.datepicker, new android.app.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(java.util.Calendar.YEAR, year);
                        calendar.set(java.util.Calendar.MONTH, monthOfYear);
                        calendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                        date_ship_millis = calendar.getTimeInMillis();
                        datedialog = getFormattedDateSimple(date_ship_millis);
                        URL_ptot = getString(R.string.URL) + "dateprevious?date=" + datedialog;
                        collapsingToolbarLayout.setTitle("Usage on " + datedialog);
                        dateDateactivity.setText(datedialog + "'s Activity");
                        if (URL_ptot != null) {
                            lyt_progress.setVisibility(View.VISIBLE);
                            lyt_progress.setAlpha(1.0f);
                            nestedScrollView.setVisibility(View.GONE);
                            getdate(URL_ptot);
                        } else {
                            Toast.makeText(Previousdate.this, "Select date", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                datePickerDialog.show();
            }

    public void getdate(String url) {
        queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("[]")) {
                            new Dialogs(Previousdate.this, 2);
                            Toast.makeText(Previousdate.this, "No Data Available", Toast.LENGTH_SHORT).show();

                        }
                        JSONArray json = null;
                        try {
                            json = new JSONArray(response);
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject e = json.getJSONObject(i);

                                Date = e.getString("DATE");
                                String EC = e.getString("Energy Consumed");
                                String MID = e.getString("Meter ID");

                                editor.putString("date_DATE" + val, Date);
                                editor.putString("date_Energy Consumed" + val, EC);
                                editor.putString("date_Meter ID" + val, MID);
                                editor.putInt("Jsonlength", json.length());
                                editor.apply();
                                val++;
                            }
                            setdata();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // enjoy your response
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Previousdate.this, error.toString()+" PD 1", LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void setdata() {

        try {
            M1 = numberFormat.parse(sharedPreferences.getString("date_Energy Consumed" + 0, "1")).intValue();
            M2 = numberFormat.parse(sharedPreferences.getString("date_Energy Consumed" + 1, "1")).intValue();
            M3 = numberFormat.parse(sharedPreferences.getString("date_Energy Consumed" + 2, "1")).intValue();
            M4 = numberFormat.parse(sharedPreferences.getString("date_Energy Consumed" + 3, "1")).intValue();
            M5 = numberFormat.parse(sharedPreferences.getString("date_Energy Consumed" + 4, "1")).intValue();
            M6 = numberFormat.parse(sharedPreferences.getString("date_Energy Consumed" + 5, "1")).intValue();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        TEC = M2 + M3 + M4 + M5;
        Todayscos = TEC * 7;


        ValueAnimator animator = ValueAnimator.ofInt(0, TEC);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                dateTotalpower.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        animator.start();

        DecimalFormat decim = new DecimalFormat("#,###.##");
        dateTotalcost.setText("₹ " + decim.format(Todayscos));

        ValueAnimator school = ValueAnimator.ofInt(0, M1);
        school.setDuration(1500);
        school.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                dateSchool.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        school.start();

        ValueAnimator schoolace = ValueAnimator.ofInt(0, M2);
        schoolace.setDuration(1500);
        schoolace.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                dateSchAcad.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        schoolace.start();

        ValueAnimator schooladmin = ValueAnimator.ofInt(0, M3);
        schooladmin.setDuration(1500);
        schooladmin.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                dateSchAdmin.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        schooladmin.start();

        ValueAnimator girlshostel = ValueAnimator.ofInt(0, M4);
        girlshostel.setDuration(1500);
        girlshostel.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                dateGirlshostel.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        girlshostel.start();

        ValueAnimator audi = ValueAnimator.ofInt(0, M5);
        audi.setDuration(1500);
        audi.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                dateAuditorium.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        audi.start();

        ValueAnimator generator = ValueAnimator.ofInt(0, M6);
        generator.setDuration(1500);
        generator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                dateGenerator.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        generator.start();


        nestedScrollView.setVisibility(View.VISIBLE);
        lyt_progress.setVisibility(View.GONE);
//        ValueAnimator Todayscost = ValueAnimator.ofInt(0, Todayscos);
//        Todayscost.setDuration(1500);
//        Todayscost.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            public void onAnimationUpdate(ValueAnimator animation) {
//                today_cost.setText("₹ " + animation.getAnimatedValue().toString());
//            }
//        });
//        Todayscost.start();
    }

}
