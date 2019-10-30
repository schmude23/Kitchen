package com.example.kitchen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;
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

    @Test
    public void useAppContext() {
        assertEquals("com.example.kitchen", appContext.getPackageName());
    }

    @Test
    public void addCategory_ReturnsID(){
        int returned = testDatabase.addCategory(category);
        assertNotEquals("addCategory - Returns True", -1, returned);
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
        assertNotEquals("addIngredients - Returns True",-1, returned);
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
        assertNotEquals("addRecipe - Returns True", -1, returned);
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

        assertEquals("editRecipe - Returns True", true, testDatabase.editRecipe(testRecipe));
    }

    /**
     * This method checks that editRecipe(Recipe) return false when Recipe doesn't exist in database
     */
    @Test
    public void editRecipe_ReturnsFalse(){
        Recipe invalidRecipe = new Recipe();
        assertEquals("editRecipe - Returns False", false, testDatabase.editRecipe(invalidRecipe));
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
        assertEquals("editRecipe - Correct Title", "TestRecipe Updated", test.getTitle());
        assertEquals("editRecipe - Correct Servings", 1.5, test.getServings(), 0);
        assertEquals("editRecipe - Correct prep_time", 15, test.getPrep_time(), 0);
        assertEquals("editRecipe - Correct total_time", 45, test.getTotal_time(), 0);
        assertEquals("editRecipe - Correct Favorited", true, test.getFavorited());
        assertEquals("editRecipe - Correct Ingredient Unit", "tbsp", test.getIngredientList().get(0).getUnit());
        assertEquals("editRecipe - Correct Ingredient Quantity", 1.5, test.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("editRecipe - Correct Ingredient Details", "Brown Sugar", test.getIngredientList().get(0).getDetails());
        assertEquals("editRecipe - Correct Direction Text", "TestDirection1", test.getDirectionsList().get(0).getDirectionText());
        assertEquals("editRecipe - Correct Direction Number", 1, test.getDirectionsList().get(0).getDirectionNumber());
    }

    /**
     * This method checks that deleteRecipe(RecipeID) return true when Recipe existed in the database
     */
    @Test
    public void deleteRecipe_ReturnsTrue(){
        int returned = testDatabase.addRecipe(testRecipe);
        assertEquals("deleteRecipe - Returns True",true, testDatabase.deleteRecipe(returned));

    }

    /**
     * This method checks that deleteRecipe(RecipeID) return false when attempt to delete a non-existing Recipe
     */
    @Test
    public void deleteRecipe_ReturnsFalse(){
        assertEquals("deleteRecipe - Returns False", false, testDatabase.deleteRecipe(Integer.MAX_VALUE));
    }

    /**
     * This method checks that deleteRecipe(RecipeID) actually deletes the Recipe from the database
     */
    @Test
    public void deleteRecipe_Deletes(){
        int returned = testDatabase.addRecipe(testRecipe);
        testDatabase.deleteRecipe(returned);
        assertEquals("deleteRecipe - Deletes From Database", null, testDatabase.getRecipe(returned));
    }

    @Test
    public void addPicture_ReturnsTrue(){
        Bitmap image = BitmapFactory.decodeResource(appContext.getResources(),R.drawable.ic_add);
        assertEquals("addPicture - Returns True Upon Valid Pic", true, testRecipe.setImage(image, appContext));
        assertNotEquals("addPicture - Not Null Upon Valid Pic", null, testRecipe.getImage(appContext));
    }

    @Test
    public void addPicture_ReturnsFalse(){
        Bitmap image = BitmapFactory.decodeResource(appContext.getResources(),R.drawable.ic_default_image);
        assertEquals("addPicture - Returns False Upon Invalid Pic", false, testRecipe.setImage(image, appContext));
        assertEquals("addPicture - Image is Null Upon Invalid Pic", null, testRecipe.getImage(appContext));
    }

    @Test
    public void editPicture_NotNull(){
        Bitmap originalImage = BitmapFactory.decodeResource(appContext.getResources(),R.drawable.ic_add);
        testRecipe.setImage(originalImage, appContext);
        Bitmap newImage = BitmapFactory.decodeResource(appContext.getResources(),R.drawable.ic_cart);
        testRecipe.setImage(newImage, appContext);
        assertNotEquals("editPicture - New Image Not Null", null, testRecipe.getImage(appContext));
    }

    @Test
    public void getPicture_Exists(){
        Bitmap image = BitmapFactory.decodeResource(appContext.getResources(),R.drawable.ic_add);
        testRecipe.setImage(image, appContext);
        Bitmap retrieved = testRecipe.getImage(appContext);
        assertNotEquals("getPicture - Not Null", null, retrieved);
    }

    @Test
    public void getPicture_NoPictureExist(){
        Bitmap retrieved = testRecipe.getImage(appContext);
        assertNotEquals("getPicture - Not Null", null, retrieved);
    }

    @Test
    public void getIngredient_ReturnsIngredient(){
        int returned = testDatabase.addIngredient(ingredient);
        assertNotEquals("getIngredient - Return Non-Null Ingredient",null, testDatabase.getIngredient(returned));
    }

    @Test
    public void getIngredient_ReturnsNull(){
        assertEquals("getIngredient - Returns Null", null, testDatabase.getIngredient(Integer.MAX_VALUE));
    }

    @Test
    public void getIngredient_CorrectInformation(){
        int returned = testDatabase.addIngredient(ingredient);
        Ingredient retrieved = testDatabase.getIngredient(returned);
        assertEquals("getIngredient - Correct Name", "Flour", retrieved.getName());
        assertEquals("getIngredient - Correct ID", returned, retrieved.getKeyID());
    }

    @Test
    public void editIngredient_ReturnsTrue(){
        Ingredient newIngredient = new Ingredient(ingredientID, "egg");
        assertEquals("editIngredient - Returns True", true, testDatabase.editIngredient(newIngredient));
    }

    @Test
    public void editIngredient_ReturnsFalse(){
        Ingredient newIngredient = new Ingredient(Integer.MAX_VALUE, "egg");
        assertEquals("editIngredient - Ingredient Doesn't Exist", false, testDatabase.editIngredient(newIngredient));
    }

    @Test
    public void editIngredient_CorrectInformation(){
        Ingredient newIngredient = new Ingredient(ingredientID, "egg");
        testDatabase.editIngredient(newIngredient);
        Ingredient retrieved = testDatabase.getIngredient(ingredientID);
        assertEquals("editIngredient - Correct Name", "egg", retrieved.getName());
        assertEquals("editIngredient - Correct ID", ingredientID, retrieved.getKeyID());
    }

    @Test
    public void deleteIngredient_ReturnsTrue(){
        int returned = testDatabase.addIngredient(ingredient);
        assertEquals("deleteIngredient - Returns True",true, testDatabase.deleteIngredient(returned));
    }

    @Test
    public void deleteIngredient_ReturnsFalse(){
        assertEquals("deleteIngredient - Returns False",false, testDatabase.deleteIngredient(Integer.MAX_VALUE));
    }

    @Test
    public void deleteIngredient_Deletes(){
        int returned = testDatabase.addIngredient(ingredient);
        testDatabase.deleteIngredient(returned);
        assertEquals("deleteIngredient - Deletes From Database",null, testDatabase.getIngredient(returned));
    }

    @Test
    public void getCategory_ReturnsIngredient(){
        int returned = testDatabase.addCategory(category);
        assertNotEquals("getCategory - Return Non-Null Category",null, testDatabase.getCategory(returned));
    }

    @Test
    public void getCategory_ReturnsNull(){
        assertEquals("getCategory - Returns Null", null, testDatabase.getCategory(Integer.MAX_VALUE));
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
        assertEquals("deleteCategory - Returns True", true, testDatabase.deleteCategory(returned));
    }

    @Test
    public void deleteCategory_ReturnsFalse(){
        assertEquals("deleteCategory - Returns False", false, testDatabase.deleteCategory(Integer.MAX_VALUE));
    }

    @Test
    public void deleteCategory_Deletes(){
        int returned = testDatabase.addCategory(category);
        testDatabase.deleteCategory(returned);
        assertEquals("deleteCategory - Deletes From Database", null, testDatabase.getCategory(returned));
    }

    @Test
    public void addRecipeIngredient_ReturnsID(){
        int returned = testDatabase.addRecipeIngredient(recipeIngredient);
        assertNotEquals("addRecipeIngredient - Returns True", -1, returned);
    }

    @Test
    public void addRecipeIngredient_DatabaseUpdates(){
        int returnedRecipe = testDatabase.addRecipe(testRecipe);
        ArrayList<RecipeIngredient> allRecipeIngredients = testDatabase.getAllRecipeIngredients(returnedRecipe);
        RecipeIngredient retrieved = new RecipeIngredient();
        if(allRecipeIngredients != null){
            for(int i = 0; i < allRecipeIngredients.size(); i++){
                if(allRecipeIngredients.get(i).getIngredientID() == ingredientID){
                    retrieved = allRecipeIngredients.get(i);
                }
            }
        }
        assertEquals("addRecipeIngredient - Correct Quantity", 2.0, retrieved.getQuantity(), 0);
        assertEquals("addRecipeIngredient - Correct Unit", "cups", retrieved.getUnit());
        assertEquals("addRecipeIngredient - Correct Details", "White Flour", retrieved.getDetails());
    }

    @Test
    public void deleteRecipeIngredient_ReturnsTrue(){
        int returned = testDatabase.addRecipe(testRecipe);
        assertEquals("deleteRecipeIngredient - Returns True",true, testDatabase.deleteRecipeIngredients(returned));
    }

    @Test
    public void deleteRecipeIngredient_ReturnsFalse(){
        assertEquals("deleteRecipeIngredient - Returns False",false, testDatabase.deleteRecipeIngredients(Integer.MAX_VALUE));
    }

    @Test
    public void deleteRecipeIngredient_Deletes(){
        int returnedRecipe = testDatabase.addRecipe(testRecipe);
        int returnedIngredient = testDatabase.addRecipeIngredient(recipeIngredient);
        testDatabase.deleteRecipeIngredients(returnedIngredient);
        ArrayList<RecipeIngredient> allIngredients = testDatabase.getAllRecipeIngredients(returnedRecipe);
        boolean deleted = true;
        if(allIngredients != null){
            for(int i = 0; i < allIngredients.size(); i++){
                if(allIngredients.get(i).getIngredientID() == returnedIngredient){
                    deleted = false;
                }
            }
        }
        assertEquals("deleteRecipeIngredient - Deletes From Database", true, deleted);
    }

    @Test
    public void addRecipeCategory_ReturnsID(){
        int returned = testDatabase.addRecipeCategory(recipeCategory);
        assertNotEquals("addRecipeCategory - Returns True", -1, returned);
    }

    @Test
    public void addRecipeCategory_DatabaseUpdates(){
        int returnedRecipe = testDatabase.addRecipe(testRecipe);
        int returnedCategory = testDatabase.addRecipeCategory(recipeCategory);
        ArrayList<RecipeCategory> allCategories = testDatabase.getAllRecipeCategories(returnedRecipe);
        RecipeCategory retrieved = new RecipeCategory();
        if(allCategories != null){
            for(int i = 0; i < allCategories.size(); i++){
                if(allCategories.get(i).getKeyID() == returnedCategory){
                    retrieved = allCategories.get(i);
                }
            }
        }
        assertEquals("addRecipeDirection - Correct RecipeID", returnedRecipe, retrieved.getRecipeID());
        assertEquals("addRecipeDirection - Correct CategoryID", categoryID, retrieved.getCategoryID());
    }

    @Test
    public void deleteRecipeCategory_ReturnsTrue(){
        int returned = testDatabase.addRecipe(testRecipe);
        assertEquals("deleteRecipeCategory - Returns True",true, testDatabase.deleteRecipeCategory(returned));
    }

    @Test
    public void deleteRecipeCategory_ReturnsFalse(){
        assertEquals("deleteRecipeCategory - Returns False",false, testDatabase.deleteRecipeCategory(Integer.MAX_VALUE));
    }

    @Test
    public void deleteRecipeCategory_Deletes(){
        int returnedRecipe = testDatabase.addRecipe(testRecipe);
        int returnedCategory = testDatabase.addRecipeCategory(recipeCategory);
        testDatabase.deleteRecipeCategory(returnedCategory);
        ArrayList<RecipeCategory> allCategories = testDatabase.getAllRecipeCategories(returnedRecipe);
        boolean deleted = true;
        if(allCategories != null){
            for(int i = 0; i < allCategories.size(); i++){
                if(allCategories.get(i).getCategoryID() == returnedCategory){
                    deleted = false;
                }
            }
        }
        assertEquals("deleteRecipeCategory - Deletes From Database", true, deleted);
    }

    @Test
    public void addRecipeDirection_ReturnsID(){
        int returned = testDatabase.addRecipeDirection(recipeDirection1);
        assertNotEquals("addRecipeDirection - Returns True", -1, returned);
    }

    @Test
    public void addRecipeDirection_DatabaseUpdates(){
        int returnedRecipe = testDatabase.addRecipe(testRecipe);
        int returnedDirection1 = testDatabase.addRecipeDirection(recipeDirection1);
        ArrayList<RecipeDirection> allDirections = testDatabase.getAllRecipeDirections(returnedRecipe);
        RecipeDirection retrieved = new RecipeDirection();
        if(allDirections != null){
            for(int i = 0; i < allDirections.size(); i++){
                if(allDirections.get(i).getKeyID() == returnedDirection1){
                    retrieved = allDirections.get(i);
                }
            }
        }
        assertEquals("addRecipeDirection - Correct Number", 1, retrieved.getDirectionNumber(), 0);
        assertEquals("addRecipeDirection - Correct Text", "TestDirection1", retrieved.getDirectionText());
    }

    @Test
    public void deleteRecipeDirections_ReturnsTrue(){
        int returned = testDatabase.addRecipe(testRecipe);
        assertEquals("deleteRecipeDirections - Returns True",true, testDatabase.deleteRecipeDirections(returned));
    }

    @Test
    public void deleteRecipeDirections_ReturnsFalse(){
        assertEquals("deleteRecipeDirections - Returns False",false, testDatabase.deleteRecipeDirections(Integer.MAX_VALUE));
    }

    @Test
    public void deleteRecipeDirections_Deletes(){
        int returnedRecipe = testDatabase.addRecipe(testRecipe);
        int returnedDirection1 = testDatabase.addRecipeDirection(recipeDirection1);
        int returnedDirection2 = testDatabase.addRecipeDirection(recipeDirection2);
        testDatabase.deleteRecipeDirections(returnedRecipe);
        ArrayList<RecipeDirection> allDirections = testDatabase.getAllRecipeDirections(returnedRecipe);
        boolean deleted1 = true;
        boolean deleted2 = true;
        if(allDirections != null){
            for(int i = 0; i < allDirections.size(); i++){
                if(allDirections.get(i).getKeyID() == returnedDirection1){
                    deleted1 = false;
                }
                if(allDirections.get(i).getKeyID() == returnedDirection2){
                    deleted2 = false;
                }
            }
        }
        assertEquals("deleteRecipeDirections - Deleted Direction1", true, deleted1);
        assertEquals("deleteRecipeDirections - Deleted Direction2", true, deleted2);
    }

    //TODO ADD MORE TESTS

    @Test
    public void getRecipeByID_ReturnsRecipe(){
        int returned = testDatabase.addRecipe(testRecipe);
        assertNotEquals("getRecipeByName - Returns a Recipe", null, testDatabase.getRecipe(returned));
    }

    @Test
    public void getRecipeByID_CorrectInformation(){
        int returned = testDatabase.addRecipe(testRecipe);
        Recipe retrieved = testDatabase.getRecipe(returned);
        assertEquals("getRecipeByID - Correct Title", recipeTitle, retrieved.getTitle());
        assertEquals("getRecipeByID - Correct Servings", 1, retrieved.getServings(), 0);
        assertEquals("getRecipeByID - Correct Prep Time", 30, retrieved.getPrep_time(), 0);
        assertEquals("getRecipeByID - Correct Total Time", 60, retrieved.getTotal_time(), 0);
        assertEquals("getRecipeByID - Correct Favorited", false, retrieved.getFavorited());
        assertEquals("getRecipeByID - Ingredient Unit", "cups", retrieved.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByID - Ingredient Quantity", 2.0, retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByID - Ingredient Details", "White Flour", retrieved.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByID - First Direction Number", 1, retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByID - First Direction Text", "TestDirection1", retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByID - Second Direction Number", 2, retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByID - Second Direction Text", "TestDirection2", retrieved.getDirectionsList().get(1).getDirectionText());
    }

    @Test
    public void getRecipeByID_ReturnsNull(){
        assertEquals("getRecipeByID - Returns Null", null, testDatabase.getRecipe(Integer.MAX_VALUE));
    }

    /*@Test
    public void getRecipeByName_ReturnsRecipe(){
        int returned = testDatabase.addRecipe(testRecipe);
        assertNotEquals("getRecipeByName - Returns a Recipe", null, testDatabase.getRecipe(recipeTitle));
    }

    @Test
    public void getRecipeByName_CorrectInformation(){
        int returned = testDatabase.addRecipe(testRecipe);
        Recipe retrieved = testDatabase.getRecipe(recipeTitle);
        assertEquals("getRecipeByName - Correct Title", recipeTitle, retrieved.getTitle());
        assertEquals("getRecipeByName - Correct Servings", 1, retrieved.getServings(), 0);
        assertEquals("getRecipeByName - Correct Prep Time", 30, retrieved.getPrep_time(), 0);
        assertEquals("getRecipeByName - Correct Total Time", 60, retrieved.getTotal_time(), 0);
        assertEquals("getRecipeByName - Correct Favorited", false, retrieved.getFavorited());
        assertEquals("getRecipeByName - Ingredient Unit", "cups", retrieved.getIngredientList().get(0).getUnit());
        assertEquals("getRecipeByName - Ingredient Quantity", 2.0, retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("getRecipeByName - Ingredient Details", "White Flour", retrieved.getIngredientList().get(0).getDetails());
        assertEquals("getRecipeByName - First Direction Number", 1, retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("getRecipeByName - First Direction Text", "TestDirection1", retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("getRecipeByName - Second Direction Number", 2, retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("getRecipeByName - Second Direction Text", "TestDirection2", retrieved.getDirectionsList().get(1).getDirectionText());
    }

    @Test
    public void getRecipeByName_ReturnsNull(){
        assertEquals("getRecipeByName - Returns Null", null, testDatabase.getRecipe("NotHere"));
    }*/

    @Test
    public void getAllRecipeIngredients_ReturnsList(){
        int returned = testDatabase.addRecipe(testRecipe);
        ArrayList<RecipeIngredient> allIngredients = testDatabase.getAllRecipeIngredients(returned);
        assertNotEquals("getAllRecipeIngredients - Non-empty List Returned", 0, allIngredients.size());
    }

    @Test
    public void getAllRecipeIngredients_RecipeInfo(){
        int returned = testDatabase.addRecipe(testRecipe);
        ArrayList<RecipeIngredient> allIngredients = testDatabase.getAllRecipeIngredients(returned);
        RecipeIngredient retrieved = allIngredients.get(allIngredients.size()-1);
        //Retrieved should now contain all the same information as testRecipe
        assertEquals("getAllRecipeIngredients - Correct Quantity", 2.0, retrieved.getQuantity(), 0);
        assertEquals("getAllRecipeIngredients - Correct Unit", "cups", retrieved.getUnit());
        assertEquals("getAllRecipeIngredients - Correct Details", "White Flour", retrieved.getDetails());
    }

    @Test
    public void getAllRecipeIngredients_ReturnsNull(){
        int returned = testDatabase.addRecipe(testRecipe);
        testDatabase.deleteRecipeIngredients(returned);
        assertEquals("getAllRecipeIngredients - Empty List Returned", null, testDatabase.getAllRecipeIngredients(returned));
    }

    @Test
    public void getAllRecipeCategories_ReturnsList(){
        int returned = testDatabase.addRecipe(testRecipe);
        ArrayList<RecipeCategory> allCategories = testDatabase.getAllRecipeCategories(returned);
        assertNotEquals("getAllRecipeCategories - Non-empty List Returned", 0, allCategories.size());
    }

    @Test
    public void getAllRecipeCategories_RecipeInfo(){
        int returned = testDatabase.addRecipe(testRecipe);
        ArrayList<RecipeCategory> allCategories = testDatabase.getAllRecipeCategories(returned);
        RecipeCategory retrieved = allCategories.get(allCategories.size()-1);
        //Retrieved should now contain all the same information as testRecipe
        assertEquals("getAllRecipeCategories - Correct Recipe ID", returned, retrieved.getRecipeID());
        assertEquals("getAllRecipeCategories - Correct Category ID", categoryID, retrieved.getCategoryID());
    }

    @Test
    public void getAllRecipeCategories_ReturnsNull(){
        int returned = testDatabase.addRecipe(testRecipe);
        testDatabase.deleteRecipeCategory(returned);
        assertEquals("getAllRecipeCategories - Empty List Returned", null, testDatabase.getAllRecipeCategories(returned));
    }

    @Test
    public void getAllRecipeDirections_ReturnsList(){
        int returned = testDatabase.addRecipe(testRecipe);
        ArrayList<RecipeDirection> allDirections = testDatabase.getAllRecipeDirections(returned);
        assertNotEquals("getAllRecipeDirections - Non-empty List Returned", 0, allDirections.size());
    }

    @Test
    public void getAllRecipeDirections_RecipeInfo(){
        int returned = testDatabase.addRecipe(testRecipe);
        ArrayList<RecipeDirection> allDirections = testDatabase.getAllRecipeDirections(returned);
        RecipeDirection retrieved = allDirections.get(allDirections.size()-1);
        //Retrieved should now contain all the same information as testRecipe
        assertEquals("getAllRecipeDirections - Correct Text", "TestDirection2", retrieved.getDirectionText());
        assertEquals("getAllRecipeDirections - Correct Number", 2, retrieved.getDirectionNumber());
    }

    @Test
    public void getAllRecipeDirections_ReturnsNull(){
        int returned = testDatabase.addRecipe(testRecipe);
        testDatabase.deleteRecipeDirections(returned);
        assertEquals("getAllRecipeCategories - Empty List Returned", null, testDatabase.getAllRecipeDirections(returned));
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
        //Retrieved should now contain all the same information as testRecipe
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
        assertEquals("getAllRecipes - Empty List Returned", null, testDatabase.getAllRecipes());
    }
}
