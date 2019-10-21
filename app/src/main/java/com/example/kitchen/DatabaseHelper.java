package com.example.kitchen;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int VERSION_NUMBER = 1;
    public static final String DATABASE_NAME = "RECIPE_DATABASE";
    public static final String KEY_ID = "ID";

    //Recipe Table
    private static final String TABLE_Recipe_List = "RECIPE_LIST";
    public static final String title = "TITLE";
    public static final String category = "CATEGORY_ID";
    public static final String ingredients = "INGREDIENTS_ID";
    public static final String directions = "DIRECTIONS_ID";
    public static final String tTime = "TOTAL_TIME";
    public static final String pTime = "PREP_TIME";
    public static final String servings = "SERVINGS";
    public static final String favorited = "FAVORITED";
    public static final String imageId = "IMAGE_ID";

    //Ingredient Table
    private static final String TABLE_Ingredient_List = "INGREDIENT_LIST";
    public static final String name = "NAME";
    public static final String unit = "UNIT";
    public static final String details = "DETAILS";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    /**
     * Runs on creation of the database.
     * This method does all of the setup neccesary for the database such as creating tables.
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //TODO: Implement this method - DatabaseHelper - onCreate

        //Recipe Table
        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_Recipe_List + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + title + " TEXT,"
                + category + " INTEGER,"
                + ingredients + " INTEGER,"
                + directions + " INTEGER,"
                + tTime + " INTEGER,"
                + pTime + " INTEGER,"
                + servings + " INTEGER,"
                + favorited + " BOOLEAN,"
                + imageId + " INTEGER" +")";
        sqLiteDatabase.execSQL(CREATE_RECIPE_TABLE);

         //Ingredients Table
        String CREATE_INGREDIENT_TABLE = "CREATE TABLE " + TABLE_Ingredient_List + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + name + " TEXT,"
                + unit + " TEXT,"
                + details + " TEXT,"
                + imageId + " INTEGER" +")";
        sqLiteDatabase.execSQL(CREATE_INGREDIENT_TABLE);

    }

    /**
     * This method runs when the version of the database is updated.
     * This method should do all actions necessary to ensure that the database version is
     * updated correctly.
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO: Implement this method - DatabaseHelper - onUpgrade

        //find and drop existing databases
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Recipe_List);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Ingredient_List);

        //rebuild the database
        onCreate(sqLiteDatabase);
    }

    /**
     * This method creates a new row in the Recipe table using the provided Recipe
     *
     * @param recipe
     * @return true if the operation was successful, false otherwise
     */
    public boolean createRecipe(Recipe recipe) {
        return false; //TODO: implement this method
    }

    /**
     * This method retrieves the recipe for the given recipeId
     *
     * @param recipeId
     * @return The recipe corresponding to the provided recipeId, or null if one is not found.
     */
    public Recipe getRecipe(int recipeId) {
        return null; //TODO: implement this method
    }

    /**
     * This method modifies the recipe in the recipe table with the same recipeId as the recipe provided
     * to be the recipe provided
     *
     * @param recipe the provided recipe
     * @return true if the operation was successful, false otherwise.
     */
    public boolean editRecipe(Recipe recipe) {

        //TODO: correct this method
        Recipe temp = getRecipe(recipe.getKeyID());
        boolean allpassed = true;
        //updating recipe portion of table
        try {
            int id = recipe.getKeyID();
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues cVals = new ContentValues();
            cVals.put(title, recipe.getTitle());
            cVals.put(pTime, recipe.getPrep_time());
            cVals.put(tTime, recipe.getTotal_time());
            cVals.put(servings, recipe.getServings());
            cVals.put(favorited, recipe.getFavorited());
            sqLiteDatabase.update(TABLE_Ingredient_List, cVals, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        }
        catch( Exception e){
            allpassed = false;
        }


        //implement ingredientlist update
        //implement category update
        //implement directions update
        return allpassed; //TODO: Implement this method
    }

    /**
     * This method removes the recipe with the specified recipe id from the recipe's table and
     * removes all associated data from other tables
     *
     * @param recipeId
     * @return true if successful, false if not
     */
    public boolean deleteRecipe(int recipeId) {
        return false; //TODO: Implement this mehtod

        // Note to whoever is implementing this: You not only need to remove the recipe from the
        // Recipe table, but you also need to remove all of the RecipeIngredients for that recipe,
        // All of the Directions for that recipe, etc.
    }

    /**
     * This method returns a list of all recipes
     *
     * @return If successful in fetching the recipes it will return an Array list of recipes, if not
     *  it will return null.
     */
    public ArrayList<Recipe> getAllRecipes() {
        return null; //TODO: Implement this method
    }

    //TODO: Add methods to get recipes by ingredients, category, both, etc. (Iteration 1 task 5.5)


    /**
     * This method modifies the ingredient in the ingredients table with the same key id
     *
     * @param ingredient
     * @return
     */
    public boolean editIngredient(Ingredient ingredient) {
        //TODO: correct this method
        try {
            int id = ingredient.getKeyID();
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues cVals = new ContentValues();
            cVals.put(name, ingredient.getName());
            cVals.put(unit, ingredient.getUnit());
            cVals.put(details, ingredient.getDetails());
            sqLiteDatabase.update(TABLE_Ingredient_List, cVals, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        }
        catch( Exception e){
            return false;
        }

        return true;
    }
}
