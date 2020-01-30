package com.rgs.cems.Auth;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rgs.cems.MainActivity;
import com.rgs.cems.R;


public class Login extends AppCompatActivity {

    public EditText login_username, login_password;
    Button button_login;
    TextView signup, logintext;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;
    static public String fb_name, fb_uid, fb_email;
    CountDownTimer mCountDownTimer, mnCountDownTimer;
    int i = 0;
    String auth = "0",admin, rupee;
    LinearLayout linearLayout,lyt_progress;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        login_username = findViewById(R.id.username);
        login_password = findViewById(R.id.password);
        button_login = findViewById(R.id.button_login);
        signup = findViewById(R.id.signup);
        linearLayout = findViewById(R.id.login);
        lyt_progress = (LinearLayout) findViewById(R.id.login_loading);
        lyt_progress.setVisibility(View.GONE);
        logintext = findViewById(R.id.login_text);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    lyt_progress.setVisibility(View.VISIBLE);
                    lyt_progress.setAlpha(1.0f);
                    linearLayout.setVisibility(View.GONE);

                    new Firebaseretrive().execute();
                } else {
                    firebaseAuth.removeAuthStateListener(authStateListener);
                    Toast.makeText(Login.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(Login.this, Signup.class);
                startActivity(I);
                finish();
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = login_username.getText().toString();
                String userPaswd = login_password.getText().toString();

                if (userEmail.isEmpty()) {
                    login_username.setError("Provide your Email first!");
                    login_username.requestFocus();
                } else if (userPaswd.isEmpty()) {
                    login_password.setError("Enter Password!");
                    login_password.requestFocus();
                } else if (userEmail.isEmpty() && userPaswd.isEmpty()) {
                    Toast.makeText(Login.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPaswd.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPaswd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                lyt_progress.setVisibility(View.VISIBLE);
                                lyt_progress.setAlpha(1.0f);
                                linearLayout.setVisibility(View.GONE);
                                new Firebaseretrive().execute();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    button_login.performClick();
                }
                return false;
            }
        });


    }

    private class Firebaseretrive extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            //Getting data from Firebase Database
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //To make sure the data exist
                    if (dataSnapshot.hasChild("Name") && dataSnapshot.hasChild("Email") && dataSnapshot.hasChild("UID")) {
                        fb_name = dataSnapshot.child("Name").getValue().toString();
                        fb_email = dataSnapshot.child("Email").getValue().toString();
                        fb_uid = dataSnapshot.child("UID").getValue().toString();
                        auth = dataSnapshot.child("V1").getValue().toString();
                        admin = dataSnapshot.child("V2").getValue().toString();

                    } else {
                        fb_name = "NO data found";
                        fb_email = "NO data found";
                        fb_uid = "NO data found";
                        auth = "0";
                        admin = "0";
                    }

                    if (admin.equals("1")){
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admins/" + firebaseAuth.getUid());
                        databaseReference.child("Name").setValue(fb_name);
                        databaseReference.child("UID").setValue(fb_uid);
                    } else {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admins/" + firebaseAuth.getUid());
                        databaseReference.removeValue();
                    }
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("admin" , admin);
                    editor.putString("uid", fb_uid);
                    editor.putString("name", fb_name);
                    editor.putString("email", fb_email);
                    editor.apply();

                    mCountDownTimer = new CountDownTimer(4000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            if ((millisUntilFinished / 1000) == 2){
                                logintext.setText("Getting Data..");
                            }
                        }

                        public void onFinish() {
                            if (auth.equals("1")) {
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            } else if (auth.equals("0")) {
                                notauthdialog();
                            } else {
                            }

                        }
                    }.start();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    @Override
    public void onBackPressed() {
        showexitDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void notauthdialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.auth_failed);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AuthRequest/" + firebaseAuth.getUid());
                databaseReference.child("Name").setValue(fb_email);
                finish();
                Toast.makeText(Login.this, "Please contact the Admin or wait for some time", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    //Forgot password Dialog
    public void forgotpassword(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.forgotpass);
        dialog.setCancelable(true);

        final EditText email = dialog.findViewById(R.id.passemail);
        email.setText(userEmail);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Please Check your mail!", Toast.LENGTH_SHORT).show(); 
                                } else {
                                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    //Exit Dialog
    private void showexitDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.exitdialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //  System.exit(0);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


}
