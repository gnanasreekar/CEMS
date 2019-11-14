package com.rgs.cems.Auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rgs.cems.MainActivity;
import com.rgs.cems.R;


public class Login extends AppCompatActivity {

    public EditText login_username, login_password;
    Button button_login;
    TextView signup;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("SignIn");
        firebaseAuth = FirebaseAuth.getInstance();
        login_username = findViewById(R.id.username);
        login_password = findViewById(R.id.password);
        button_login = findViewById(R.id.button_login);
        signup = findViewById(R.id.signup);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(Login.this, "User logged in ", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(Login.this, MainActivity.class);
                    startActivity(I);
                } else {
                    Toast.makeText(Login.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(Login.this, Signup.class);
                startActivity(I);
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = login_username.getText().toString();
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
                                Toast.makeText(Login.this, "Not sucessfull", Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("uid" , firebaseAuth.getUid());
                                editor.apply();
                                Toast.makeText(Login.this, firebaseAuth.getUid(), Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(Login.this, MainActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}