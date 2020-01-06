package com.rgs.cems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.rgs.cems.Auth.Login;
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
    String date1, date2, response1, response2;
    String URL_ptot , URL_ptot2;
    CatLoadingView mView;
    CountDownTimer mCountDownTimer;
    ArrayList<ChartItem> list = new ArrayList<>();
    ListView lv;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparechart);
        setTitle("Temp");
        getSupportActionBar().hide();

        DatePickerDark();


        mView = new CatLoadingView();
        mView.show(getSupportFragmentManager(), "");

        lv = findViewById(R.id.listView1);





    }

    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //noinspection ConstantConditions
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            ChartItem ci = getItem(position);
            return ci != null ? ci.getItemType() : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }


    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        return newFormat.format(new Date(dateTime));
    }

    private void DatePickerDark() {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        int m = monthOfYear + 1;
                        Calendar calendar = Calendar.getInstance();
                        Log.d("aaatimey", String.valueOf(year));
                        Log.d("aaatimem", String.valueOf(m));
                        Log.d("aaatimed", String.valueOf(dayOfMonth));

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        long date_ship_millis = calendar.getTimeInMillis();
                        URL_ptot = "http://18.208.162.97/previoususageptot?date=" + getFormattedDateSimple(date_ship_millis);
                        Log.d("aaaUrl", URL_ptot);
                        DatePickerDark2();

                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)

        );
        //set dark theme
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }

    private void DatePickerDark2() {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        int m = monthOfYear + 1;
                        Calendar calendar = Calendar.getInstance();
                        Log.d("aaatimey", String.valueOf(year));
                        Log.d("aaatimem", String.valueOf(m));
                        Log.d("aaatimed", String.valueOf(dayOfMonth));

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        long date_ship_millis = calendar.getTimeInMillis();
                        URL_ptot2 = "http://18.208.162.97/previoususageptot?date=" + getFormattedDateSimple(date_ship_millis);
                        Log.d("aaaUrl2", URL_ptot2);
                        getdata(URL_ptot , URL_ptot2);

                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)

        );
        //set dark theme
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }


    public void getdata(String URLptot, final String URLptot2) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLptot,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response1 = response;
                        plot2(URLptot2);
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

    public void plot2(String URLptot2){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URLptot2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response2 = response;
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comparechart.this, error.toString(), LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest2);

        mCountDownTimer = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                list.add(new LineChartItem(plot(response2), getApplicationContext()));
                list.add(new LineChartItem(plot(response1), getApplicationContext()));
                ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
                lv.setAdapter(cda);
                mView.dismiss();
                Log.d("Listarr", String.valueOf(list.size()));
            }
        }.start();
    }

    private LineData plot(String resp) {

        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        Log.d("Arraylength" , String.valueOf(values1.size()));

        try {

            JSONArray jArray2 = new JSONArray(resp);
            for (int i = 0; i < jArray2.length(); i++) {
                JSONObject jsonObject = jArray2.getJSONObject(i);
                String ptot2 = jsonObject.getString("Ptot");
                String tstamp2 = jsonObject.getString("tstamp");
                values1.add(new Entry(i, Float.parseFloat(ptot2)));

                String[] parts = tstamp2.split(" ");
                String second = parts[1];
                String[] timewithoutsec = second.split(":");
                String time = timewithoutsec[0] + "." + timewithoutsec[1];
                labels.add(time);
                date2 = parts[0];
            }

        } catch (JSONException e) {
            Toast.makeText(Comparechart.this, "Fetch failed!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        Log.d("Arraylength2" , String.valueOf(values1.size()));

        LineDataSet d1 = new LineDataSet(values1, date2);
        d1.setLineWidth(2.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        d1.setDrawCircles(false);
        d1.setDrawValues(true);


        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);

        return new LineData(sets);
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
