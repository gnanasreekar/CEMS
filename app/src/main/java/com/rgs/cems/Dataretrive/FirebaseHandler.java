package com.rgs.cems.Dataretrive;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/


import android.app.Application;
import android.app.admin.DeviceAdminInfo;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rgs.cems.Justclasses.Dialogs;
import com.rgs.cems.MainActivity;
import com.rgs.cems.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class FirebaseHandler extends Application {

    SharedPreferences sharedPreferences;
    int val = 0 , gen = 0;
    RequestQueue queue;
    String url,URL_ptot;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    DatabaseReference databaseReference;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String Date;
    long date_ship_millis;
    CountDownTimer mCountDownTimer;


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Cost");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rupee = dataSnapshot.getValue().toString();
                float rs = Float.parseFloat(rupee);
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("cost", rs);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        String url =  getString(R.string.URL) + "todaysusage";
        queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                    if(MainActivity.getInstance()!= null){
                                        MainActivity.getInstance().TEC();
                                    } else {
                                        Toast.makeText(FirebaseHandler.this, "New data might be available", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }.start();
                        } catch (JSONException e) {
                            Log.d("Json exception fb" , e.getMessage());
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirebaseHandler.this, error.toString()+" Firebase Handler", LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1500,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newFormat.format(new Date(dateTime));
    }

    public void warningcheck(){
        String usage = getString(R.string.URL) + "todaysusage";
        sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, usage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                } else {
                                    editor.putInt("warning" + MID, 0);
                                    editor.apply();
                                    Log.d("Warningshss" + MID , MID + "data aval    warning"+MID);
                                }

                            }

                        } catch (JSONException e) {
                            Log.d("Json exception fb" , e.getMessage());
                            e.printStackTrace();
                        }

                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirebaseHandler.this, error.toString()+" FH THEBSE", LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest1);

    }
    }