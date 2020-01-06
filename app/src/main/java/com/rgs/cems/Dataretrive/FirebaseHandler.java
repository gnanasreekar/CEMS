package com.rgs.cems.Dataretrive;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rgs.cems.Auth.Login;
import com.rgs.cems.MainActivity;
import com.rgs.cems.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class FirebaseHandler extends Application {

    SharedPreferences sharedPreferences;
    int val = 0 , gen = 0;
    RequestQueue queue;
    String url;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    DatabaseReference databaseReference;




    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        String url =  getString(R.string.URL) + "todaysusage";
        Log.d("TEMPeee" , String.valueOf(url));
        queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley" , response);

                        JSONArray json = null;
                        try {
                            json = new JSONArray(response);
                            for(int i=0;i<json.length();i++){
                                JSONObject e = json.getJSONObject(i);

                                sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String Date =  e.getString("DATE");
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
                        } catch (JSONException e) {
                            Log.d("HEllo" , e.getMessage());
                            e.printStackTrace();
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

}