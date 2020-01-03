package com.rgs.cems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.rgs.cems.Justclasses.MyMarkerView;
import com.roger.catloadinglibrary.CatLoadingView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class Comparechart extends AppCompatActivity {

    LineChart chart;
    ArrayList<String> labels = new ArrayList<>();
    int flag = 0, z;
    ArrayList<Entry> entries1 = new ArrayList<>();
    ArrayList<Entry> entries2 = new ArrayList<>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    String date1, date2;
    String URL_ptot;
    CatLoadingView mView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparechart);
        setTitle("Temp");


        chart = findViewById(R.id.comparechart);

        mView = new CatLoadingView();
        mView.show(getSupportFragmentManager(), "");

        {
            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
            // Set the marker to the barChart
            mv.setChartView(chart);
            chart.setMarker(mv);

            chart.setDrawGridBackground(false);
            chart.getDescription().setEnabled(false);
            chart.setDrawBorders(false);

            chart.getAxisLeft().setEnabled(true);
            chart.getAxisRight().setEnabled(false);
            chart.getAxisRight().setDrawAxisLine(false);
            chart.getAxisRight().setDrawGridLines(false);
            chart.getXAxis().setDrawAxisLine(false);
            chart.getXAxis().setDrawGridLines(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);

            // if disabled, scaling can be done on x- and y-axis separately
            chart.setPinchZoom(true);

            chart.getXAxis().setTextColor(Color.WHITE);
            chart.getAxisLeft().setTextColor(Color.WHITE);
            chart.getLegend().setTextColor(Color.WHITE);

            YAxis yAxis;
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMinimum(-50f);

            LimitLine ll1 = new LimitLine(200f, "Upper Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);
            yAxis.addLimitLine(ll1);


            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
        }

        plot();
    }

    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newFormat.format(new Date(dateTime));
    }



    public void plot() {
        URL_ptot = "http://18.208.162.97/testptot";
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
                                Log.d("Hello1", ptot);
                                Log.d("Hellodate1", tstamp);
                                entries1.add(new Entry(i, Float.parseFloat(ptot)));


                                String[] parts = tstamp.split(" ");
                                date2 = parts[0];
                                String second = parts[1];
                                String[] timewithoutsec = second.split(":");
                                String time = timewithoutsec[0] + "." + timewithoutsec[1];
                                labels.add(time);

                                Log.d("Helloper1", "First is done");


                            }
                            LineDataSet lDataSet1 = new LineDataSet(entries1,date1);
                            lDataSet1.setDrawCircles(false);
                            lDataSet1.setValueTextColor(Color.GREEN);
                            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                            dataSets.add(lDataSet1);
                            plot2();
                            chart.animateX(2000);

                            Log.d("Helloentries1", String.valueOf(entries1));
                        } catch (JSONException e) {
                            Toast.makeText(Comparechart.this, "Fetch failed!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comparechart.this, error.toString(), LENGTH_LONG).show();
            }
        }) {

        };
        requestQueue.add(stringRequest);

    }

    public void plot2(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String URL_ptot2 = "http://18.208.162.97/testptot2";
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URL_ptot2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONArray jArray2 = new JSONArray(response);
                            for (int i = 0; i < jArray2.length(); i++) {
                                JSONObject jsonObject = jArray2.getJSONObject(i);
                                String ptot2 = jsonObject.getString("Ptot");
                                String tstamp2 = jsonObject.getString("tstamp");
                                Log.d("Hello2", ptot2);
                                Log.d("Hellodate2", tstamp2);
                                entries2.add(new Entry(i, Float.parseFloat(ptot2)));

                                String[] parts = tstamp2.split(" ");
                                date2 = parts[0];
                            }


                            LineDataSet lDataSet2 = new LineDataSet(entries2, date2);
                            lDataSet2.setColor(Color.RED);
                            lDataSet2.setCircleColor(Color.RED);
                            lDataSet2.setDrawCircles(false);
                            lDataSet2.setValueTextColor(Color.WHITE);

                            Log.d("Helloentries22", String.valueOf(lDataSet2));
                            dataSets.add(lDataSet2);
                            chart.resetTracking();
                            Log.d("Helloentries13", String.valueOf(dataSets));
                            LineData data = new LineData(dataSets);
                            chart.setData(data);
                            chart.invalidate();
                            chart.animateX(2000);

                            mView.dismiss();
                            Log.d("Helloentries2", String.valueOf(entries2));
                        } catch (JSONException e) {
                            Toast.makeText(Comparechart.this, "Fetch failed!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comparechart.this, error.toString(), LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        menu.removeItem(R.id.actionToggleIcons);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.actionToggleValues: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
            /*
            case R.id.actionToggleIcons: { break; }
             */
            case R.id.actionTogglePinch: {
                if (chart.isPinchZoomEnabled())
                    chart.setPinchZoom(false);
                else
                    chart.setPinchZoom(true);

                chart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled());
                chart.notifyDataSetChanged();
                break;
            }
            case R.id.actionToggleHighlight: {
                if(chart.getData() != null) {
                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
                    chart.invalidate();
                }
                break;
            }
            case R.id.actionToggleFilled: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleCircles: {
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
                break;
            }
            case R.id.actionToggleCubic: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            :  LineDataSet.Mode.CUBIC_BEZIER);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleStepped: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.STEPPED
                            ? LineDataSet.Mode.LINEAR
                            :  LineDataSet.Mode.STEPPED);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleHorizontalCubic: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            :  LineDataSet.Mode.HORIZONTAL_BEZIER);
                }
                chart.invalidate();
                break;
            }
            case R.id.animateX: {
                chart.animateX(2000);
                break;
            }
            case R.id.animateY: {
                chart.animateY(2000);
                break;
            }
            case R.id.animateXY: {
                chart.animateXY(2000, 2000);
                break;
            }
        }
        return true;
    }


}
