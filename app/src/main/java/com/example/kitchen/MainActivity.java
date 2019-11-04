package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

/**
 * This Class is the controller class for the recipe list activity
 */
public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnClickListener {
    RecyclerView mRecyclerView;
    List<Recipe> recipes;
    List<RecipeListItem> recipeListItems;
    DatabaseHelper database = new DatabaseHelper(this);

    /**
     * This method is run when the activity is created and sets up the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.recipe_list_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        recipes = database.getAllRecipes();
        checkRecipes();
        getRecipeListItems();
        RecipeAdapter recipeAdapter = new RecipeAdapter(recipeListItems, this);
        mRecyclerView.setAdapter(recipeAdapter);
       /* for (int i = 0; i < recipeAdapter.getItemCount(); i++) {
            recipeAdapter.getItemId(i);
            mRecyclerView.getChildAt(i).image

        }*/
    }


    /**
     * This method is run when the options menu is being created and is where the menu is set.
     *
     * @param menu the menu that was created
     */
    // Toolbar functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * This method is run when an item from the options menu is selected
     *
     * @param item the item that was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.advanced_search_item:
                return true;
            case R.id.action_add_recipe:
                Intent addRecipe = new Intent(this, EditRecipeActivity.class);
                addRecipe.putExtra("recipeId", -1); // New recipe
                addRecipe.putExtra("newRecipe", true); // New recipe
                startActivity(addRecipe);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This is the onclick method for the recipe adapter.
     *
     * @param position the position of the item clicked on within the dataset
     */
    @Override
    public void onClick(int position) {
        Context context = getApplicationContext();
        CharSequence text = "Retrieving recipe";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        int recipeId = recipes.get(position).getKeyID();
        Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);

        //adding extra information from intent
        intent.putExtra("recipeId", recipeId);
        startActivity(intent);
    }

    /**
     * Checks for any incomplete recipes and deletes them
     */
    private void checkRecipes() {
        if (recipes != null) {
            for (int i = 0; i < recipes.size(); i++) {
                Recipe recipe = database.getRecipe(recipes.get(i).getKeyID());
                if (recipe.getIngredientList() == null || recipe.getDirectionsList() == null || recipe.getCategoryList() == null) {
                    database.deleteRecipe(recipe.getKeyID());
                    recipes.remove(i);
                }
            }
        }
    }

    private void getRecipeListItems() {
        if (recipes != null) {
            recipeListItems = new ArrayList<RecipeListItem>();
            for (int i = 0; i < recipes.size(); i++) {
                String recipe_name = recipes.get(i).getTitle();
                double servings = recipes.get(i).getServings();
                int prep_time = recipes.get(i).getPrep_time();
                int total_time = recipes.get(i).getTotal_time();
                Bitmap image = recipes.get(i).getImage(this);
                recipeListItems.add(new RecipeListItem(recipe_name, servings, prep_time, total_time, image, recipes.get(i).getFavorited()));
            }
        }
    }
}

