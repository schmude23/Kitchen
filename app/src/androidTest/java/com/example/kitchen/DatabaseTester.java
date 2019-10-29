package com.example.kitchen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
public class DatabaseTester {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    DatabaseHelper testDatabase = new DatabaseHelper(appContext.getApplicationContext());

    // Create recipe to test with
    String recipeTitle = "TestRecipe";
    Category category = new Category(-1, "Lunch");
    int categoryID;
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

    @Test
    public void useAppContext() {
        assertEquals("com.example.kitchen", appContext.getPackageName());
    }

    @Test
    public void addCategory_ReturnsID(){
        int returned = testDatabase.addCategory(category);
        assertNotEquals(-1, returned);
    }

    @Test
    public void addCategory_DatabaseUpdates(){
        int returned = testDatabase.addCategory(category);
        Category retrieved = testDatabase.getCategory(returned);
        assertEquals("addCategory - Correct Category Name", "Lunch", retrieved.getName());
        assertEquals("addCategory - Set Category ID", returned, retrieved.getKeyID());
    }

    @Test
    public void addIngredient_ReturnsID(){
        int returned = testDatabase.addIngredient(ingredient);
        assertNotEquals(-1, returned);
    }

    @Test
    public void addIngredient_DatabaseUpdates(){
        int returned = testDatabase.addIngredient(ingredient);
        Ingredient retrieved = testDatabase.getIngredient(returned);
        assertEquals("addIngredient - Correct Ingredient Name", "Flour", retrieved.getName());
        assertEquals("addIngredient - Set Ingredients ID", returned, retrieved.getKeyID());
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
     * This method checks that all Recipe information was saved correctly after adding a recipe to the database
     */
    @Test
    public void addRecipe_CorrectInformation(){
        int returned = testDatabase.addRecipe(testRecipe);
        Recipe retrieved = testDatabase.getRecipe(returned);

        // Check all recipe fields are accurate
        assertEquals("addRecipe - Correct Title", recipeTitle, retrieved.getTitle());
        assertEquals("addRecipe - Correct Servings", 1, retrieved.getServings(), 0);
        assertEquals("addRecipe - Correct prep_time", 30, retrieved.getPrep_time(), 0);
        assertEquals("addRecipe - Correct total_time", 60, retrieved.getTotal_time(), 0);
        assertEquals("addRecipe - Correct Favorited", false, retrieved.getFavorited());
        assertEquals("addRecipe - Correct Ingredient Unit", "cups", retrieved.getIngredientList().get(0).getUnit());
        assertEquals("addRecipe - Correct Ingredient Quantity", 2.0, retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("addRecipe - Correct Ingredient Details", "White Flour", retrieved.getIngredientList().get(0).getDetails());
        assertEquals("addRecipe - Correct First Direction Number", 1, retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("addRecipe - Correct First Direction Text", "TestDirection1", retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("addRecipe - Correct Second Direction Number", 2, retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("addRecipe - Correct Second Direction Text", "TestDirection2", retrieved.getDirectionsList().get(1).getDirectionText());
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
        assertEquals(false, testDatabase.deleteRecipe(Integer.MAX_VALUE));
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

    //TODO ADD MORE TESTS
    @Test
    public void addPicture(){
        /*Bitmap image = null;
        image = BitmapFactory.decodeResource(appContext.getResources(),R.drawable.ic_default_image);
        assertEquals("Check addPicture when valid picture", true, testRecipe.setImage(image, appContext));*/
    }

    @Test
    public void editPicture(){
        //TODO
    }

    @Test
    public void getPicture(){
        //TODO
    }

    @Test
    public void getIngredient_ReturnsIngredient(){
        int returned = testDatabase.addIngredient(ingredient);
        assertNotEquals("getIngredient - Return Non-Null Ingredient",null, testDatabase.getIngredient(returned));
    }

    @Test
    public void getIngredient_ReturnsNull(){
        //assertEquals("getIngredient - Returns Null", null, testDatabase.getIngredient(Integer.MAX_VALUE));
        //TODO - Zander fixing
    }

    @Test
    public void getIngredient_CorrectInformation(){
        int returned = testDatabase.addIngredient(ingredient);
        Ingredient retrieved = testDatabase.getIngredient(returned);
        assertEquals("getIngredient - Correct Name", "Flour", retrieved.getName());
        assertEquals("getIngredient - Correct ID", returned, retrieved.getKeyID());
    }

    @Test
    public void deleteIngredient_ReturnsTrue(){
        int returned = testDatabase.addIngredient(ingredient);
        assertEquals(true, testDatabase.deleteIngredient(returned));
    }

    @Test
    public void deleteIngredient_ReturnsFalse(){
        assertEquals(false, testDatabase.deleteIngredient(Integer.MAX_VALUE));
    }

    @Test
    public void deleteIngredient_Deletes(){
        /*int returned = testDatabase.addIngredient(ingredient);
        testDatabase.deleteIngredient(returned);
        assertEquals(null, testDatabase.getIngredient(returned));*/
    }

    @Test
    public void getCategory_ReturnsIngredient(){
        int returned = testDatabase.addCategory(category);
        assertNotEquals("getCategory - Return Non-Null Category",null, testDatabase.getCategory(returned));
    }

    @Test
    public void getCategory_ReturnsNull(){
        //assertEquals("getCategory - Returns Null", null, testDatabase.getCategory(Integer.MAX_VALUE));
        //TODO - Zander fixing
    }

    @Test
    public void getCategory_CorrectInformation(){
        int returned = testDatabase.addCategory(category);
        Category retrieved = testDatabase.getCategory(returned);
        assertEquals("getCategory - Correct Name", "Lunch", retrieved.getName());
        assertEquals("getCategory - Correct ID", returned, retrieved.getKeyID());
    }

    @Test
    public void deleteCategory_ReturnsTrue(){
        int returned = testDatabase.addCategory(category);
        assertEquals(true, testDatabase.deleteCategory(returned));
    }

    @Test
    public void deleteCategory_ReturnsFalse(){
        assertEquals(false, testDatabase.deleteCategory(Integer.MAX_VALUE));
    }

    @Test
    public void deleteCategory_Deletes(){
        /*int returned = testDatabase.addCategory(category);
        testDatabase.deleteCategory(returned);
        assertEquals(null, testDatabase.getCategory(returned));*/
    }

    //TODO ADD MORE TESTS

    @Test
    public void getRecipe_CorrectInformation(){
        int returned = testDatabase.addRecipe(testRecipe);
        Recipe retrieved = testDatabase.getRecipe(returned);
        assertEquals("getRecipe - Correct Title", recipeTitle, retrieved.getTitle());
        assertEquals("getRecipe - Correct Servings", 1, retrieved.getServings(), 0);
        assertEquals("getRecipe - Correct Prep Time", 30, retrieved.getPrep_time(), 0);
        assertEquals("getRecipe - Correct Total Time", 60, retrieved.getTotal_time(), 0);
        assertEquals("getRecipe - Correct Favorited", false, retrieved.getFavorited());
        assertEquals("getRecipe - Ingredient Unit", "cups", retrieved.getIngredientList().get(0).getUnit());
        assertEquals("getRecipe - Ingredient Quantity", 2.0, retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipe - Ingredient Details", "White Flour", retrieved.getIngredientList().get(0).getDetails());
        assertEquals("getRecipe - First Direction Number", 1, retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipe - First Direction Text", "TestDirection1", retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipe - Second Direction Number", 2, retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipe - Second Direction Text", "TestDirection2", retrieved.getDirectionsList().get(1).getDirectionText());
    }

    @Test
    public void getRecipe_ReturnsNull(){
        assertEquals("Check getRecipe returns null", null, testDatabase.getRecipe(Integer.MAX_VALUE));
    }

    /**
     * This method checks that getAllRecipes() return the list of recipes
     */
    @Test
    public void getAllRecipes_ReturnsList(){
        ArrayList<Recipe> allRecipes = testDatabase.getAllRecipes();
        assertNotEquals("getAllRecipes - Non-empty List Returned", 0, allRecipes.size());
    }

    @Test
    public void getAllRecipes_RecipeInfo(){
        int returned = testDatabase.addRecipe(testRecipe);
        ArrayList<Recipe> allRecipes = testDatabase.getAllRecipes();
        Recipe retrieved = allRecipes.get(allRecipes.size()-1);
        //Retrieved should not contain all the same information as testRecipe
        assertEquals("getAllRecipes - Correct Title", recipeTitle, retrieved.getTitle());
        assertEquals("getAllRecipes - Correct Servings", 1, retrieved.getServings(), 0);
        assertEquals("getAllRecipes - Correct Prep Time", 30, retrieved.getPrep_time(), 0);
        assertEquals("getAllRecipes - Correct Total Time", 60, retrieved.getTotal_time(), 0);
        assertEquals("getAllRecipes - Correct Favorited", false, retrieved.getFavorited());
    }

    /**
     * This method checks that getAllRecipes() returns null when there are no recipes in the database
     */
    @Test
    public void getAllRecipes_ReturnsNull(){
        ArrayList<Recipe> allRecipes = testDatabase.getAllRecipes();
        for(int i = 0; i < allRecipes.size(); i++){
            testDatabase.deleteRecipe(allRecipes.get(i).getKeyID());
        }
        ArrayList<Recipe> noRecipes = testDatabase.getAllRecipes();
        assertEquals("getAllRecipes - Empty List Returned", 0, noRecipes.size());
    }
}
