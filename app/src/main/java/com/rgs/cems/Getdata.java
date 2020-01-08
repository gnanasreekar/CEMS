package com.rgs.cems;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rgs.cems.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;

public class Getdata {

    String url = "http://3.6.41.81/todaysusage";

    //TODO: Url change when needed

    SharedPreferences sharedPreferences;
    int val = 0, gen =0;
    RequestQueue queue;
    StringRequest stringRequest;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);


    Getdata(final Context context) {
        queue = Volley.newRequestQueue(context);
        stringRequest = new StringRequest(Request.Method.GET, url,
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

                                sharedPreferences = context.getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String Date =  e.getString("DATE");
                                String EC = e.getString("Energy Consumed");
                                String MID = e.getString("Meter ID");
                                Log.d("HEllo DATE other calss" , Date);
                                Log.d("HEllo EC other calss" , String.valueOf(EC));
                                Log.d("HEllo MID other calss" , MID);
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
