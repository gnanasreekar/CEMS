package com.rgs.cems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DetailsDisplay extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView a,b,c,d,n;
    Float asa;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

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

        try {
            a.setText(numberFormat.parse((sharedPreferences.getString("Energy Consumed" + 0 , "1"))).intValue() + " Units");
            b.setText(numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 1 , "1")).intValue() + " Units");
            c.setText(numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 2 , "2")).intValue() + " Units");
            d.setText(numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 3 , "3")).intValue() + " Units");
            n.setText(numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 4 , "4")).intValue() + " Units");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        asa = Float.parseFloat(sharedPreferences.getString("Energy Consumed" + 4 , "1"));

    }
}
