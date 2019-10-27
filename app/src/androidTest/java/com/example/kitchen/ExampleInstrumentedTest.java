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

    @Test
    public void addCategory_ReturnsID(){
        /*int returned = testDatabase.addCategory(category);
        assertNotEquals(-1, returned);*/
    }

    @Test
    public void addCategory_ReturnsNeg1(){
        /*int returned; // set equal to addCategory on an invalid category
        assertEquals(-1, returned);*/
    }

    @Test
    public void addCategory_DatabaseUpdates(){
        /*int returned = testDatabase.addCategory(category);
        Category retrieved = testDatabase.getCategory(returned);
        assertEquals("Check that Category was added with the correct name", "Lunch", retrieved.getName());
        assertEquals("Check the added Category's ID was set", returned, retrieved.getKeyID());*/
    }

    {
        testDatabase.addCategory(category);
    }
    RecipeCategory recipeCategory = new RecipeCategory();
    //RecipeCategory recipeCategory = new RecipeCategory(-1, "Lunch", -1, );
    List<RecipeCategory> listOfCategories = new ArrayList<RecipeCategory>();
    {
        recipeCategory.setName("Lunch");
        listOfCategories.add(recipeCategory);
    }

    Ingredient ingredient = new Ingredient(-1, "Flour");
    @Test
    public void addIngredient_ReturnsID(){
        /*int returned = testDatabase.addIngredient(ingredient);
        assertNotEquals(-1, returned);*/
    }

    @Test
    public void addIngredient_ReturnsNeg1(){
        /*int returned; // set equal to addIngredient on an invalid ingredient
        assertEquals(-1, returned);*/
    }

    @Test
    public void addIngredient_DatabaseUpdates(){
        /*int returned = testDatabase.addIngredient(category);
        Ingredient retrieved = testDatabase.getIngredient(returned);
        assertEquals("Check that Ingredient was added with correct name", "Flour", retrieved.getName());
        assertEquals("Check the added Ingredients ID was set", returned, retrieved.getKeyID());*/
    }

    {
        //testDatabase.addIngredient(ingredient);
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
       assertEquals(true, testDatabase.addRecipe(testRecipe));
    }

    /**
     * This method checks that addRecipe(Recipe) return false when Recipe is an invalid Recipe
     */
    @Test
    public void addRecipe_ReturnsFalse(){
        Recipe invalidRecipe = new Recipe();
        assertEquals(false, testDatabase.addRecipe(invalidRecipe));
    }

    /**
     * This method checks that all Recipe information was saved correctly after adding a recipe to the database
     */
    @Test
    public void addRecipe_CorrectInformation(){
        //testDatabase.addRecipe(testRecipe);
        int num = -1;
        ArrayList<Recipe> allRecipes = testDatabase.getAllRecipes();
        try {
            for (int i = 0; i < allRecipes.size(); i++) {
                if (allRecipes.get(i).getTitle().equals(recipeTitle)) {
                    num = i;
                }
            }
        } catch(NullPointerException e){
            // Do nothing, all assertEquals will give AssertionErrors
        }

        ArrayList<RecipeIngredient> allRecipeIngredients = testDatabase.getAllRecipeIngredients(testRecipe.getKeyID());
        ArrayList<RecipeDirection> allRecipeDirections = testDatabase.getAllRecipeDirections(testRecipe.getKeyID());
        //ArrayList<RecipeCategory> allRecipeCategories = testDatabase.getAllRecipeCategorys(testRecipe.getKeyID());

        // Check all recipe fields are accurate
        assertEquals("Check Recipe Title", recipeTitle, allRecipes.get(num).getTitle());
        assertEquals("Check Recipe Servings", 1, allRecipes.get(num).getServings(), 0); //TODO: Failing
        assertEquals("Check Recipe prep_time", 30, allRecipes.get(num).getPrep_time(), 0);
        assertEquals("Check Recipe total_time", 60, allRecipes.get(num).getTotal_time(), 0);
        assertEquals("Check Recipe Favorited", false, allRecipes.get(num).getFavorited());
        //TODO: Failing from here on down
        assertEquals("Check Ingredient Name", "Flour", allRecipeIngredients.get(0).getName());
        assertEquals("Check Ingredient Unit", "cups", allRecipeIngredients.get(0).getUnit());
        assertEquals("Check Ingredient Quantity", 2.0, allRecipeIngredients.get(0).getQuantity(), 0);
        assertEquals("Check Ingredient Details", "White Flour", allRecipeIngredients.get(0).getDetails());
        assertEquals("Check First Direction", "Test Direction1", allRecipeDirections.get(0));
        assertEquals("Check Second Direction", "Test Direction2", allRecipeDirections.get(1));
        //assertEquals("Check Recipe Category", "Lunch", allRecipeCategories.get(0));
        // TODO: Add picture check
    }

    /**
     * This method checks that editRecipe(Recipe) return true when Recipe is a valid Recipe
     */
    @Test
    public void editRecipe_ReturnsTrue(){
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

        assertEquals(true, testDatabase.editRecipe(testRecipe));
    }

    /**
     * This method checks that editRecipe(Recipe) return false when Recipe doesn't exist in database
     */
    @Test
    public void editRecipe_ReturnsFalse(){
        Recipe invalidRecipe = new Recipe();
        List<RecipeIngredient> invalidIngredients = null;
        List<RecipeCategory> invalidCategories = null;
        List<RecipeDirection> invalidDirections = null;
        assertEquals(false, testDatabase.editRecipe(invalidRecipe));
    }

    /**
     * This method checks that editRecipe(Recipe) saves all the correct information after updating the database
     */
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

        testDatabase.editRecipe(testRecipe);
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

    /**
     * This method checks that deleteRecipe(RecipeID) return true when Recipe existed in the database
     */
    @Test
    public void deleteRecipe_ReturnsTrue(){
        // TODO: .getKeyID will only work if they set this in the createRecipe method
        // TODO: Add recipe to database first or does it count from the other tests?
        assertEquals(true, testDatabase.deleteRecipe(testRecipe.getKeyID()));

    }

    /**
     * This method checks that deleteRecipe(RecipeID) return false when attempt to delete a non-existing Recipe
     */
    @Test
    public void deleteRecipe_ReturnsFalse(){
        //assertEquals(false, testDatabase.deleteRecipe(String.valueOf(Integer.MAX_VALUE)));
    }

    /**
     * This method checks that deleteRecipe(RecipeID) actually deletes the Recipe from the database
     */
    @Test
    public void deleteRecipe_Deletes(){
        // TODO: FIX THIS TEST
        /*testDatabase.deleteRecipe(testRecipe.getKeyID());
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
        assertEquals(true, deleted);*/
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
