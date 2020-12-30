package com.kunaldhongadi.milkydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";

    EditText rEmail, rPassword, rConfirmPassword;
    ImageButton registerBtn;
    TextView loginBtn;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rEmail = findViewById(R.id.rEditTextEmail);
        rPassword = findViewById(R.id.rEditTextPassword);
        rConfirmPassword = findViewById(R.id.confirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.rLogInBtn);

        fAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = rEmail.getText().toString().trim();
                String passwordStr = rPassword.getText().toString().trim();
                String passwordStr2 = rConfirmPassword.getText().toString().trim();

                //For validating the input.
                if(TextUtils.isEmpty(emailStr)) {
                    rEmail.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(passwordStr)) {
                    rPassword.setError("Password is required.");
                    return;
                }
                if(passwordStr.length() < 6) {
                    rPassword.setError("Password must have 6 or more characters.");
                    return;
                }
                if(! passwordStr.equals(passwordStr2)) {
                    rConfirmPassword.setError("The passwords do not match.");
                    return;
                }

                //Creating new account
                fAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Register.this, "Account Created!", Toast.LENGTH_SHORT).show();
                            Log.v(TAG,"Account is created");
                            Intent mainActivityIntent = new Intent(Register.this, MainActivity.class);
                            startActivity(mainActivityIntent);
                            finish();
                        } else {
                            Log.v(TAG,"There was an error-" + task.getException().getMessage());
                            Toast.makeText(Register.this, "There was an Error!\n" +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Register.this, Login.class);
                startActivity(loginIntent);
            }
        });
    }
}