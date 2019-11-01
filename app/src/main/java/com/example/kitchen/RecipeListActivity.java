package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * This class is the controller for the Recipe List activity
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * This method runs on the creation of the activity and is responsible for the setup.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
    }
}
