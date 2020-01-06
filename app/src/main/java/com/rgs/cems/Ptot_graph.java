package com.rgs.cems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rgs.cems.Justclasses.MyMarkerView;
import com.rgs.cems.Justclasses.ViewAnimation;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

public class Ptot_graph extends AppCompatActivity {

    String URL_ptot, Block;
    LineChart chart;
    String access_token;
    private static final int PERMISSION_STORAGE = 0;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    LineDataSet set;
    LineData data;
    CatLoadingView mView;

    private View back_drop;
    private boolean rotate = false;


    private View steppedLayout;
    private View circlesLayout;
    private View cubicLayout;
    private View tooglePinchLayout;
    private View saveGraphLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptot_graph);
        getSupportActionBar().hide();
        chart = findViewById(R.id.chart1);
        mView = new CatLoadingView();
        mView.show(getSupportFragmentManager(), "");

        back_drop = findViewById(R.id.back_drop);

        steppedLayout = findViewById(R.id.stepped_layout);
        TextView stepped = findViewById(R.id.stepped);
        circlesLayout = findViewById(R.id.circles_layout);
        TextView circles = findViewById(R.id.circles);
        cubicLayout = findViewById(R.id.cubic_layout);
        TextView cubic = findViewById(R.id.cubic);
        tooglePinchLayout = findViewById(R.id.toogle_pinch_layout);
        TextView tooglePinch = findViewById(R.id.toogle_pinch);
        saveGraphLayout = findViewById(R.id.save_graph_layout);
        TextView saveGraph = findViewById(R.id.save_graph);


        int plot = getIntent().getIntExtra("value", 0);
        switch (plot) {
            case 0:
                //disp error
                nodataaval();
                break;
            case 2:
                Block = "School";
                URL_ptot = "http://18.208.162.97/ptottoday2";
                makeJsonObjectRequestGraph(URL_ptot);
                break;
            case 3:
                Block = "School Academic Block";
                URL_ptot = "http://18.208.162.97/ptottoday3";
                makeJsonObjectRequestGraph(URL_ptot);
                break;
            case 4:
                Block = "School Admin Block";
                URL_ptot = "http://18.208.162.97/ptottoday4";
                makeJsonObjectRequestGraph(URL_ptot);
                break;
            case 5:
                Block = "Girls Hostel";
                URL_ptot = "http://18.208.162.97/ptottoday5";
                makeJsonObjectRequestGraph(URL_ptot);
                break;
            case 6:
                Block = "Auditorium";
                URL_ptot = "http://18.208.162.97/ptottoday6";
                makeJsonObjectRequestGraph(URL_ptot);
                break;
            default:
                nodataaval();
        }

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        // Set the marker to the barChart
        mv.setChartView(chart);
        chart.setMarker(mv);

        {   // // Chart Style // //
            chart = findViewById(R.id.chart1);

            // background color
            chart.setBackgroundColor(Color.DKGRAY);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setDrawGridBackground(false);


            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // barChart.setScaleXEnabled(true);
            // barChart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(200f);
            yAxis.setAxisMinimum(-50f);
        }


        {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);


            LimitLine ll1 = new LimitLine(200f, "Upper Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);


            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
            //  yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }


        // draw points over time
        chart.animateX(1500);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getAxisLeft().setTextColor(Color.WHITE);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);

        //Checking for Internet
        if (isNetworkAvailable()) {
            Log.d("Internet Status", "On line");
        } else {
            showCustomDialog();
            Log.d("Internet Status", "Off line");
        }
        {
            final FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);

            ViewAnimation.initShowOut(steppedLayout);
            ViewAnimation.initShowOut(saveGraphLayout);
            ViewAnimation.initShowOut(circlesLayout);
            ViewAnimation.initShowOut(cubicLayout);
            ViewAnimation.initShowOut(tooglePinchLayout);
            ViewAnimation.initShowOut(saveGraphLayout);

            back_drop.setVisibility(View.GONE);

            fab_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFabMode(v);
                }
            });

            back_drop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFabMode(fab_add);
                }
            });

            stepped.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ILineDataSet> sets = chart.getData()
                            .getDataSets();
                    for (ILineDataSet iSet : sets) {

                        LineDataSet set = (LineDataSet) iSet;
                        set.setMode(set.getMode() == LineDataSet.Mode.STEPPED
                                ? LineDataSet.Mode.LINEAR
                                : LineDataSet.Mode.STEPPED);
                    }
                    chart.invalidate();
                }
            });

            cubic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ILineDataSet> sets = chart.getData()
                            .getDataSets();

                    for (ILineDataSet iSet : sets) {

                        LineDataSet set = (LineDataSet) iSet;
                        set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                                ? LineDataSet.Mode.LINEAR
                                : LineDataSet.Mode.CUBIC_BEZIER);
                    }
                    chart.invalidate();
                }
            });

            tooglePinch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chart.isPinchZoomEnabled())
                        chart.setPinchZoom(false);
                    else
                        chart.setPinchZoom(true);

                    chart.invalidate();
                }
            });

            saveGraph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(Ptot_graph.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        saveToGallery(chart, "LineChartActivity1");
                    } else {
                        requestStoragePermission(chart);
                    }
                }
            });

            circles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ILineDataSet> sets = chart.getData()
                            .getDataSets();

                    for (ILineDataSet iSet : sets) {

                        LineDataSet set = (LineDataSet) iSet;
                        if (set.isDrawCirclesEnabled())
                            set.setDrawCircles(false);
                        else
                            set.setDrawCircles(true);
                    }
                    chart.invalidate();
                }
            });
        }

    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(saveGraphLayout);
            ViewAnimation.showIn(circlesLayout);
            ViewAnimation.showIn(cubicLayout);
            ViewAnimation.showIn(tooglePinchLayout);
            ViewAnimation.showIn(steppedLayout);
            ViewAnimation.showIn(saveGraphLayout);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(saveGraphLayout);
            ViewAnimation.showOut(circlesLayout);
            ViewAnimation.showOut(steppedLayout);
            ViewAnimation.showOut(cubicLayout);
            ViewAnimation.showOut(tooglePinchLayout);
            ViewAnimation.showOut(saveGraphLayout);
            back_drop.setVisibility(View.GONE);
        }
    }

    //Checking for internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //No internet dialog
    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.nonet_warning);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void makeJsonObjectRequestGraph(String URL_ptot) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_ptot,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONArray jArray = new JSONArray(response);
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject jsonObject = jArray.getJSONObject(i);
                                String ptot = jsonObject.getString("Ptot");
                                String tstamp = jsonObject.getString("tstamp");
                                Log.d("Hello", ptot);


                                entries.add(new Entry(i, Float.parseFloat(ptot)));

                                String[] parts = tstamp.split(" ");
                                String second = parts[1];
                                String[] timewithoutsec = second.split(":");
                                String time = timewithoutsec[0] + "." + timewithoutsec[1];
                                labels.add(time);

                            }


                            set = new LineDataSet(entries, Block);
                            data = new LineData(set);

                            set.setDrawIcons(false);
                            // draw dashed line
                            set.enableDashedLine(10f, 5f, 0f);
                            // black lines and points
                            set.setDrawCircles(false);
                            // set.setColor(Color.BLACK);
                            set.setCircleColor(Color.BLACK);
                            // line thickness and point size
                            set.setLineWidth(1f);
                            set.setCircleRadius(3f);
                            // draw points as solid circles
                            set.setDrawCircleHole(false);
                            // customize legend entry
                            set.setFormLineWidth(1f);
                            set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                            set.setFormSize(15.f);
                            // text size of values
                            set.setValueTextSize(9f);
                            set.setValueTextColor(Color.WHITE);
                            // draw selection line as dashed
                            set.enableDashedHighlightLine(10f, 5f, 0f);
                            // set the filled area
                            set.setDrawFilled(true);
                            set.setFillFormatter(new IFillFormatter() {
                                @Override
                                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                                    return chart.getAxisLeft().getAxisMinimum();
                                }
                            });

