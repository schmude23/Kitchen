package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;

import android.os.Bundle;
import android.view.View;
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
    Boolean filter = false;
    List<RecipeListItem> filteredList;
    public final int SEARCH_BY_NAME = 0;
    public final int SEARCH_BY_PREP_TIME = 1;
    public final int SEARCH_BY_TOTAL_TIME = 2;

    /**
     * This method is run when the activity is created and sets up the activity
     *
     * @param savedInstanceState
     */
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
        setContentView(R.layout.activity_recipe_list);

        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recipe_list_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setLayoutManager(layoutManager);


        if (getIntent().getBooleanExtra("advancedSearch", false)) {
            int recipeRadioChecked = getIntent().getIntExtra("recipeRadio", -1);
            String input = getIntent().getStringExtra("input");
            int[] ingredientId = getIntent().getIntArrayExtra("ingredientArray");
            int category = getIntent().getIntExtra("categoryId", -1);
            boolean ascending = getIntent().getBooleanExtra("ascending", true);
            advancedSearch(input, recipeRadioChecked, ingredientId, category, ascending);
        } else {
            recipes = database.getAllRecipes();
            fillDefaultRecipes();
            recipes = database.getAllRecipes();
            checkRecipes();
            recipes = quickSortByTitle(recipes, true);
        }
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
                if (!searchView.isIconified())
                    searchView.setIconified(true);
                menuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<RecipeListItem> filteredRecipeList = filter(recipeListItems, newText);
                filter = true;
                filteredList = filteredRecipeList;
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
            case R.id.action_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                this.finish();
                return true;
            case R.id.advanced_search_item:
                Intent advancedSearch = new Intent(this, AdvancedSearchActivity.class);
                startActivity(advancedSearch);
                this.finish();
                return true;
            case R.id.action_add_recipe:
                Intent addRecipe = new Intent(this, EditRecipeActivity.class);
                addRecipe.putExtra("recipeId", -1); // New recipe
                addRecipe.putExtra("newRecipe", true); // New recipe
                startActivity(addRecipe);
                this.finish();
                return true;
            case R.id.action_view_cart:
                Intent viewCart = new Intent(this, ShoppingCartActivity.class);
                viewCart.putExtra("recipeId", -1);
                startActivity(viewCart);
                this.finish();
                return true;
            case R.id.action_ingredient_list:
                Intent intent = new Intent(this, IngredientListActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.action_retrieve_recipe:
                Intent retrieveRecipe = new Intent(this, ShareRecipeActivity.class);
                retrieveRecipe.putExtra("recipeId", -1);
                startActivity(retrieveRecipe);
                this.finish();
                return true;
            case R.id.action_paste_recipe:
                Intent pasteRecipe = new Intent(this, PasteRecipeActivity.class);
                startActivity(pasteRecipe);
                this.finish();
                return true;
            case R.id.action_view_favorites:
                viewFavorites();
                return true;
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


        Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);

        //adding extra information from intent
        if (filter)
            intent.putExtra("recipeId", filteredList.get(position).getRecipeId());
        else
            intent.putExtra("recipeId", recipes.get(position).getKeyID());
        startActivity(intent);
        this.finish();
    }

    /**
     * Checks for any incomplete recipes and deletes them
     */
    private void checkRecipes() {
        if (recipes != null) {
            for (int i = 0; i < recipes.size(); i++) {
                Recipe recipe = database.getRecipe(recipes.get(i).getKeyID());
                if (recipe.getIngredientList() == null || recipe.getDirectionsList() == null) {
                    database.deleteRecipe(recipe.getKeyID());
                    recipes.remove(i);
                }
            }
        }
    }

    /**
     * Adds default recipes to database when app is opened for the first time
     */
    private void fillDefaultRecipes() {
        String title;
        double servings;
        int prep_time;
        int total_time;
        boolean favorited;
        int ingredientID;
        int recipeID;
        int categoryID;
        Recipe recipe;
        Ingredient ingredient;
        RecipeIngredient ringredient;
        RecipeDirection direction;
        RecipeCategory rcategory;
        List<RecipeIngredient> ingredients;
        List<RecipeDirection> directions;
        List<RecipeCategory> categories;
        if (recipes == null) {

            Category lunch = new Category(-1, "lunch");
            categoryID = database.addCategory(lunch);
            Category dinner = new Category(-1, "dinner");
            categoryID = database.addCategory(dinner);
            Category breakfast = new Category(-1, "breakfast");
            categoryID = database.addCategory(breakfast);
            Category dessert = new Category(-1, "dessert");
            categoryID = database.addCategory(dessert);
            Category snack = new Category(-1, "snack");
            categoryID = database.addCategory(snack);

            title = "Mac n Cheese";
            servings = 4;
            prep_time = 0;
            total_time = 30;
            favorited = false;

            recipe = new Recipe(title, servings, prep_time, total_time, favorited);
            recipeID = database.addRecipe(recipe);

            ingredients = new ArrayList<RecipeIngredient>();

            ingredient = new Ingredient(-1, "macaroni");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "box");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "butter");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 4, "tablespoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "milk");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "cup(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "cheese");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 0.5, "none", "bag of shredded cheese");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "garlic powder");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "pinch(es)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "pepper");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "pinch(es)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            recipe.setIngredientList(ingredients);

            directions = new ArrayList<RecipeDirection>();

            direction = new RecipeDirection(-1, recipe.getKeyID(), "boil a pot of water", 1);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "follow instructions to boil macaroni and then strain it", 2);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "combine butter, milk, cheese, and garlic powder in pot", 3);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "once combined, add macaroni and stir, then serve", 4);
            directions.add(direction);

            recipe.setDirectionsList(directions);

            categories = new ArrayList<RecipeCategory>();

            rcategory = new RecipeCategory(-1, recipe.getKeyID(), dinner.getKeyID());
            categories.add(rcategory);

            recipe.setCategoryList(categories);

            boolean worked = database.editRecipe(recipe);

            title = "Chicken and Rice";
            servings = 4;
            prep_time = 15;
            total_time = 60;
            favorited = false;

            recipe = new Recipe(title, servings, prep_time, total_time, favorited);
            recipeID = database.addRecipe(recipe);

            ingredients = new ArrayList<RecipeIngredient>();

            ingredient = new Ingredient(-1, "chicken");

            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 2, "none", "breasts");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "oil");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 4, "tablespoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "wild rice");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 2, "cup(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "mushroom");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 2, "cup(s)", "sliced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "onion");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "diced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("garlic powder"), (double) 1, "pinch(es)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "pepper");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "pinch(es)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "salt");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "pinch(es)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            recipe.setIngredientList(ingredients);

            directions = new ArrayList<RecipeDirection>();

            direction = new RecipeDirection(-1, recipe.getKeyID(), "boil a pot of water", 1);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "follow instructions to boil rice", 2);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "preheat oil in pan on medium-high heat", 3);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "coat chicken in seasonings and sear " +
                    "until cooked through, then let rest for 5 minutes", 4);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "sweat mushroom and onions in pan until mushrooms and onions " +
                    "have lost all moisture", 5);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "add mushrooms and onions to rice and stir", 6);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "slice chicken breast and serve with rice", 7);
            directions.add(direction);

            recipe.setDirectionsList(directions);

            categories = new ArrayList<RecipeCategory>();

            rcategory = new RecipeCategory(-1, recipe.getKeyID(), dinner.getKeyID());
            categories.add(rcategory);

            recipe.setCategoryList(categories);

            worked = database.editRecipe(recipe);

            title = "Beef and Bean Chili";
            servings = 20;
            prep_time = 60;
            total_time = 360;
            favorited = false;

            recipe = new Recipe(title, servings, prep_time, total_time, favorited);
            recipeID = database.addRecipe(recipe);

            ingredients = new ArrayList<RecipeIngredient>();

            ingredient = new Ingredient(-1, "beef");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 2, "pound(s)", "chuck roast");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "beans");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 2, "pound(s)", "mixed beans");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "tomato");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "can, diced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("onion"), (double) 1, "none", "diced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "garlic");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 5, "none", "cloves, minced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "beef stock");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "box");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "chili powder");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "teaspoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("salt"), (double) 1, "tablespoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("pepper"), (double) 1, "tablespoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("cheese"), (double) 1, "none", "shredded cheddar");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "sour cream");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "tub");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "green onion");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "bunch, chopped");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            recipe.setIngredientList(ingredients);

            directions = new ArrayList<RecipeDirection>();

            direction = new RecipeDirection(-1, recipe.getKeyID(), "follow directions to cook beans", 1);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "slice beef into bite sized pieces", 2);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "sear beef in large pot, but do not cook through", 3);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "add onions and sweat to remove moisture", 4);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "add tomatoes, garlic, and chili powder", 5);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "add beef stock and spices, then stir and cover completely", 6);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "simmer for 5 hours, occasionally stirring", 7);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "ladel into bowl, and add cheese, green onions, and sour cream to the top, then serve", 8);
            directions.add(direction);

            recipe.setDirectionsList(directions);

            categories = new ArrayList<RecipeCategory>();

            rcategory = new RecipeCategory(-1, recipe.getKeyID(), dinner.getKeyID());
            categories.add(rcategory);

            recipe.setCategoryList(categories);

            worked = database.editRecipe(recipe);

            title = "Restaurant-style Salsa";
            servings = 12;
            prep_time = 5;
            total_time = 10;
            favorited = false;

            recipe = new Recipe(title, servings, prep_time, total_time, favorited);
            recipeID = database.addRecipe(recipe);

            ingredients = new ArrayList<RecipeIngredient>();

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("tomato"), (double) 28, "none", "ounce can, whole peeled");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("onion"), (double) 1, "none", "white, diced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "jalapeno");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "seeded and finely chopped");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("garlic"), (double) 3, "none", "cloves, minced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "cumin");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "teaspoon(s)", "ground");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("salt"), (double) 1, "teaspoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "cilantro");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "bunch, stems removed");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "lime");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 3, "tablespoon(s)", "juice");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            recipe.setIngredientList(ingredients);

            directions = new ArrayList<RecipeDirection>();

            direction = new RecipeDirection(-1, recipe.getKeyID(), "add ingredients to food processor and blend until desired consistency is reached", 1);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "optional: cover and chill in fridge for a couple hours before serving", 2);
            directions.add(direction);

            recipe.setDirectionsList(directions);

            categories = new ArrayList<RecipeCategory>();

            rcategory = new RecipeCategory(-1, recipe.getKeyID(), snack.getKeyID());
            categories.add(rcategory);

            recipe.setCategoryList(categories);

            worked = database.editRecipe(recipe);

            title = "Breakfast Sandwich";
            servings = 1;
            prep_time = 10;
            total_time = 20;
            favorited = false;

            recipe = new Recipe(title, servings, prep_time, total_time, favorited);
            recipeID = database.addRecipe(recipe);

            ingredients = new ArrayList<RecipeIngredient>();

            ingredient = new Ingredient(-1, "bagel");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("salt"), (double) 1, "tablespoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("pepper"), (double) 1, "tablespoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "egg");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 2, "none", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "bacon");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 2, "none", "slices");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("onion"), (double) 0.25, "none", "sliced into rings");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("cheese"), (double) 1, "none", "handful shredded cheddar");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            recipe.setIngredientList(ingredients);

            directions = new ArrayList<RecipeDirection>();

            direction = new RecipeDirection(-1, recipe.getKeyID(), "toast bagel in toaster or toaster oven", 1);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "fry bacon in pan, then set aside to dry", 2);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "fry eggs and onions in bacon grease, season with salt and pepper", 3);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "after flipping eggs, cover with cheese", 4);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "place bacon, eggs, and onions on bagel and let cool before serving", 5);
            directions.add(direction);

            recipe.setDirectionsList(directions);

            categories = new ArrayList<RecipeCategory>();

            rcategory = new RecipeCategory(-1, recipe.getKeyID(), breakfast.getKeyID());
            categories.add(rcategory);

            recipe.setCategoryList(categories);

            worked = database.editRecipe(recipe);

            title = "Quick Lunch Panini";
            servings = 1;
            prep_time = 5;
            total_time = 10;
            favorited = false;

            recipe = new Recipe(title, servings, prep_time, total_time, favorited);
            recipeID = database.addRecipe(recipe);

            ingredients = new ArrayList<RecipeIngredient>();

            ingredient = new Ingredient(-1, "bread");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 2, "none", "sourdough, sliced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "meat");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 0.5, "pound(s)", "choice of deli meat, sliced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("cheese"), (double) 0.25, "pound(s)", "desired cheese, sliced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "mayonnaise");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "bottle");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            recipe.setIngredientList(ingredients);

            directions = new ArrayList<RecipeDirection>();

            direction = new RecipeDirection(-1, recipe.getKeyID(), "spread a thin layer of mayo on both sides of each slice of bread (trust me)", 1);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "layer meat and cheese on one slice of bread", 2);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "cover with second slice of bread, and place in panini press", 3);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "use panini press, allowing mayo on outside to turn into a crisp crust, then serve", 4);
            directions.add(direction);

            recipe.setDirectionsList(directions);

            categories = new ArrayList<RecipeCategory>();

            rcategory = new RecipeCategory(-1, recipe.getKeyID(), lunch.getKeyID());
            categories.add(rcategory);

            recipe.setCategoryList(categories);

            worked = database.editRecipe(recipe);

            title = "Ground Beef Tacos";
            servings = 10;
            prep_time = 30;
            total_time = 45;
            favorited = false;

            recipe = new Recipe(title, servings, prep_time, total_time, favorited);
            recipeID = database.addRecipe(recipe);

            ingredients = new ArrayList<RecipeIngredient>();

            ingredient = new Ingredient(-1, "tortillas");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "pack");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("beef"), (double) 1, "pound(s)", "ground");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("onion"), (double) 1, "none", "white, diced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("cheese"), (double) 1, "none", "bag shredded cheddar");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "salsa");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "container");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("garlic"), (double) 2, "none", "cloves, minced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("cumin"), (double) 1, "teaspoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("salt"), (double) 1, "teaspoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("pepper"), (double) 1, "teaspoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("cilantro"), (double) 1, "none", "bunch, finely chopped");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            recipe.setIngredientList(ingredients);

            directions = new ArrayList<RecipeDirection>();

            direction = new RecipeDirection(-1, recipe.getKeyID(), "add beef, seasonings, garlic, and half the diced onion to a pan on " +
                    "medium-high heat and cook until done", 1);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "warm tortillas in microwave covered with a damp paper towel or on the stovetop", 2);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "serve each ingredient in separate bowls and build tacos as desired", 3);
            directions.add(direction);

            recipe.setDirectionsList(directions);

            categories = new ArrayList<RecipeCategory>();

            rcategory = new RecipeCategory(-1, recipe.getKeyID(), lunch.getKeyID());
            categories.add(rcategory);
            rcategory = new RecipeCategory(-1, recipe.getKeyID(), dinner.getKeyID());
            categories.add(rcategory);

            recipe.setCategoryList(categories);

            worked = database.editRecipe(recipe);

            title = "Spaghetti with Meat Sauce";
            servings = 4;
            prep_time = 15;
            total_time = 45;
            favorited = false;

            recipe = new Recipe(title, servings, prep_time, total_time, favorited);
            recipeID = database.addRecipe(recipe);

            ingredients = new ArrayList<RecipeIngredient>();

            ingredient = new Ingredient(-1, "spaghetti");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "box");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "italian sausage");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "pack, ground or cased");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("tomato"), (double) 28, "none", "ounce can, crushed");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "tomato paste");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "none", "8 oz can");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "wine");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "cup(s)", "dry red");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("onion"), (double) 1, "none", "white, diced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("salt"), (double) 1, "teaspoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("pepper"), (double) 1, "teaspoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("garlic"), (double) 6, "none", "cloves, minced");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), database.getIngredient("cheese"), (double) 1, "none", "parmesan");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "oregano");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 1, "teaspoon(s)", "");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            ingredient = new Ingredient(-1, "basil");
            ingredientID = database.addIngredient(ingredient);
            ringredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, (double) 4, "none", "fresh leaves");
            database.addRecipeIngredient(ringredient);
            ingredients.add(ringredient);

            recipe.setIngredientList(ingredients);

            directions = new ArrayList<RecipeDirection>();

            direction = new RecipeDirection(-1, recipe.getKeyID(), "boil spaghetti in large pot per instructions", 1);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "sear sausage in medium pot, removing casings if necessary", 2);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "add onions and garlic, and sweat", 3);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "add tomatoes, tomato paste, seasonings, wine, cover, " +
                    "and let simmer for 30 minutes", 4);
            directions.add(direction);
            direction = new RecipeDirection(-1, recipe.getKeyID(), "drain pasta when done, and serve with sauce poured over top", 5);
            directions.add(direction);

            recipe.setDirectionsList(directions);

            categories = new ArrayList<RecipeCategory>();

            rcategory = new RecipeCategory(-1, recipe.getKeyID(), dinner.getKeyID());
            categories.add(rcategory);

            recipe.setCategoryList(categories);

            worked = database.editRecipe(recipe);
        }
    }

    /**
     *
     */
    private void getRecipeListItems() {
        try {
            if (recipes != null) {
                recipeListItems = new ArrayList<>();
                for (int i = 0; i < recipes.size(); i++) {
                    String recipe_name = recipes.get(i).getTitle();
                    double servings = recipes.get(i).getServings();
                    int prep_time = recipes.get(i).getPrep_time();
                    int total_time = recipes.get(i).getTotal_time();
                    Bitmap image = recipes.get(i).getImage(this);
                    recipeListItems.add(new RecipeListItem(recipe_name, servings, prep_time, total_time, image, recipes.get(i).getFavorited(), recipes.get(i).getKeyID()));
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No Recipes Found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param list
     * @param query
     * @return
     */
    private List<RecipeListItem> filter(List<RecipeListItem> list, String query) {
        query = query.toLowerCase();
        final List<RecipeListItem> filteredList = new ArrayList<>();
        for (RecipeListItem recipe : list) {
            final String text = recipe.getRecipeName().toLowerCase();
            if (text.contains(query))
                filteredList.add(recipe);
        }
        return filteredList;
    }

    private void advancedSearch(String input, int checkedRecipeRadio, int ingredientId[], int catgeoryId, boolean ascending) {
        recipes = new ArrayList<>();
        if (ingredientId.length > 0) {
            if (ingredientId.length == 1)
                recipes.addAll(database.getRecipeByIngredientId(ingredientId[0]));
            else
                recipes.addAll(database.getRecipeByIngredientIdList(ingredientId));
        }
        if (catgeoryId != -1) {
            recipes.addAll(database.getRecipeByCategoryId(catgeoryId));
        }
        switch (checkedRecipeRadio) {
            case SEARCH_BY_NAME:
                if (input.compareTo("") != 0)
                    if (database.getAllRecipesByTitle(input) != null)
                        recipes.addAll(database.getAllRecipesByTitle(input));
                recipes = quickSortByTitle(recipes, ascending);

                break;
            case SEARCH_BY_PREP_TIME:
                if (input.compareTo("") != 0)
                    if (database.getRecipeByPrepTime(Integer.valueOf(input)) != null)
                        recipes.addAll(database.getRecipeByPrepTime(Integer.valueOf(input)));
                recipes = quickSortByPrepTime(recipes, ascending);

                break;
            case SEARCH_BY_TOTAL_TIME:
                if (input.compareTo("") != 0)
                    if (database.getRecipeByTotalTime(Integer.valueOf(input)) != null)
                        recipes.addAll(database.getRecipeByTotalTime(Integer.valueOf(input)));
                recipes = quickSortByTotalTime(recipes, ascending);
                break;
        }
        if (recipes.size() == 0) {
            Toast.makeText(this, "No recipes found. Displaying random recipes", Toast.LENGTH_SHORT).show();
            recipes = database.getRandomRecipes();
        }

    }


    public static List<Recipe> quickSortByTitle(List<Recipe> list, boolean ascending) {
        List<Recipe> sorted;  // this shall be the sorted list to return, no needd to initialise
        List<Recipe> smaller = new ArrayList<Recipe>(); // Vehicles smaller than pivot
        List<Recipe> greater = new ArrayList<Recipe>(); // Vehicles greater than pivot

        if (list.isEmpty())
            return list; // start with recursion base case
        Recipe pivot = list.get(0);  // first Vehicle in list, used as pivot
        int i;
        Recipe j;     // Variable used for Vehicles in the loop
        for (i = 1; i < list.size(); i++) {
            j = list.get(i);
            if (ascending) {
                if (j.getTitle().compareTo(pivot.getTitle()) < 0)   // make sure Vehicle has proper compareTo method
                    smaller.add(j);
                else
                    greater.add(j);
            } else {
                if (j.getTitle().compareTo(pivot.getTitle()) > 0)   // make sure Vehicle has proper compareTo method
                    smaller.add(j);
                else
                    greater.add(j);
            }
        }
        smaller = quickSortByTitle(smaller, ascending);  // capitalise 's'
        greater = quickSortByTitle(greater, ascending);  // sort both halfs recursively
        smaller.add(pivot);          // add initial pivot to the end of the (now sorted) smaller Vehicles
        smaller.addAll(greater);     // add the (now sorted) greater Vehicles to the smaller ones (now smaller is essentially your sorted list)
        sorted = smaller;            // assign it to sorted; one could just as well do: return smaller

        return sorted;


    }

    public static List<Recipe> quickSortByPrepTime(List<Recipe> list, boolean ascending) {
        List<Recipe> sorted;  // this shall be the sorted list to return, no needd to initialise
        List<Recipe> smaller = new ArrayList<Recipe>(); // Vehicles smaller than pivot
        List<Recipe> greater = new ArrayList<Recipe>(); // Vehicles greater than pivot

        if (list.isEmpty())
            return list; // start with recursion base case
        Recipe pivot = list.get(0);  // first Vehicle in list, used as pivot
        int i;
        Recipe j;     // Variable used for Vehicles in the loop
        for (i = 1; i < list.size(); i++) {
            j = list.get(i);
            if (ascending) {
                if (j.getPrep_time() < pivot.getPrep_time())   // make sure Vehicle has proper compareTo method
                    smaller.add(j);
                else
                    greater.add(j);
            } else {
                if (j.getPrep_time() > pivot.getPrep_time())   // make sure Vehicle has proper compareTo method
                    smaller.add(j);
                else
                    greater.add(j);
            }
        }
        smaller = quickSortByPrepTime(smaller, ascending);  // capitalise 's'
        greater = quickSortByPrepTime(greater, ascending);  // sort both halfs recursively
        smaller.add(pivot);          // add initial pivot to the end of the (now sorted) smaller Vehicles
        smaller.addAll(greater);     // add the (now sorted) greater Vehicles to the smaller ones (now smaller is essentially your sorted list)
        sorted = smaller;            // assign it to sorted; one could just as well do: return smaller

        return sorted;


    }

    public static List<Recipe> quickSortByTotalTime(List<Recipe> list, boolean ascending) {
        List<Recipe> sorted;  // this shall be the sorted list to return, no needd to initialise
        List<Recipe> smaller = new ArrayList<Recipe>(); // Vehicles smaller than pivot
        List<Recipe> greater = new ArrayList<Recipe>(); // Vehicles greater than pivot

        if (list.isEmpty())
            return list; // start with recursion base case
        Recipe pivot = list.get(0);  // first Vehicle in list, used as pivot
        int i;
        Recipe j;     // Variable used for Vehicles in the loop
        for (i = 1; i < list.size(); i++) {
            j = list.get(i);
            if (ascending) {
                if (j.getTotal_time() < pivot.getTotal_time())   // make sure Vehicle has proper compareTo method
                    smaller.add(j);
                else
                    greater.add(j);
            } else {
                if (j.getTotal_time() > pivot.getTotal_time())   // make sure Vehicle has proper compareTo method
                    smaller.add(j);
                else
                    greater.add(j);
            }
        }
        smaller = quickSortByTotalTime(smaller, ascending);  // capitalise 's'
        greater = quickSortByTotalTime(greater, ascending);  // sort both halfs recursively
        smaller.add(pivot);          // add initial pivot to the end of the (now sorted) smaller Vehicles
        smaller.addAll(greater);     // add the (now sorted) greater Vehicles to the smaller ones (now smaller is essentially your sorted list)
        sorted = smaller;            // assign it to sorted; one could just as well do: return smaller

        return sorted;


    }

    public <Recipe> List<Recipe> intersection(List<Recipe> list1, List<Recipe> list2) {
        List<Recipe> list = new ArrayList<Recipe>();

        for (Recipe t : list1) {
            if (list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public void onToolbarTextClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void viewFavorites() {
        recipes = database.getRecipesByFavorite();
        if (recipes == null) {
            Toast.makeText(this, "You have not favorited any recipes", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }
        getRecipeListItems();
        recipeAdapter = new RecipeAdapter(recipeListItems, this);
        recyclerView.setAdapter(recipeAdapter);
    }
}