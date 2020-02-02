package com.rgs.cems;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Getdata extends Application{

    String url = "http://3.6.41.81/todaysusage";

    //TODO: Url change when needed

    public static Resources resources;

    @Override
    public void onCreate() {
        resources = getResources();
        super.onCreate();
    }


    SharedPreferences sharedPreferences;
    int val = 0, gen =0;
    RequestQueue queue;
    StringRequest stringRequest;


    Getdata(final Context context) {
        queue = Volley.newRequestQueue(context);
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray json = null;
                        try {
                            json = new JSONArray(response);
                            for(int i=0;i<json.length();i++){
                                HashMap<String, String> map = new HashMap<String, String>();
                                JSONObject e = json.getJSONObject(i);

                                sharedPreferences = context.getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String Date =  e.getString("DATE");
                                String EC = e.getString("Energy Consumed");
                                String MID = e.getString("Meter ID");
                                editor.putString("DATE" +val ,Date);
                                editor.putString("Energy Consumed" + val, EC);
                                editor.putString("Meter ID" + val , MID);
                                editor.putInt("Jsonlength" , json.length());
                                editor.apply();
                                val++;
                                MainActivity mainActivity = MainActivity.getInstance();
                                mainActivity.TEC();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // enjoy your response
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString() + " getdata", Toast.LENGTH_SHORT).show();
                 NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Toast.makeText(context, String.valueOf(networkResponse.statusCode)+ " Getdata", Toast.LENGTH_SHORT).show();
                }
            }
        });
        queue.add(stringRequest);
    }


}
