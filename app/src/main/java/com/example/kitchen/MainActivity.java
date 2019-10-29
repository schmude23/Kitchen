package com.example.kitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.View.OnClickListener;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnClickListener {
    RecyclerView mRecyclerView;
    List<Recipe> recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        setSupportActionBar(toolbar);
        //toolbarText.setText(getTitle());

        mRecyclerView = findViewById(R.id.recipe_list_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        recipes = new ArrayList<>();
        for(int i = 0; i < 100; i++)
        {
            Recipe recipe = new Recipe();
            recipe.createRecipe("Recipe" + i);
            recipes.add(recipe);
        }
        mRecyclerView.setAdapter(new RecipeAdapter(recipes, this));
    }


    // Toolbar functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.advanced_search_item:
                return true;
            case R.id.action_add_recipe:
                Intent addRecipe = new Intent(this, EditRecipeActivity.class);
                startActivity(addRecipe);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(int position) {
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        int recipeId = recipes.get(position).getKeyID();
        Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);

        //adding extra information from intent
        intent.putExtra("recipeId", recipeId);
        startActivity(intent);
        }
    }

