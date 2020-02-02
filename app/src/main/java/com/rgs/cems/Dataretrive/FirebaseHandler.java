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

        queue.add(stringRequest);
warningcheck();
    }

    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newFormat.format(new Date(dateTime));
    }

    public void warningcheck(){

               String URL_ptot1 = getString(R.string.URL) + "ptottoday2";
                String URL_ptot2 = getString(R.string.URL) + "ptottoday3";
                String URL_ptot3 = getString(R.string.URL) + "ptottoday4";
                String URL_ptot4 = getString(R.string.URL) + "ptottoday5";
                String URL_ptot5 = getString(R.string.URL) + "ptottoday6";
        sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, URL_ptot1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (response.contains("[]")){
                            editor.putInt("warning1", 1);
                            editor.apply();
                            Log.d("Warnings" , "Num 1 no data aval");
                        }   else {
                            editor.putInt("warning1", 0);
                            editor.apply();
                            Log.d("Warnings" , "Num 1 data aval");
                        }
                        Log.d("warninig1" , response);

                            }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirebaseHandler.this, error.toString()+" FH 1", LENGTH_LONG).show();
            }
        });

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URL_ptot2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (response.contains("[]")){
                            editor.putInt("warning2", 1);
                            editor.apply();
                            Log.d("Warnings" , "Num 2 no data aval");
                        } else {
                            editor.putInt("warning2", 0);
                            editor.apply();
                            Log.d("Warnings" , "Num 2 data aval");
                        }                        Log.d("warninig2" , response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirebaseHandler.this, error.toString()+" FH 2", LENGTH_LONG).show();
            }
        });

        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, URL_ptot3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (response.contains("[]")){
                            editor.putInt("warning3", 1);
                            editor.apply();
                            Log.d("Warnings" , "Num 3 no data aval");
                        } else {
                            editor.putInt("warning3", 0);
                            editor.apply();
                            Log.d("Warnings" , "Num 3 data aval");
                        }                        Log.d("warninig3" , response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirebaseHandler.this, error.toString()+" FH 3", LENGTH_LONG).show();
            }
        });

        StringRequest stringRequest4 = new StringRequest(Request.Method.GET, URL_ptot4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (response.contains("[]")){
                            editor.putInt("warning4", 1);
                            editor.apply();
                            Log.d("Warnings" , "Num 4 no data aval");
                        } else {
                            editor.putInt("warning4", 0);
                            editor.apply();
                            Log.d("Warnings" , "Num 4 data aval");
                        }                        Log.d("warninig4" , response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirebaseHandler.this, error.toString()+" FH 4", LENGTH_LONG).show();
            }
        });

        StringRequest stringRequest5 = new StringRequest(Request.Method.GET, URL_ptot5,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (response.contains("[]")){
                            editor.putInt("warning5", 1);
                            editor.apply();
                            Log.d("Warnings" , "Num 5 no data aval");
                        } else {
                            editor.putInt("warning5", 0);
                            editor.apply();
                            Log.d("Warnings" , "Num 5 data aval");
                        }
                        Log.d("warninig5" , response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirebaseHandler.this, error.toString()+" FH 5", LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest1);
        queue.add(stringRequest2);
        queue.add(stringRequest3);
        queue.add(stringRequest4);
        queue.add(stringRequest5);
    }

}