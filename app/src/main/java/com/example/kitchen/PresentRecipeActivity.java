package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        setContentView(R.layout.activity_present_recipe);

        int recipeId = getIntent().getIntExtra("recipeId", -1);
        recipe = database.getRecipe(recipeId);
        textView = findViewById(R.id.present_recipe_text_view);
        position = 0;
        String string = recipe.getDirectionsList().get(position).getDirectionText();
        string = string.substring(0,1).toUpperCase() + string.substring(1);
        string = (position + 1) + ". " + string;
        textView.setText(string);

    }

    public void onPresentRecipeNextButtonPressed(View view){
        position++;
        if(position < recipe.getDirectionsList().size()){
            String string = recipe.getDirectionsList().get(position).getDirectionText();
            string = string.substring(0,1).toUpperCase() + string.substring(1);
            string = (position + 1) + ". " + string;
            textView.setText(string);
        }else {
            Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);
            intent.putExtra("recipeId", recipe.getKeyID());
            startActivity(intent);
            this.finish();
        }
    }
    public void onPresentRecipeBackButtonPressed(View view){
        position--;
        if(position > -1){
            String string = recipe.getDirectionsList().get(position).getDirectionText();
            string = string.substring(0,1).toUpperCase() + string.substring(1);
            string = (position + 1) + ". " + string;
            textView.setText(string);
        }else {
            Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);
            intent.putExtra("recipeId", recipe.getKeyID());
            startActivity(intent);
            this.finish();
        }
    }
}
