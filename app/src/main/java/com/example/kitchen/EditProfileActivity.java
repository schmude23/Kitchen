package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {
    DatabaseHelper database;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextNewUsername;
    EditText mTextNewPassword;
    EditText mTextCnfNewPassword;
    Button mButtonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get theme
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        int themeId = sharedPreferences.getInt("ThemeId", 0);
        if (themeId == 0)
            setTheme(R.style.AppTheme);
        else
            setTheme(R.style.DarkMode);
        setContentView(R.layout.activity_edit_profile);

        database = new DatabaseHelper(this);
        mTextUsername = findViewById(R.id.edit_username);
        mTextPassword = findViewById(R.id.edit_password);
        mTextNewUsername = findViewById(R.id.new_username);
        mTextNewPassword = findViewById(R.id.new_password);
        mTextCnfNewPassword = findViewById(R.id.cnf_new_password);
        mButtonConfirm = findViewById(R.id.cnf_edit_profile);

        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String newUser = mTextNewUsername.getText().toString().trim();
                String newPwd = mTextNewPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfNewPassword.getText().toString().trim();

                int res = database.loginCheck(user, pwd);
                int res2 = database.getUser(newUser);
                if(res != -1) {
                    if(res2 == -1 || user.equals(newUser)) {
                        if (newPwd.equals(cnf_pwd)) {
                            database.editUser(user, pwd, newUser, newPwd, -1);
                            Toast toast = Toast.makeText(EditProfileActivity.this, "Successfully Changed Login", Toast.LENGTH_SHORT);
                            toast.setGravity(0, 0, 500);
                            toast.show();
                            Intent moveToViewProfile = new Intent(EditProfileActivity.this, ViewProfileActivity.class);
                            moveToViewProfile.putExtra("username", newUser);
                            int res3 = database.getUser(newUser);
                            moveToViewProfile.putExtra("userID", res3);
                            startActivity(moveToViewProfile);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(EditProfileActivity.this, "Passwords do not match", Toast.LENGTH_SHORT);
                            toast.setGravity(0, 0, 500);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(EditProfileActivity.this, "Username already exists", Toast.LENGTH_SHORT);
                        toast.setGravity(0, 0, 500);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(EditProfileActivity.this, "Wrong username/password", Toast.LENGTH_SHORT);
                    toast.setGravity(0, 0, 500);
                    toast.show();
                }
            }
        });

    }
    public void onToolbarTextClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}