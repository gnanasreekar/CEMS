package com.rgs.cems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.LinearLayout;
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
    TextView a, b, c, d, n, ac, bc, cc, dc, nc, total_cost_tv, total_power_tv;
    Integer a1, b1, c1, d1, n1, a1c, b1c, c1c, d1c, n1c, cost = 7, total_cost_value, total_power_value;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    LinearLayout school_details, schoo_acd, schol_admin, girls_hostel, audotirium;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Todays Usage");

        {

            a = findViewById(R.id.ablock_power_tv);
            b = findViewById(R.id.bblock_power_tv);
            c = findViewById(R.id.cblock_power_tv);
            d = findViewById(R.id.dblock_power_tv);
            n = findViewById(R.id.nblock_power_tv);
            ac = findViewById(R.id.ablock_cost_tv);
            bc = findViewById(R.id.bblock_cost_tv);
            cc = findViewById(R.id.cblock_cost_tv);
            dc = findViewById(R.id.dblock_cost_tv);
            nc = findViewById(R.id.nblock_cost_tv);
            total_cost_tv = findViewById(R.id.total_cost_tv);
            total_power_tv = findViewById(R.id.total_power_tv);
            school_details = findViewById(R.id.school_details);
            schol_admin = findViewById(R.id.school_amdin_block_details);
            schoo_acd = findViewById(R.id.school_acadamic_block_details);
            girls_hostel = findViewById(R.id.girlshostel_details);
            audotirium = findViewById(R.id.auditotium_details);

        }

        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);

        try {
            a1 = numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 0, "1")).intValue();
            b1 = numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 1, "1")).intValue();
            c1 = numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 2, "2")).intValue();
            d1 = numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 3, "3")).intValue();
            n1 = numberFormat.parse(sharedPreferences.getString("Energy Consumed" + 4, "4")).intValue();
            a1c = a1 * cost;
            b1c = b1 * cost;
            c1c = c1 * cost;
            d1c = d1 * cost;
            n1c = n1 * cost;

            total_cost_value = a1c + b1c + c1c + d1c + n1c;
            total_power_value = a1 + b1 + c1 + d1 + n1;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        settingtext();

        {
            school_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailsDisplay.this, Ptot_graph.class);
                    intent.putExtra("value", 2);
                    startActivity(intent);
                }
            });

            schoo_acd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailsDisplay.this, Ptot_graph.class);
                    intent.putExtra("value", 3);
                    startActivity(intent);
                }
            });

            schol_admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailsDisplay.this, Ptot_graph.class);
                    intent.putExtra("value", 4);
                    startActivity(intent);
                }
            });

            girls_hostel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailsDisplay.this, Ptot_graph.class);
                    intent.putExtra("value", 5);
                    startActivity(intent);
                }
            });

            audotirium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailsDisplay.this, Ptot_graph.class);
                    intent.putExtra("value", 6);
                    startActivity(intent);
                }
            });
        }
    }

    private void settingtext() {

        final ValueAnimator total_power = ValueAnimator.ofInt(0, total_power_value);
        total_power.setDuration(1500);
        total_power.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                total_power_tv.setText(animation.getAnimatedValue().toString() + " Units");
            }
        });
        total_power.start();

        ValueAnimator total_cost = ValueAnimator.ofInt(0, total_cost_value);
        total_cost.setDuration(1500);
        total_cost.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                total_cost_tv.setText("₹ " + animation.getAnimatedValue().toString());
            }
        });
        total_cost.start();

        ValueAnimator schoolcost = ValueAnimator.ofInt(0, a1c);
        schoolcost.setDuration(1500);
        schoolcost.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                ac.setText("₹ " + animation.getAnimatedValue().toString());
            }
        });
        schoolcost.start();

        ValueAnimator schooladmincost = ValueAnimator.ofInt(0, b1c);
        schooladmincost.setDuration(1500);
        schooladmincost.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                bc.setText("₹ " + animation.getAnimatedValue().toString());
            }
        });
        schooladmincost.start();

        ValueAnimator schoolacademiccost = ValueAnimator.ofInt(0, c1c);
        schoolacademiccost.setDuration(1500);
        schoolacademiccost.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                cc.setText("₹ " + animation.getAnimatedValue().toString());
            }
        });
        schoolacademiccost.start();

        ValueAnimator girlshostelcost = ValueAnimator.ofInt(0, d1c);
        girlshostelcost.setDuration(1500);
        girlshostelcost.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                dc.setText("₹ " + animation.getAnimatedValue().toString());
            }
        });
        girlshostelcost.start();

        ValueAnimator audotourimcost = ValueAnimator.ofInt(0, n1c);
        audotourimcost.setDuration(1500);
        audotourimcost.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                nc.setText("₹ " + animation.getAnimatedValue().toString());
            }
        });
        audotourimcost.start();

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
