package com.example.kitchen;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class It3_DatabaseTester {
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
     *
     */
    @Test
    public void loginCheck_ReturnsTrue(){
        //TODO: IMPLEMENT
    }

    /**
     *
     */
    @Test
    public void loginCheck_ReturnsFalse(){
        //TODO: IMPLEMENT
    }

    /**
     *
     */
    @Test
    public void editUser_ReturnsTrue(){
        //TODO: IMPLEMENT
    }

    /**
     *
     */
    @Test
    public void editUser_ReturnsFalse(){
        //TODO: IMPLEMENT
    }

    /**
     *
     */
    @Test
    public void editUser_Updates(){
        //TODO: IMPLEMENT
    }

    /**
     *
     */
    @Test
    public void editUser_DoesNotChange(){
        //TODO: IMPLEMENT
    }

    /**
     *
     */
    @Test
    public void chooseRandom_ReturnsRecipes(){
        //TODO: IMPLEMENT
        ArrayList<Recipe> randoms = testDatabase.getRandomRecipes();
    }

    /**
     * This method checks that getRandomRecipes() returns null when there are no recipes in the database
     */
    @Test
    public void chooseRandom_ReturnsNull(){
        assertEquals("getRandomRecipes - Returns Null", null, testDatabase.getRandomRecipes());
    }

    /**
     * This method tests that getAllRecipesSorted() returns an ArrayList of recipes if there are
     * recipes in the database
     */
    @Test
    public void sortedRecipes_ReturnsRecipeList(){
        setUp();
        ArrayList<Recipe> sorted = testDatabase.getAllRecipesSorted();
        assertNotEquals("getAllRecipesSorted - Returns a Recipe List", null, sorted);
        tearDown();
    }

    /**
     * This method tests that getAllRecipesSorted() returns null if there are no recipes in the database
     */
    @Test
    public void sortedRecipes_ReturnsNull(){
        assertEquals("getAllRecipesSorted - Returns Null", null, testDatabase.getAllRecipesSorted());
    }

    /**
     * This method tests that getAllRecipesSorted() returns a correctly sorted array list of recipes
     * that are sorted in alphabetical order by title
     */
    @Test
    public void sortedRecipes_CorrectlySorts(){
        setUp();
        createRecipe_TwoIngredients();
        ArrayList<Recipe> sorted = testDatabase.getAllRecipesSorted();
        assertEquals("getAllRecipesSorted - Number of Recipes", 2 , sorted.size());
        assertEquals("getAllRecipesSorted - 1st Recipe Title", recipeTitle, sorted.get(0).getTitle());
        assertEquals("getAllRecipesSorted - 1st Recipe Servings", 1, sorted.get(0).getServings(), 0);
        assertEquals("getAllRecipesSorted - 1st Recipe Prep_Time", 30, sorted.get(0).getPrep_time(), 0);
        assertEquals("getAllRecipesSorted - 1st Recipe Total_Time", 60, sorted.get(0).getTotal_time(), 0);
        assertEquals("getAllRecipesSorted - 1st Recipe Favorited", false, sorted.get(0).getFavorited());
        assertEquals("getAllRecipesSorted - 1st RecipeIngredient Units", "cup(s)", sorted.get(0).getIngredientList().get(0).getUnit());
        assertEquals("getAllRecipesSorted - 1st RecipeIngredient Quantity", 2, sorted.get(0).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getAllRecipesSorted - 1st RecipeIngredient Details", "White Flour", sorted.get(0).getIngredientList().get(1).getDetails());
        assertEquals("getAllRecipesSorted - 1st RecipeDirection1 Number", 1, sorted.get(0).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getAllRecipesSorted - 1st RecipeDirection1 Text", "TestDirection1", sorted.get(0).getDirectionsList().get(0).getDirectionText());
        assertEquals("getAllRecipesSorted - 1st RecipeDirection2 Number", 2, sorted.get(0).getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getAllRecipesSorted - 1st RecipeDirection2 Text", "TestDirection2", sorted.get(0).getDirectionsList().get(1).getDirectionText());
        assertEquals("getAllRecipesSorted - 2nd Recipe Title", "TestRecipe2", sorted.get(1).getTitle());
        assertEquals("getAllRecipesSorted - 2nd Recipe Servings", 4, sorted.get(1).getServings(), 0);
        assertEquals("getAllRecipesSorted - 2nd Recipe Prep_Time", 5, sorted.get(1).getPrep_time(), 0);
        assertEquals("getAllRecipesSorted - 2nd Recipe Total_Time", 15, sorted.get(1).getTotal_time(), 0);
        assertEquals("getAllRecipesSorted - 2nd Recipe Favorited", false, sorted.get(1).getFavorited());
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Units", "cup(s)", sorted.get(1).getIngredientList().get(0).getUnit());
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Quantity", 1, sorted.get(1).getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Details", "", sorted.get(1).getIngredientList().get(0).getDetails());
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Units", "cup(s)", sorted.get(1).getIngredientList().get(1).getUnit());
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Quantity", 3.0, sorted.get(1).getIngredientList().get(1).getQuantity(), 0);
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Details", "Olive Oil Preferred", sorted.get(1).getIngredientList().get(1).getDetails());
        assertEquals("getAllRecipesSorted - 2nd RecipeDirection Number", 1, sorted.get(1).getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getAllRecipesSorted - 2nd RecipeDirection Text", "TestDirection", sorted.get(1).getDirectionsList().get(1).getDirectionText());
        tearDown();
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
        RecipeIngredient recipeIngredient3 = new RecipeIngredient(-1, -1, ingredientIDM3, 3.0, "cup(s)", "Olive Oil Preferred");
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
