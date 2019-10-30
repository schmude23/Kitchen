package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.widget.Toast;


import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnClickListener {
    RecyclerView mRecyclerView;
    List<Recipe> recipes;
    DatabaseHelper database = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.recipe_toolbar);
        //TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        setSupportActionBar(toolbar);
        //toolbarText.setText(getTitle());

        mRecyclerView = findViewById(R.id.recipe_list_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        recipes = database.getAllRecipes();
        mRecyclerView.setAdapter(new RecipeAdapter(recipes, this));
    }


    // Toolbar functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
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

