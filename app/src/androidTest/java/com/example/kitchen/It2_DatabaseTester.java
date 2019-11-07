package com.example.kitchen;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class It2_DatabaseTester {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    DatabaseHelper testDatabase = new DatabaseHelper(appContext.getApplicationContext());

    // Create recipe to test with
    String recipeTitle = "TestRecipe";
    Category category = new Category(-1, "Lunch");
    int categoryID;

    {
        categoryID = testDatabase.addCategory(category);
    }

    RecipeCategory recipeCategory = new RecipeCategory(-1, -1, categoryID);
    List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();

    {
        listOfCategories.add(recipeCategory);
    }

    Ingredient ingredient = new Ingredient(-1, "Flour");
    int ingredientID;

    {
        ingredientID = testDatabase.addIngredient(ingredient);
    }

    RecipeIngredient recipeIngredient = new RecipeIngredient(-1, -1, ingredientID, 2.0, "cups", "White Flour");
    List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();

    {
        listOfIngredients.add(recipeIngredient);
    }

    RecipeDirection recipeDirection1 = new RecipeDirection(-1, -1, "TestDirection1", 1);
    RecipeDirection recipeDirection2 = new RecipeDirection(-1, -1, "TestDirection2", 2);
    List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();

    {
        listOfDirections.add(recipeDirection1);
        listOfDirections.add(recipeDirection2);
    }

    Recipe testRecipe = new Recipe(recipeTitle, 1.0, 30, 60, false, listOfIngredients, listOfDirections, listOfCategories);

    /**
     * This method tests to make sure appContext is correct
     * This was a built-in test. I did not write this
     */
    @Test
    public void useAppContext() {
        assertEquals("com.example.kitchen", appContext.getPackageName());
    }

    /**
     * This method checks that addIngredient returns 1 when adding a ingredient that previously existed
     */
    @Test
    public void addDuplicateIngredient_Returns1() {
        //TODO: Implement
    }

    /**
     * This method checks that addIngredient does not add the ingredient to the database again
     * if the ingredient previously existed
     */
    @Test
    public void addDuplicateIngredient_NoDatabaseChange() {
        //TODO: Implement
    }

    /**
     * This method checks that addCategory returns 1 when adding a category that previously existed
     */
    @Test
    public void addDuplicateCategory_Returns1() {
        //TODO: Implement
    }

    /**
     * This method checks that addCategory does not add the category to the database again
     * if the category previously existed
     */
    @Test
    public void addDuplicateCategory_NoDatabaseChange() {
        //TODO: Implement
    }

    /**
     * This method checks that getIngredient returns the correct ingredient object
     */
    @Test
    public void getIngredient_IngredientExists() {
        //TODO: Implement
    }

    /**
     * This method checks that getIngredient returns null when attempting to get an ingredient
     * that doesn't exist
     */
    @Test
    public void getIngredient_DoesntExist() {
        assertEquals("getIngredient - Doesn't Exist", null, testDatabase.getIngredient(Integer.MAX_VALUE));
    }

    /**
     * This method checks that getRecipeByIngredientId returns an ArrayList of Recipes
     * when passed an ingredient that exists in at least one recipe
     */
    @Test
    public void getRecipeByIngredientId_NoErrors() {
        //TODO: Implement
    }

    /**
     * This method checks that getRecipeByIngredientId returns all the correct recipes
     */
    @Test
    public void getRecipeByIngredientId_CorrectReturn() {
        //TODO: Implement
    }

    /**
     * This method tries to get recipes by an ingredient that is not in any recipes
     * Check this case returns null
     */
    @Test
    public void getRecipeByIngredientId_NoRecipes() {
        assertEquals("getRecipeByIngredientId with ID that doesn't exist", null, testDatabase.getRecipeByIngredientId(Integer.MAX_VALUE));
    }

    /**
     * This method checks that getRecipeByIngredientId returns an ArrayList of Recipes
     * when passed an ingredient list that exists in at least one recipe
     */
    @Test
    public void getRecipeByIngredientIdList_NoErrors() {
        //TODO: Implement
    }

    /**
     * This method checks that getRecipeByIngredientIdList returns all the correct recipes
     */
    @Test
    public void getRecipeByIngredientIdList_CorrectReturn() {
        //TODO: Implement
    }

    /**
     * This method attempts to get recipes by an ingredient list that is not in any recipe
     * Check this case returns null
     */
    @Test
    public void getRecipeByIngredientIdList_NoRecipes() {
        //TODO: Implement
    }

    /**
     * This method checks that getRecipeByCategoryId returns an ArrayList of Recipes
     * when passed a category that exists in at least one recipe
     */
    @Test
    public void getRecipeByCategoryId_NoErrors() {
        //TODO: Implement
    }

    /**
     * This method checks that getRecipeByCategoryId returns all the correct recipes
     */
    @Test
    public void getRecipeByCategoryId_CorrectReturn() {
        //TODO: Implement
    }

    /**
     * This method attempts to get recipes by a category that is not in any recipe
     * Check this case returns null
     */
    @Test
    public void getRecipeByCategoryId_NoRecipes() {
        assertEquals("getRecipeByCategoryId with ID that doesn't exist", null, testDatabase.getRecipeByCategoryId(Integer.MAX_VALUE));
    }

    /**
     * This method checks that addRecipeToCart returns true upon success
     */
    @Test
    public void addRecipeToCart_ReturnsTrue() {
        int recipeId = testDatabase.addRecipe(testRecipe);
        assertEquals("Adding Recipe to Cart Returns True", true, testDatabase.addRecipeToCart(recipeId));
    }

    /**
     * This method checks that addRecipeToCart returns false upon failure
     */
    @Test
    public void addRecipeToCart_ReturnsFalse() {
        assertEquals("Adding Recipe to Cart That Doesn't Exist", false, testDatabase.addRecipeToCart(Integer.MAX_VALUE));
    }

    /**
     * This method checks that addRecipeToCart updates the shopping cart correctly
     */
    @Test
    public void addRecipeToCart_WorkedCorrectly() {
        //TODO: Implement
    }

    //TODO: ADD MORE TESTS

}
