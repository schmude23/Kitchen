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
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class It2_DatabaseTester {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    DatabaseHelper testDatabase = new DatabaseHelper(appContext.getApplicationContext());
    String recipeTitle;
    int categoryID, ingredientID, recipeID;
    Recipe testRecipe;
    Category category;
    Ingredient ingredient;
    RecipeCategory recipeCategory;

    public void setUp(){
        // Create recipe to test with
        recipeTitle = "TestRecipe";
        category = new Category(-1, "Lunch");
        categoryID = testDatabase.addCategory(category);

        recipeCategory = new RecipeCategory(-1, -1, categoryID);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory);

        ingredient = new Ingredient(-1, "Flour");
        ingredientID = testDatabase.addIngredient(ingredient);

        RecipeIngredient recipeIngredient = new RecipeIngredient(-1, -1, ingredientID, 2.0, "cups", "White Flour");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient);

        RecipeDirection recipeDirection1 = new RecipeDirection(-1, -1, "TestDirection1", 1);
        RecipeDirection recipeDirection2 = new RecipeDirection(-1, -1, "TestDirection2", 2);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection1);
        listOfDirections.add(recipeDirection2);

        testRecipe = new Recipe(recipeTitle, 1.0, 30, 60, false, listOfIngredients, listOfDirections, listOfCategories);
        recipeID = testDatabase.addRecipe(testRecipe);
    }

    public void tearDown(){
        // Delete all database parts
        testDatabase.deleteCategory(categoryID);
        testDatabase.deleteIngredient(ingredientID);
        testDatabase.deleteRecipe(recipeID);
    }


    /**
     * This method tests to make sure appContext is correct
     * This was a built-in test. I did not write this
     */
    @Test
    public void useAppContext() {
        assertEquals("com.example.kitchen", appContext.getPackageName());
    }

    /**
     * This method checks that addIngredient returns the ingredient's ID when adding an
     * ingredient that already exists
     */
    @Test
    public void addDuplicateIngredient_ReturnsID() {
        setUp();
        int id = testDatabase.addIngredient(ingredient);
        assertEquals("Adding duplicate ingredient returns appropriate ID", ingredientID, id);
        tearDown();
    }

    /**
     * This method checks that addCategory returns appropriate ID when adding a category that already exists
     */
    @Test
    public void addDuplicateCategory_ReturnsID() {
        setUp();
        int id = testDatabase.addCategory(category);
        assertEquals("Adding duplicate category returns appropriate ID", categoryID, id);
        tearDown();
    }

    /**
     * This method checks that getIngredient returns the correct ingredient object
     */
    @Test
    public void getIngredient_IngredientExists() {
        setUp();
        assertEquals("getIngredient - Already Exists", ingredientID, testDatabase.getIngredient("Flour"));
        tearDown();
    }

    /**
     * This method checks that getIngredient returns -1 when attempting to get an ingredient that
     * doesn't exist
     */
    @Test
    public void getIngredient_DoesNotExist() {
        //TODO: FIX DATABASE
        setUp();
        assertEquals("getIngredient - Doesn't Exist", -1, testDatabase.getIngredient("Ham"));
        tearDown();
    }

    /**
     * This method checks that getCategory returns the correct ingredient object
     */
    @Test
    public void getCategory_IngredientExists() {
        setUp();
        assertEquals("getCategory - Already Exists", categoryID, testDatabase.getCategory("Lunch"));
        tearDown();
    }

    /**
     * This method checks that getIngredient returns -1 when attempting to get an ingredient that
     * doesn't exist
     */
    @Test
    public void getCategory_DoesNotExist() {
        //TODO: FIX DATABASE
        setUp();
        assertEquals("getCategory - Doesn't Exist", -1, testDatabase.getCategory("TestCategory"));
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientId returns an ArrayList of Recipes
     * when passed an ingredient that exists in at least one recipe
     */
    @Test
    public void getRecipeByIngredientId_NoErrors() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientId returns all the correct recipes
     */
    @Test
    public void getRecipeByIngredientId_CorrectReturn() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method tries to get recipes by an ingredient that is not in any recipes
     * Check this case returns null
     */
    @Test
    public void getRecipeByIngredientId_NoRecipes() {
        setUp();
        assertEquals("getRecipeByIngredientId with ID that doesn't exist", null, testDatabase.getRecipeByIngredientId(Integer.MAX_VALUE));
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientId returns an ArrayList of Recipes
     * when passed an ingredient list that exists in at least one recipe
     */
    @Test
    public void getRecipeByIngredientIdList_NoErrors() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientIdList returns all the correct recipes
     */
    @Test
    public void getRecipeByIngredientIdList_CorrectReturn() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method attempts to get recipes by an ingredient list that is not in any recipe
     * Check this case returns null
     */
    @Test
    public void getRecipeByIngredientIdList_NoRecipes() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method checks that getRecipeByCategoryId returns an ArrayList of Recipes
     * when passed a category that exists in at least one recipe
     */
    @Test
    public void getRecipeByCategoryId_NoErrors() {
        setUp();
        //TODO: FIX DATABASE
        ArrayList<Recipe> returned = testDatabase.getRecipeByCategoryId(recipeCategory.getKeyID());
        assertNotEquals("getRecipeByCategoryId returns a list", null, returned);
        tearDown();
    }

    /**
     * This method checks that getRecipeByCategoryId returns the correct recipe when there's only one match
     */
    @Test
    public void getRecipeByCategoryId_OneRecipe() {
        setUp();
        ArrayList<Recipe> returned = testDatabase.getRecipeByCategoryId(recipeCategory.getKeyID());
        assertEquals("getRecipeByCategoryId returns one recipe", 1, returned.size());
        //TODO: Implement more checks on the recipe
        tearDown();

    }

    /**
     * This method checks that getRecipeByCategoryId returns the correct recipes when there's multiple matches
     */
    @Test
    public void getRecipeByCategoryId_MultipleRecipes() {
        setUp();
        //TODO: Implement
        ArrayList<Recipe> returned = testDatabase.getRecipeByCategoryId(categoryID);

        tearDown();
    }

    /**
     * This method attempts to get recipes by a category that is not in any recipe
     * Check this case returns null
     */
    @Test
    public void getRecipeByCategoryId_NoRecipes() {
        setUp();
        assertEquals("getRecipeByCategoryId with ID that doesn't exist", null, testDatabase.getRecipeByCategoryId(Integer.MAX_VALUE));
        tearDown();
    }

    /**
     * This method checks that addRecipeToCart updates the shopping cart correctly
     */
    @Test
    public void addRecipeToCart_CorrectInformation() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method checks that addRecipeToCart returns true upon success
     */
    @Test
    public void addRecipeToCart_ReturnsTrue() {
        setUp();
        assertEquals("Adding Recipe to Cart Returns True", true, testDatabase.addRecipeToCart(recipeID));
        tearDown();
    }

    /**
     * This method checks that addRecipeToCart returns false upon failure
     */
    @Test
    public void addRecipeToCart_ReturnsFalse() {
        setUp();
        assertEquals("Adding Recipe to Cart That Doesn't Exist", false, testDatabase.addRecipeToCart(Integer.MAX_VALUE));
        tearDown();
    }

    //TODO: ADD MORE TESTS

}
