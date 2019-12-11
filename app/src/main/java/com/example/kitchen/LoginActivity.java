package com.example.kitchen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText mTextUsername;
    EditText mTextPassword;
    Button mButtonLogin;
    Button mButtonGuest;
    TextView mTextViewRegister;
    DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = new DatabaseHelper(this);
        mTextUsername = findViewById(R.id.edittext_username);
        mTextPassword = findViewById(R.id.edittext_password);
        mButtonLogin = findViewById(R.id.button_login);
        mButtonGuest = findViewById(R.id.button_guest);
        mTextViewRegister = findViewById(R.id.textview_register);

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                int res = database.loginCheck(user, pwd);
                if (res != -1) {
                    Toast toast = Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT);
                    toast.setGravity(0, 0, 500);
                    toast.show();
                    Intent i = new Intent(LoginActivity.this, SettingsActivity.class);
                  //  i.putExtra("username", user);
                  //  i.putExtra("userID", res);
                    startActivity(i);
                    finish();
                } else {
                    Toast toast = Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT);
                    toast.setGravity(0, 0, 500);
                    toast.show();
                }
            }
        });

        mButtonGuest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(LoginActivity.this, "Logged in as Guest", Toast.LENGTH_SHORT);
                toast.setGravity(0, 0, 500);
                toast.show();
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}
