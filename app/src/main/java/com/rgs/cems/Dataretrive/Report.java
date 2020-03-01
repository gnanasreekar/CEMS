package com.rgs.cems.Dataretrive;

/*
  Developed by : R.Gnana Sreekar
  Github : https://github.com/gnanasreekar
  Linkdin : https://www.linkedin.com/in/gnana-sreekar/
  Instagram : https://www.instagram.com/gnana_sreekar/
  Website : https://gnanasreekar.com
*/


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rgs.cems.Auth.Login;
import com.rgs.cems.Charts.Previousdate;
import com.rgs.cems.Justclasses.Model;
import com.rgs.cems.Justclasses.MyAdapter;
import com.rgs.cems.MainActivity;
import com.rgs.cems.R;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Report extends AppCompatActivity {

    DatabaseReference databaseReference;
    private List<Model> listData;
    private RecyclerView rv;
    private MyAdapter adapter;
    SharedPreferences sharedPreferences;
    int size;
    String date = "Not selected";
    TextView date_tv;

    private static final String[] Block = new String[]{
            "School", "School Admin", "School Aced Block", "Girls Hostel", "Auditorium"
    };

    private static final String[] phase = new String[]{
            "A", "B", "C"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Reports");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        adapter = new MyAdapter(Report.this);
        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        rv = (RecyclerView) findViewById(R.id.rec_view);
        date_tv = findViewById(R.id.tv_date);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();

        getreports();
    }

    public void Datedialog(View view) {

        final android.icu.util.Calendar calendar = android.icu.util.Calendar.getInstance();
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(Report.this, R.style.datepicker, new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(java.util.Calendar.YEAR, year);
                calendar.set(java.util.Calendar.MONTH, monthOfYear);
                calendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                date = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(calendar.getTime());
                date_tv.setText(date);
            }
        }, calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        datePickerDialog.show();
    }

    public void getreports() {


        databaseReference.child("Report").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                size = (int) dataSnapshot.getChildrenCount();
                listData.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if (childDataSnapshot.hasChild("Name")) {
                        String key = childDataSnapshot.getKey();
                        String Name = childDataSnapshot.child("Name").getValue().toString();
                        String Date = childDataSnapshot.child("Dateh").getValue().toString();
                        String Block = childDataSnapshot.child("Block").getValue().toString();
                        String Phase = childDataSnapshot.child("Phase").getValue().toString();
                        String Report = childDataSnapshot.child("Report").getValue().toString();
                        String Urg = childDataSnapshot.child("Urg").getValue().toString();
                        listData.add(new Model(Name, Date, Block, Phase, Report, Urg, key));
                    }

                }
                adapter.setlist(listData);
                rv.setAdapter(adapter);


                // get total available quest
                //   int size = (int) dataSnapshot.getChildrenCount();
//                TextView usercount = findViewById(R.id.admin_usercount);
//                usercount.setText(size+"");s
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addreport() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_review);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Date d = new Date();
        final CharSequence s = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());
        final EditText et_report = dialog.findViewById(R.id.addrep_report);
        final EditText et_block = dialog.findViewById(R.id.edit_block);
        final EditText et_phase = dialog.findViewById(R.id.edit_phase);
        final TextView tv_name = dialog.findViewById(R.id.addrep_name);
        final TextView tv_time = dialog.findViewById(R.id.addrep_time);
        date_tv = dialog.findViewById(R.id.tv_date);


        final Switch imp = dialog.findViewById(R.id.report_imp);
        tv_time.setText(s);
        tv_name.setText(sharedPreferences.getString("name", "Not aval"));
        (dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        (dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String report = et_report.getText().toString().trim();
                if (report.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill report text", Toast.LENGTH_SHORT).show();
                }

                ++size;
                DatabaseReference reportdb = FirebaseDatabase.getInstance().getReference("Report");
                String key = reportdb.push().getKey();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report/" + key);

                databaseReference.child("Report").setValue(et_report.getText().toString());
                databaseReference.child("Name").setValue(sharedPreferences.getString("name", "Not aval"));
                databaseReference.child("Date").setValue(s);
                databaseReference.child("Uid").setValue(sharedPreferences.getString("uid", "Not aval"));
                databaseReference.child("Block").setValue(et_block.getText().toString());
                databaseReference.child("Phase").setValue(et_phase.getText().toString());
                databaseReference.child("Dateh").setValue(date);
                if (imp.isChecked()) {
                    databaseReference.child("Urg").setValue(1);
                } else {
                    databaseReference.child("Urg").setValue(0);

                }

                getreports();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(Report.this, "NTg", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.createrepote) {
            addreport();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    EditText report;
//    String uid, block_string = "Ntg Selected",Phase = "Ntg Selected";
//    Button edit_block , edit_phase;
//    View parent_view;
//    SharedPreferences sharedPreferences;
//
//    private static final String[] Block = new String[]{
//            "School", "School Admin" , "School Aced Block" , "Girls Hostel" , "Auditorium"
//    };
//
//    private static final String[] phase = new String[]{
//            "A", "B" , "C"
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_report);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        parent_view = findViewById(R.id.activity_report);
//        edit_block = findViewById(R.id.button_block);
//        edit_phase = findViewById(R.id.button_phase);
//        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        report = findViewById(R.id.report);
//        setTitle("Report");
//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
//        uid = sharedPreferences.getString("name" , null);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                report_todb();
//                Snackbar.make(view, "Thanks for your feedback! ;-)", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//
//
//    }
//
//    public void clickAction(View view) {
//        int id = view.getId();
//        switch (id) {
//            case R.id.button_block:
//                BlockDialog();
//                break;
//            case R.id.button_phase:
//                PhaseDialog();
//                break;
//        }
//    }
//
//    private void BlockDialog() {
//        block_string = Block[0];
//        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.ALertdialogmee);
//        builder.setTitle("Branch");
//        builder.setSingleChoiceItems(Block, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                block_string = Block[i];
//            }
//        });
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                edit_block.setText(block_string);
//                Snackbar.make(parent_view, "selected : " + block_string, Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//        builder.show();
//    }
//
//    private void PhaseDialog() {
//        Phase = phase[0];
//        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.ALertdialogmee);
//        builder.setTitle("Branch");
//        builder.setSingleChoiceItems(phase, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Phase = phase[i];
//            }
//        });
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                edit_phase.setText(Phase);
//                Snackbar.make(parent_view, "selected : " + Phase, Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        builder.setNegativeButton("Cancel", null);
//        builder.show();
//    }
//
//
//
//
//    public void report_todb() {
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Report/" + uid);
//        databaseReference.child("Report").setValue(report.getText().toString());
//        Date d = new Date();
//        CharSequence s  = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());
//        databaseReference.child("Name").setValue(sharedPreferences.getString("name","Not aval"));
//        databaseReference.child("Date").setValue(s);
//        databaseReference.child("Block").setValue(block_string);
//        databaseReference.child("Phase").setValue(Phase);
//        Toast.makeText(this, "Report has been Taken"  , Toast.LENGTH_SHORT).show();
//    }

}
