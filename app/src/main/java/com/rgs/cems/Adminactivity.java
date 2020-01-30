package com.rgs.cems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;
import com.rgs.cems.Justclasses.Users;
import com.rgs.cems.Justclasses.ViewAnimation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adminactivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    float costperunuit;
    TextView rupeeunit,costhange,versionchange;
    View parentview;
    SharedPreferences sharedPreferences;
    CharSequence s;
    float rs;
    String TAG = " asdfg",Email,Pass;
    List list;
    ArrayList<String> users = new ArrayList<String>();
    ArrayList<String> uid = new ArrayList<String>();
    ArrayList<String> usersar = new ArrayList<String>();
    ArrayList<String> uidar = new ArrayList<String>();
    Users users2;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminactivity);

        costhange = findViewById(R.id.cost_change);
        Date d = new Date();
        s  = DateFormat.format("MMMM d, yyyy HH:mm:ss", d.getTime());
        sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        rupeeunit = findViewById(R.id.rupeeperunit);
        versionchange = findViewById(R.id.Version_change);
        parentview = findViewById(R.id.adminmain);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("AuthRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get total available quest
                int size = (int) dataSnapshot.getChildrenCount();
                TextView authreqcount = findViewById(R.id.admin_authreqcount);
                authreqcount.setText(size+"");

                usersar.clear();
                uidar.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    usersar.add(childDataSnapshot.child("Name").getValue().toString());
                    uidar.add(childDataSnapshot.getKey());

                    Log.d(TAG , String.valueOf(childDataSnapshot.getKey()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Cost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String rupee = dataSnapshot.getValue().toString();
                rs = Float.parseFloat(rupee);
                rupeeunit.setText("Rs. "+ rs +"/Unit");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Version").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Version = dataSnapshot.getValue().toString();
                versionchange.setText("Current Version = " + Version);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("CostEdit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //To make sure the data exist
                if (dataSnapshot.hasChild("Name")) {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String time = dataSnapshot.child("Date").getValue().toString();
                    costhange.setText(name + " on " + time);

                } else {
                    costhange.setText("Not avaliable");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // get total available quest
                int size = (int) dataSnapshot.getChildrenCount();
                TextView usercount = findViewById(R.id.admin_usercount);
                usercount.setText(size+"");

                users.clear();
                uid.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    users.add(childDataSnapshot.child("Name").getValue().toString());
                    uid.add(childDataSnapshot.getKey());

                    Log.d(TAG , String.valueOf(childDataSnapshot.getKey()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TextView uid = findViewById(R.id.admin_udi);
        uid.setText("UID: " + sharedPreferences.getString("uid","Not aval"));
    }

    public void Onclick(View view){
        finish();
    }

    public void security(View view){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.password);
        dialog.setCancelable(true);

        final EditText vers_pass = dialog.findViewById(R.id.version_change_edit_password);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Pass").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Pass = dataSnapshot.getValue().toString();
                        if (vers_pass.getText().toString().equals(Pass)){
                            versionchangedialog();
                        }else {
                            Snackbar.make(parentview, "Wrong Password", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    public void costchangedialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.edit);
        dialog.setCancelable(true);

        final EditText rupee = dialog.findViewById(R.id.rupeeunit);
        rupee.setHint("Current Cost: "+rs);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!rupee.getText().toString().isEmpty()) {
                    costperunuit = Float.parseFloat(rupee.getText().toString());
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cost/");
                    databaseReference.setValue(costperunuit);
                    DatabaseReference costedit = FirebaseDatabase.getInstance().getReference("CostEdit/");
                    costedit.child("UID").setValue(sharedPreferences.getString("uid", "Not aval"));
                    costedit.child("Name").setValue(sharedPreferences.getString("name", "Not aval"));
                    costedit.child("Date").setValue(s);
                    //TODO: make all in one
                    rupeeunit.setText("Rs. " + costperunuit + "/Unit");
                    Snackbar.make(parentview, "Changed to Rs. " + costperunuit + "/Unit by " + sharedPreferences.getString("name", "Not aval") + " on " + s, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(parentview, "Please Enter a valid number", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void versionchangedialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.versionchange);
        dialog.setCancelable(true);

        final EditText vers = dialog.findViewById(R.id.version_change_edit);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vers.getText().toString().isEmpty()) {
                    String ver = vers.getText().toString();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Version/");
                    databaseReference.setValue(ver);
                    DatabaseReference costedit = FirebaseDatabase.getInstance().getReference("VersionEdit/");
                    costedit.child("UID").setValue(sharedPreferences.getString("uid", "Not aval"));
                    costedit.child("Name").setValue(sharedPreferences.getString("name", "Not aval"));
                    costedit.child("Date").setValue(s);
                    //TODO: make all in one

                } else {
                    Snackbar.make(parentview, "Please Enter a valid number", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void setchange(final int position) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.setchange);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText editName = (EditText) dialog.findViewById(R.id.edit_name);
        final TextView editEmail = dialog.findViewById(R.id.textv_email);
        final TextView textvDate = (TextView) dialog.findViewById(R.id.textv_date);
        final TextView textvUid = (TextView) dialog.findViewById(R.id.textv_uid);
        final EditText editV1 = (EditText) dialog.findViewById(R.id.edit_v1);
        final EditText editV2 = (EditText) dialog.findViewById(R.id.edit_v2);


        databaseReference.child("Users/"+uid.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                     String Date = dataSnapshot.child("Date").getValue().toString();
                     String Name = dataSnapshot.child("Name").getValue().toString();
                     Email = dataSnapshot.child("Email").getValue().toString();
                     String UID = dataSnapshot.getKey();
                     String V1 = dataSnapshot.child("V1").getValue().toString();
                     String V2 = dataSnapshot.child("V2").getValue().toString();

                     editName.setText(Name);
                     editEmail.setText(Email);
                     textvDate.setText(Date);
                     textvUid.setText(UID);
                     editV1.setText(V1);
                     editV2.setText(V2);
                    Log.v(TAG,""+ users.size()); //displays the key for the node
                    Log.v(TAG,""+ dataSnapshot.child("Name").getValue());   //gives the value for given keyname
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close_setchange)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save_setchange)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Email.equals(editEmail.getText().toString())){
                    Toast.makeText(Adminactivity.this, "Progess", Toast.LENGTH_SHORT).show();
                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + uid.get(position));
                databaseReference.child("Name").setValue(editName.getText().toString());
                databaseReference.child("V1").setValue(editV1.getText().toString());
                databaseReference.child("V2").setValue(editV2.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void userslist(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.userslist);
        dialog.setCancelable(true);

        ListView lv;
        lv = (ListView) dialog.findViewById(R.id.listviewaa);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users );

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setchange(position);
                Toast.makeText(Adminactivity.this, ""+uid.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void userslistar(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.userslist);
        dialog.setCancelable(true);

        ListView lv;
        lv = (ListView) dialog.findViewById(R.id.listviewaa);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usersar );

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Adminactivity.this, ""+uid.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
