package com.rgs.cems.Dataretrive;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/


import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.admin.DeviceAdminInfo;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.rgs.cems.Charts.Previousdate;
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

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_LONG;

public class FirebaseHandler extends Application {

    SharedPreferences sharedPreferences;
    int val = 0 , gen = 0;
    RequestQueue queue;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    DatabaseReference databaseReference,urlfb;
    private String Date;
    long date_ship_millis;
    CountDownTimer mCountDownTimer;
    SharedPreferences.Editor editor;
    String URL;


    @Override
    public void onCreate() {
        super.onCreate();
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        editor = sharedPreferences.edit();

        urlfb = FirebaseDatabase.getInstance().getReference("URL");

        urlfb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String URL = dataSnapshot.getValue().toString();
                editor.putString("URL", URL);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Cost");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rupee = dataSnapshot.getValue().toString();
                float rs = Float.parseFloat(rupee);

                editor.putFloat("cost", rs);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        todaysusage();
    }

    public void todaysusage(){
        String url =  sharedPreferences.getString("URL" , "") + "todaysusage";
        queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("[]")){

                            mCountDownTimer = new CountDownTimer(5000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    if(MainActivity.getInstance()!= null){
                                        nodata();
                                    } else {
                                        Toasty.error(FirebaseHandler.this, "Today's data is  not available", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            }.start();

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
                                        if(MainActivity.getInstance()!= null){
                                            MainActivity.getInstance().TEC();
                                        } else {
                                            Toasty.info(FirebaseHandler.this, "New data might be available", Toast.LENGTH_LONG, true).show();
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

                        warningcheck();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirebaseHandler.this, error.toString()+" SERVER SLOW FBH", LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newFormat.format(new Date(dateTime));
    }

    public void warningcheck(){
        String usage = sharedPreferences.getString("URL" , "") + "todaysusage";
        sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, usage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("[]")){

                            mCountDownTimer = new CountDownTimer(2000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    if(MainActivity.getInstance()!= null){
                                        nodata();
                                    } else {
                                        Toasty.error(FirebaseHandler.this, "Today's data is  not available", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            }.start();

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

                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FirebaseHandler.this, error.toString()+" SLOW SERVER FBH2", LENGTH_LONG).show();
            }
        });
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(2000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest1);

    }

    public void nodata(){
        final Dialog dialog = new Dialog(MainActivity.getInstance());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.no_data_aval);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}