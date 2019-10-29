package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DisplaySelectedRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_selected_recipe);
        DatabaseHelper dbHandler = new DatabaseHelper(DisplaySelectedRecipeActivity.this);

        //retrieving the extra infromation from intent
        int recipeId = getIntent().getIntExtra("recipeId", -1);
        Recipe recipe = dbHandler.getRecipe(recipeId);
    }
}
