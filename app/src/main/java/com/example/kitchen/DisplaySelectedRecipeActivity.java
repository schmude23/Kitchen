package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DisplaySelectedRecipeActivity extends AppCompatActivity {
    private TextView recipe_title;
    private TextView servings;
    private TextView prep_time;
    private TextView total_time;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_selected_recipe);
        DatabaseHelper dbHandler = new DatabaseHelper(DisplaySelectedRecipeActivity.this);

        //retrieving the extra infromation from intent
        int recipeId = getIntent().getIntExtra("recipeId", -1);
        recipe = dbHandler.getRecipe(recipeId);

        recipe_title = (TextView) findViewById(R.id.text_recipe_name);
        servings = (TextView) findViewById(R.id.recipe_servings);
        prep_time = (TextView) findViewById(R.id.recipe_prep_time);
        total_time = (TextView) findViewById(R.id.recipe_total_time);

        recipe_title.setText(recipe.getTitle());
        servings.setText(String.valueOf(recipe.getServings()));
        String text = recipe.getPrep_time() + " min";
        prep_time.setText(text);
        text = recipe.getTotal_time() + " min";
        total_time.setText(text);


    }
}
