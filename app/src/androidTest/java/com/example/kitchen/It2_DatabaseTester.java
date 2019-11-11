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
        assertEquals("getRecipeByCategoryId - Correct Recipe Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByCategoryId - Correct Recipe Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByCategoryId - Correct Recipe Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByCategoryId - Correct Recipe Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByCategoryId - Correct Recipe Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByCategoryId - Correct RecipeIngredient Units", "cups", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByCategoryId - Correct RecipeIngredient Quantity", 2.0, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByCategoryId - Correct RecipeIngredient Details", "White Flour", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByCategoryId - Correct RecipeDirection1 Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByCategoryId - Correct RecipeDirection1 Text", "TestDirection1", returned.get(0).getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByCategoryId - Correct RecipeDirection2 Number", 2, returned.get(0).getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByCategoryId - Correct RecipeDirection2 Text", "TestDirection2", returned.get(0).getDirectionsList().get(1).getDirectionText());
        tearDown();

    }

    /**
     * This method checks that getRecipeByCategoryId returns the correct recipes when there's multiple matches
     */
    @Test
    public void getRecipeByCategoryId_MultipleRecipes() {
        setUp();
        // Add a second recipe with the same category
        Category category2 = new Category(-1, "Lunch");
        int categoryID2 = testDatabase.addCategory(category);

        RecipeCategory recipeCategory2 = new RecipeCategory(-1, -1, categoryID);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory2);

        Ingredient ingredient2 = new Ingredient(-1, "Sugar");
        int ingredientID2 = testDatabase.addIngredient(ingredient2);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientID, 1, "cups", "");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient2);

        RecipeDirection recipeDirection = new RecipeDirection(-1, -1, "TestDirection", 1);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection);

        Recipe testRecipe2 = new Recipe("TestRecipe2", 4, 5, 15, false, listOfIngredients, listOfDirections, listOfCategories);
        int recipeID2 = testDatabase.addRecipe(testRecipe2);

        // Check that it returns both recipes
        ArrayList<Recipe> returned = testDatabase.getRecipeByCategoryId(categoryID2);
        assertEquals("getRecipeByCategoryId - Returns Multiple Recipes", 2, returned.size());
        //TODO: Fix Database and Add Checks on the Recipes

        tearDown();
    }

    /**
     * This method attempts to get recipes by a prep_time that is not in any recipe
     * Check this case returns null
     */
    @Test
    public void getRecipeByPrepTime_NoRecipes() {
        setUp();
        //TODO FIX
        assertEquals("getRecipeByPrepTime with time that doesn't exist", null, testDatabase.getRecipeByPrepTime(Integer.MAX_VALUE));
        tearDown();
    }

    /**
     * This method checks that getRecipeByPrepTime returns an ArrayList of Recipes
     * when passed a category that exists in at least one recipe
     */
    @Test
    public void getRecipeByPrepTime_NoErrors() {
        setUp();
        //TODO: FIX DATABASE
        ArrayList<Recipe> returned = testDatabase.getRecipeByPrepTime(30);
        assertNotEquals("getRecipeByPrepTime returns a list", null, returned);
        tearDown();
    }

    /**
     * This method checks that getRecipeByPrepTime returns the correct recipe when there's only one match
     */
    @Test
    public void getRecipeByPrepTime_OneRecipe() {
        setUp();
        //TODO FIX
        ArrayList<Recipe> returned = testDatabase.getRecipeByPrepTime(30);
        assertEquals("getRecipeByPrepTime returns one recipe", 1, returned.size());
        assertEquals("getRecipeByPrepTime - Correct Recipe Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByPrepTime - Correct Recipe Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByPrepTime - Correct RecipeIngredient Units", "cups", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByPrepTime - Correct RecipeIngredient Quantity", 2.0, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByPrepTime - Correct RecipeIngredient Details", "White Flour", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByPrepTime - Correct RecipeDirection1 Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Correct RecipeDirection1 Text", "TestDirection1", returned.get(0).getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByPrepTime - Correct RecipeDirection2 Number", 2, returned.get(0).getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Correct RecipeDirection2 Text", "TestDirection2", returned.get(0).getDirectionsList().get(1).getDirectionText());
        tearDown();

    }

    /**
     * This method checks that getRecipeByPrepTime returns the correct recipes when there's multiple matches
     */
    @Test
    public void getRecipeByPrepTime_MultipleRecipes() {
        setUp();
        //TODO FIX
        // Add a second recipe with the same category
        Category category2 = new Category(-1, "Dinner");
        int categoryID2 = testDatabase.addCategory(category);

        RecipeCategory recipeCategory2 = new RecipeCategory(-1, -1, categoryID);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory2);

        Ingredient ingredient2 = new Ingredient(-1, "Sugar");
        int ingredientID2 = testDatabase.addIngredient(ingredient2);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientID, 1, "cups", "");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient2);

        RecipeDirection recipeDirection = new RecipeDirection(-1, -1, "TestDirection", 1);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection);

        Recipe testRecipe2 = new Recipe("TestRecipe2", 4, 30, 45, false, listOfIngredients, listOfDirections, listOfCategories);
        int recipeID2 = testDatabase.addRecipe(testRecipe2);

        // Check that it returns both recipes
        ArrayList<Recipe> returned = testDatabase.getRecipeByPrepTime(30);
        assertEquals("getRecipeByPrepTime - Returns Multiple Recipes", 2, returned.size());
        //TODO: Fix Database and Add Checks on the Recipes

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
        testDatabase.addRecipeToCart(recipeID);
        ArrayList<RecipeIngredient> shoppingCart = testDatabase.getShoppingCartIngredients();
        assertEquals("addRecipeToCart - Adds one ingredient", 1, shoppingCart.size());
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
    /*@Test
    public void addRecipeToCart_ReturnsFalse() {
        setUp();
        assertEquals("Adding Recipe to Cart That Doesn't Exist", false, testDatabase.addRecipeToCart(Integer.MAX_VALUE));
        tearDown();
    }*/ // TODO: DO WE WANT THIS TEST

    //TODO: ADD MORE TESTS

}
