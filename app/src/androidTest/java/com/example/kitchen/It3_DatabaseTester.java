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

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was gallons, is now fluid ounces
     */
    @Test
    public void convertRecipeIngredientUnits_GallonsToFluidOunces() {
        setUp();
        createRecipe_DifferentUnits("gallon(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "gallon(s)", "fluid ounce(s)");
        assertEquals("convertRecipeIngredientUnits_GallonsToFluidOunces - Quantity", 768, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_GallonsToFluidOunces - Unit", "fluid ounce(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was fluid ounces, is now cups
     */
    @Test
    public void convertRecipeIngredientUnits_FluidOuncesToCups() {
        setUp();
        createRecipe_DifferentUnits("fluid ounce(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "fluid ounce(s)", "cup(s)");
        assertEquals("convertRecipeIngredientUnits_FluidOuncesToCups - Quantity", 0.75, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_FluidOuncesToCups - Unit", "cup(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was pinches, is now tablespoons
     */
    @Test
    public void convertRecipeIngredientUnits_PinchesToTablespoons() {
        setUp();
        createRecipe_DifferentUnits("pinch(es)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "pinch(es)", "tablespoon(s)");
        assertEquals("convertRecipeIngredientUnits_PinchesToTablespoons - Quantity", 0.13, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_PinchesToTablespoons - Unit", "tablespoon(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was tablespoons, is now pinches
     */
    @Test
    public void convertRecipeIngredientUnits_TablespoonsToPinches() {
        setUp();
        createRecipe_DifferentUnits("tablespoon(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "tablespoon(s)", "pinch(es)");
        assertEquals("convertRecipeIngredientUnits_TablespoonsToPinches - Quantity", 288, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_TablespoonsToPinches - Unit", "pinch(es)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was kilograms, is now milligrams
     */
    @Test
    public void convertRecipeIngredientUnits_KilogramsToMilligrams() {
        setUp();
        createRecipe_DifferentUnits("kilogram(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "kilogram(s)", "milligram(s)");
        assertEquals("convertRecipeIngredientUnits_KilogramToMilligram - Quantity", 6000000, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_KilogramToMilligram - Unit", "milligram(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was milligrams, is now kilograms
     */
    @Test
    public void convertRecipeIngredientUnits_MilligramsToKilograms() {
        setUp();
        createRecipe_DifferentUnits("milligram(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "milligram(s)", "kilogram(s)");
        assertEquals("convertRecipeIngredientUnits_MilligramsToKilograms - Quantity", 0, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_MilligramsToKilograms - Unit", "kilogram(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was ounces, is now pounds
     */
    @Test
    public void convertRecipeIngredientUnits_OuncesToPounds() {
        setUp();
        createRecipe_DifferentUnits("ounce(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "ounce(s)", "pound(s)");
        assertEquals("convertRecipeIngredientUnits_OuncesToPounds - Quantity", 0.37, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_OuncesToPounds - Unit", "pound(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was pounds, is now ounces
     */
    @Test
    public void convertRecipeIngredientUnits_PoundsToOunces() {
        setUp();
        createRecipe_DifferentUnits("pound(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "pound(s)", "ounce(s)");
        assertEquals("convertRecipeIngredientUnits_PoundsToOunces - Quantity", 96, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_PoundsToOunces - Unit", "ounce(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was ounces, is now grains
     */
    @Test
    public void convertRecipeIngredientUnits_OuncesToGrains() {
        setUp();
        createRecipe_DifferentUnits("ounce(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "ounce(s)", "grain(s)");
        assertEquals("convertRecipeIngredientUnits_OuncesToGrains - Quantity", 2625, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_OuncesToGrains - Unit", "grain(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was grains, is now ounces
     */
    @Test
    public void convertRecipeIngredientUnits_GrainsToOunces() {
        setUp();
        createRecipe_DifferentUnits("grain(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "grain(s)", "ounce(s)");
        assertEquals("convertRecipeIngredientUnits_GrainsToOunces - Quantity", 0.01, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_GrainsToOunces - Unit", "ounce(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was grams, is now milligrams
     */
    @Test
    public void convertRecipeIngredientUnits_GramsToMilligrams() {
        setUp();
        createRecipe_DifferentUnits("gram(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "gram(s)", "milligram(s)");
        assertEquals("convertRecipeIngredientUnits_GramsToMilligrams - Quantity", 6000, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_GramsToMilligrams - Unit", "milligram(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method checks that convertRecipeIngredientUnits returns a Recipe with the units correctly
     * changes
     * This method tests convertUnits was kilograms, is now grams
     */
    @Test
    public void convertRecipeIngredientUnits_KilogramsToGrams() {
        setUp();
        createRecipe_DifferentUnits("kilogram(s)");
        Recipe returned = testDatabase.convertRecipeIngredientUnits(testRecipe2, "kilogram(s)", "gram(s)");
        assertEquals("convertRecipeIngredientUnits_KilogramsToGrams - Quantity", 6000, returned.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("convertRecipeIngredientUnits_KilogramsToGrams - Unit", "gram(s)", returned.getIngredientList().get(0).getUnit());
        tearDown();
    }

    /**
     * This method tests that getRecipesByFavorite returns null if no recipes are favorited
     */
    @Test
    public void getFavoritedRecipes_ReturnsNull(){
        setUp();
        assertEquals("getFavoritedRecipes - Returns Null", null, testDatabase.getRecipesByFavorite());
        tearDown();
    }

    /**
     * This method tests that getRecipesByFavorite returns a list of recipes if at least one recipe is favorited
     */
    @Test
    public void getFavoritedRecipes_ReturnsList(){
        setUp();
        createRecipe_Favorited();
        assertNotEquals("getFavoritedRecipes - Returns List", null, testDatabase.getRecipesByFavorite());
        tearDown();
    }

    /**
     * This method test that getRecipesByFavorite returns a correct list of favorited recipes
     */
    @Test
    public void getFavoritedRecipes_CorrectInformation(){
        setUp();
        createRecipe_Favorited();
        createRecipe_Favorited();
        List<Recipe> returned = testDatabase.getRecipesByFavorite();
        assertEquals("getFavoritedRecipes - Returns 2", 2, returned.size());
        assertEquals("getFavoritedRecipes - 1st Recipe Title", "TestRecipe2", returned.get(0).getTitle());
        assertEquals("getFavoritedRecipes - 1st Recipe Servings", 4, returned.get(0).getServings(), 0);
        assertEquals("getFavoritedRecipes - 1st Recipe Prep_Time", 5, returned.get(0).getPrep_time(), 0);
        assertEquals("getFavoritedRecipes - 1st Recipe Total_Time", 15, returned.get(0).getTotal_time(), 0);
        assertEquals("getFavoritedRecipes - 1st Recipe Favorited", true, returned.get(0).getFavorited());
        ArrayList<RecipeIngredient> recipe1Ingredients = testDatabase.getAllRecipeIngredients(recipeID2);
        assertEquals("getFavoritedRecipes - 1st RecipeIngredient Units", "cup(s)", recipe1Ingredients.get(0).getUnit());
        assertEquals("getFavoritedRecipes - 1st RecipeIngredient Quantity", 1, recipe1Ingredients.get(0).getQuantity(), 0);
        assertEquals("getFavoritedRecipes - 1st RecipeIngredient Details", "", recipe1Ingredients.get(0).getDetails());
        ArrayList<RecipeDirection> recipe1Directions = testDatabase.getAllRecipeDirections(recipeID2);
        assertEquals("getFavoritedRecipes - 1st RecipeDirection Number", 1, recipe1Directions.get(0).getDirectionNumber());
        assertEquals("getFavoritedRecipes - 1st RecipeDirection Text", "TestDirection", recipe1Directions.get(0).getDirectionText());
        assertEquals("getFavoritedRecipes - 2nd Recipe Title", "TestRecipe2", returned.get(1).getTitle());
        assertEquals("getFavoritedRecipes - 2nd Recipe Servings", 4, returned.get(1).getServings(), 0);
        assertEquals("getFavoritedRecipes - 2nd Recipe Prep_Time", 5, returned.get(1).getPrep_time(), 0);
        assertEquals("getFavoritedRecipes - 2nd Recipe Total_Time", 15, returned.get(1).getTotal_time(), 0);
        assertEquals("getFavoritedRecipes - 2nd Recipe Favorited", true, returned.get(1).getFavorited());
        ArrayList<RecipeIngredient> recipe2Ingredients = testDatabase.getAllRecipeIngredients(recipeID2);
        assertEquals("getFavoritedRecipes - 2nd RecipeIngredient Units", "cup(s)", recipe2Ingredients.get(0).getUnit());
        assertEquals("getFavoritedRecipes - 2nd RecipeIngredient Quantity", 1, recipe2Ingredients.get(0).getQuantity(), 0);
        assertEquals("getFavoritedRecipes - 2nd RecipeIngredient Details", "", recipe2Ingredients.get(0).getDetails());
        ArrayList<RecipeDirection> recipe2Directions = testDatabase.getAllRecipeDirections(recipeID2);
        assertEquals("getFavoritedRecipes - 2nd RecipeDirection Number", 1, recipe2Directions.get(0).getDirectionNumber());
        assertEquals("getFavoritedRecipes - 2nd RecipeDirection Text", "TestDirection", recipe2Directions.get(0).getDirectionText());
        tearDown();
    }

    /**
     * This method tests that getAllCategories returns null when no categories have been created
     */
    @Test
    public void getAllCategories_ReturnsNull(){
        assertEquals("getAllCategories - Returns Null", null, testDatabase.getAllCategories());
    }

    /**
     * This method tests that getAllCategories returns a list when at least one category has been created
     */
    @Test
    public void getAllCategories_ReturnsList(){
        setUp();
        assertNotEquals("getAllCategories - Returns List", null, testDatabase.getAllCategories());
        tearDown();
    }

    /**
     * This method tests that getAllCategories returns correct information when at least one category has been created
     */
    @Test
    public void getAllCategories_CorrectInfo(){
        setUp();
        createRecipe_Favorited();
        ArrayList<Category> returned = testDatabase.getAllCategories();
        assertEquals("getAllCategories - List size", 1, returned.size());
        assertEquals("getAllCategories - Returns Correct Category Name", "Lunch", returned.get(0).getName());
        tearDown();
    }

    /**
     * This method tests that editIngredient returns false if trying to change an ingredient to something
     * that already exists
     */
    @Test
    public void editIngredient_ReturnsFalse(){
        setUp();
        Ingredient editedIngredient = new Ingredient(-1, "Flour");
        assertEquals("editIngredient - Returns False", false, testDatabase.editIngredient(editedIngredient));
        tearDown();
    }

    /**
     * This method tests that the Recipe(String) constructor is working.
     */
    @Test
    public void testCreateRecipeUsingString() {
        setUp();

        String initialStr = testRecipe.toString(appContext);
        Recipe retrieved = new Recipe(initialStr, appContext);

        // Check all recipe fields are accurate
        assertEquals("testCreateRecipeUsingString - Correct Title", testRecipe.getTitle(), retrieved.getTitle());
        assertEquals("testCreateRecipeUsingString - Correct Servings", testRecipe.getServings(), retrieved.getServings(), 0);
        assertEquals("testCreateRecipeUsingString - Correct prep_time", testRecipe.getPrep_time(), retrieved.getPrep_time(), 0);
        assertEquals("testCreateRecipeUsingString - Correct total_time", testRecipe.getTotal_time(), retrieved.getTotal_time(), 0);
        assertEquals("testCreateRecipeUsingString - Correct Ingredient Unit", testRecipe.getIngredientList().get(0).getUnit(), retrieved.getIngredientList().get(0).getUnit());
        assertEquals("testCreateRecipeUsingString - Correct Ingredient Quantity", testRecipe.getIngredientList().get(0).getQuantity(), retrieved.getIngredientList().get(0).getQuantity(), 0);
        assertEquals("testCreateRecipeUsingString - Correct Ingredient Details", testRecipe.getIngredientList().get(0).getDetails(), retrieved.getIngredientList().get(0).getDetails());
        assertEquals("testCreateRecipeUsingString - Correct First Direction Number", testRecipe.getDirectionsList().get(0).getDirectionNumber(), retrieved.getDirectionsList().get(0).getDirectionNumber());
        assertEquals("testCreateRecipeUsingString - Correct First Direction Text", testRecipe.getDirectionsList().get(0).getDirectionText(), retrieved.getDirectionsList().get(0).getDirectionText());
        assertEquals("testCreateRecipeUsingString - Correct Second Direction Number", testRecipe.getDirectionsList().get(1).getDirectionNumber(), retrieved.getDirectionsList().get(1).getDirectionNumber());
        assertEquals("testCreateRecipeUsingString - Correct Second Direction Text", testRecipe.getDirectionsList().get(1).getDirectionText(), retrieved.getDirectionsList().get(1).getDirectionText());
        assertEquals("testCreateRecipeUsingString - Correct Category", testRecipe.getCategoryList().get(0).getCategoryID(), retrieved.getCategoryList().get(0).getCategoryID());

        tearDown();
    }

    /**
     * This method tests that the Recipe Copy Paste Creator constructor is working.
     */
    @Test
    public void testRecipeCopyPasteCreator() {
        RecipeCopyPasteCreator rcp = new RecipeCopyPasteCreator(appContext.getApplicationContext());
        String test = "Overnight Apple Cinnamon French Toast\n" +
                "Servings\n" +
                "10\n" +
                "Prep\n" +
                "\n" +
                "20 m\n" +
                "Cook\n" +
                "\n" +
                "1 h 30 m\n" +
                "Ready In\n" +
                "\n" +
                "15 h\n" +
                "Recipe By:CALJAKE\n" +
                "This is a great brunch recipe, it's perfect for large gatherings. A yummy bread casserole baked with sweet apple filling.\n" +
                "Ingredients\n" +
                "1 1/2 cups milk\n" +
                "3/4 cup butter, melted\n" +
                "1 cup brown sugar\n" +
                "1 teaspoon ground cinnamon\n" +
                "2 (21 ounce) cans apple pie filling\n" +
                "20 slices white bread\n" +
                " \n" +
                "6 eggs\n" +
                "1 teaspoon vanilla extract\n" +
                "1/2 cup maple syrup\n" +
                "Directions\n" +
                "Grease a 9x13 inch baking pan. In a small bowl, stir together the melted butter, brown sugar and cinnamon.\n" +
                "Spread the sugar mixture into the bottom of the prepared pan. Spread the apple pie filling evenly over the sugar mixture. Layer the bread slices on top of the filling, pressing down as you go. In a medium bowl, beat the eggs with the milk and vanilla. Slowly pour this mixture over the bread, making sure that it is completely absorbed. Cover the pan with aluminum foil and refrigerate overnight.\n" +
                "In the morning, preheat oven to 350 degrees F (175 degrees C).\n" +
                "Place covered pan into the oven and bake at 350 degrees F (175 degrees C) for 60 to 75 minutes. When done remove from oven and turn on broiler. Remove foil and drizzle maple syrup on top of the egg topping; broil for 2 minutes, or until the syrup begins to caramelize. Remove from the oven and let stand for 10 minutes, then cut into squares. Invert the pan onto a serving tray or baking sheet so the apple filling is on top. Serve hot.";
        rcp.main(test);

        test = "South African Melktert (Milk Tart)\n" +
                "Rated as 4.18 out of 5 Stars\n" +
                "Prep\n" +
                "\n" +
                "30 m\n" +
                "Cook\n" +
                "\n" +
                "40 m\n" +
                "Ready In\n" +
                "\n" +
                "1 h 10 m\n" +
                "Recipe By:TWAKMUIS\n" +
                "\"My 'ouma' South African grandmother's legendary milk tart. It is lip-smacking. The recipe is a real winner. It is a traditional South African tart that is very easy to prepare.\"\n" +
                "Ingredients\n" +
                "3 tablespoons butter, melted\n" +
                "1 cup white sugar\n" +
                "3 egg yolks\n" +
                "1 cup cake flour\n" +
                "1 teaspoon baking powder\n" +
                " \n" +
                "1/4 teaspoon salt\n" +
                "1 teaspoon vanilla extract\n" +
                "4 cups milk\n" +
                "3 egg white\n" +
                "1 tablespoon cinnamon sugar\n" +
                "Directions\n" +
                "Preheat the oven to 375 degrees F (190 degrees C). Coat a 9 inch deep dish pie plate with vegetable oil cooking spray.\n" +
                "In a large bowl, mix together the butter and sugar until smooth. Add the egg yolks and beat until light and fluffy. Sift in the cake flour, baking powder and salt, and stir until well blended. Mix in the vanilla and milk. In a separate bowl, whip the egg whites to stiff peaks using an electric mixer. Fold into the batter. Pour into the prepared pie plate, and sprinkle cinnamon sugar over the top.\n" +
                "Bake for 25 minutes in the preheated oven, then reduce the temperature to 325 degrees F (165 degrees C). Continue to bake for 25 to 30 minutes, or until the center is set when you gently jiggle the pie. Serve hot or cold.";
        rcp.main(test);

        test = "Easy One Pot Chicken Burrito Bowl\n" +
                " Prep Time 10 minutes\n" +
                " Cook Time 20 minutes\n" +
                " Total Time 30 minutes\n" +
                " Servings 6\n" +
                " AuthorLayla\n" +
                "Ingredients\n" +
                "1 pound chicken breasts cubed into 1 inch pieces\n" +
                "1/2 cup diced onion\n" +
                "1 Tablespoon olive oil\n" +
                "1 15 oz can black beans rinsed and drained\n" +
                "1 15 oz can corn drained\n" +
                "1 14.5 oz can diced tomatoes\n" +
                "1 4 oz can diced green chiles\n" +
                "1/4 cup Chunky Salsa\n" +
                "1 cup jasmine or Basmati rice I used Basmati\n" +
                "2 Tablespoons taco seasoning\n" +
                "1 teaspoon cumin\n" +
                "1/2 teaspoon chili powder\n" +
                "1/2 teaspoon salt\n" +
                "1/2 teaspoon pepper\n" +
                "2 cups chicken stock or water\n" +
                "1 cup shredded cheddar/jack cheese\n" +
                "Optional toppings:\n" +
                "Tomatoes\n" +
                "Avocado\n" +
                "Sour cream\n" +
                "Cilantro\n" +
                "guacamole\n" +
                "Instructions\n" +
                "Heat a large pan or pot to medium heat. Add the cubed chicken, onion and olive oil. Saute for 2-3 minutes or until the chicken is cooked and the onions are lightly browned. Add the black beans, corn, tomatoes, green chiles,salsa, jasmine rice, taco seasoning,cumin, chili powder and salt & pepper. Pour in chicken stock or water and then bring to a light boil. Cover the pan and reduce heat to low. Cook for 15-20 minutes, or until the rice is all the way cooked.\n" +
                "Top with your favorite toppings.";
        rcp.main(test);
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
     * Helper method for tests that need a recipe that is favorited
     */
    public void createRecipe_Favorited(){
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
        List<RecipeIngredient> listOfIngredients = new ArrayList<RecipeIngredient>();
        listOfIngredients.add(recipeIngredient2);

        RecipeDirection recipeDirection = new RecipeDirection(-1, -1, "TestDirection", 1);
        List<RecipeDirection> listOfDirections = new ArrayList<RecipeDirection>();
        listOfDirections.add(recipeDirection);

        testRecipe2 = new Recipe("TestRecipe2", 4, 5, 15, true, listOfIngredients, listOfDirections, listOfCategories);
        recipeID2 = testDatabase.addRecipe(testRecipe2);
    }
}
