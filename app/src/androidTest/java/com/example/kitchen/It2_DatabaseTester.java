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
    int categoryID, ingredientID, recipeID, ingredientID2, recipeID2, ingredientIDM2, ingredientIDM3;
    Recipe testRecipe, testRecipe2;
    Category category;
    Ingredient ingredient;
    RecipeCategory recipeCategory;
    RecipeIngredient recipeIngredient;

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
        if(allRecipes != null) {
            for (int i = 0; i < allRecipes.size(); i++) {
                testDatabase.deleteRecipe(allRecipes.get(i).getKeyID());
            }
        }
        testDatabase.deleteAllShoppingCartIngredients();
        ArrayList<Ingredient> allIngredients = testDatabase.getAllIngredients();
        if(allIngredients != null){
            for(int i = 0; i < allIngredients.size(); i++){
                testDatabase.deleteIngredient(allIngredients.get(i).getKeyID());
            }
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
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientId(ingredientID);
        assertNotEquals("getRecipeByIngredientId returns a list", null, returned);
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientId returns the correct recipe
     */
    @Test
    public void getRecipeByIngredientId_OneRecipe() {
        setUp();
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientId(ingredientID);
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
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientId(ingredientID2);
        assertEquals("getRecipeByIngredientId - Returns Multiple Recipes", 2, returned.size());

        // Check first Recipe
        assertEquals("getRecipeByIngredientId - Multiple: Recipe1 Title", recipeTitle, returned.get(0).getTitle());
        assertEquals("getRecipeByIngredientId - Multiple: Recipe1 Servings", 1, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByIngredientId - Multiple: Recipe1 Prep_Time", 30, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByIngredientId - Multiple: Recipe1 Total_Time", 60, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByIngredientId - Multiple: Recipe1 Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByIngredientId - Multiple: RecipeIngredient1 Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByIngredientId - Multiple: RecipeIngredient1 Quantity", 2.0, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByIngredientId - Multiple: RecipeIngredient1 Details", "White Flour", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByIngredientId - Multiple: RecipeDirection1.1 Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByIngredientId - Multiple: RecipeDirection1.1 Text", "TestDirection1", returned.get(0).getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByIngredientId - Multiple: RecipeDirection1.2 Number", 2, returned.get(0).getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByIngredientId - Multiple: RecipeDirection1.2 Text", "TestDirection2", returned.get(0).getDirectionsList().get(1).getDirectionText());

        // Check second Recipe
        assertEquals("getRecipeByIngredientId - Multiple: Recipe2 Title", "TestRecipe2", returned.get(1).getTitle());
        assertEquals("getRecipeByIngredientId - Multiple: Recipe2 Servings", 4, returned.get(1).getServings(), 0);
        assertEquals("getRecipeByIngredientId - Multiple: Recipe2 Prep_Time", 5, returned.get(1).getPrep_time(), 0);
        assertEquals("getRecipeByIngredientId - Multiple: Recipe2 Total_Time", 15, returned.get(1).getTotal_time(), 0);
        assertEquals("getRecipeByIngredientId - Multiple: Recipe2 Favorited", false, returned.get(1).getFavorited());
        assertEquals("getRecipeByIngredientId - Multiple: RecipeIngredient2 Units", "cup(s)", returned.get(1).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByIngredientId - Multiple: RecipeIngredient2 Quantity", 1, returned.get(1).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByIngredientId - Multiple: RecipeIngredient2 Details", "", returned.get(1).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByIngredientId - Multiple: RecipeDirection2 Number", 1, returned.get(1).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByIngredientId - Multiple: RecipeDirection2 Text", "TestDirection", returned.get(1).getDirectionsList().get(0).getDirectionText());
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
        createRecipe_TwoIngredients();
        int[] ingredientIDs = {ingredientIDM2, ingredientIDM3};
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientIdList(ingredientIDs);
        assertNotEquals("getRecipeByIngredientIdList returns a list", null, returned);
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientIdList returns the correct recipe
     */
    @Test
    public void getRecipeByIngredientIdList_OneRecipe() {
        setUp();
        createRecipe_TwoIngredients();
        int[] ingredientIDs = {ingredientIDM2, ingredientIDM3};
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientIdList(ingredientIDs);

        assertEquals("getRecipeByIngredientIdList - One Recipe", 1, returned.size());
        assertEquals("getRecipeByIngredientIdList - Recipe Title", "TestRecipe2", returned.get(0).getTitle());
        assertEquals("getRecipeByIngredientIdList - Recipe Servings", 4, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByIngredientIdList - Recipe Prep_Time", 5, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByIngredientIdList - Recipe Total_Time", 15, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByIngredientIdList - Recipe Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient Quantity", 1, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient Details", "", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient Units", "cup(s)", returned.get(0).getIngredientList().get(1).getUnit());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient Quantity", 3.0, returned.get(0).getIngredientList().get(1).getQuantity(), 0);
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient Details", "White Flour", returned.get(0).getIngredientList().get(1).getDetails());
        assertEquals("getRecipeByIngredientIdList - RecipeDirection Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByIngredientIdList - RecipeDirection Text", "TestDirection", returned.get(0).getDirectionsList().get(0).getDirectionText());
        tearDown();
    }

    /**
     * This method checks that getRecipeByIngredientIdList returns all the correct recipes when there's
     * more than one recipe that contains the ingredient list
     */
    @Test
    public void getRecipeByIngredientIdList_Multiple() {
        setUp();
        createRecipe_TwoIngredients();
        //Create second recipe that has the same two ingredients
        Category category3 = new Category(-1, "Breakfast");
        int categoryID3 = testDatabase.addCategory(category3);

        RecipeCategory recipeCategory3 = new RecipeCategory(-1, -1, categoryID3);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory3);

        Ingredient ingredient4 = new Ingredient(-1, "Sugar");
        int ingredientIDM4 = testDatabase.addIngredient(ingredient4);
        Ingredient ingredient5 = new Ingredient(-1, "Oil");
        int ingredientIDM5 = testDatabase.addIngredient(ingredient5);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientIDM4, 1, "cup(s)", "");
        RecipeIngredient recipeIngredient3 = new RecipeIngredient(-1, -1, ingredientIDM5, 3.0, "cup(s)", "White Flour");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient2);
        listOfIngredients.add(recipeIngredient3);

        RecipeDirection recipeDirection = new RecipeDirection(-1, -1, "Direction", 1);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection);

        Recipe testRecipe3 = new Recipe("TestRecipe3", 2, 10, 20, false, listOfIngredients, listOfDirections, listOfCategories);
        int recipeID3 = testDatabase.addRecipe(testRecipe3);

        int[] ingredientIDs = {ingredientIDM4, ingredientIDM5};
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientIdList(ingredientIDs);

        assertEquals("getRecipeByIngredientIdList - Two Recipes", 2, returned.size());
        // Test first match
        assertEquals("getRecipeByIngredientIdList - Recipe1 Title", "TestRecipe2", returned.get(0).getTitle());
        assertEquals("getRecipeByIngredientIdList - Recipe1 Servings", 4, returned.get(0).getServings(), 0);
        assertEquals("getRecipeByIngredientIdList - Recipe1 Prep_Time", 5, returned.get(0).getPrep_time(), 0);
        assertEquals("getRecipeByIngredientIdList - Recipe1 Total_Time", 15, returned.get(0).getTotal_time(), 0);
        assertEquals("getRecipeByIngredientIdList - Recipe1 Favorited", false, returned.get(0).getFavorited());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient1 Units", "cup(s)", returned.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient1 Quantity", 1, returned.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient1 Details", "", returned.get(0).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient1 Units", "cup(s)", returned.get(0).getIngredientList().get(1).getUnit());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient1 Quantity", 3.0, returned.get(0).getIngredientList().get(1).getQuantity(), 0);
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient1 Details", "White Flour", returned.get(0).getIngredientList().get(1).getDetails());
        assertEquals("getRecipeByIngredientIdList - RecipeDirection1 Number", 1, returned.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByIngredientIdList - RecipeDirection1 Text", "TestDirection", returned.get(0).getDirectionsList().get(0).getDirectionText());

        assertEquals("getRecipeByIngredientIdList - Recipe2 Title", "TestRecipe3", returned.get(1).getTitle());
        assertEquals("getRecipeByIngredientIdList - Recipe2 Servings", 2, returned.get(1).getServings(), 0);
        assertEquals("getRecipeByIngredientIdList - Recipe2 Prep_Time", 10, returned.get(1).getPrep_time(), 0);
        assertEquals("getRecipeByIngredientIdList - Recipe2 Total_Time", 20, returned.get(1).getTotal_time(), 0);
        assertEquals("getRecipeByIngredientIdList - Recipe2 Favorited", false, returned.get(1).getFavorited());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient2 Units", "cup(s)", returned.get(1).getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient2 Quantity", 1, returned.get(1).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient2 Details", "", returned.get(1).getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient2 Units", "cup(s)", returned.get(1).getIngredientList().get(1).getUnit());
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient2 Quantity", 3.0, returned.get(1).getIngredientList().get(1).getQuantity(), 0);
        assertEquals("getRecipeByIngredientIdList - RecipeIngredient2 Details", "White Flour", returned.get(1).getIngredientList().get(1).getDetails());
        assertEquals("getRecipeByIngredientIdList - RecipeDirection2 Number", 1, returned.get(1).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByIngredientIdList - RecipeDirection2 Text", "Direction", returned.get(1).getDirectionsList().get(0).getDirectionText());

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
     * This method checks that getRecipeByCategoryId returns null if there is no Category with the
     * corresponding ID. This test is needed for branch coverage
     */
    @Test
    public void getRecipeByCategoryId_ReturnsNull() {
        setUp();
        ArrayList<Recipe> returned = testDatabase.getRecipeByCategoryId(-1);
        assertEquals("getRecipeByCategoryId returns null", null, returned);
        tearDown();
    }

    /**
     * This method checks that getRecipeByCategoryId returns an ArrayList of Recipes
     * when passed a category that exists in at least one recipe
     */
    @Test
    public void getRecipeByCategoryId_NoErrors() {
        setUp();
        ArrayList<Recipe> returned = testDatabase.getRecipeByCategoryId(recipeCategory.getCategoryID());
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
        Recipe retrieved = testDatabase.getRecipe(returned.get(0).getKeyID());
        assertEquals("getRecipeByCategoryId - Correct Recipe Title", recipeTitle, retrieved.getTitle());
        assertEquals("getRecipeByCategoryId - Correct Recipe Servings", 1, retrieved.getServings(), 0);
        assertEquals("getRecipeByCategoryId - Correct Recipe Prep_Time", 30, retrieved.getPrep_time(), 0);
        assertEquals("getRecipeByCategoryId - Correct Recipe Total_Time", 60, retrieved.getTotal_time(), 0);
        assertEquals("getRecipeByCategoryId - Correct Recipe Favorited", false, retrieved.getFavorited());
        assertEquals("getRecipeByCategoryId - Correct RecipeIngredient Units", "cup(s)", retrieved.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByCategoryId - Correct RecipeIngredient Quantity", 2.0, retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByCategoryId - Correct RecipeIngredient Details", "White Flour", retrieved.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByCategoryId - Correct RecipeDirection1 Number", 1, retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByCategoryId - Correct RecipeDirection1 Text", "TestDirection1", retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByCategoryId - Correct RecipeDirection2 Number", 2, retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByCategoryId - Correct RecipeDirection2 Text", "TestDirection2", retrieved.getDirectionsList().get(1).getDirectionText());
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
        assertEquals("getRecipeByPrepTime with time that doesn't exist", null, testDatabase.getRecipeByPrepTime(0));
        tearDown();
    }

    /**
     * This method checks that getRecipeByPrepTime returns an ArrayList of Recipes
     * when passed a category that exists in at least one recipe
     */
    @Test
    public void getRecipeByPrepTime_NoErrors() {
        setUp();
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
        ArrayList<Recipe> returned = testDatabase.getRecipeByPrepTime(30);
        Recipe retrieved = testDatabase.getRecipe(returned.get(0).getKeyID());
        assertEquals("getRecipeByPrepTime returns one recipe", 1, returned.size());

        assertEquals("getRecipeByPrepTime - Correct Recipe Title", "TestRecipe", retrieved.getTitle());
        assertEquals("getRecipeByPrepTime - Correct Recipe Servings", 1, retrieved.getServings(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Prep_Time", 30, retrieved.getPrep_time(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Total_Time", 60, retrieved.getTotal_time(), 0);
        assertEquals("getRecipeByPrepTime - Correct Recipe Favorited", false, retrieved.getFavorited());
        assertEquals("getRecipeByPrepTime - Correct RecipeIngredient Units", "cup(s)", retrieved.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByPrepTime - Correct RecipeIngredient Quantity", 2.0, retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByPrepTime - Correct RecipeIngredient Details", "White Flour", retrieved.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByPrepTime - Correct RecipeDirection1 Number", 1, retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Correct RecipeDirection1 Text", "TestDirection1", retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByPrepTime - Correct RecipeDirection2 Number", 2, retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Correct RecipeDirection2 Text", "TestDirection2", retrieved.getDirectionsList().get(1).getDirectionText());
        tearDown();

    }

    /**
     * This method checks that getRecipeByPrepTime returns the correct recipes when there's multiple matches
     */
    @Test
    public void getRecipeByPrepTime_MultipleRecipes() {
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
        Recipe retrieved1 = testDatabase.getRecipe(returned.get(0).getKeyID());
        Recipe retrieved2 = testDatabase.getRecipe(returned.get(1).getKeyID());

        // Check first Recipe
        assertEquals("getRecipeByPrepTime - Multiple: Recipe1 Title", recipeTitle, retrieved1.getTitle());
        assertEquals("getRecipeByPrepTime - Multiple: Recipe1 Servings", 1, retrieved1.getServings(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe1 Prep_Time", 30, retrieved1.getPrep_time(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe1 Total_Time", 60, retrieved1.getTotal_time(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe1 Favorited", false, retrieved1.getFavorited());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient1 Units", "cup(s)", retrieved1.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient1 Quantity", 2.0, retrieved1.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient1 Details", "White Flour", retrieved1.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection1.1 Number", 1, retrieved1.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection1.1 Text", "TestDirection1", retrieved1.getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection1.2 Number", 2, retrieved1.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection1.2 Text", "TestDirection2", retrieved1.getDirectionsList().get(1).getDirectionText());

        // Check second Recipe
        assertEquals("getRecipeByPrepTime - Multiple: Recipe2 Title", "TestRecipe2", retrieved2.getTitle());
        assertEquals("getRecipeByPrepTime - Multiple: Recipe2 Servings", 4, retrieved2.getServings(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe2 Prep_Time", 30, retrieved2.getPrep_time(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe2 Total_Time", 45, retrieved2.getTotal_time(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: Recipe2 Favorited", false, retrieved2.getFavorited());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient2 Units", "cup(s)", retrieved2.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient2 Quantity", 1, retrieved2.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByPrepTime - Multiple: RecipeIngredient2 Details", "", retrieved2.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection2 Number", 1, retrieved2.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByPrepTime - Multiple: RecipeDirection2 Text", "TestDirection", retrieved2.getDirectionsList().get(0).getDirectionText());

        tearDown();
    }

    /**
     * This method attempts to get recipes by a total_time that is not in any recipe
     * Check this case returns null
     */
    @Test
    public void getRecipeByTotalTime_NoRecipes() {
        setUp();
        assertEquals("getRecipeByTotalTime with time that doesn't exist", null, testDatabase.getRecipeByTotalTime(0));
        tearDown();
    }

    /**
     * This method checks that getRecipeByTotalTime returns an ArrayList of Recipes
     * when passed a total_time that exists in at least one recipe
     */
    @Test
    public void getRecipeByTotalTime_NoErrors() {
        setUp();
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
        ArrayList<Recipe> returned = testDatabase.getRecipeByTotalTime(60);
        Recipe retrieved = testDatabase.getRecipe(returned.get(0).getKeyID());
        assertEquals("getRecipeByTotalTime returns one recipe", 1, returned.size());
        assertEquals("getRecipeByTotalTime - Correct Recipe Title", recipeTitle, retrieved.getTitle());
        assertEquals("getRecipeByTotalTime - Correct Recipe Servings", 1, retrieved.getServings(), 0);
        assertEquals("getRecipeByTotalTime - Correct Recipe Prep_Time", 30, retrieved.getPrep_time(), 0);
        assertEquals("getRecipeByTotalTime - Correct Recipe Total_Time", 60, retrieved.getTotal_time(), 0);
        assertEquals("getRecipeByTotalTime - Correct Recipe Favorited", false, retrieved.getFavorited());
        assertEquals("getRecipeByTotalTime - Correct RecipeIngredient Units", "cup(s)", retrieved.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByTotalTime - Correct RecipeIngredient Quantity", 2.0, retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByTotalTime - Correct RecipeIngredient Details", "White Flour", retrieved.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByTotalTime - Correct RecipeDirection1 Number", 1, retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByTotalTime - Correct RecipeDirection1 Text", "TestDirection1", retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByTotalTime - Correct RecipeDirection2 Number", 2, retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByTotalTime - Correct RecipeDirection2 Text", "TestDirection2", retrieved.getDirectionsList().get(1).getDirectionText());
        tearDown();

    }

    /**
     * This method checks that getRecipeByTotalTime returns the correct recipes when there's multiple matches
     */
    @Test
    public void getRecipeByTotalTime_MultipleRecipes() {
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
        Recipe retrieved1 = testDatabase.getRecipe(returned.get(0).getKeyID());
        Recipe retrieved2 = testDatabase.getRecipe(returned.get(1).getKeyID());
        assertEquals("getRecipeByTotalTime - Returns Multiple Recipes", 2, returned.size());

        // Check first Recipe
        assertEquals("getRecipeByTotalTime - Multiple: Recipe1 Title", recipeTitle, retrieved1.getTitle());
        assertEquals("getRecipeByTotalTime - Multiple: Recipe1 Servings", 1, retrieved1.getServings(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe1 Prep_Time", 30, retrieved1.getPrep_time(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe1 Total_Time", 60, retrieved1.getTotal_time(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe1 Favorited", false, retrieved1.getFavorited());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient1 Units", "cup(s)", retrieved1.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient1 Quantity", 2.0, retrieved1.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient1 Details", "White Flour", retrieved1.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection1.1 Number", 1, retrieved1.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection1.1 Text", "TestDirection1", retrieved1.getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection1.2 Number", 2, retrieved1.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection1.2 Text", "TestDirection2", retrieved1.getDirectionsList().get(1).getDirectionText());

        // Check second Recipe
        assertEquals("getRecipeByTotalTime - Multiple: Recipe2 Title", "TestRecipe2", retrieved2.getTitle());
        assertEquals("getRecipeByTotalTime - Multiple: Recipe2 Servings", 4, retrieved2.getServings(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe2 Prep_Time", 15, retrieved2.getPrep_time(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe2 Total_Time", 60, retrieved2.getTotal_time(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: Recipe2 Favorited", false, retrieved2.getFavorited());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient2 Units", "cup(s)", retrieved2.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient2 Quantity", 1, retrieved2.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByTotalTime - Multiple: RecipeIngredient2 Details", "", retrieved2.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection2 Number", 1, retrieved2.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByTotalTime - Multiple: RecipeDirection2 Text", "TestDirection", retrieved2.getDirectionsList().get(0).getDirectionText());
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
     * This method checks that addRecipeToCart returns true upon success
     */
    @Test
    public void addRecipeToCart_ReturnsTrue() {
        setUp();
        assertEquals("addRecipeToCart - Returns True", true, testDatabase.addRecipeToCart(recipeID));
        tearDown();
    }

    /**
     * This method checks that addRecipeToCart returns false if the recipe doesn't exist
     */
    @Test
    public void addRecipeToCart_ReturnsFalse() {
        setUp();
        assertEquals("addRecipeToCart - Returns False", false, testDatabase.addRecipeToCart(Integer.MAX_VALUE));
        tearDown();
    }

    /**
     * This method checks that addRecipeToCart updates the shopping cart correctly
     */
    @Test
    public void addRecipeToCart_CorrectInformation() {
        setUp();
        testDatabase.addRecipeToCart(recipeID);
        ArrayList<RecipeIngredient> shoppingCart = testDatabase.getAllShoppingCartIngredients();
        assertEquals("addRecipeToCart - Adds one ingredient", 1, shoppingCart.size());
        assertEquals("addRecipeToCart - Correct ingredientID", ingredientID, shoppingCart.get(0).getIngredientID());
        assertEquals("addRecipeToCart - Correct Quantity", 2, shoppingCart.get(0).getQuantity(), 0);
        assertEquals("addRecipeToCart - Correct Unit", "cup(s)", shoppingCart.get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that addRecipeToCart updates the shopping cart correctly
     */
    @Test
    public void addRecipeToCart_NoDuplicateIngredients() {
        setUp();
        testDatabase.addRecipeToCart(recipeID);
        testDatabase.addRecipeToCart(recipeID);
        ArrayList<RecipeIngredient> shoppingCart = testDatabase.getAllShoppingCartIngredients();
        assertEquals("addRecipeToCart - Only one ingredient", 1, shoppingCart.size());
        assertEquals("addRecipeToCart - Correct ingredientID", ingredientID, shoppingCart.get(0).getIngredientID());
        assertEquals("addRecipeToCart - Correct Quantity", 4, shoppingCart.get(0).getQuantity(), 0);
        assertEquals("addRecipeToCart - Correct Unit", "cup(s)", shoppingCart.get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that getShoppingCartIngredient returns null if the ingredient doesn't exist in the
     * shopping cart
     */
    @Test
    public void getShoppingCartIngredient_ReturnsNull() {
        setUp();
        assertEquals("getShoppingCartIngredient - Returns Null", null, testDatabase.getShoppingCartIngredient(Integer.MAX_VALUE));
        tearDown();
    }

    /**
     * This method checks that getShoppingCartIngredient returns a non-null ingredient when getting an
     * ingredient that already existed in the shopping cart
     */
    @Test
    public void getShoppingCartIngredient_ReturnsIngredient() {
        setUp();
        testDatabase.addRecipeToCart(recipeID);
        RecipeIngredient returned = testDatabase.getShoppingCartIngredient(recipeIngredient.getIngredientID());
        assertNotEquals("getShoppingCartIngredient - Returns Ingredient", null, returned);
        tearDown();
    }

    /**
     * This method checks that getShoppingCartIngredient returns correct ingredient information
     * if the ingredient was previously added to the shopping cart
     */
    @Test
    public void getShoppingCartIngredient_CorrectInfo() {
        setUp();
        testDatabase.addRecipeToCart(recipeID);
        RecipeIngredient returned = testDatabase.getShoppingCartIngredient(recipeIngredient.getIngredientID());
        assertEquals("getShoppingCartIngredient - Correct ingredientID", ingredientID, returned.getIngredientID());
        assertEquals("getShoppingCartIngredient - Correct Quantity", 2, returned.getQuantity(), 0);
        assertEquals("getShoppingCartIngredient - Correct Unit", "cup(s)", returned.getUnit());
        tearDown();
    }

    /**
     * This method checks that getAllShoppingCartIngredients returns null when the shopping cart is empty
     */
    @Test
    public void getAllShoppingCartIngredients_ReturnsNull() {
        setUp();
        testDatabase.deleteAllShoppingCartIngredients();
        ArrayList<RecipeIngredient> shoppingCart = testDatabase.getAllShoppingCartIngredients();
        assertEquals("getAllShoppingCartIngredients - Returns Null", null, shoppingCart);
        tearDown();
    }

    /**
     * This method checks that getAllShoppingCartIngredients returns correct information
     * if there's only one instance of an ingredient that was added to the shopping cart
     */
    @Test
    public void getAllShoppingCartIngredients_OneInstanceCorrect() {
        setUp();
        testDatabase.addRecipeToCart(recipeID);
        ArrayList<RecipeIngredient> newShoppingCart = testDatabase.getAllShoppingCartIngredients();
        assertEquals("getAllShoppingCartIngredients - Correct ingredientID", ingredientID, newShoppingCart.get(newShoppingCart.size()-1).getIngredientID());
        assertEquals("getAllShoppingCartIngredients - Correct Quantity", 2, newShoppingCart.get(newShoppingCart.size()-1).getQuantity(), 0);
        assertEquals("getAllShoppingCartIngredients - Correct Unit", "cup(s)", newShoppingCart.get(newShoppingCart.size()-1).getUnit());
        tearDown();
    }

    /**
     * This method checks that getAllShoppingCartIngredients returns correct information
     * if there were multiple instances of an ingredient that were added to the shopping cart
     */
    @Test
    public void getAllShoppingCartIngredients_MultipleInstancesCorrect() {
        setUp();
        // Add a second recipe
        createRecipe_TwoIngredients();
        testDatabase.addRecipeToCart(recipeID);
        testDatabase.addRecipeToCart(recipeID2);
        ArrayList<RecipeIngredient> shopCart = testDatabase.getAllShoppingCartIngredients();
        assertEquals("getAllShoppingCartIngredients - Correct Ingredient1 ID", ingredientID, shopCart.get(0).getIngredientID());
        assertEquals("getAllShoppingCartIngredients - Correct Ingredient1 Quantity",2, shopCart.get(0).getQuantity(),0);
        assertEquals("getAllShoppingCartIngredients - Correct Ingredient1 Unit","cup(s)", shopCart.get(0).getUnit());
        assertEquals("getAllShoppingCartIngredients - Correct Ingredient2 ID", ingredientIDM2, shopCart.get(1).getIngredientID());
        assertEquals("getAllShoppingCartIngredients - Correct Ingredient2 Quantity",1, shopCart.get(1).getQuantity(),0);
        assertEquals("getAllShoppingCartIngredients - Correct Ingredient2 Unit","cup(s)", shopCart.get(1).getUnit());
        assertEquals("getAllShoppingCartIngredients - Correct Ingredient3 ID",ingredientIDM3, shopCart.get(2).getIngredientID());
        assertEquals("getAllShoppingCartIngredients - Correct Ingredient3 Quantity",3, shopCart.get(2).getQuantity(),0);
        assertEquals("getAllShoppingCartIngredients - Correct Ingredient3 Unit","cup(s)", shopCart.get(2).getUnit());

        tearDown();
    }

    /**
     * This method checks that updateShoppingCartIngredient returns true upon success
     */
    @Test
    public void updateShoppingCartIngredient_ReturnsTrue() {
        setUp();
        testDatabase.addRecipeToCart(recipeID);
        recipeIngredient.setQuantity(6);
        recipeIngredient.setUnit("tablespoon(s)");
        assertEquals("updateShoppingCart - Returns True",true, testDatabase.updateShoppingCartIngredient(recipeIngredient));
        tearDown();
    }

    /**
     * This method checks that updateShoppingCartIngredient returns false if the ingredient does not exist in the
     * shopping cart
     */
    @Test
    public void updateShoppingCartIngredient_ReturnsFalse() {
        setUp();
        RecipeIngredient ri = new RecipeIngredient();
        assertEquals("updateShoppingCart - Returns False",false, testDatabase.updateShoppingCartIngredient(ri));
        tearDown();
    }

    /**
     * This method checks that updateShoppingCartIngredient does not change the cart if the ingredient does not change
     */
    @Test
    public void updateShoppingCartIngredient_NoChange() {
        setUp();
        testDatabase.addRecipeToCart(recipeID);
        testDatabase.updateShoppingCartIngredient(recipeIngredient);
        RecipeIngredient returned = testDatabase.getShoppingCartIngredient(recipeIngredient.getIngredientID());
        assertEquals("updateShoppingCartIngredient - No Change ID", recipeIngredient.getIngredientID(), returned.getIngredientID());
        assertEquals("updateShoppingCartIngredient - No Change Quantity", 2, returned.getQuantity(), 0);
        assertEquals("updateShoppingCartIngredient - No Change Unit", "cup(s)", returned.getUnit());
        tearDown();
    }

    /**
     * This method checks that updateShoppingCartIngredient actually updates the ingredient in the
     * shopping cart
     */
    @Test
    public void updateShoppingCartIngredient_Updates() {
        setUp();
        testDatabase.addRecipeToCart(recipeID);
        recipeIngredient.setQuantity(6);
        recipeIngredient.setUnit("tablespoon(s)");
        testDatabase.updateShoppingCartIngredient(recipeIngredient);
        RecipeIngredient returned = testDatabase.getShoppingCartIngredient(recipeIngredient.getIngredientID());
        assertEquals("updateShoppingCartIngredient - Updates, Doesn't Change ID", recipeIngredient.getIngredientID(), returned.getIngredientID());
        assertEquals("updateShoppingCartIngredient - Updates Quantity", 6, returned.getQuantity(), 0);
        assertEquals("updateShoppingCartIngredient - Updates Unit", "tablespoon(s)", returned.getUnit());
        tearDown();
    }

    /**
     * This method checks that deleteShoppingCartIngredient returns true upon success
     */
    @Test
    public void deleteShoppingCartIngredient_ReturnsTrue() {
        setUp();
        assertEquals("deleteShoppingCartIngredient - Returns true", true, testDatabase.deleteShoppingCartIngredient(ingredientID));
        tearDown();
    }

    /**
     * This method checks that deleteShoppingCartIngredient returns false upon failure
     */
    @Test
    public void deleteShoppingCartIngredient_ReturnsFalse() {
        setUp();
        assertEquals("deleteShoppingCartIngredient - Returns false", false, testDatabase.deleteShoppingCartIngredient(Integer.MAX_VALUE));
        tearDown();
    }

    /**
     * This method checks that deleteShoppingCartIngredient actually deletes the ingredient from the
     * shopping cart
     */
    @Test
    public void deleteShoppingCartIngredient_Deletes() {
        setUp();
        testDatabase.addRecipeToCart(recipeID);
        testDatabase.deleteShoppingCartIngredient(ingredientID);
        assertEquals("deleteShoppingCartIngredient - Deletes Ingredient", null, testDatabase.getAllShoppingCartIngredients());
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
    public void convertRecipeIngredientUnits_RecipeNotChanged() {
        setUp();
        createRecipe_DifferentUnits("teaspoon(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "teaspoon(s)", "tablespoon(s)");
        Recipe updated = testDatabase.getRecipe(testRecipe2.getKeyID());
        assertEquals("convertRecipeIngredientUnits - Correct Recipe Title", "TestRecipe2", updated.getTitle());
        assertEquals("convertRecipeIngredientUnits - Correct Recipe Servings", 4, updated.getServings(), 0);
        assertEquals("convertRecipeIngredientUnits - Correct Recipe Prep_Time", 5, updated.getPrep_time(), 0);
        assertEquals("convertRecipeIngredientUnits - Correct Recipe Total_Time", 15, updated.getTotal_time(), 0);
        assertEquals("convertRecipeIngredientUnits - Correct Recipe Favorited", false, updated.getFavorited());
        assertEquals("convertRecipeIngredientUnits - Correct RecipeIngredient Units", "teaspoon(s)", updated.getIngredientList().get(0).getUnit());
        assertEquals("convertRecipeIngredientUnits - Correct RecipeIngredient Quantity", 6, updated.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits - Correct RecipeIngredient Details", "", updated.getIngredientList().get(0).getDetails());
        assertEquals("convertRecipeIngredientUnits - Correct RecipeDirection Number", 1, updated.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("convertRecipeIngredientUnits - Correct RecipeDirection Text", "TestDirection", updated.getDirectionsList().get(0).getDirectionText());
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
        createRecipe_DifferentUnits("pound(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "pound(s)", "pinch(es)");
        assertEquals("convertRecipeIngredientUnits_PintToCups - Quantity", 8834.56, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_PintToCups - Unit", "pinch(es)", returned.getIngredientList().get(0).getUnit());
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
        createRecipe_DifferentUnits("pinch(es)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "pinch(es)", "pound(s)");
        assertEquals("convertRecipeIngredientUnits_PintToCups - Quantity", 0.00, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_PintToCups - Unit", "pound(s)", returned.getIngredientList().get(0).getUnit());
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

    /**
     * This method checks that scaleRecipe returns null if the Recipe doesn't exist
     */
    @Test
    public void scaleRecipe_ReturnsNull() {
        setUp();
        Recipe recipe = new Recipe();
        assertEquals("scaleRecipe - Returns Null", null, testDatabase.scaleRecipe(recipe, 1));
        tearDown();
    }

    /**
     * This method checks that scaleRecipe returns a non-null Recipe
     */
    @Test
    public void scaleRecipe_ReturnsRecipe() {
        setUp();
        assertNotEquals("scaleRecipe - Returns a Recipe", null, testDatabase.scaleRecipe(testRecipe, 2));
        tearDown();
    }

    /**
     * This method checks that scaleRecipe returns a Recipe with the updated servings and ingredient quantities
     * This method is specifically to check the case where the Recipe only has one ingredient
     * The method also checks that no other part of the Recipe was changed
     */
    @Test
    public void scaleRecipe_CorrectW1Ingredient() {
        setUp();
        Recipe scaled = testDatabase.scaleRecipe(testRecipe, 2);
        assertEquals("scaleRecipe - Recipe Title", recipeTitle, scaled.getTitle());
        assertEquals("scaleRecipe - Recipe Servings", 2, scaled.getServings(), 0);
        assertEquals("scaleRecipe - Recipe Prep_Time", 30, scaled.getPrep_time(), 0);
        assertEquals("scaleRecipe - Recipe Total_Time", 60, scaled.getTotal_time(), 0);
        assertEquals("scaleRecipe - Recipe Favorited", false, scaled.getFavorited());
        assertEquals("scaleRecipe - RecipeIngredient Units", "cup(s)", scaled.getIngredientList().get(0).getUnit());
        assertEquals("scaleRecipe - RecipeIngredient Quantity", 4.0, scaled.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("scaleRecipe - RecipeIngredient Details", "White Flour", scaled.getIngredientList().get(0).getDetails());
        assertEquals("scaleRecipe - RecipeDirection1 Number", 1, scaled.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("scaleRecipe - RecipeDirection1 Text", "TestDirection1", scaled.getDirectionsList().get(0).getDirectionText());
        assertEquals("scaleRecipe - RecipeDirection2 Number", 2, scaled.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("scaleRecipe - RecipeDirection2 Text", "TestDirection2", scaled.getDirectionsList().get(1).getDirectionText());
        tearDown();
    }

    /**
     * This method checks that scaleRecipe returns a Recipe with the updated servings and ingredient quantities
     * This method is specifically to check the case where the Recipe only has multiple ingredients
     * The method also checks that no other part of the Recipe was changed
     */
    @Test
    public void scaleRecipe_CorrectWMultipleIngredients() {
        setUp();
        createRecipe_TwoIngredients();
        Recipe scaled = testDatabase.scaleRecipe(testRecipe2, 2);

        assertEquals("scaleRecipe - Recipe Title", "TestRecipe2", scaled.getTitle());
        assertEquals("scaleRecipe - Recipe Servings", 2, scaled.getServings(), 0);
        assertEquals("scaleRecipe - Recipe Prep_Time", 5, scaled.getPrep_time(), 0);
        assertEquals("scaleRecipe - Recipe Total_Time", 15, scaled.getTotal_time(), 0);
        assertEquals("scaleRecipe - Recipe Favorited", false, scaled.getFavorited());
        assertEquals("scaleRecipe - RecipeIngredient1 Units", "cup(s)", scaled.getIngredientList().get(0).getUnit());
        assertEquals("scaleRecipe - RecipeIngredient1 Quantity", 0.5, scaled.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("scaleRecipe - RecipeIngredient1 Details", "", scaled.getIngredientList().get(0).getDetails());
        assertEquals("scaleRecipe - RecipeIngredient2 Units", "cup(s)", scaled.getIngredientList().get(1).getUnit());
        assertEquals("scaleRecipe - RecipeIngredient2 Quantity", 1.5, scaled.getIngredientList().get(1).getQuantity(), 0);
        assertEquals("scaleRecipe - RecipeIngredient2 Details", "White Flour", scaled.getIngredientList().get(1).getDetails());
        assertEquals("scaleRecipe - RecipeDirection Number", 1, scaled.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("scaleRecipe - RecipeDirection Text", "TestDirection", scaled.getDirectionsList().get(0).getDirectionText());
        tearDown();
    }


    /**
     * This method checks that deleteAllShoppingCartIngredients works
     */
    @Test
    public void deleteAllShoppingCartIngredients_Deletes() {
        setUp();
        testDatabase.deleteAllShoppingCartIngredients();
        assertEquals("deleteAllShoppingCartIngredients - Deletes every ingredient", null, testDatabase.getAllShoppingCartIngredients());
        tearDown();
    }

    /**
     * This method checks tht getAllRecipesByTitle returns null if passed a string that doesn't exist
     * in any recipes
     */
    @Test
    public void getAllRecipesByTitle_ReturnsNull(){
        setUp();
        assertEquals(null, testDatabase.getAllRecipesByTitle("Fail"));
        tearDown();
    }

    /**
     * This method checks tht getAllRecipesByTitle returns a list if passed a string that exists in at least one recipe
     */
    @Test
    public void getAllRecipesByTitle_ReturnsList(){
        setUp();
        ArrayList<Recipe> recipes = testDatabase.getAllRecipesByTitle(recipeTitle);
        assertNotEquals(null, recipes);
        tearDown();
    }

    /**
     * This method checks tht getAllRecipesByTitle returns the correct recipes
     * when passed a string that exists in at least one recipe
     */
    @Test
    public void getAllRecipesByTitle_CorrectInformation(){
        tearDown();
        setUp();
        createRecipe_TwoIngredients();
        ArrayList<Recipe> recipes = testDatabase.getAllRecipesByTitle("TestRecipe");
        assertEquals("getAllRecipesByTitle - Number of Recipes",2, recipes.size());
        assertEquals("getAllRecipesByTitle - Recipe 1 Title", recipeTitle, recipes.get(0).getTitle());
        assertEquals("getAllRecipesByTitle - Recipe 2 Title", "TestRecipe2", recipes.get(1).getTitle());
        tearDown();
    }

    /**
     * This method checks that getAllIngredients returns null if no ingredients exist in the database
     */
    @Test
    public void getAllIngredients_ReturnsNull(){
        tearDown();
        assertEquals("getAllIngredients returns null", null, testDatabase.getAllIngredients());
    }

    /**
     * This method checks that getAllIngredients returns null if ingredients exist in the database
     */
    @Test
    public void getAllIngredients_ReturnsList(){
        setUp();
        assertNotEquals("getAllIngredients returns list", null, testDatabase.getAllIngredients());
        tearDown();
    }

    /**
     * This method checks that getAllIngredients returns the correct information for ingredients that
     * are in the database
     */
    @Test
    public void getAllIngredients_CorrectInfo(){
        setUp();
        createRecipe_TwoIngredients();
        ArrayList<Ingredient> allIngredients = testDatabase.getAllIngredients();
        assertEquals("getAllIngredients - Number of Ingredients",3, allIngredients.size());
        assertEquals("getAllIngredients - Ingredient 1", "Flour", allIngredients.get(0).getName());
        assertEquals("getAllIngredients - Ingredient 2", "Sugar", allIngredients.get(1).getName());
        assertEquals("getAllIngredients - Ingredient 3", "Oil", allIngredients.get(2).getName());
        tearDown();
    }

    // Tests to increase branch coverage:

    /**
     * This method checks that getRecipe(RecipeID) returns all the correct information of a previously existing Recipe
     * that was favorited
     *
     * This test was added to increase branch coverage
     */
    @Test
    public void getRecipeByID_Favorited(){
        setUp();
        testRecipe.setFavorited(true);
        int returned = testDatabase.addRecipe(testRecipe);
        Recipe retrieved = testDatabase.getRecipe(returned);
        assertEquals("getRecipeByID - Correct Title", recipeTitle, retrieved.getTitle());
        assertEquals("getRecipeByID - Correct Servings", 1, retrieved.getServings(), 0);
        assertEquals("getRecipeByID - Correct Prep Time", 30, retrieved.getPrep_time(), 0);
        assertEquals("getRecipeByID - Correct Total Time", 60, retrieved.getTotal_time(), 0);
        assertEquals("getRecipeByID - Correct Favorited", true, retrieved.getFavorited());
        assertEquals("getRecipeByID - Ingredient Unit", "cup(s)", retrieved.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByID - Ingredient Quantity", 2.0, retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByID - Ingredient Details", "White Flour", retrieved.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByID - First Direction Number", 1, retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByID - First Direction Text", "TestDirection1", retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByID - Second Direction Number", 2, retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByID - Second Direction Text", "TestDirection2", retrieved.getDirectionsList().get(1).getDirectionText());
        tearDown();
    }

    /**
     * This method tests that editRecipe will return false if category is the reason that it fails
     *
     * This test was added to increase branch coverage
     */
    @Test
    public void editRecipe_CategoryCausedFalse(){
        setUp();
        int returned = testDatabase.addRecipe(testRecipe);
        recipeCategory = new RecipeCategory(-1, -1, -1);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();

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
        boolean edited = testDatabase.editRecipe(testRecipe);
        assertEquals("editRecipe - Returns False BC Category",false, edited);
        tearDown();
    }

    /**
     * This method tests that editRecipe will return false if ingredient is the reason that it fails
     *
     * This test was added to increase branch coverage
     */
    @Test
    public void editRecipe_IngredientCausedFalse(){
        setUp();
        int returned = testDatabase.addRecipe(testRecipe);
        category = new Category(-1, "Lunch");
        categoryID = testDatabase.addCategory(category);
        recipeCategory = new RecipeCategory(-1, -1, categoryID);
        List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
        listOfCategories.add(recipeCategory);

        recipeIngredient = new RecipeIngredient(-1, -1, -1, 2.0, "cup(s)", "White Flour");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();

        RecipeDirection recipeDirection1 = new RecipeDirection(-1, -1, "TestDirection1", 1);
        RecipeDirection recipeDirection2 = new RecipeDirection(-1, -1, "TestDirection2", 2);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection1);
        listOfDirections.add(recipeDirection2);
        testRecipe = new Recipe(recipeTitle, 1.0, 30, 60, false, listOfIngredients, listOfDirections, listOfCategories);
        boolean edited = testDatabase.editRecipe(testRecipe);
        assertEquals("editRecipe - Returns False BC Ingredient",false, edited);
        tearDown();
    }

    /**
     * This method tests that editRecipe will return false if direction is the reason that it fails
     *
     * This test was added to increase branch coverage
     */
    @Test
    public void editRecipe_DirectionsCausedFalse(){
        setUp();
        int returned = testDatabase.addRecipe(testRecipe);
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

        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        testRecipe = new Recipe(recipeTitle, 1.0, 30, 60, false, listOfIngredients, listOfDirections, listOfCategories);
        boolean edited = testDatabase.editRecipe(testRecipe);
        assertEquals("editRecipe - Returns False BC Direction", false, edited);
        tearDown();
    }

    /**
     * This method attempts to get recipes by an ingredient list when the first ingredient ID passed in
     * is not in any recipes. Expect null
     */
    @Test
    public void getRecipeByIngredientIdList_OtherNullCase() {
        setUp();
        Ingredient i = new Ingredient(-1, "Egg");
        int iID = testDatabase.addIngredient(i);
        int[] ingredientIDs = {iID, ingredientID};
        ArrayList<Recipe> returned = testDatabase.getRecipeByIngredientIdList(ingredientIDs);
        assertEquals("getRecipeByIngredientIdList returns null", null, returned);
        tearDown();
    }

    /**
     * This method tests that onCreate creates a non-null database
     *
     * This test was added to increase statement coverage
     */
    @Test
    public void onCreate_Database(){
        DatabaseHelper nonNull = new DatabaseHelper(appContext.getApplicationContext());
        assertNotEquals("onCreate returns a nonNull database", null, nonNull);
    }

    // Helper Methods:

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
        ingredientIDM2 = testDatabase.addIngredient(ingredient2);
        Ingredient ingredient3 = new Ingredient(-1, "Oil");
        ingredientIDM3 = testDatabase.addIngredient(ingredient3);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientIDM2, 1, "cup(s)", "");
        RecipeIngredient recipeIngredient3 = new RecipeIngredient(-1, -1, ingredientIDM3, 3.0, "cup(s)", "White Flour");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient2);
        listOfIngredients.add(recipeIngredient3);

        RecipeDirection recipeDirection = new RecipeDirection(-1, -1, "TestDirection", 1);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection);

        testRecipe2 = new Recipe("TestRecipe2", 4, 5, 15, false, listOfIngredients, listOfDirections, listOfCategories);
        recipeID2 = testDatabase.addRecipe(testRecipe2);
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
        ingredientID2 = testDatabase.addIngredient(ingredient2);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(-1, -1, ingredientID2, 6.0, units, "");
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient2);

        RecipeDirection recipeDirection = new RecipeDirection(-1, -1, "TestDirection", 1);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection);

        testRecipe2 = new Recipe("TestRecipe2", 4, 5, 15, false, listOfIngredients, listOfDirections, listOfCategories);
        recipeID2 = testDatabase.addRecipe(testRecipe2);
    }
}