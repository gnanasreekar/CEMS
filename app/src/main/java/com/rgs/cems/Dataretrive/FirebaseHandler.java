package com.rgs.cems.Dataretrive;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    String url,URL_ptot;
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
warningcheck();
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
                        if (response.equals("[]")){
                            editor.putInt("warning1", 1);
                            editor.apply();Log.d("Warnings" , "Num 1 no data aval");
                        }
                        Log.d("Volley" , response);

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
                        if (response.equals("[]")){
                            editor.putInt("warning2", 1);
                            editor.apply();
                            Log.d("Warnings" , "Num 2 no data aval");
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
                        if (response.equals("[]")){
                            editor.putInt("warning3", 1);
                            editor.apply();
                            Log.d("Warnings" , "Num 3 no data aval");
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
                        if (response.equals("[]")){
                            editor.putInt("warning4", 1);
                            editor.apply();
                            Log.d("Warnings" , "Num 4 no data aval");
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
                        if (response.equals("[]")){
                            editor.putInt("warning5", 1);
                            editor.apply();
                            Log.d("Warnings" , "Num 5 no data aval");
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
        queue.add(stringRequest1);
        queue.add(stringRequest2);
        queue.add(stringRequest3);
        queue.add(stringRequest4);
        queue.add(stringRequest5);

        Log.d("Checkwaring" , "compleer");

    }

}