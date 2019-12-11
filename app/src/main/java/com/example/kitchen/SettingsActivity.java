package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {
    RadioGroup themeRadioGroup;
    SharedPreferences sharedPreferences;
    int themeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get theme
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        themeId = sharedPreferences.getInt("ThemeId", 0);
        if (themeId == 0)
            setTheme(R.style.AppTheme);
        else
            setTheme(R.style.DarkMode);
        setContentView(R.layout.activity_settings);

        initRadioGroup();


    }

    private void initRadioGroup() {
        themeRadioGroup = (RadioGroup) findViewById(R.id.theme_radio_group);
        if (themeId == 1)
            themeRadioGroup.check(R.id.dark_mode_radio);
        themeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.default_theme_radio:
                        sharedPreferences.edit().putInt("ThemeId", 0).apply();
                        if (themeId == 1) {
                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    case R.id.dark_mode_radio:
                        sharedPreferences.edit().putInt("ThemeId", 1).apply();
                        if (themeId == 0) {
                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;

                }

            }

        });

    }

    public void onSettingsActivityLoginClicked(View view) {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        this.finish();
    }

    public void onSettingsActivityViewProfileClicked(View view) {
        Intent i = new Intent(this,ViewProfileActivity.class);
        i.putExtra("username", sharedPreferences.getString("Username", ""));
        i.putExtra("userID", sharedPreferences.getInt("UserId", -1 ));
        startActivity(i);
        finish();
    }

    public void onToolbarTextClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

}

