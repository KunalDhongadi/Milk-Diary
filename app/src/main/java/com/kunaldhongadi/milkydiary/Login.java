package com.kunaldhongadi.milkydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    public static final String TAG = "TAG";

    EditText email,password;
    ImageButton loginBtn;
    TextView createAccount;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.logInBtn);
        createAccount = findViewById(R.id.createAcc);

        fAuth =FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = email.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();

                //For validating the input.
                if(TextUtils.isEmpty(emailStr)) {
                    email.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(passwordStr)) {
                    password.setError("Password is required.");
                    return;
                }

                if(passwordStr.length() < 6) {
                    password.setError("Password must have 6 or more characters.");
                    return;
                }

                //Authenticating the user
                fAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                            Intent mainActivityIntent = new Intent(Login.this,MainActivity.class);
                            startActivity(mainActivityIntent);
                            finish();
                        } else {
                            Toast.makeText(Login.this,"Error Logging In." + task.getException()
                                    .getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(Login.this,Register.class);
                startActivity(registerIntent);
            }
        });






    }
}