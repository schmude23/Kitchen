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
     * This method checks that loginCheck returns an int != -1 when checking for a user/password
     * combo that exists in the database
     */
    @Test
    public void loginCheck_ReturnsTrue(){
        setUp();
        testDatabase.deleteUser(testDatabase.getUser("user"));
        int userId = testDatabase.addUser("user", "pass");
        assertNotEquals("loginCheck - Returns True", -1, testDatabase.loginCheck("user", "pass"));
        testDatabase.deleteUser(userId);
        tearDown();
    }

    /**
     * This method checks that loginCheck returns -1 when checking for a user/password
     * that does not exist in the database
     */
    @Test
    public void loginCheck_ReturnsFalse(){
        setUp();
        assertEquals("loginCheck - Returns False", -1, testDatabase.loginCheck("username", "password"));
        tearDown();
    }

    /**
     * This method checks that addUser() returns an int != -1 when passed a username that doesn't
     * already exist
     */
    @Test
    public void addUser_ReturnsTrue(){
        setUp();
        testDatabase.deleteUser(testDatabase.getUser("user"));
        int userId = testDatabase.addUser("user", "pass");
        assertNotEquals("addUser - Returns True", -1, userId);
        testDatabase.deleteUser(userId);
        tearDown();
    }

    /**
     * This method checks that addUser() returns -1 when user already exists
     */
    @Test
    public void addUser_ReturnsFalse(){
        setUp();
        testDatabase.deleteUser(testDatabase.getUser("user"));
        int userId = testDatabase.addUser("user", "pass");
        int userId2 = testDatabase.addUser("user", "password");
        assertEquals("addUser - Returns False", -1, userId2);
        testDatabase.deleteUser(userId);
        tearDown();
    }

    /**
     * This method checks that addUser() updates the database
     */
    @Test
    public void addUser_UpdatedDatabase(){
        setUp();
        testDatabase.deleteUser(testDatabase.getUser("user1"));
        int userId = testDatabase.addUser("user1", "pass");
        assertNotEquals("addUser - Adds to Database", -1, testDatabase.loginCheck("user1", "pass"));
        assertEquals("addUser - Adds Correctly", userId, testDatabase.loginCheck("user1", "pass"));
        testDatabase.deleteUser(userId);
        tearDown();
    }

    /**
     * This method checks that getUser() returns true (an int != -1) when getting a user that already exists
     */
    @Test
    public void getUser_ReturnsTrue(){
        setUp();
        testDatabase.deleteUser(testDatabase.getUser("user"));
        int userId = testDatabase.addUser("user", "pass");
        assertNotEquals("getUser - Returns True", -1, testDatabase.getUser("user"));
        testDatabase.deleteUser(userId);
        tearDown();
    }

    /**
     * This method checks that getUser() returns -1 when attempting to get a user that does not exist in the database
     */
    @Test
    public void getUser_ReturnsFalse(){
        setUp();
        assertEquals("getUser - Returns False", -1, testDatabase.getUser("username"));
        tearDown();
    }

    /**
     * This method checks that getUser() returns the correct UserId when getting a user that already exists
     */
    @Test
    public void getUser_CorrectUser(){
        setUp();
        testDatabase.deleteUser(testDatabase.getUser("u"));
        int user = testDatabase.addUser("u", "pass");
        int returned = testDatabase.getUser("u");
        assertEquals("getUser - Returns Correct User", user, returned);
        testDatabase.deleteUser(user);
        tearDown();
    }

    /**
     * This method checks that editUser returns true if the user exists
     */
    @Test
    public void editUser_ReturnsTrue(){
        setUp();
        int userId = testDatabase.addUser("user", "pass");
        assertEquals("editUser - Returns True", true, testDatabase.editUser("user", "pass", "user", "pass", 1));
        testDatabase.deleteUser(userId);
        tearDown();
    }

    /**
     * This method checks that editUser() returns false if the user does not exist
     */
    @Test
    public void editUser_ReturnsFalse(){
        setUp();
        assertEquals("editUser - Returns False", false, testDatabase.editUser("username", "password", "username", "password", 0));
        tearDown();
    }

    /**
     * This method checks that editUser() updates the user's information in the database table correctly
     */
    @Test
    public void editUser_Updates(){
        tearDown();
        setUp();
        int userId = testDatabase.addUser("testUser", "testPass");
        testDatabase.editUser("testUser", "testPass", "test", "test", 0);
        assertEquals("editUser - Deleted Old User/Pass", -1, testDatabase.loginCheck("testUser", "testPass"));
        assertNotEquals("editUser - Changed to New User/Pass", -1, testDatabase.loginCheck("test", "test"));
        testDatabase.deleteUser(userId);
        tearDown();
    }

    /**
     * This method checks that the username and password did not get updated when passed the same
     * username and password that previously existed
     */
    @Test
    public void editUser_DoesNotChange(){
        setUp();
        int userId = testDatabase.addUser("user", "pass");
        testDatabase.editUser("user", "pass", "user", "pass", 1);
        assertNotEquals("editUser - Did Not Change User/Pass", -1, testDatabase.loginCheck("user", "pass"));
        testDatabase.deleteUser(userId);
        tearDown();
    }

    /**
     * This method checks that deleteUser() returns true when passed a userId that exists in the database
     */
    @Test
    public void deleteUser_ReturnsTrue(){
        setUp();
        int userId = testDatabase.addUser("user", "pass");
        assertEquals("deleteUser - Returns True", true, testDatabase.deleteUser(userId));
        tearDown();
    }

    /**
     * This method checks that deleteUser() returns false when passed a userId that does not exist
     * in the database
     */
    @Test
    public void deleteUser_ReturnsFalse(){
        setUp();
        assertEquals("deleteUser - Returns False", false, testDatabase.deleteUser(Integer.MAX_VALUE));
        tearDown();
    }

    /**
     * This method checks that deleteUser() deletes the user info from the table
     */
    @Test
    public void deleteUser_Deletes(){
        tearDown();
        setUp();
        int userId = testDatabase.addUser("deleteUser", "pass");
        testDatabase.deleteUser(userId);
        assertEquals("deleteUser - Deletes", -1, testDatabase.getUser("deleteUser"));
        assertEquals("deleteUser - Deletes", -1, testDatabase.loginCheck("deleteUser", "pass"));
        tearDown();
    }

    /**
     * This method checks that getRandomRecipes returns all recipes if <50 in a random order
     */
    @Test
    public void chooseRandom_ReturnsRecipes(){
        tearDown();
        setUp();
        createRecipe_TwoIngredients();
        ArrayList<Recipe> randoms = testDatabase.getRandomRecipes();
        assertEquals("getRandomRecipes - Returns all recipes if < 50", 2, randoms.size());
        boolean correct = false;
        if(randoms.get(0).getKeyID() == recipeID){
            if(randoms.get(1).getKeyID() == recipeID2)
                correct = true;
        }
        else if(randoms.get(1).getKeyID() == recipeID){
            if(randoms.get(0).getKeyID() == recipeID2)
                correct = true;
        }
        assertEquals("getRandomRecipes - Returns correct recipes if < 50", true, correct);
        tearDown();
    }

    /**
     * This method checks that getRandomRecipes() returns null when there are no recipes in the database
     */
    @Test
    public void chooseRandom_ReturnsNull(){
        tearDown();
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
        tearDown();
        setUp();
        createRecipe_TwoIngredients();
        ArrayList<Recipe> sorted = testDatabase.getAllRecipesSorted();
        assertEquals("getAllRecipesSorted - Number of Recipes", 2 , sorted.size());
        assertEquals("getAllRecipesSorted - 1st Recipe Title", recipeTitle, sorted.get(0).getTitle());
        assertEquals("getAllRecipesSorted - 1st Recipe Servings", 1, sorted.get(0).getServings(), 0);
        assertEquals("getAllRecipesSorted - 1st Recipe Prep_Time", 30, sorted.get(0).getPrep_time(), 0);
        assertEquals("getAllRecipesSorted - 1st Recipe Total_Time", 60, sorted.get(0).getTotal_time(), 0);
        assertEquals("getAllRecipesSorted - 1st Recipe Favorited", false, sorted.get(0).getFavorited());
        ArrayList<RecipeIngredient> recipe1Ingredients = testDatabase.getAllRecipeIngredients(recipeID);
        assertEquals("getAllRecipesSorted - 1st RecipeIngredient Units", "cup(s)", recipe1Ingredients.get(0).getUnit());
        assertEquals("getAllRecipesSorted - 1st RecipeIngredient Quantity", 2, recipe1Ingredients.get(0).getQuantity(), 0);
        assertEquals("getAllRecipesSorted - 1st RecipeIngredient Details", "White Flour", recipe1Ingredients.get(0).getDetails());
        ArrayList<RecipeDirection> recipe1Directions = testDatabase.getAllRecipeDirections(recipeID);
        assertEquals("getAllRecipesSorted - 1st RecipeDirection1 Number", 1, recipe1Directions.get(0).getDirectionNumber());
        assertEquals("getAllRecipesSorted - 1st RecipeDirection1 Text", "TestDirection1", recipe1Directions.get(0).getDirectionText());
        assertEquals("getAllRecipesSorted - 1st RecipeDirection2 Number", 2, recipe1Directions.get(1).getDirectionNumber());
        assertEquals("getAllRecipesSorted - 1st RecipeDirection2 Text", "TestDirection2", recipe1Directions.get(1).getDirectionText());
        assertEquals("getAllRecipesSorted - 2nd Recipe Title", "TestRecipe2", sorted.get(1).getTitle());
        assertEquals("getAllRecipesSorted - 2nd Recipe Servings", 4, sorted.get(1).getServings(), 0);
        assertEquals("getAllRecipesSorted - 2nd Recipe Prep_Time", 5, sorted.get(1).getPrep_time(), 0);
        assertEquals("getAllRecipesSorted - 2nd Recipe Total_Time", 15, sorted.get(1).getTotal_time(), 0);
        assertEquals("getAllRecipesSorted - 2nd Recipe Favorited", false, sorted.get(1).getFavorited());
        ArrayList<RecipeIngredient> recipe2Ingredients = testDatabase.getAllRecipeIngredients(recipeID2);
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Units", "cup(s)", recipe2Ingredients.get(0).getUnit());
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Quantity", 1, recipe2Ingredients.get(0).getQuantity(), 0);
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Details", "", recipe2Ingredients.get(0).getDetails());
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Units", "cup(s)", recipe2Ingredients.get(1).getUnit());
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Quantity", 3.0, recipe2Ingredients.get(1).getQuantity(), 0);
        assertEquals("getAllRecipesSorted - 2nd RecipeIngredient Details", "Olive Oil Preferred", recipe2Ingredients.get(1).getDetails());
        ArrayList<RecipeDirection> recipe2Directions = testDatabase.getAllRecipeDirections(recipeID2);
        assertEquals("getAllRecipesSorted - 2nd RecipeDirection Number", 1, recipe2Directions.get(0).getDirectionNumber());
        assertEquals("getAllRecipesSorted - 2nd RecipeDirection Text", "TestDirection", recipe2Directions.get(0).getDirectionText());
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

    /**
     * This method tests that the Recipe(String) constructor is working.
     */
    @Test
    public void testCreateRecipeUsingString() {
        setUp();

        String initialStr = testRecipe.toString();
        Recipe retrieved = new Recipe(initialStr);

        // Check all recipe fields are accurate
        assertEquals("testCreateRecipeUsingString - Correct Title", testRecipe.getTitle(), retrieved.getTitle());
        assertEquals("testCreateRecipeUsingString - Correct Servings", testRecipe.getServings(), retrieved.getServings(), 0);
        assertEquals("testCreateRecipeUsingString - Correct prep_time", testRecipe.getPrep_time(), retrieved.getPrep_time(), 0);
        assertEquals("testCreateRecipeUsingString - Correct total_time", testRecipe.getTotal_time(), retrieved.getTotal_time(), 0);
        assertEquals("testCreateRecipeUsingString - Correct Favorited", testRecipe.getFavorited(), retrieved.getFavorited());
        assertEquals("testCreateRecipeUsingString - Correct Ingredient Unit", testRecipe.getIngredientList().get(0).getUnit(), retrieved.getIngredientList().get(0).getUnit());
        assertEquals("testCreateRecipeUsingString - Correct Ingredient Quantity", testRecipe.getIngredientList().get(0).getQuantity(), retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("testCreateRecipeUsingString - Correct Ingredient Details", testRecipe.getIngredientList().get(0).getDetails(), retrieved.getIngredientList().get(0).getDetails());
        assertEquals("testCreateRecipeUsingString - Correct First Direction Number", testRecipe.getDirectionsList().get(0).getDirectionNumber(), retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("testCreateRecipeUsingString - Correct First Direction Text", testRecipe.getDirectionsList().get(0).getDirectionText(), retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("testCreateRecipeUsingString - Correct Second Direction Number", testRecipe.getDirectionsList().get(1).getDirectionNumber(), retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("testCreateRecipeUsingString - Correct Second Direction Text", testRecipe.getDirectionsList().get(1).getDirectionText(), retrieved.getDirectionsList().get(1).getDirectionText());
        assertEquals("testCreateRecipeUsingString - Correct Category", testRecipe.getCategoryList().get(0).getCategoryID(), retrieved.getCategoryList().get(0).getCategoryID());

    }

}
