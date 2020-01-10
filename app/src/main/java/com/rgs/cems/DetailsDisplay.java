package com.rgs.cems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.rgs.cems.Charts.Ptot_graph;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;

public class DetailsDisplay extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView a, b, c, d, n, ac, bc, cc, dc, nc;
    Integer a1, b1, c1, d1, n1, a1c, b1c, c1c, d1c, n1c, cost = 7, total_cost_value, total_power_value;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    LinearLayout school_details, schoo_acd, schol_admin, girls_hostel, audotirium;
    BarChart barChart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Today's Usage");

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

            total_cost_value =  b1c + c1c + d1c + n1c;
            total_power_value = b1 + c1 + d1 + n1;

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

        {
            barChart = findViewById(R.id.barchart);
            //  barChart.setOnChartValueSelectedListener(DetailsDisplay.this);

            barChart.setDrawBarShadow(false);
            barChart.setTouchEnabled(false);
            barChart.setDragEnabled(false);
            barChart.setScaleEnabled(false);
            barChart.setScaleXEnabled(false);
            barChart.setScaleYEnabled(false);
            barChart.setDrawValueAboveBar(true);

            barChart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the barChart, no values will be
            // drawn
            barChart.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            barChart.setPinchZoom(false);

            barChart.setDrawGridBackground(false);
            // barChart.setDrawYLabels(false);

            //    ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);
            //   xAxis.setValueFormatter(xAxisFormatter);

            //   ValueFormatter custom = new MyValueFormatter("$");

            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setLabelCount(8, false);
            //  leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setDrawGridLines(false);
            rightAxis.setLabelCount(8, false);
            //  rightAxis.setValueFormatter(custom);
            rightAxis.setSpaceTop(15f);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            Legend l = barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);

//            XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//            mv.setChartView(barChart); // For bounds control
//            barChart.setMarker(mv);
        }

        if (!sharedPreferences.getBoolean("firstTime2", false)) {
            ShowIntro("Graphs", "Click here to view the power usage in realtime", R.id.graph_tutorial, 1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime2", true);
            editor.apply();
        }



        setDataBar();

    }


    private void setDataBar() {

        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();


        values.add(new BarEntry(1f, a1));
        values.add(new BarEntry(2f, b1));
        values.add(new BarEntry(3f, c1));
        values.add(new BarEntry(4f, d1));
        values.add(new BarEntry(5f, n1));

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("School");
        xAxisLabel.add("School");
        xAxisLabel.add("Acd block");
        xAxisLabel.add("Admin block");
        xAxisLabel.add("Girls Hostel");
        xAxisLabel.add("Auditorium");

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));


        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "Today's Power Usage");

            set1.setDrawIcons(false);

//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/

            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new GradientColor(startColor1, endColor1));
            gradientColors.add(new GradientColor(startColor2, endColor2));
            gradientColors.add(new GradientColor(startColor3, endColor3));
            gradientColors.add(new GradientColor(startColor4, endColor4));
            gradientColors.add(new GradientColor(startColor5, endColor5));

            set1.setGradientColors(gradientColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            barChart.setData(data);
        }
    }

    private void ShowIntro(String title, String text, int viewId, final int type) {

        new GuideView.Builder(this)
                .setTitle(title)
                .setContentText(text)
                .setTargetView((LinearLayout)findViewById(viewId))
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .setDismissType(GuideView.DismissType.anywhere) //optional - default dismissible by TargetView
                .setGuideListener(new GuideView.GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        if (type == 1) {
                            ShowIntro("Cost till now", "The amount of money spend on the block today", R.id.costfortheunit, 2);
                        }
                    }
                })
                .build()
                .show();
    }



    private void settingtext() {

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
