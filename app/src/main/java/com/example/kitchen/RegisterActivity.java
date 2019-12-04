package com.example.kitchen;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper database;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;
    TextView mTextViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = new DatabaseHelper(this);
        mTextUsername = findViewById(R.id.edittext_username);
        mTextPassword = findViewById(R.id.edittext_password);
        mTextCnfPassword = findViewById(R.id.edittext_cnf_password);
        mButtonRegister = findViewById(R.id.button_register);
        mTextViewLogin = findViewById(R.id.textview_login);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(LoginIntent);
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();

                if (mTextUsername.getText().toString().trim().length() != 0 ||
                        mTextPassword.getText().toString().trim().length() != 0 ||
                        mTextCnfPassword.getText().toString().trim().length() != 0){
                    if (pwd.equals(cnf_pwd)) {
                        int res = database.getUser(user);
                        if (res != -1) {
                            Toast toast = Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT);
                            toast.setGravity(0, 0, 500);
                            toast.show();
                        } else {
                            long val = database.addUser(user, pwd);
                            if (val > 0) {
                                Toast toast = Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT);
                                toast.setGravity(0, 0, 500);
                                toast.show();
                                Intent moveToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(moveToLogin);
                            } else {
                                Toast toast = Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT);
                                toast.setGravity(0, 0, 500);
                                toast.show();
                            }
                        }

                    } else {
                        Toast toast = Toast.makeText(RegisterActivity.this, "Passwords don't match", Toast.LENGTH_SHORT);
                        toast.setGravity(0, 0, 500);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(RegisterActivity.this, "All fields required", Toast.LENGTH_SHORT);
                    toast.setGravity(0, 0, 500);
                    toast.show();
                }
            }
        });
    }
}
