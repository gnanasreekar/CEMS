package com.rgs.cems;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ret extends AppCompatActivity {
    private TextView tv1;
    private TextView tv2;
    private Button bt;
    ProgressBar progressBar;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ret);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        bt = (Button) findViewById(R.id.bt);
        progressBar = findViewById(R.id.pbbar);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Sreekar");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("Sreekar")) {
                            String email = dataSnapshot.child("Email").getValue().toString();
                            String name = dataSnapshot.child("Name").getValue().toString();
                            tv1.setText(email);
                            tv2.setText(name);
                        }else{
                            Toast.makeText(ret.this, "No data", Toast.LENGTH_SHORT).show();
                    }}

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.toException();
                        Toast.makeText(ret.this, "No data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
