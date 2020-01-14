package com.rgs.cems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rgs.cems.Justclasses.ViewAnimation;

public class Adminactivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    float costperunuit;
    TextView rupeeunit;
    View parentview;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminactivity);

        rupeeunit = findViewById(R.id.rupeeperunit);
        parentview = findViewById(R.id.adminmain);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get total available quest
                        int size = (int) dataSnapshot.getChildrenCount();
                        TextView usercount = findViewById(R.id.admin_usercount);
                        usercount.setText(size+"");
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        databaseReference.child("AuthRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get total available quest
                int size = (int) dataSnapshot.getChildrenCount();
                TextView authreqcount = findViewById(R.id.admin_authreqcount);
                authreqcount.setText(size+"");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Cost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String rupee = dataSnapshot.getValue().toString();
                float rs = Float.parseFloat(rupee);
                rupeeunit.setText("Rs. "+ rs +"/Unit");

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
        TextView uid = findViewById(R.id.admin_udi);
        uid.setText("UID: " + sharedPreferences.getString("uid","Not aval"));


    }



    public void Onclick(View view){
        finish();
    }

    public void showtempmonthssDialog(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.edit);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText rupee = dialog.findViewById(R.id.rupeeunit);
                costperunuit = Float.parseFloat(rupee.getText().toString());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cost/");
                databaseReference.setValue(costperunuit);
                rupeeunit.setText("Rs. "+ costperunuit +"/Unit");
                Snackbar.make(parentview, "Changed to Rs. "+ costperunuit +"/Unit", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }




}
