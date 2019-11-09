package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
    private RecyclerView recyclerView;
    private List<Recipe> recipes;
    private List<RecipeListItem> recipeListItems;
    private DatabaseHelper database = new DatabaseHelper(this);
    private SearchView searchView;
    private RecipeAdapter recipeAdapter;

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

        recyclerView = findViewById(R.id.recipe_list_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recipes = database.getAllRecipes();
        checkRecipes();
        getRecipeListItems();
        recipeAdapter = new RecipeAdapter(recipeListItems, this);
        recyclerView.setAdapter(recipeAdapter);
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
        final MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified())
                    searchView.setIconified(true);
                menuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<RecipeListItem> filteredRecipeList = filter(recipeListItems, newText);
                recipeAdapter.setFilter(filteredRecipeList);
                return true;
            }
        });
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
                Intent advancedSearch = new Intent(this, AdvancedSearchActivity.class);
                startActivity(advancedSearch);
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

    /**
     *
     */
    private void getRecipeListItems() {
        if (recipes != null) {
            recipeListItems = new ArrayList<>();
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

    /**
     *
     * @param list
     * @param query
     * @return
     */
    private List<RecipeListItem> filter(List<RecipeListItem> list, String query){
        query=query.toLowerCase();
        final List<RecipeListItem> filteredList = new ArrayList<>();
        for(RecipeListItem recipe: list){
            final String text = recipe.getRecipeName().toLowerCase();
            if(text.contains(query))
                filteredList.add(recipe);
        }
        return filteredList;
    }
}

