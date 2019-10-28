package com.example.kitchen;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.ThreadSafe;

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
    Category category = new Category(-1, "Lunch");
    int categoryID;

    @Test
    public void addCategory_ReturnsID(){
        int returned = testDatabase.addCategory(category);
        assertNotEquals(-1, returned);
    }

    @Test
    public void addCategory_ReturnsNeg1(){
        /*int returned; //TODO set equal to addCategory on an invalid category
        assertEquals(-1, returned);*/
    }

    @Test
    public void addCategory_DatabaseUpdates(){
        int returned = testDatabase.addCategory(category);
        Category retrieved = testDatabase.getCategory(returned);
        assertEquals("Check that Category was added with the correct name", "Lunch", retrieved.getName());
        assertEquals("Check the added Category's ID was set", returned, retrieved.getKeyID());
    }

    {
        categoryID = testDatabase.addCategory(category);
    }
    RecipeCategory recipeCategory = new RecipeCategory(-1, "Lunch", -1, categoryID);
    List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
    {
        listOfCategories.add(recipeCategory);
    }

    Ingredient ingredient = new Ingredient(-1, "Flour");
    int ingredientID;
    @Test
    public void addIngredient_ReturnsID(){
        int returned = testDatabase.addIngredient(ingredient);
        assertNotEquals(-1, returned);
    }

    @Test
    public void addIngredient_ReturnsNeg1(){
        /*int returned; // TODO set equal to addIngredient on an invalid ingredient
        assertEquals(-1, returned);*/
    }

    @Test
    public void addIngredient_DatabaseUpdates(){
        int returned = testDatabase.addIngredient(ingredient);
        Ingredient retrieved = testDatabase.getIngredient(returned);
        assertEquals("Check that Ingredient was added with correct name", "Flour", retrieved.getName());
        assertEquals("Check the added Ingredients ID was set", returned, retrieved.getKeyID());
    }

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
    // TODO: Add picture
    Recipe testRecipe = new Recipe(recipeTitle, 1.0, 30, 60, false, listOfIngredients, listOfDirections, listOfCategories);

    @Test
    public void useAppContext() {
        // Context of the app under test
        //Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.kitchen", appContext.getPackageName());
    }

    /**
     * This method checks that addRecipe(Recipe) return true when Recipe is a valid Recipe
     */
    @Test
    public void addRecipe_ReturnsTrue(){
        int returned = testDatabase.addRecipe(testRecipe);
        assertNotEquals(-1, returned);
    }

    /**
     * This method checks that addRecipe(Recipe) return false when Recipe is an invalid Recipe
     */
    @Test
    public void addRecipe_ReturnsFalse(){
        Recipe invalidRecipe = new Recipe();
        //TODO WHAT COUNTS AS AN INVALID RECIPE BECAUSE THIS ADDED IT - user interface
        int returned = testDatabase.addRecipe(invalidRecipe);
        assertEquals(-1, returned);
    }

    /**
     * This method checks that all Recipe information was saved correctly after adding a recipe to the database
     */
    @Test
    public void addRecipe_CorrectInformation(){
        int returned = testDatabase.addRecipe(testRecipe);
        Recipe retrieved = testDatabase.getRecipe(returned);

        // Check all recipe fields are accurate
        assertEquals("Check Recipe Title", recipeTitle, retrieved.getTitle());
        assertEquals("Check Recipe Servings", 1, retrieved.getServings(), 0);
        assertEquals("Check Recipe prep_time", 30, retrieved.getPrep_time(), 0);
        assertEquals("Check Recipe total_time", 60, retrieved.getTotal_time(), 0);
        assertEquals("Check Recipe Favorited", false, retrieved.getFavorited());
        assertEquals("Check Ingredient Unit", "cups", retrieved.getIngredientList().get(0).getUnit());
        assertEquals("Check Ingredient Quantity", 2.0, retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("Check Ingredient Details", "White Flour", retrieved.getIngredientList().get(0).getDetails());
        assertEquals("Check First Direction Number", 1, retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("Check First Direction Text", "TestDirection1", retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("Check Second Direction Number", 2, retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("Check Second Direction Text", "TestDirection2", retrieved.getDirectionsList().get(1).getDirectionText());
        // TODO: Add picture check
    }

    /**
     * This method checks that editRecipe(Recipe) return true when Recipe is a valid Recipe
     */
    @Test
    public void editRecipe_ReturnsTrue(){
        testDatabase.addRecipe(testRecipe);
        testRecipe.setTitle("TestRecipe Updated");
        testRecipe.setServings(1.5);
        testRecipe.setPrep_time(15);
        testRecipe.setTotal_time(45);
        testRecipe.setFavorited(true);
        listOfCategories.clear();
        listOfCategories.add(recipeCategory);
        testRecipe.setCategoryList(listOfCategories);
        listOfIngredients.clear();
        recipeIngredient.setUnit("tbsp");
        recipeIngredient.setQuantity(1.5);
        recipeIngredient.setDetails("Brown Sugar");
        listOfIngredients.add(recipeIngredient);
        testRecipe.setIngredientList(listOfIngredients);
        listOfDirections.clear();
        listOfDirections.add(recipeDirection1);
        testRecipe.setDirectionsList(listOfDirections);
        // TODO: Change picture

        assertEquals(true, testDatabase.editRecipe(testRecipe));
    }

    /**
     * This method checks that editRecipe(Recipe) return false when Recipe doesn't exist in database
     */
    @Test
    public void editRecipe_ReturnsFalse(){
        Recipe invalidRecipe = new Recipe();
        assertEquals(false, testDatabase.editRecipe(invalidRecipe));
    }

    /**
     * This method checks that editRecipe(Recipe) saves all the correct information after updating the database
     */
    @Test
    public void editRecipe_CorrectInformation(){
        int returned = testDatabase.addRecipe(testRecipe);
        listOfCategories.clear();
        listOfCategories.add(recipeCategory);
        listOfIngredients.clear();
        Ingredient ingredient = new Ingredient(-1, "Sugar");
        testDatabase.addIngredient(ingredient);
        int ingredientID = testDatabase.addIngredient(ingredient);
        RecipeIngredient recipeIngredient = new RecipeIngredient(-1, -1, ingredientID, 1.5, "tbsp", "Brown Sugar");
        listOfIngredients.add(recipeIngredient);
        listOfDirections.clear();
        RecipeDirection recipeDirection1 = new RecipeDirection(-1, -1, "TestDirection1", 1);
        listOfDirections.add(recipeDirection1);
        //TODO: Change picture

        Recipe edited = new Recipe(testRecipe.getKeyID(), "TestRecipe Updated", 1.5, 15, 45, true, listOfIngredients, listOfDirections, listOfCategories);

        testDatabase.editRecipe(edited);
        Recipe test = testDatabase.getRecipe(returned);

        // Check all recipe fields are accurate
        assertEquals("Check Recipe Title", "TestRecipe Updated", test.getTitle());
        assertEquals("Check Recipe Servings", 1.5, test.getServings(), 0);
        assertEquals("Check Recipe prep_time", 15, test.getPrep_time(), 0);
        assertEquals("Check Recipe total_time", 45, test.getTotal_time(), 0);
        assertEquals("Check Recipe Favorited", true, test.getFavorited());
        assertEquals("Check Ingredient Unit", "tbsp", test.getIngredientList().get(0).getUnit());
        assertEquals("Check Ingredient Quantity", 1.5, test.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("Check Ingredient Details", "Brown Sugar", test.getIngredientList().get(0).getDetails());
        assertEquals("Check First Direction", "TestDirection1", test.getDirectionsList().get(0).getDirectionText());
        assertEquals("Check First Direction", 1, test.getDirectionsList().get(0).getDirectionNumber());
        // TODO: Add picture check
    }

    /**
     * This method checks that deleteRecipe(RecipeID) return true when Recipe existed in the database
     */
    @Test
    public void deleteRecipe_ReturnsTrue(){
        int returned = testDatabase.addRecipe(testRecipe);
        assertEquals(true, testDatabase.deleteRecipe(returned));

    }

    /**
     * This method checks that deleteRecipe(RecipeID) return false when attempt to delete a non-existing Recipe
     */
    @Test
    public void deleteRecipe_ReturnsFalse(){
        assertEquals(false, testDatabase.deleteRecipe(Integer.MAX_VALUE)); // TODO
    }

    /**
     * This method checks that deleteRecipe(RecipeID) actually deletes the Recipe from the database
     */
    @Test
    public void deleteRecipe_Deletes(){
        int returned = testDatabase.addRecipe(testRecipe);
        testDatabase.deleteRecipe(returned);
        assertEquals(null, testDatabase.getRecipe(returned));
    }

    /**
     * This method checks that getAllRecipes() return the list of recipes
     */
    @Test
    public void getAllRecipes_ReturnsList(){
        // TODO
    }

    /**
     * This method checks that getAllRecipes() returns null when there are no recipes in the database
     */
    @Test
    public void getAllRecipes_ReturnsNull(){
        // TODO
    }

    //TODO ADD MORE TESTS
}
