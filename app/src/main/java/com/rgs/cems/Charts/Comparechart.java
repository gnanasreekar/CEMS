package com.rgs.cems.Charts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rgs.cems.Justclasses.ChartItem;
import com.rgs.cems.Justclasses.Dialogs;
import com.rgs.cems.Justclasses.LineChartItem;
import com.rgs.cems.Justclasses.TinyDB;
import com.rgs.cems.R;
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

    int flag = 0,f1, mid;
    long date_ship_millis1, date_ship_millis2;
    String date, response1, response2;
    String URL_ptot , URL_ptot2;
    CountDownTimer mCountDownTimer;
    ArrayList<ChartItem> list = new ArrayList<>();
    ListView lv;
    TinyDB tinydb;
    LinearLayout lyt_progress;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparechart);
        setTitle("Comparing");
        lv = findViewById(R.id.listView1);

        lyt_progress = (LinearLayout) findViewById(R.id.compare_loading);
        progressBar = findViewById(R.id.progress_compare);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);



        // toolbar
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        //DatePickerDark();
        //mView = new CatLoadingView();
       // mView.show(getSupportFragmentManager(), "");
        showCustomDialog();
        tinydb = new TinyDB(Comparechart.this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_selectdate);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextView date1 =  dialog.findViewById(R.id.date1);
        final TextView date2 =  dialog.findViewById(R.id.date2);
        final AppCompatSpinner block = (AppCompatSpinner) dialog.findViewById(R.id.selectblock);

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cur_calender = Calendar.getInstance();
                DatePickerDialog datePicker = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                int m = monthOfYear + 1;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                date_ship_millis1 = calendar.getTimeInMillis();
                                URL_ptot = getString(R.string.URL) + "previoususageptot?date=" + getFormattedDateSimple(date_ship_millis1);
                                date1.setText(getFormattedDateSimple(date_ship_millis1));
                                Log.d("aaaUrl", URL_ptot);
                            }
                        },
                        cur_calender.get(Calendar.YEAR),
                        cur_calender.get(Calendar.MONTH),
                        cur_calender.get(Calendar.DAY_OF_MONTH)

                );
                //set dark theme
                datePicker.setThemeDark(true);
                datePicker.setOkColor(Color.WHITE);
                datePicker.setMaxDate(Calendar.getInstance());
                datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
                datePicker.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cur_calender = Calendar.getInstance();
                DatePickerDialog datePicker = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                int m = monthOfYear + 1;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                date_ship_millis2 = calendar.getTimeInMillis();
                                URL_ptot2 = getString(R.string.URL) + "previoususageptot?date=" + getFormattedDateSimple(date_ship_millis2);
                                Log.d("aaaUrl2", URL_ptot2);
                                date2.setText(getFormattedDateSimple(date_ship_millis2));


                            }
                        },
                        cur_calender.get(Calendar.YEAR),
                        cur_calender.get(Calendar.MONTH),
                        cur_calender.get(Calendar.DAY_OF_MONTH)

                );
                //set dark theme
                datePicker.setThemeDark(true);
                datePicker.setMaxDate(Calendar.getInstance());
                datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
                datePicker.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        String[] timezones = getResources().getStringArray(R.array.blocks);
        ArrayAdapter<String> array = new ArrayAdapter<>(this, R.layout.simple_spinner_item, timezones);
        array.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        block.setAdapter(array);
        block.setSelection(0);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(URL_ptot != null && URL_ptot2 != null ){
                    if (block.getSelectedItemId() == 1) {
                        mid = 2;
                    } else if(block.getSelectedItemId() == 2){
                        mid = 3;
                    } else if(block.getSelectedItemId() == 3){
                        mid = 4;
                    }else if(block.getSelectedItemId() == 4){
                        mid = 5;
                    }else if(block.getSelectedItemId() == 5){
                        mid = 6;
                    }

                    if(mid!=0){
                        URL_ptot = URL_ptot +"&mid="+ mid;
                        URL_ptot2 = URL_ptot2 +"&mid="+ mid;
                        Log.d("Selected" , URL_ptot);
                        lyt_progress.setVisibility(View.VISIBLE);
                        lyt_progress.setAlpha(1.0f);
                        lv.setVisibility(View.GONE);

                        setTitle("Comparing " + block.getSelectedItem() + " on");
                        String subtit =getFormattedDateSimple(date_ship_millis1) + " , " + getFormattedDateSimple(date_ship_millis2);
                        getSupportActionBar().setSubtitle(subtit);

                        mCountDownTimer = new CountDownTimer(1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                getdata(URL_ptot , URL_ptot2);

                            }
                        }.start();

                        dialog.dismiss();
                    } else {
                        Toast.makeText(Comparechart.this, "Select Block", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(Comparechart.this, "Select date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

    public void getdata(String URLptot, final String URLptot2) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLptot,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Temp12" , response);
                        Log.d("nodataresponse1" , response);

                        if (response.equals("[]")){
                            Log.d("nodataresponse" , "yEp");
                        } else {
                            response1 = response;
                        }
                        plot2(URLptot2);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comparechart.this, error.toString(), LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    public void plot2(String URLptot2){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, URLptot2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("nodataresponse2" , response);

                        if(response.equals("[]")){
                            Log.d("nodataresponse" , "yEp");
                        } else {
                            response2 = response;
                        }
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
                lv.setVisibility(View.VISIBLE);
                lyt_progress.setVisibility(View.GONE);
                Log.d("Listarr", String.valueOf(list.size()));
            }
        }.start();
    }

    private LineData plot(String resp) {

        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        Log.d("Arraylength" , String.valueOf(values1.size()));

        if (resp.contains("[]")){
            new Dialogs(Comparechart.this , 2);
            Toast.makeText(Comparechart.this, "No Data Available", Toast.LENGTH_SHORT).show();

        }

        try {
            f1 = 0;
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

                if (time.equals("00.00")) {
                    f1++;
                }

                labels.add(time);
                tinydb.putListString("labels", labels);
                date = parts[0];
            }


            if (f1 > 1) {
                new Dialogs(Comparechart.this , 1);
            }

        } catch (JSONException e) {
            Toast.makeText(Comparechart.this, "Fetch failed!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        Log.d("Arraylength2" , String.valueOf(values1.size()));

        LineDataSet d1 = new LineDataSet(values1, date);
        d1.setLineWidth(1.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        d1.setDrawCircles(false);
        d1.setDrawValues(true);
        d1.setValueTextColor(Color.WHITE);
        d1.setColor(Color.WHITE);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);

        return new LineData(sets);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
