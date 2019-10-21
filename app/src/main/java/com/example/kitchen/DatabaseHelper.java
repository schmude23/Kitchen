package com.example.kitchen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int VERSION_NUMBER = 1;
    public static final String DATABASE_NAME = "RECIPE_DATABASE";

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
        return false; //TODO: Implement this method
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
     * This method modifies the ingredient in the ingredients table with the same
     *
     * @param ingredient
     * @return
     */
    public boolean editIngredient(Ingredient ingredient) {
        return false; //TODO: implement this method
    }
}
