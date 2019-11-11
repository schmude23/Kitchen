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
    Recipe testRecipe, testRecipe2;
    Category category;
    Ingredient ingredient;
    RecipeCategory recipeCategory;
    RecipeIngredient recipeIngredient;

    int ingredientIDM1, ingredientIDM2;

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

        recipeIngredient = new RecipeIngredient(-1, -1, ingredientID, 2.0, "cup(s)", "White Flour");
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
        ArrayList<Recipe> allRecipes = testDatabase.getAllRecipes();
        for(int i = 0; i < allRecipes.size(); i++){
            testDatabase.deleteRecipe(allRecipes.get(i).getKeyID());
        }
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
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientId(recipeIngredient.getKeyID());
        assertNotEquals("getRecipeByIngredientId returns a list", null, returned);
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientId returns the correct recipe
     */
    @Test
    public void getRecipeByIngredientId_OneRecipe() {
        setUp();
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientId(recipeIngredient.getKeyID());
        assertEquals("getRecipeByIngredientId returns one recipe", 1, returned.size());
        assertEquals("getRecipeByIngredientId - Correct Recipe Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByIngredientId - Correct Recipe Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByIngredientId - Correct Recipe Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByIngredientId - Correct Recipe Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByIngredientId - Correct Recipe Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByIngredientId - Correct RecipeIngredient Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByIngredientId - Correct RecipeIngredient Quantity", 2.0, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByIngredientId - Correct RecipeIngredient Details", "White Flour", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByIngredientId - Correct RecipeDirection1 Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByIngredientId - Correct RecipeDirection1 Text", "TestDirection1", returned.get(0).getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByIngredientId - Correct RecipeDirection2 Number", 2, returned.get(0).getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByIngredientId - Correct RecipeDirection2 Text", "TestDirection2", returned.get(0).getDirectionsList().get(1).getDirectionText());
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientId returns the correct recipe when there's multiple
     * recipes that contain the same ingredient
     */
    @Test
    public void getRecipeByIngredientId_Multiple() {
        setUp();
        // Add a second recipe with the same ingredient
        Category category2 = new Category(-1, "Lunch");
        int categoryID2 = testDatabase.addCategory(category);

        RecipeCategory recipeCategory2 = new RecipeCategory(-1, -1, categoryID);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory2);

        Ingredient ingredient2 = new Ingredient(-1, "Flour");
        int ingredientID2 = testDatabase.addIngredient(ingredient2);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientID, 1, "cup(s)", "");
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

        // Check first Recipe
        assertEquals("getRecipeByCategoryId - Multiple: Recipe1 Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByCategoryId - Multiple: Recipe1 Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe1 Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe1 Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe1 Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient1 Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient1 Quantity", 2.0, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient1 Details", "White Flour", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection1.1 Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection1.1 Text", "TestDirection1", returned.get(0).getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection1.2 Number", 2, returned.get(0).getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection1.2 Text", "TestDirection2", returned.get(0).getDirectionsList().get(1).getDirectionText());

        // Check second Recipe
        assertEquals("getRecipeByCategoryId - Multiple: Recipe2 Title", "TestRecipe2", returned.get(1).getTitle());
        assertEquals("getRecipeByCategoryId - Multiple: Recipe2 Servings", 4, returned.get(1).getServings(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe2 Prep_Time", 5, returned.get(1).getPrep_time(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe2 Total_Time", 15, returned.get(1).getTotal_time(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe2 Favorited", false, returned.get(1).getFavorited());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient2 Units", "cup(s)", returned.get(1).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient2 Quantity", 1, returned.get(1).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient2 Details", "", returned.get(1).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection2 Number", 1, returned.get(1).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection2 Text", "TestDirection", returned.get(1).getDirectionsList().get(0).getDirectionText());
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
        //TODO: FIX (Test or Database?)
        createRecipe_TwoIngredients();
        int[] ingredientIDs = {ingredientIDM1, ingredientIDM2};
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientIdList(ingredientIDs);
        assertNotEquals("getRecipeByIngredientIdList returns a list", null, returned);
        tearDown();
    }

    @Test
    public void HELP() {
        setUp();
        createRecipe_TwoIngredients();
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientId(ingredientIDM1);
        assertNotEquals("HELP", null, returned);
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientIdList returns the correct recipe
     */
    @Test
    public void getRecipeByIngredientIdList_OneRecipe() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientIdList returns all the correct recipes when there's
     * more than one recipe that contains the ingredient list
     */
    @Test
    public void getRecipeByIngredientIdList_Multiple() {
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
        Ingredient ingredient2 = new Ingredient(-1, "Sugar");
        int ingredientID2 = testDatabase.addIngredient(ingredient2);
        int[] ingredientIDs = {ingredientID, ingredientID2};
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientIdList(ingredientIDs);
        assertEquals("getRecipeByIngredientIdList returns null", null, returned);
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
        ArrayList<Recipe> returned = testDatabase.getRecipeByCategoryId(recipeCategory.getCategoryID());
        assertEquals("getRecipeByCategoryId returns one recipe", 1, returned.size());
        assertEquals("getRecipeByCategoryId - Correct Recipe Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByCategoryId - Correct Recipe Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByCategoryId - Correct Recipe Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByCategoryId - Correct Recipe Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByCategoryId - Correct Recipe Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByCategoryId - Correct RecipeIngredient Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
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

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientID, 1, "cup(s)", "");
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

        // Check first Recipe
        assertEquals("getRecipeByCategoryId - Multiple: Recipe1 Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByCategoryId - Multiple: Recipe1 Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe1 Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe1 Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe1 Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient1 Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient1 Quantity", 2.0, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient1 Details", "White Flour", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection1.1 Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection1.1 Text", "TestDirection1", returned.get(0).getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection1.2 Number", 2, returned.get(0).getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection1.2 Text", "TestDirection2", returned.get(0).getDirectionsList().get(1).getDirectionText());

        // Check second Recipe
        assertEquals("getRecipeByCategoryId - Multiple: Recipe2 Title", "TestRecipe2", returned.get(1).getTitle());
        assertEquals("getRecipeByCategoryId - Multiple: Recipe2 Servings", 4, returned.get(1).getServings(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe2 Prep_Time", 5, returned.get(1).getPrep_time(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe2 Total_Time", 15, returned.get(1).getTotal_time(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: Recipe2 Favorited", false, returned.get(1).getFavorited());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient2 Units", "cup(s)", returned.get(1).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient2 Quantity", 1, returned.get(1).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByCategoryId - Multiple: RecipeIngredient2 Details", "", returned.get(1).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection2 Number", 1, returned.get(1).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByCategoryId - Multiple: RecipeDirection2 Text", "TestDirection", returned.get(1).getDirectionsList().get(0).getDirectionText());

        tearDown();
    }

    /**
     * This method attempts to get recipes by a prep_time that is not in any recipe
     * Check this case returns null
     */
    @Test
    public void getRecipeByPrepTime_NoRecipes() {
        setUp();
        //TODO: Fix Database
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
        //TODO: Fix Database
        ArrayList<Recipe> returned = testDatabase.getRecipeByPrepTime(30);
        assertEquals("getRecipeByPrepTime returns one recipe", 1, returned.size());
        assertEquals("getRecipeByPrepTime - Correct Recipe Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByPrepTime - Correct Recipe Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByPrepTime - Correct RecipeIngredient Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
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
        //TODO: Fix Database
        setUp();
        // Add a second recipe with the same prep_time
        Category category2 = new Category(-1, "Dinner");
        int categoryID2 = testDatabase.addCategory(category);

        RecipeCategory recipeCategory2 = new RecipeCategory(-1, -1, categoryID);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory2);

        Ingredient ingredient2 = new Ingredient(-1, "Sugar");
        int ingredientID2 = testDatabase.addIngredient(ingredient2);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientID, 1, "cup(s)", "");
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

        // Check first Recipe
        assertEquals("getRecipeByPrepTime - Multiple: Recipe1 Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByPrepTime - Multiple: Recipe1 Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe1 Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe1 Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe1 Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient1 Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient1 Quantity", 2.0, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient1 Details", "White Flour", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection1.1 Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection1.1 Text", "TestDirection1", returned.get(0).getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection1.2 Number", 2, returned.get(0).getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection1.2 Text", "TestDirection2", returned.get(0).getDirectionsList().get(1).getDirectionText());

        // Check second Recipe
        assertEquals("getRecipeByPrepTime - Multiple: Recipe2 Title", "TestRecipe2", returned.get(1).getTitle());
        assertEquals("getRecipeByPrepTime - Multiple: Recipe2 Servings", 4, returned.get(1).getServings(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe2 Prep_Time", 30, returned.get(1).getPrep_time(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe2 Total_Time", 45, returned.get(1).getTotal_time(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe2 Favorited", false, returned.get(1).getFavorited());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient2 Units", "cup(s)", returned.get(1).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient2 Quantity", 1, returned.get(1).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient2 Details", "", returned.get(1).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection2 Number", 1, returned.get(1).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection2 Text", "TestDirection", returned.get(1).getDirectionsList().get(0).getDirectionText());

        tearDown();
    }

    /**
     * This method attempts to get recipes by a total_time that is not in any recipe
     * Check this case returns null
     */
    @Test
    public void getRecipeByTotalTime_NoRecipes() {
        setUp();
        //TODO: Fix Database
        assertEquals("getRecipeByTotalTime with time that doesn't exist", null, testDatabase.getRecipeByTotalTime(Integer.MAX_VALUE));
        tearDown();
    }

    /**
     * This method checks that getRecipeByTotalTime returns an ArrayList of Recipes
     * when passed a total_time that exists in at least one recipe
     */
    @Test
    public void getRecipeByTotalTime_NoErrors() {
        setUp();
        //TODO: FIX DATABASE
        ArrayList<Recipe> returned = testDatabase.getRecipeByTotalTime(60);
        assertNotEquals("getRecipeByTotalTime returns a list", null, returned);
        tearDown();
    }

    /**
     * This method checks that getRecipeByTotalTime returns the correct recipe when there's only one match
     */
    @Test
    public void getRecipeByTotalTime_OneRecipe() {
        setUp();
        //TODO: Fix Database
        ArrayList<Recipe> returned = testDatabase.getRecipeByTotalTime(60);
        assertEquals("getRecipeByTotalTime returns one recipe", 1, returned.size());
        assertEquals("getRecipeByTotalTime - Correct Recipe Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByTotalTime - Correct Recipe Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByTotalTime - Correct Recipe Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByTotalTime - Correct Recipe Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByTotalTime - Correct Recipe Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByTotalTime - Correct RecipeIngredient Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByTotalTime - Correct RecipeIngredient Quantity", 2.0, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByTotalTime - Correct RecipeIngredient Details", "White Flour", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByTotalTime - Correct RecipeDirection1 Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByTotalTime - Correct RecipeDirection1 Text", "TestDirection1", returned.get(0).getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByTotalTime - Correct RecipeDirection2 Number", 2, returned.get(0).getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByTotalTime - Correct RecipeDirection2 Text", "TestDirection2", returned.get(0).getDirectionsList().get(1).getDirectionText());
        tearDown();

    }

    /**
     * This method checks that getRecipeByTotalTime returns the correct recipes when there's multiple matches
     */
    @Test
    public void getRecipeByTotalTime_MultipleRecipes() {
        //TODO: Fix Database
        setUp();
        // Add a second recipe with the same total_time
        Category category2 = new Category(-1, "Dinner");
        int categoryID2 = testDatabase.addCategory(category);

        RecipeCategory recipeCategory2 = new RecipeCategory(-1, -1, categoryID);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory2);

        Ingredient ingredient2 = new Ingredient(-1, "Sugar");
        int ingredientID2 = testDatabase.addIngredient(ingredient2);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientID, 1, "cup(s)", "");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient2);

        RecipeDirection recipeDirection = new RecipeDirection(-1, -1, "TestDirection", 1);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection);

        Recipe testRecipe2 = new Recipe("TestRecipe2", 4, 15, 60, false, listOfIngredients, listOfDirections, listOfCategories);
        int recipeID2 = testDatabase.addRecipe(testRecipe2);

        // Check that it returns both recipes
        ArrayList<Recipe> returned = testDatabase.getRecipeByPrepTime(30);
        assertEquals("getRecipeByTotalTime - Returns Multiple Recipes", 2, returned.size());

        // Check first Recipe
        assertEquals("getRecipeByTotalTime - Multiple: Recipe1 Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByTotalTime - Multiple: Recipe1 Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe1 Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe1 Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe1 Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient1 Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient1 Quantity", 2.0, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient1 Details", "White Flour", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection1.1 Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection1.1 Text", "TestDirection1", returned.get(0).getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection1.2 Number", 2, returned.get(0).getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection1.2 Text", "TestDirection2", returned.get(0).getDirectionsList().get(1).getDirectionText());

        // Check second Recipe
        assertEquals("getRecipeByTotalTime - Multiple: Recipe2 Title", "TestRecipe2", returned.get(1).getTitle());
        assertEquals("getRecipeByTotalTime - Multiple: Recipe2 Servings", 4, returned.get(1).getServings(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe2 Prep_Time", 15, returned.get(1).getPrep_time(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe2 Total_Time", 60, returned.get(1).getTotal_time(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe2 Favorited", false, returned.get(1).getFavorited());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient2 Units", "cup(s)", returned.get(1).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient2 Quantity", 1, returned.get(1).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient2 Details", "", returned.get(1).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection2 Number", 1, returned.get(1).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection2 Text", "TestDirection", returned.get(1).getDirectionsList().get(0).getDirectionText());
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
        ArrayList<RecipeIngredient> prevShoppingCart = testDatabase.getShoppingCartIngredients();
        testDatabase.addRecipeToCart(recipeID);
        ArrayList<RecipeIngredient> newShoppingCart = testDatabase.getShoppingCartIngredients();
        assertEquals("addRecipeToCart - Adds one ingredient", 1, (newShoppingCart.size()-prevShoppingCart.size()));
        assertEquals("addRecipeToCart - Correct ingredientID", ingredientID, newShoppingCart.get(newShoppingCart.size()-1).getIngredientID());
        assertEquals("addRecipeToCart - Correct Quantity", 2, newShoppingCart.get(newShoppingCart.size()-1).getQuantity(), 0);
        assertEquals("addRecipeToCart - Correct Unit", "cup(s)", newShoppingCart.get(newShoppingCart.size()-1).getUnit());
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

    /**
     * This method checks that getShoppingCartIngredients returns correct information
     * if there's only one instance of an ingredient that was added to the shopping cart
     */
    @Test
    public void getShoppingCartIngredients_ReturnsNull() {
        setUp();
        ArrayList<RecipeIngredient> shoppingCart = testDatabase.getShoppingCartIngredients();
        assertEquals("getShoppingCartIngredients - Returns Null", null, shoppingCart);
        tearDown();
    } //TODO: Might need to move this up

    /**
     * This method checks that getShoppingCartIngredients returns correct information
     * if there's only one instance of an ingredient that was added to the shopping cart
     */
    @Test
    public void getShoppingCartIngredients_OneInstanceCorrect() {
        setUp();
        testDatabase.addRecipeToCart(recipeID);
        ArrayList<RecipeIngredient> newShoppingCart = testDatabase.getShoppingCartIngredients();
        assertEquals("getShoppingCartIngredients - Correct ingredientID", ingredientID, newShoppingCart.get(newShoppingCart.size()-1).getIngredientID());
        assertEquals("getShoppingCartIngredients - Correct Quantity", 2, newShoppingCart.get(newShoppingCart.size()-1).getQuantity(), 0);
        assertEquals("getShoppingCartIngredients - Correct Unit", "cup(s)", newShoppingCart.get(newShoppingCart.size()-1).getUnit());
        tearDown();
    }

    /**
     * This method checks that getShoppingCartIngredients returns correct information
     * if there were multiple instances of an ingredient that were added to the shopping cart
     */
    @Test
    public void getShoppingCartIngredients_MultipleInstancesCorrect() {
        setUp();
        //TODO: FIX TEST
        // Add a second recipe
        Category category2 = new Category(-1, "Lunch");
        int categoryID2 = testDatabase.addCategory(category);

        RecipeCategory recipeCategory2 = new RecipeCategory(-1, -1, categoryID);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory2);

        Ingredient ingredient2 = new Ingredient(-1, "Sugar");
        int ingredientID2 = testDatabase.addIngredient(ingredient2);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientID, 2, "cup(s)", "");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient2);

        RecipeDirection recipeDirection = new RecipeDirection(-1, -1, "TestDirection", 1);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection);

        Recipe testRecipe2 = new Recipe("TestRecipe2", 4, 5, 15, false, listOfIngredients, listOfDirections, listOfCategories);
        int recipeID2 = testDatabase.addRecipe(testRecipe2);

        testDatabase.addRecipeToCart(recipeID);
        ArrayList<RecipeIngredient> prevShoppingCart = testDatabase.getShoppingCartIngredients();
        int prevSize = prevShoppingCart.size();
        testDatabase.addRecipeToCart(recipeID2);
        ArrayList<RecipeIngredient> newShoppingCart = testDatabase.getShoppingCartIngredients();
        assertEquals("getShoppingCartIngredients1 - Correct ingredientID", ingredientID, newShoppingCart.get(prevSize-1).getIngredientID());
        assertEquals("getShoppingCartIngredients1 - Correct Quantity", 1, newShoppingCart.get(prevSize-1).getQuantity(), 0);
        assertEquals("getShoppingCartIngredients1 - Correct Unit", "cup(s)", newShoppingCart.get(prevSize-1).getUnit());

        assertEquals("getShoppingCartIngredients2 - Correct ingredientID", ingredientID2, newShoppingCart.get(prevSize).getIngredientID());
        assertEquals("getShoppingCartIngredients2 - Correct Quantity", 2, newShoppingCart.get(prevSize).getQuantity(), 0);
        assertEquals("getShoppingCartIngredients2 - Correct Unit", "cup(s)", newShoppingCart.get(prevSize).getUnit());
        tearDown();
    }

    /**
     * This method checks that updateShoppingCartIngredient returns true upon success
     */
    @Test
    public void updateShoppingCartIngredient_ReturnsTrue() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method checks that updateShoppingCartIngredient returns false upon failure
     */
    @Test
    public void updateShoppingCartIngredient_ReturnsFalse() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method checks that updateShoppingCartIngredient actually updates the ingredient in the
     * shopping cart
     */
    @Test
    public void updateShoppingCartIngredient_Updates() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method checks that deleteShoppingCartIngredient returns true upon success
     */
    @Test
    public void deleteShoppingCartIngredient_ReturnsTrue() {
        setUp();
        assertEquals("deleteShoppingCartIngredeint - Returns true", true, testDatabase.deleteShoppingCartIngredient(ingredientID));
        tearDown();
    }

    /**
     * This method checks that deleteShoppingCartIngredient returns false upon failure
     */
    @Test
    public void deleteShoppingCartIngredient_ReturnsFalse() {
        setUp();
        //TODO: Zander fixing
        assertEquals("deleteShoppingCartIngredient - Returns true", true, testDatabase.deleteShoppingCartIngredient(Integer.MAX_VALUE));
        tearDown();
    }

    /**
     * This method checks that deleteShoppingCartIngredient actually deletes the ingredient from the
     * shopping cart
     */
    @Test
    public void deleteShoppingCartIngredient_Deletes() {
        setUp();
        ArrayList<RecipeIngredient> prevShoppingCart = testDatabase.getShoppingCartIngredients();
        testDatabase.deleteShoppingCartIngredient(ingredientID);
        ArrayList<RecipeIngredient> newShoppingCart = testDatabase.getShoppingCartIngredients();
        boolean deleted = true;
        for(int i = 0; i < newShoppingCart.size(); i++){
            if(newShoppingCart.get(i).getIngredientID() == ingredientID){
                deleted = false;
            }
        }
        assertEquals("deleteShoppingCartIngredient - Deletes Ingredient", true, deleted);
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns the same recipe if passed the same unit
     */
    @Test
    public void convertRecipeIngredientUnits_NoChange() {
        setUp();
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe, "cup(s)", "cup(s)");
        assertEquals("convertRecipeIngredientUnits - Correct Quantity", 2, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits - Correct Unit", "cup(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe
     */
    @Test
    public void convertRecipeIngredientUnits_ReturnsRecipe() {
        setUp();
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe, "cup(s)", "cup(s)");
        assertNotEquals("convertRecipeIngredientUnits - Returns Recipe", null, returned);
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     */
    @Test
    public void convertRecipeIngredientUnits_RecipeChanged() {
        setUp();
        //TODO: Do we want this?
        createRecipe_DifferentUnits("teaspoon(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "teaspoon(s)", "tablespoon(s)");
        Recipe updated = testDatabase.getRecipe(testRecipe2.getKeyID());
        assertEquals("getRecipeByPrepTime - Correct Recipe Title", "TestRecipe2", updated.getTitle());
        assertEquals("getRecipeByPrepTime - Correct Recipe Servings", 4, updated.getServings(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Prep_Time", 5, updated.getPrep_time(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Total_Time", 15, updated.getTotal_time(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Favorited", false, updated.getFavorited());
        assertEquals("getRecipeByPrepTime - Correct RecipeIngredient Units", "tablespoon(s)", updated.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByPrepTime - Correct RecipeIngredient Quantity", 2, updated.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByPrepTime - Correct RecipeIngredient Details", "", updated.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByPrepTime - Correct RecipeDirection Number", 1, updated.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Correct RecipeDirection Text", "TestDirection", updated.getDirectionsList().get(0).getDirectionText());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes when there are multiple ingredients
     */
    @Test
    public void convertRecipeIngredientUnits_MultipleIngredients() {
        setUp();
        createRecipe_TwoIngredients();
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "cup(s)", "tablespoon(s)");
        assertEquals("convertRecipeIngredientUnits - Correct Sugar Quantity", 16, returned.getIngredientList().get(0).getQuantity(), 0); //1 cup
        assertEquals("convertRecipeIngredientUnits - Correct Sugar Unit", "tablespoon(s)", returned.getIngredientList().get(0).getUnit());
        assertEquals("convertRecipeIngredientUnits - Correct Oil Quantity", 48, returned.getIngredientList().get(1).getQuantity(), 0); //3 cups
        assertEquals("convertRecipeIngredientUnits - Correct Oil Unit", "tablespoon(s)", returned.getIngredientList().get(1).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was tablespoons, is now teaspoons
     */
    @Test
    public void convertRecipeIngredientUnits_TablespoonsToTeaspoons() {
        setUp();
        createRecipe_DifferentUnits("tablespoon(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "tablespoon(s)", "teaspoon(s)");
        assertEquals("convertRecipeIngredientUnits_TablespoonsToTeaspoons - Quantity", 18, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_TablespoonsToTeaspoons - Unit", "teaspoon(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was teaspoons, is now tablespoons
     */
    @Test
    public void convertRecipeIngredientUnits_TeaspoonsToTablespoons() {
        setUp();
        createRecipe_DifferentUnits("teaspoon(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "teaspoon(s)", "tablespoon(s)");
        assertEquals("convertRecipeIngredientUnits_TeaspoonsToTablespoons - Quantity", 2, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_TeaspoonsToTablespoons - Unit", "tablespoon(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was cups, is now pint
     */
    @Test
    public void convertRecipeIngredientUnits_CupsToPint() {
        setUp();
        createRecipe_DifferentUnits("cup(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "cup(s)", "pint(s)");
        assertEquals("convertRecipeIngredientUnits_CupsToPint - Quantity", 3, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_CupsToPint - Unit", "pint(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was pint, is now cups
     */
    @Test
    public void convertRecipeIngredientUnits_PintToCups() {
        setUp();
        createRecipe_DifferentUnits("pint(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "pint(s)", "cup(s)");
        assertEquals("convertRecipeIngredientUnits_PintToCups - Quantity", 12, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_PintToCups - Unit", "cup(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was quarts, is now gallons
     */
    @Test
    public void convertRecipeIngredientUnits_QuartsToGallons() {
        setUp();
        createRecipe_DifferentUnits("quart(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "quart(s)", "gallon(s)");
        assertEquals("convertRecipeIngredientUnits_PintToCups - Quantity", 1.5, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_PintToCups - Unit", "gallon(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was gallons, is now quarts
     */
    @Test
    public void convertRecipeIngredientUnits_GallonsToQuarts() {
        setUp();
        createRecipe_DifferentUnits("gallon(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "gallon(s)", "quart(s)");
        assertEquals("convertRecipeIngredientUnits_PintToCups - Quantity", 24, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_PintToCups - Unit", "quart(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was pounds, is now pinches
     */
    @Test
    public void convertRecipeIngredientUnits_PoundsToPinches() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was pinches, is now pounds
     */
    @Test
    public void convertRecipeIngredientUnits_PinchesToPounds() {
        setUp();
        //TODO: Implement
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was blank, is now cups. This should not change the ingredient at all
     */
    @Test
    public void convertRecipeIngredientUnits_BlankToCups() {
        setUp();
        createRecipe_DifferentUnits("none");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "none", "cup(s)");
        assertEquals("convertRecipeIngredientUnits_PintToCups - Quantity", 6, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_PintToCups - Unit", "none", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was blank, is now cups. This should not change the ingredient at all
     */
    @Test
    public void convertRecipeIngredientUnits_CupsToBlank() {
        setUp();
        createRecipe_DifferentUnits("cup(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "cup(s)", "none");
        assertEquals("convertRecipeIngredientUnits_PintToCups - Quantity", 6, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_PintToCups - Unit", "cup(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    //TODO: Add tests for scaling a Recipe


    //TODO: ADD MORE TESTS?

    /**
     * Helper method for tests that need a recipe with two ingredients
     */
    public void createRecipe_TwoIngredients(){
        Category category2 = new Category(-1, "Lunch");
        int categoryID2 = testDatabase.addCategory(category);

        RecipeCategory recipeCategory2 = new RecipeCategory(-1, -1, categoryID);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory2);

        Ingredient ingredient2 = new Ingredient(-1, "Sugar");
        ingredientIDM1 = testDatabase.addIngredient(ingredient2);
        Ingredient ingredient3 = new Ingredient(-1, "Oil");
        ingredientIDM2 = testDatabase.addIngredient(ingredient3);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientIDM1, 1, "cup(s)", "");
        RecipeIngredient recipeIngredient3 = new RecipeIngredient(-1, -1, ingredientIDM2, 3.0, "cup(s)", "White Flour");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient2);
        listOfIngredients.add(recipeIngredient3);

        RecipeDirection recipeDirection = new RecipeDirection(-1, -1, "TestDirection", 1);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection);

        testRecipe2 = new Recipe("TestRecipe2", 4, 5, 15, false, listOfIngredients, listOfDirections, listOfCategories);
        int recipeID2 = testDatabase.addRecipe(testRecipe2);
    }

    /**
     * Helper method for tests that check unit conversion
     */
    public void createRecipe_DifferentUnits(String units){
        Category category2 = new Category(-1, "Lunch");
        int categoryID2 = testDatabase.addCategory(category);

        RecipeCategory recipeCategory2 = new RecipeCategory(-1, -1, categoryID);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory2);

        Ingredient ingredient2 = new Ingredient(-1, "Flour");
        ingredientIDM1 = testDatabase.addIngredient(ingredient2);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientIDM1, 6.0, units, "");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient2);

        RecipeDirection recipeDirection = new RecipeDirection(-1, -1, "TestDirection", 1);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection);

        testRecipe2 = new Recipe("TestRecipe2", 4, 5, 15, false, listOfIngredients, listOfDirections, listOfCategories);
        int recipeID2 = testDatabase.addRecipe(testRecipe2);
    }
}
