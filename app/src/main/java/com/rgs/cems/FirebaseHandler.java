package com.rgs.cems;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FirebaseHandler extends Application {

    String url = "http://18.208.162.97/todaysusage";
    String moviedb;
    SharedPreferences sharedPreferences;
    int val = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        getdata();

    }

    public void getdata() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley" , response);

                        JSONArray json = null;
                        try {
                            json = new JSONArray(response);
                            for(int i=0;i<json.length();i++){
                                HashMap<String, String> map = new HashMap<String, String>();
                                JSONObject e = json.getJSONObject(i);

                                sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String Date =  e.getString("DATE");
                                String EC = e.getString("Energy Consumed");
                                String MID = e.getString("Meter ID");
                                Log.d("HEllo DATE" , Date);
                                Log.d("HEllo EC" , String.valueOf(EC));
                                Log.d("HEllo MID" , MID);
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