package com.rgs.cems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailsDisplay extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView a,b,c,d,n;
    Float asa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_display);
        a = findViewById(R.id.ablock_power_tv);
        b = findViewById(R.id.bblock_power_tv);
        c = findViewById(R.id.cblock_power_tv);
        d = findViewById(R.id.dblock_power_tv);
        n = findViewById(R.id.nblock_power_tv);
        sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);

        a.setText(sharedPreferences.getString("Energy Consumed" + 0 , "NO data found") + " Units");
        b.setText(sharedPreferences.getString("Energy Consumed" + 1 , "NO data found") + " Units");
        c.setText(sharedPreferences.getString("Energy Consumed" + 2 , "NO data found") + " Units");
        d.setText(sharedPreferences.getString("Energy Consumed" + 3 , "NO data found") + " Units");
        n.setText(sharedPreferences.getString("Energy Consumed" + 4 , "NO data found") + " Units");

        asa = Float.parseFloat(sharedPreferences.getString("Energy Consumed" + 4 , "1"));

    }
}