//                            if (Utils.getSDKInt() >= 18) {
//                                // drawables only supported on api level 18 and above
//                                Drawable drawable = ContextCompat.getDrawable(Ptot_graph.this, R.drawable.fade_red);
//                                set.setFillDrawable(drawable);
//                            } else {
//                                set.setFillColor(Color.BLACK);
//                            }

                            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                            chart.setData(data);
                            chart.notifyDataSetChanged();
                            chart.invalidate();
                            set.setColor(Color.WHITE);
                            mView.dismiss();


                            //Collections.sort(entries, new EntryXComparator());


                        } catch (JSONException e) {
                            Toast.makeText(Ptot_graph.this, "Fetch failed!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Ptot_graph.this, error.toString(), LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + access_token);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    protected void saveToGallery(Chart chart, String name) {
        if (chart.saveToGallery(name + "_" + System.currentTimeMillis(), 70))
            Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                    .show();
    }

    protected void requestStoragePermission(View view) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(view, "Write permission is required to save image to gallery", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(Ptot_graph.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
                        }
                    }).show();
        } else {
            Toast.makeText(getApplicationContext(), "Permission Required!", Toast.LENGTH_SHORT)
                    .show();
            ActivityCompat.requestPermissions(Ptot_graph.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
        }
    }

    public void nodataaval() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.no_data_aval);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
