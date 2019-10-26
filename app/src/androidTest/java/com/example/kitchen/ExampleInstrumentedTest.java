package com.example.kitchen;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    DatabaseHelper testDatabase = new DatabaseHelper(appContext.getApplicationContext());

    // Create recipe to test with
    String recipeTitle = "TestRecipe";
    RecipeCategory recipeCategory = new RecipeCategory();
    List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
    {
        recipeCategory.setName("Lunch");
        listOfCategories.add(recipeCategory);
    }
    RecipeIngredient recipeIngredient = new RecipeIngredient();
    List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
    {
        recipeIngredient.setName("Flour");
        recipeIngredient.setUnit("cups");
        recipeIngredient.setQuantity(2.0);
        recipeIngredient.setDetails("White Flour");
        listOfIngredients.add(recipeIngredient);
    }
    RecipeDirection recipeDirection1 = new RecipeDirection();
    RecipeDirection recipeDirection2 = new RecipeDirection();
    List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
    {
        recipeDirection1.setDirectionNumber(1);
        recipeDirection1.setDirectionText("Test Direction1");
        recipeDirection2.setDirectionNumber(2);
        recipeDirection2.setDirectionText("Test Direction2");
        listOfDirections.add(recipeDirection1);
        listOfDirections.add(recipeDirection2);
    }
    // TODO: Add picture
    Recipe testRecipe = new Recipe(recipeTitle, 1.0, 30, 60, false, listOfIngredients, listOfDirections, listOfCategories);

    @Test
    public void useAppContext() {
        // Context of the app under test
        //Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.kitchen", appContext.getPackageName());
    }

    @Test
    public void createRecipe_ReturnsTrue(){
       assertEquals(true, testDatabase.createRecipe(testRecipe));
    }

    @Test
    public void createRecipe_ReturnsFalse(){
        Recipe invalidRecipe = new Recipe();
        assertEquals(false, testDatabase.createRecipe(invalidRecipe));
    }

    @Test
    public void createRecipe_CorrectInformation(){
        testDatabase.createRecipe(testRecipe);
        Recipe test = new Recipe();
        ArrayList<Recipe> allRecipes = testDatabase.getAllRecipes();
        try {
            for (int i = 0; i < allRecipes.size(); i++) {
                if (allRecipes.get(i).getTitle().equals(recipeTitle)) {
                    test = allRecipes.get(i);
                }
            }
        } catch(NullPointerException e){
            // Do nothing, all assertEquals will give AssertionErrors
        }

        // Check all recipe fields are accurate
        assertEquals("Check Recipe Title", recipeTitle, test.getTitle());
        assertEquals("Check Recipe Servings", 1, test.getServings(), 0);
        assertEquals("Check Recipe prep_time", 30, test.getPrep_time(), 0);
        assertEquals("Check Recipe total_time", 60, test.getTotal_time(), 0);
        assertEquals("Check Recipe Favorited", false, test.getFavorited());
        assertEquals("Check Ingredient Name", "Flour", test.getIngredientList().get(0).getName());
        assertEquals("Check Ingredient Unit", "cups", test.getIngredientList().get(0).getUnit());
        assertEquals("Check Ingredient Quantity", 2.0, test.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("Check Ingredient Details", "White Flour", test.getIngredientList().get(0).getDetails());
        assertEquals("Check First Direction", "Test Direction1", test.getDirectionsList().get(0));
        assertEquals("Check Second Direction", "Test Direction2", test.getDirectionsList().get(1));
        assertEquals("Check Recipe Category", "Lunch", test.getCategoryList().get(0));
        // TODO: Add picture check
    }

    @Test
    public void editRecipe_ReturnsTrue(){
        // TODO: Add recipe to database first or does it count from the other tests?
        testRecipe.setTitle("TestRecipe Updated");
        testRecipe.setServings(1.5);
        testRecipe.setPrep_time(15);
        testRecipe.setTotal_time(45);
        testRecipe.setFavorited(true);
        listOfCategories.clear();
        recipeCategory.setName("Dinner");
        listOfCategories.add(recipeCategory);
        testRecipe.setCategoryList(listOfCategories);
        listOfIngredients.clear();
        recipeIngredient.setName("Sugar");
        recipeIngredient.setUnit("tbsp");
        recipeIngredient.setQuantity(1.5);
        recipeIngredient.setDetails("Brown Sugar");
        listOfIngredients.add(recipeIngredient);
        testRecipe.setIngredientList(listOfIngredients);
        listOfDirections.clear();
        listOfDirections.add(recipeDirection1);
        testRecipe.setDirectionsList(listOfDirections);
        // TODO: Change picture

        assertEquals(true, testDatabase.editRecipe(testRecipe, listOfIngredients, listOfCategories, listOfDirections));
    }

    @Test
    public void editRecipe_ReturnsFalse(){
        Recipe invalidRecipe = new Recipe();
        List<RecipeIngredient> invalidIngredients = null;
        List<RecipeCategory> invalidCategories = null;
        List<RecipeDirection> invalidDirections = null;
        assertEquals(false, testDatabase.editRecipe(invalidRecipe, invalidIngredients, invalidCategories, invalidDirections));
    }

    @Test
    public void editRecipe_CorrectInformation(){
        // TODO: Add recipe to database first or does it count from the other tests?
        testRecipe.setTitle("TestRecipe Updated");
        testRecipe.setServings(1.5);
        testRecipe.setPrep_time(15);
        testRecipe.setTotal_time(45);
        testRecipe.setFavorited(true);
        listOfCategories.clear();
        recipeCategory.setName("Dinner");
        listOfCategories.add(recipeCategory);
        testRecipe.setCategoryList(listOfCategories);
        listOfIngredients.clear();
        recipeIngredient.setName("Sugar");
        recipeIngredient.setUnit("tbsp");
        recipeIngredient.setQuantity(1.5);
        recipeIngredient.setDetails("Brown Sugar");
        listOfIngredients.add(recipeIngredient);
        testRecipe.setIngredientList(listOfIngredients);
        listOfDirections.clear();
        listOfDirections.add(recipeDirection1);
        testRecipe.setDirectionsList(listOfDirections);
        //TODO: Change picture

        testDatabase.editRecipe(testRecipe, listOfIngredients, listOfCategories, listOfDirections);
        Recipe test = new Recipe();
        ArrayList<Recipe> allRecipes = testDatabase.getAllRecipes();
        try {
            for (int i = 0; i < allRecipes.size(); i++) {
                if (allRecipes.get(i).getTitle().equals("TestRecipe Updated")) {
                    test = allRecipes.get(i);
                }
            }
        } catch(NullPointerException e){
            // Do nothing, all assertEquals will give AssertionErrors
        }

        // Check all recipe fields are accurate
        assertEquals("Check Recipe Title", recipeTitle, test.getTitle());
        assertEquals("Check Recipe Servings", 1.5, test.getServings(), 0);
        assertEquals("Check Recipe prep_time", 15, test.getPrep_time(), 0);
        assertEquals("Check Recipe total_time", 45, test.getTotal_time(), 0);
        assertEquals("Check Recipe Favorited", true, test.getFavorited());
        assertEquals("Check Ingredient Name", "Sugar", test.getIngredientList().get(0).getName());
        assertEquals("Check Ingredient Unit", "tbsp", test.getIngredientList().get(0).getUnit());
        assertEquals("Check Ingredient Quantity", 1.5, test.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("Check Ingredient Details", "Brown Sugar", test.getIngredientList().get(0).getDetails());
        assertEquals("Check First Direction", "Test Direction1", test.getDirectionsList().get(0));
        assertEquals("Check Recipe Category", "Dinner", test.getCategoryList().get(0));
        // TODO: Add picture check
    }

    @Test
    public void deleteRecipe_ReturnsTrue(){
        // TODO: .getKeyID will only work if they set this in the createRecipe method
        // TODO: Add recipe to database first or does it count from the other tests?
        assertEquals(true, testDatabase.deleteRecipe(testRecipe.getKeyID()));

    }

    @Test
    public void deleteRecipe_ReturnsFalse(){
        assertEquals(false, testDatabase.deleteRecipe(Integer.MAX_VALUE));
    }

    @Test
    public void deleteRecipe_Deletes(){
        // TODO: Add recipe to database first or does it count from the other tests?
        // TODO: .getKeyID will only work if they set this in the createRecipe method
        testDatabase.deleteRecipe(testRecipe.getKeyID());
        ArrayList<Recipe> allRecipes = testDatabase.getAllRecipes();
        Boolean deleted = true;
        try {
            for (int i = 0; i < allRecipes.size(); i++) {
                if (allRecipes.get(i).getTitle().equals(recipeTitle)) {
                    deleted = false;
                }
            }
        } catch(NullPointerException e){
            // Database is empty, do nothing so deleted stays true
        }
        assertEquals(true, deleted);
    }

    @Test
    public void getAllRecipes_ReturnsList(){
        // TODO
    }

    @Test
    public void getAllRecipes_ReturnsNull(){
        // TODO
    }
}
