package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class PresentRecipeActivity extends AppCompatActivity {
    DatabaseHelper database = new DatabaseHelper(this);
    Recipe recipe;
    int position;
    TextView textView;

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
        setContentView(R.layout.activity_present_recipe);
        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        int recipeId = getIntent().getIntExtra("recipeId", -1);
        recipe = database.getRecipe(recipeId);
        textView = findViewById(R.id.present_recipe_text_view);
        position = 0;
        String string = recipe.getDirectionsList().get(position).getDirectionText();
        string = string.substring(0, 1).toUpperCase() + string.substring(1);
        string = (position + 1) + ". " + string;
        textView.setText(string);

    }

    public void onPresentRecipeNextButtonPressed(View view) {
        position++;
        if (position < recipe.getDirectionsList().size()) {
            String string = recipe.getDirectionsList().get(position).getDirectionText();
            string = string.substring(0, 1).toUpperCase() + string.substring(1);
            string = (position + 1) + ". " + string;
            textView.setText(string);
        } else {
            Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);
            intent.putExtra("recipeId", recipe.getKeyID());
            startActivity(intent);
            this.finish();
        }
    }

    public void onPresentRecipeBackButtonPressed(View view) {
        position--;
        if (position > -1) {
            String string = recipe.getDirectionsList().get(position).getDirectionText();
            string = string.substring(0, 1).toUpperCase() + string.substring(1);
            string = (position + 1) + ". " + string;
            textView.setText(string);
        } else {
            Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);
            intent.putExtra("recipeId", recipe.getKeyID());
            startActivity(intent);
            this.finish();
        }
    }
    public void onToolbarTextClicked(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
