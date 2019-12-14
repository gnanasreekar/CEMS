package com.rgs.cems;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class DetailsDisplay extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView a,b,c,d,n;
    Integer a1,b1,c1,d1,n1;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    PieChart chart;
    private Typeface tf;



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
            a1 =numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 0 , "1")).intValue();
            b1 =numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 1 , "1")).intValue();
            c1 =numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 2 , "2")).intValue();
            d1 =numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 3 , "3")).intValue();
            n1 =numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 4 , "4")).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ValueAnimator school = ValueAnimator.ofInt(0, a1);
        school.setDuration(1500);
        school.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                a.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        school.start();

        ValueAnimator schooladmin = ValueAnimator.ofInt(0, c1);
        schooladmin.setDuration(1500);
        schooladmin.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                c.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        schooladmin.start();

        ValueAnimator schoolacamedic = ValueAnimator.ofInt(0, b1);
        schoolacamedic.setDuration(1500);
        schoolacamedic.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                b.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        schoolacamedic.start();

        ValueAnimator Girlshostel = ValueAnimator.ofInt(0, d1);
        Girlshostel.setDuration(1500);
        Girlshostel.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                d.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        Girlshostel.start();

        ValueAnimator audotourim = ValueAnimator.ofInt(0, n1);
        audotourim.setDuration(1500);
        audotourim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                n.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        audotourim.start();


    }
}
