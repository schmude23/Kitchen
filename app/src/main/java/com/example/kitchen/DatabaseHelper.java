package com.example.kitchen;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

//create read update delete
public class DatabaseHelper extends SQLiteOpenHelper {

    //The app context
    private Context context;

    public static final int VERSION_NUMBER = 5;
    public static final String DATABASE_NAME = "RECIPE_DATABASE";

    //Ingredient Table (Uses prefix IT)
    private static final String TABLE_INGREDIENT_LIST = "INGREDIENT_LIST";
    public static final String IT_KEY_ID = "ID";
    public static final String IT_NAME = "NAME";

    //Recipe Ingredient Table (Uses prefix RI)
    private static final String TABLE_RECIPE_INGREDIENT_LIST = "RECIPE_INGREDIENT_LIST";
    public static final String RI_KEY_ID = "ID";
    public static final String RI_RECIPE_ID = "RECIPE_ID";
    public static final String RI_INGREDIENT_ID = "INGREDIENT_ID";
    public static final String RI_QUANTITY = "QUANTITY";
    public static final String RI_UNIT = "UNIT";
    public static final String RI_DETAILS = "DETAILS";

    //Recipe Table (Uses prefix RT)
    private static final String TABLE_RECIPE_LIST = "RECIPE_LIST";
    public static final String RT_KEY_ID = "ID";
    public static final String RT_TITLE = "TITLE";
    public static final String RT_TOTAL_TIME = "TOTAL_TIME";
    public static final String RT_PREP_TIME = "PREP_TIME";
    public static final String RT_SERVINGS = "SERVINGS";
    public static final String RT_FAVORITED = "FAVORITED";

    //Recipe Directions Table (Uses prefix RD)
    private static final String TABLE_RECIPE_DIRECTIONS_LIST = "DIRECTION_LIST";
    public static final String RD_KEY_ID = "ID";
    public static final String RD_RECIPE_ID = "RECIPE_ID";
    public static final String RD_DIRECTION_TEXT = "DIRECTION_TEXT";
    public static final String RD_DIRECTION_NUMBER = "DIRECTION_NUMBER";

    //Category Table (Uses prefix CT)
    private static final String TABLE_CATEGORY_LIST = "CATEGORY_LIST";
    public static final String CT_KEY_ID = "ID";
    public static final String CT_NAME = "NAME";

    //Recipe Category Table (Uses prefix RC)
    private static final String TABLE_RECIPE_CATEGORY_LIST = "RECIPE_CATEGORY_LIST";
    public static final String RC_KEY_ID = "ID";
    public static final String RC_RECIPE_ID = "RECIPE_ID";
    public static final String RC_CATEGORY_ID = "CATEGORY_ID";

    //Shopping Cart Table (Uses prefix SC)
    private static final String TABLE_SHOPPING_CART_LIST = "SHOPPING_CART_LIST";
    public static final String SC_KEY_ID = "ID";
    public static final String SC_INGREDIENT_ID = "INGREDIENT_ID";
    public static final String SC_QUANTITY = "QUANTITY";
    public static final String SC_UNIT = "UNIT";

    //User Table (Uses prefix UI)
    private static final String TABLE_USER_INFO = "USER_INFO";
    public static final String UI_KEY_ID = "ID";
    public static final String UI_USERNAME = "USERNAME";
    public static final String UI_PASSWORD = "PASSWORD";
    public static final String UI_THEME = "THEME";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context = context;
    }

    /**
     * Runs on creation of the database.
     * This method does all of the setup necessary for the database such as creating tables.
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Recipe Table
        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_RECIPE_LIST + "("
                + RT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RT_TITLE + " TEXT,"
                + RT_TOTAL_TIME + " INTEGER,"
                + RT_PREP_TIME + " INTEGER,"
                + RT_SERVINGS + " INTEGER,"
                + RT_FAVORITED + " INTEGER " + ")";
        sqLiteDatabase.execSQL(CREATE_RECIPE_TABLE);

        //Ingredients Table
        String CREATE_INGREDIENT_TABLE = "CREATE TABLE " + TABLE_INGREDIENT_LIST + "("
                + IT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + IT_NAME + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_INGREDIENT_TABLE);

        //Recipe Ingredient Table
        String CREATE_RECIPE_INGREDIENT_TABLE = "CREATE TABLE " + TABLE_RECIPE_INGREDIENT_LIST + "("
                + RI_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RI_RECIPE_ID + " INTEGER, "
                + RI_INGREDIENT_ID + " INTEGER,"
                + RI_QUANTITY + " DECIMAL,"
                + RI_UNIT + " TEXT,"
                + RI_DETAILS + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_RECIPE_INGREDIENT_TABLE);

        //Recipe Directions Table
        String CREATE_RECIPE_DIRECTIONS_TABLE = "CREATE TABLE " + TABLE_RECIPE_DIRECTIONS_LIST + "("
                + RD_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RD_RECIPE_ID + " INTEGER, "
                + RD_DIRECTION_TEXT + " TEXT,"
                + RD_DIRECTION_NUMBER + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_RECIPE_DIRECTIONS_TABLE);

        //Category Table
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY_LIST + "("
                + CT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CT_NAME + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);

        //Recipe Category Table
        String CREATE_RECIPE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_RECIPE_CATEGORY_LIST + "("
                + RC_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RC_RECIPE_ID + " INTEGER, "
                + RC_CATEGORY_ID + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_RECIPE_CATEGORY_TABLE);

        //Shopping Cart Table
        String CREATE_SHOPPING_CART_TABLE = "CREATE TABLE " + TABLE_SHOPPING_CART_LIST + "("
                + SC_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SC_INGREDIENT_ID + " INTEGER, "
                + SC_QUANTITY + " INTEGER,"
                + SC_UNIT + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_SHOPPING_CART_TABLE);

        //User Info Table
        String CREATE_USER_INFO_TABLE = "CREATE TABLE " + TABLE_USER_INFO + "("
                + UI_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UI_USERNAME + " TEXT,"
                + UI_PASSWORD + " TEXT,"
                + UI_THEME + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_USER_INFO_TABLE);

    }

    /**
     * This method runs when the version of the database is updated.
     * This method should do all actions necessary to ensure that the database version is
     * updated correctly.
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //find and drop existing databases
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENT_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_CATEGORY_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_DIRECTIONS_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_CART_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INFO);
        //rebuild the database
        onCreate(sqLiteDatabase);
    }

    /**
     * This method creates a new row in the Recipe table using the provided Recipe
     * <p>
     * Proper usage:
     * To use this method you should create a recipe object. The recipeID in the object will
     * be ignored since adding the recipe to the table will generate a recipeID for it.
     *
     * @param recipe
     * @return true if the operation was successful, false otherwise
     */

    public int addRecipe(Recipe recipe) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        List<RecipeCategory> recipeCategoryList = recipe.getCategoryList();
        List<RecipeIngredient> recipeIngredientList = recipe.getIngredientList();
        List<RecipeDirection> recipeDirectionList = recipe.getDirectionsList();

        //updating recipe portion of table
        ContentValues cVals = new ContentValues();
        cVals.put(RT_TITLE, recipe.getTitle());
        cVals.put(RT_PREP_TIME, recipe.getPrep_time());
        cVals.put(RT_TOTAL_TIME, recipe.getTotal_time());
        cVals.put(RT_SERVINGS, recipe.getServings());
        cVals.put(RT_FAVORITED, recipe.getFavorited() ? 1 : 0);
        int res = (int) sqLiteDatabase.insert(TABLE_RECIPE_LIST, null, cVals);
        recipe.setKeyID(res);

        if (res == -1) {
            return -1;
        }

        //add new ingredients from ingredientList
        for (int i = 0; i < recipeIngredientList.size(); i++) {
            recipeIngredientList.get(i).setRecipeID(res);
            int result = addRecipeIngredient(recipeIngredientList.get(i));
        }

        //add new categorys from recipeCategoryList
        for (int i = 0; i < recipeCategoryList.size(); i++) {
            recipeCategoryList.get(i).setRecipeID(res);
            int result = addRecipeCategory(recipeCategoryList.get(i));
        }

        //add new directions from recipeDirectionList
        for (int i = 0; i < recipeDirectionList.size(); i++) {
            recipeDirectionList.get(i).setRecipeID(res);
            int result = addRecipeDirection(recipeDirectionList.get(i));
        }

        return res;
    }

    /**
     * This method retrieves the recipe for the given recipeId
     *
     * @param recipeId
     * @return The recipe corresponding to the provided recipeId, or null if one is not found.
     */
    public Recipe getRecipe(int recipeId) {
        Recipe recipe = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_LIST + "  WHERE " + RT_KEY_ID + " = ? ", new String[]{String.valueOf(recipeId)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            recipe = mapRecipe(cursor);
            cursor.moveToNext();

            cursor.close();
        }

        if (recipe != null) {
            //Getting Ingredient List
            ArrayList<RecipeIngredient> recipeIngredientList = getAllRecipeIngredients(recipeId);
            recipe.setIngredientList(recipeIngredientList);

            //Getting Direction List
            ArrayList<RecipeDirection> recipeDirectionList = getAllRecipeDirections(recipeId);
            recipe.setDirectionsList(recipeDirectionList);

            //Getting Category List
            ArrayList<RecipeCategory> recipeCategoryList = getAllRecipeCategories(recipeId);
            recipe.setCategoryList(recipeCategoryList);
        }
        return recipe;
    }

    /**
     * This method retrieves the recipe for the given recipe Title
     *
     * @param recipeTitle
     * @return The recipe corresponding to the provided recipe Title, or null if one is not found.
     */
    public Recipe getRecipe(String recipeTitle) {
        Recipe recipe = null;
        SQLiteDatabase db = this.getReadableDatabase();
        recipeTitle = recipeTitle.toLowerCase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_LIST + "  WHERE lower(" + RT_TITLE + ") = ? ", new String[]{String.valueOf(recipeTitle)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            recipe = mapRecipe(cursor);
            cursor.moveToNext();

            cursor.close();
        }

        if (recipe != null) {
            int recipeId = recipe.getKeyID();

            //Getting Ingredient List
            ArrayList<RecipeIngredient> recipeIngredientList = getAllRecipeIngredients(recipeId);
            recipe.setIngredientList(recipeIngredientList);


            //Getting Direction List
            ArrayList<RecipeDirection> recipeDirectionList = getAllRecipeDirections(recipeId);
            recipe.setDirectionsList(recipeDirectionList);

            //Getting Category List
            ArrayList<RecipeCategory> recipeCategoryList = getAllRecipeCategories(recipeId);
            recipe.setCategoryList(recipeCategoryList);
        }

        return recipe;
    }

    /**
     * This method retrieves the recipe for the given recipe Title
     *
     * @param recipeTitle
     * @return The recipe List corresponding to the provided recipe Title, or null if one is not found.
     */
    public ArrayList<Recipe> getAllRecipesByTitle(String recipeTitle) {
        recipeTitle = recipeTitle.toLowerCase();
        Recipe recipe;
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_LIST + "  WHERE lower(" + RT_TITLE + ") LIKE ? ", new String[]{"%" + recipeTitle + "%"});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipe = mapRecipe(cursor);
                recipeList.add(recipe);
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (recipeList.size() == 0) {
            return null;
        }
        return recipeList;
    }

    /**
     * This method retrieves all recipes equal to or below the specified prep time
     *
     * @param prepTime
     * @return The recipe corresponding to the provided recipe Title, or null if one is not found.
     */
    public ArrayList<Recipe> getRecipeByPrepTime(int prepTime) {
        ArrayList<Recipe> recipeList = getAllRecipes();
        ArrayList<Recipe> newRecipeList = new ArrayList<>();
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getPrep_time() <= prepTime) {
                newRecipeList.add(recipeList.get(i));
            }
        }
        if (newRecipeList.size() == 0) {
            return null;
        }
        return newRecipeList;
    }

    /**
     * This method retrieves all recipes equal to or below the specified total time
     *
     * @param totalTime
     * @return The recipe corresponding to the provided recipe Title, or null if one is not found.
     */
    public ArrayList<Recipe> getRecipeByTotalTime(int totalTime) {
        ArrayList<Recipe> recipeList = getAllRecipes();
        ArrayList<Recipe> newRecipeList = new ArrayList<>();
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getTotal_time() <= totalTime) {
                newRecipeList.add(recipeList.get(i));
            }
        }
        if (newRecipeList.size() == 0) {
            return null;
        }
        return newRecipeList;
    }

    /**
     * This method retrieves all recipes that have been favorited
     *
     * @return The recipes that have favorite value of 1
     */
    public ArrayList<Recipe> getRecipesByFavorite() {
        Recipe recipe;
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_LIST + "  WHERE " + RT_FAVORITED + " = ? ", new String[]{String.valueOf(1)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipe = mapRecipe(cursor);
                recipeList.add(recipe);
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (recipeList.size() == 0) {
            return null;
        }
        return recipeList;
    }

    /**
     * This method retrieves all recipes which contain this ingredient id
     *
     * @param ingredientId
     * @return The recipes corresponding to the provided ingredient id, or null if one is not found.
     */
    public ArrayList<Recipe> getRecipeByIngredientId(int ingredientId) {
        RecipeIngredient recipeIngredient;
        ArrayList<RecipeIngredient> recipeIngredientList = new ArrayList<RecipeIngredient>();
        SQLiteDatabase db = this.getReadableDatabase();

        //pulling all recipe ingredients pertaining to ingredient id
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_INGREDIENT_LIST + "  WHERE " + RI_INGREDIENT_ID + " = ? ", new String[]{String.valueOf(ingredientId)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipeIngredient = mapRecipeIngredient(cursor);
                recipeIngredientList.add(recipeIngredient);
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (recipeIngredientList.size() == 0) {
            return null;
        }

        //finding and creating list of all found recipes
        //recipeIds in type String to utilize contains
        String[] recipeIds = new String[recipeIngredientList.size()];
        int count = 0; //used to store recipe at proper location

        for (int i = 0; i < recipeIngredientList.size(); i++) {
            int tempId = recipeIngredientList.get(i).getRecipeID();
            //if string is NOT contained then add to array list
            if (!Arrays.asList(recipeIds).contains(toString().valueOf(tempId))) {
                recipeIds[count] = toString().valueOf(tempId);
                count++;
            }
        }

        //create list of all found recipes (full recipe built)
        ArrayList<Recipe> recipeList = new ArrayList<>();
        for (int j = 0; j < recipeIds.length; j++) {
            recipeList.add(getRecipe(Integer.parseInt(recipeIds[j])));
        }

        return recipeList;
    }


    /**
     * This method retrieves the recipe for the given ingredient id
     *
     * @param ingredientIdList
     * @return The recipe corresponding to the provided ingredient id, or null if one is not found.
     */
    public ArrayList<Recipe> getRecipeByIngredientIdList(int[] ingredientIdList) {
        //could this be possible by just reusing getRecipeByIngredientId() and returning the
        //recipes that had matched through all of the tests?

        ArrayList<Recipe> recipeList = getRecipeByIngredientId(ingredientIdList[0]);
        if (recipeList == null) {
            return null;
        }

        //cycle through all ingredient ids
        for (int i = 1; i < ingredientIdList.length; i++) {
            ArrayList<Recipe> tmpList = getRecipeByIngredientId(ingredientIdList[i]);
            ArrayList<Recipe> containsList = new ArrayList<>();
            if (tmpList != null) {
                //cycle through all recipes which contain ingredient i
                for (int j = 0; j < tmpList.size(); j++) {
                    //cycle through all found recipes so far
                    for (int k = 0; k < recipeList.size(); k++) {
                        if (tmpList.get(j).getKeyID() == recipeList.get(k).getKeyID()) {
                            containsList.add(recipeList.get(k));
                        }
                    }
                }
            } else {
                return null;
            }
            recipeList = containsList;
        }


        return recipeList;
    }

    /**
     * This method retrieves the recipe for the given category id
     *
     * @param categoryId
     * @return The recipe corresponding to the provided category id, or null if one is not found.
     */
    public ArrayList<Recipe> getRecipeByCategoryId(int categoryId) {
        RecipeCategory recipeCategory;
        ArrayList<RecipeCategory> recipeCategoryList = new ArrayList<RecipeCategory>();
        SQLiteDatabase db = this.getReadableDatabase();

        //pulling all recipe categories pertaining to ingredient id
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_CATEGORY_LIST + "  WHERE " + RC_CATEGORY_ID + " = ? ", new String[]{String.valueOf(categoryId)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipeCategory = mapRecipeCategory(cursor);
                recipeCategoryList.add(recipeCategory);
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (recipeCategoryList.size() == 0) {
            return null;
        }


        //finding and creating list of all found recipes
        //recipeIds in type String to utilize contains
        String[] recipeIds = new String[recipeCategoryList.size()];
        int count = 0; //used to store recipe at proper location

        for (int i = 0; i < recipeCategoryList.size(); i++) {
            int tempId = recipeCategoryList.get(i).getRecipeID();
            //if string is NOT contained then add to array list
            if (!Arrays.asList(recipeIds).contains(toString().valueOf(tempId))) {
                recipeIds[count] = toString().valueOf(tempId);
                count++;
            }
        }

        //create list of all found recipes (full recipe built)
        ArrayList<Recipe> recipeList = new ArrayList<>();
        for (int j = 0; j < recipeIds.length; j++) {
            recipeList.add(getRecipe(Integer.parseInt(recipeIds[j])));
        }

        return recipeList;
    }


    /**
     * This method returns a list of all recipes
     *
     * @return If successful in fetching the recipes this method will return an Array list of
     * recipes, if not this method will return null.
     */
    public ArrayList<Recipe> getAllRecipes() {
        Recipe recipe;
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_LIST, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipe = mapRecipe(cursor);
                recipeList.add(recipe);
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (recipeList.size() == 0) {
            return null;
        }
        return recipeList;
    }

    /**
     * This method returns a list of all recipes
     *
     * @return If successful in fetching the recipes this method will return an Array list of
     * recipes, if not this method will return null.
     */
    public ArrayList<Recipe> getAllRecipesSorted() {
        ArrayList<Recipe> recipeList = getAllRecipes();

        if (recipeList == null) {
            return null;
        }
        //Arrays.sort(recipeList, new Comparator<Recipe>());
        Collections.sort(recipeList);

        return recipeList;
    }

    /**
     * This method returns a list of 50 max randomly assorted recipes
     *
     * @return If successful in fetching the recipes this method will return an Array list of
     * randomly assorted recipes, if not this method will return null.
     */
    public ArrayList<Recipe> getRandomRecipes() {
        ArrayList<Recipe> recipeList = getAllRecipes();

        if (recipeList == null) {
            return null;
        }

        ArrayList<Recipe> randList = new ArrayList<Recipe>();
        Random r = new Random();

        //I dont think we need to check for size as above would be negative.
        int size = recipeList.size();
        int checkSize = 50;
        if (size < 50) {
            checkSize = size;
        }

        for (int i = 0; i < checkSize; i++) {
            int randInt = r.nextInt(recipeList.size());
            //add random recipe to randList
            randList.add(recipeList.get(randInt));
            //delete specific recipe to avoid duplicates.
            recipeList.remove(recipeList.get(randInt));
        }

        return randList;
    }

    /**
     * This method modifies the recipe in the recipe table with the same recipeId as the recipe provided
     * to be the recipe provided
     *
     * @param recipe the provided recipe
     * @return true if the operation was successful, false otherwise.
     */
    public boolean editRecipe(Recipe recipe) {
        //I want to save a version of recipe incase something goes wrong.
        //if so, update with original recipe.
        //Recipe temp = getRecipe(recipe.getKeyID());
        int recipeId = recipe.getKeyID();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        List<RecipeIngredient> recipeIngredientList = recipe.getIngredientList();
        List<RecipeCategory> recipeCategoryList = recipe.getCategoryList();
        List<RecipeDirection> recipeDirectionList = recipe.getDirectionsList();

        //updating recipe portion of table
        ContentValues cVals = new ContentValues();
        cVals.put(RT_TITLE, recipe.getTitle());
        cVals.put(RT_PREP_TIME, recipe.getPrep_time());
        cVals.put(RT_TOTAL_TIME, recipe.getTotal_time());
        cVals.put(RT_SERVINGS, recipe.getServings());
        cVals.put(RT_FAVORITED, recipe.getFavorited() ? 1 : 0);
        long res = sqLiteDatabase.update(TABLE_RECIPE_LIST, cVals, IT_KEY_ID + " = ?", new String[]{String.valueOf(recipeId)});

        if (res != 1) {
            return false;
        }

        //delete ingredientlist and add new ingredientlist
        sqLiteDatabase.delete(TABLE_RECIPE_INGREDIENT_LIST, RI_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
        if (recipeIngredientList != null) {
            for (int i = 0; i < recipeIngredientList.size(); i++) {
                recipeIngredientList.get(i).setRecipeID(recipeId);
                int ingredientRes = addRecipeIngredient(recipeIngredientList.get(i));
                if (ingredientRes == -1) {
                    return false;
                }
            }
        }

        //delete recipecategorylist and add new recipecategorylist
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_RECIPE_CATEGORY_LIST, RC_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
        if (recipeCategoryList != null) {
            for (int i = 0; i < recipeCategoryList.size(); i++) {
                recipeCategoryList.get(i).setRecipeID(recipeId);
                int recipeCategoryResult = addRecipeCategory(recipeCategoryList.get(i));
                if (recipeCategoryResult == -1) {
                    return false;
                }
            }
        }

        //delete recipeDirections and add new recipeDirections
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_RECIPE_DIRECTIONS_LIST, RD_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
        if (recipeDirectionList != null) {
            for (int i = 0; i < recipeDirectionList.size(); i++) {
                recipeDirectionList.get(i).setRecipeID(recipeId);
                int recipeDirectionResult = addRecipeDirection(recipeDirectionList.get(i));
                if (recipeDirectionResult == -1) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * This method removes the recipe with the specified recipe id from the recipe's table and
     * removes all associated data from other tables
     *
     * @param recipeId
     * @return true if successful, false if not
     */
    public boolean deleteRecipe(int recipeId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //delete recipe
        long returned = sqLiteDatabase.delete(TABLE_RECIPE_LIST, IT_KEY_ID + " = ?", new String[]{String.valueOf(recipeId)});

        //delete recipeIngredients
        sqLiteDatabase.delete(TABLE_RECIPE_INGREDIENT_LIST, RI_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});

        //delete recipeCategories
        sqLiteDatabase.delete(TABLE_RECIPE_CATEGORY_LIST, RC_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});

        //delete recipeDirections
        sqLiteDatabase.delete(TABLE_RECIPE_DIRECTIONS_LIST, RD_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});

        //close writable database
        sqLiteDatabase.close();

        if (returned == 0) {
            return false;
        }

        return true;
    }

    /**
     * This method adds the recipe information to the shopping cart
     *
     * @param recipeId
     * @return true if the operation was successful, false otherwise
     */
    public boolean addRecipeToCart(int recipeId) {
        Recipe recipe = getRecipe(recipeId);
        if (recipe == null) {
            return false;
        }
        List<RecipeIngredient> recipeIngredientList = recipe.getIngredientList();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        for (int i = 0; i < recipeIngredientList.size(); i++) {
            RecipeIngredient recipeIngredient = recipeIngredientList.get(i);
            RecipeIngredient existCheck = getShoppingCartIngredient(recipeIngredient.getIngredientID());
            if (existCheck == null) {
                //Create a new map of values, where column names are the keys
                ContentValues contentValues = new ContentValues();
                contentValues.put(SC_INGREDIENT_ID, recipeIngredient.getIngredientID());
                contentValues.put(SC_QUANTITY, recipeIngredient.getQuantity());
                contentValues.put(SC_UNIT, recipeIngredient.getUnit());

                // Insert the new row, returning the primary key value of the new row
                int newRowId = (int) sqLiteDatabase.insert(TABLE_SHOPPING_CART_LIST, null, contentValues);

                //check to make sure properly inserted
                if (newRowId == -1) {
                    //TODO: if failure you need to delete ALL added ingredients using int[]
                    deleteShoppingCartIngredient(newRowId);
                    return false;
                }
            } else {
                double quantity = existCheck.getQuantity();
                recipeIngredient.setQuantity(recipeIngredient.getQuantity() + quantity);
                updateShoppingCartIngredient(recipeIngredient);
            }
        }

        sqLiteDatabase.close();
        return true;
    }

    /**
     * This method adds the recipe information to the shopping cart
     *
     * @return true if the operation was successful, false otherwise
     */
    public ArrayList<RecipeIngredient> getAllShoppingCartIngredients() {
        RecipeIngredient recipeIngredient;
        ArrayList<RecipeIngredient> shoppingCartList = new ArrayList<RecipeIngredient>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SHOPPING_CART_LIST, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipeIngredient = mapShoppingCartIngredient(cursor);
                shoppingCartList.add(recipeIngredient);
                cursor.moveToNext();
            }
        }
        if (shoppingCartList.size() == 0) {
            cursor.close();
            return null;
        }
        cursor.close();
        return shoppingCartList;
    }

    /**
     * This method adds the recipe information to the shopping cart
     *
     * @return true if the operation was successful, false otherwise
     */
    public boolean updateShoppingCartIngredient(RecipeIngredient ingredient) {
        int id = ingredient.getIngredientID();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cVals = new ContentValues();
        cVals.put(SC_INGREDIENT_ID, ingredient.getIngredientID());
        cVals.put(SC_QUANTITY, ingredient.getQuantity());
        cVals.put(SC_UNIT, ingredient.getUnit());

        long returned = sqLiteDatabase.update(TABLE_SHOPPING_CART_LIST, cVals, SC_INGREDIENT_ID + " = ?", new String[]{String.valueOf(id)});
        if (returned != 1) {
            return false;
        }
        return true;

    }

    /**
     * This method returns an Ingredient using the specified ingredient ID
     *
     * @param ingredientID
     * @return If successful in fetching the ingredient return, if not, this method will return null.
     */
    public RecipeIngredient getShoppingCartIngredient(int ingredientID) {
        RecipeIngredient recipeIngredient = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SHOPPING_CART_LIST + "  WHERE " + SC_INGREDIENT_ID + " = ? ", new String[]{String.valueOf(ingredientID)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            recipeIngredient = mapRecipeIngredient(cursor);
            cursor.moveToNext();

            cursor.close();
        }

        return recipeIngredient;
    }

    /**
     * This method adds the recipe information to the shopping cart
     *
     * @param ingredientId
     * @return true if the operation was successful, false otherwise
     */
    public boolean deleteShoppingCartIngredient(int ingredientId) {
        if (getIngredient(ingredientId) == null) {
            return false;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long returned = sqLiteDatabase.delete(TABLE_SHOPPING_CART_LIST, SC_INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredientId)});

        return true;
    }

    /**
     * This method restarts the shopping cart table. effectively clearing the table
     *
     * @return true if the operation was successful, false otherwise
     */
    public void deleteAllShoppingCartIngredients() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_CART_LIST);
        //Shopping Cart Table
        String CREATE_SHOPPING_CART_TABLE = "CREATE TABLE " + TABLE_SHOPPING_CART_LIST + "("
                + SC_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SC_INGREDIENT_ID + " INTEGER, "
                + SC_QUANTITY + " INTEGER,"
                + SC_UNIT + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_SHOPPING_CART_TABLE);
        return;
    }

    /**
     * This method creates a new row in the Recipe Ingredient table using the provided recipeIngredient
     *
     * @param recipeIngredient
     * @return true if the operation was successful, false otherwise
     */
    public int addRecipeIngredient(RecipeIngredient recipeIngredient) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(RI_RECIPE_ID, recipeIngredient.getRecipeID());
        contentValues.put(RI_INGREDIENT_ID, recipeIngredient.getIngredientID());
        contentValues.put(RI_QUANTITY, recipeIngredient.getQuantity());
        contentValues.put(RI_UNIT, recipeIngredient.getUnit());
        contentValues.put(RI_DETAILS, recipeIngredient.getDetails());

        // Insert the new row, returning the primary key value of the new row
        int newRowId = (int) db.insert(TABLE_RECIPE_INGREDIENT_LIST, null, contentValues);
        recipeIngredient.setKeyID(newRowId);

        db.close();
        return newRowId;
    }

    /**
     * This method returns a list of all recipe Inredients using the specified recipeId
     *
     * @param recipeId
     * @return If successful in fetching the recipes this method will return an Array list of recipe
     * Ingredients, if not, this method will return null.
     */
    public ArrayList<RecipeIngredient> getAllRecipeIngredients(int recipeId) {
        RecipeIngredient recipeIngredient;
        ArrayList<RecipeIngredient> recipeIngredientList = new ArrayList<RecipeIngredient>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_INGREDIENT_LIST + "  WHERE " + RI_RECIPE_ID + " = ? ", new String[]{String.valueOf(recipeId)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipeIngredient = mapRecipeIngredient(cursor);
                recipeIngredientList.add(recipeIngredient);
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (recipeIngredientList.size() == 0) {
            return null;
        }
        return recipeIngredientList;
    }

    /**
     * This method adds an ingredient to the ingredients table.
     *
     * @param ingredient The ingredient to be inserted. (id is ignored)
     * @return The id of the added ingredient
     */
    public int addIngredient(Ingredient ingredient) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        if (getIngredient(ingredient.getName()) != -1) {
            return getIngredient(ingredient.getName());
        }
        //adding ingredients
        ContentValues cVals = new ContentValues();
        cVals.put(IT_NAME, ingredient.getName());
        int res = (int) sqLiteDatabase.insert(TABLE_INGREDIENT_LIST, null, cVals);
        ingredient.setKeyID(res);

        return res;
    }

    /**
     * This method returns an Ingredient using the specified category ID
     *
     * @param ingredientID
     * @return If successful in fetching the ingredient return, if not, this method will return null.
     */
    public Ingredient getIngredient(int ingredientID) {
        Ingredient ingredient = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INGREDIENT_LIST + "  WHERE " + IT_KEY_ID + " = ? ", new String[]{String.valueOf(ingredientID)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ingredient = mapIngredient(cursor);
            cursor.moveToNext();

            cursor.close();
        }

        return ingredient;
    }

    /**
     * This method retrieves the ingredient for the given ingredient Title
     *
     * @param ingredientTitle
     * @return The recipe corresponding to the provided ingredient Title, or -1 if one is not found.
     */
    public int getIngredient(String ingredientTitle) {
        Ingredient ingredient = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INGREDIENT_LIST + "  WHERE " + IT_NAME + " = ? ", new String[]{String.valueOf(ingredientTitle)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ingredient = mapIngredient(cursor);
            cursor.moveToNext();
            cursor.close();
            return ingredient.getKeyID();
        }

        return -1;
    }

    /**
     * This method returns a list of all Ingredients
     *
     * @return If successful in fetching the Ingredients this method will return an Array list of
     * Ingredients, if not this method will return null.
     */
    public ArrayList<Ingredient> getAllIngredients() {
        Ingredient ingredient;
        ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INGREDIENT_LIST, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ingredient = mapIngredient(cursor);
                ingredientList.add(ingredient);
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (ingredientList.size() == 0) {
            return null;
        }
        return ingredientList;
    }

    /**
     * This method modifies the ingredient in the ingredients table with the same key id
     *
     * @param ingredient
     * @return If successful in updating, will return true
     */
    public boolean editIngredient(Ingredient ingredient) {
        int id = ingredient.getKeyID();

        if (getIngredient(ingredient.getName()) != -1) {
            return false;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(IT_NAME, ingredient.getName());
        long returned = sqLiteDatabase.update(TABLE_INGREDIENT_LIST, cVals, IT_KEY_ID + " = ?", new String[]{String.valueOf(id)});
        if (returned == 0) {
            return false;
        }
        return true;
    }

    /**
     * This method deletes the recipe ingredient list in the recipe ingredient table using the recipeId
     *
     * @param recipeId
     * @return If successful in updating, will return true
     */
    public boolean deleteRecipeIngredients(int recipeId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long returned = sqLiteDatabase.delete(TABLE_RECIPE_INGREDIENT_LIST, RI_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});

        if (returned == 0) {
            return false;
        }
        return true;
    }

    /**
     * This method deletes the ingredient in the ingredient table using the ingredientId
     *
     * @param ingredintId
     * @return If successful in updating, will return true
     */
    public boolean deleteIngredient(int ingredintId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long returned = sqLiteDatabase.delete(TABLE_INGREDIENT_LIST, IT_KEY_ID + " = ?", new String[]{String.valueOf(ingredintId)});
        sqLiteDatabase.delete(TABLE_RECIPE_INGREDIENT_LIST, RI_INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredintId)});

        if (returned == 0) {
            return false;
        }


        return true;
    }

    /**
     * This method creates a new row in the Recipe Direction table using the provided recipeDirection
     *
     * @param recipeDirection
     * @return true if the operation was successful, false otherwise
     */

    public int addRecipeDirection(RecipeDirection recipeDirection) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(RD_RECIPE_ID, recipeDirection.getRecipeID());
        contentValues.put(RD_DIRECTION_TEXT, recipeDirection.getDirectionText());
        contentValues.put(RD_DIRECTION_NUMBER, recipeDirection.getDirectionNumber());

        // Insert the new row, returning the primary key value of the new row
        int newRowId = (int) db.insert(TABLE_RECIPE_DIRECTIONS_LIST, null, contentValues);

        db.close();
        return newRowId;
    }

    /**
     * This method returns a list of all recipe Directions using the specified recipe id
     *
     * @param recipeId
     * @return If successful in fetching the recipes this method will return an Array list of recipe
     * Directions, if not, this method will return null.
     */
    public ArrayList<RecipeDirection> getAllRecipeDirections(int recipeId) {
        RecipeDirection recipeDirection;
        ArrayList<RecipeDirection> recipeIngredientList = new ArrayList<RecipeDirection>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_DIRECTIONS_LIST + "  WHERE " + RD_RECIPE_ID + " = ? ", new String[]{String.valueOf(recipeId)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipeDirection = mapRecipeDirection(cursor);
                recipeIngredientList.add(recipeDirection);
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (recipeIngredientList.size() == 0) {
            return null;
        }
        return recipeIngredientList;
    }

    /**
     * This method deletes the recipe Direction list in the recipe Direction table using the recipeId
     *
     * @param recipeId
     * @return If successful in updating, will return true
     */
    public boolean deleteRecipeDirections(int recipeId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long returned = sqLiteDatabase.delete(TABLE_RECIPE_DIRECTIONS_LIST, RD_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
        if (returned == 0) {
            return false;
        }
        return true;
    }

    /**
     * This method returns a list of all recipe Categories using the specified recipe id
     *
     * @param recipeId
     * @return If successful in fetching the recipes this method will return an Array list of recipe
     * Categories, if not, this method will return null.
     */
    public ArrayList<RecipeCategory> getAllRecipeCategories(int recipeId) {
        RecipeCategory recipeCategory;
        ArrayList<RecipeCategory> recipeCategoryList = new ArrayList<RecipeCategory>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_CATEGORY_LIST + "  WHERE " + RC_RECIPE_ID + " = ? ", new String[]{String.valueOf(recipeId)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                recipeCategory = mapRecipeCategory(cursor);
                recipeCategoryList.add(recipeCategory);
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (recipeCategoryList.size() == 0) {
            return null;
        }
        return recipeCategoryList;
    }

    /**
     * This method creates a new row in the Recipe Category table using the provided recipeCategory
     *
     * @param recipeCategory
     * @return true if the operation was successful, false otherwise
     */
    public int addRecipeCategory(RecipeCategory recipeCategory) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();

        contentValues.put(RC_RECIPE_ID, recipeCategory.getRecipeID());
        contentValues.put(RC_CATEGORY_ID, recipeCategory.getCategoryID());

        // Insert the new row, returning the primary key value of the new row
        int newRowId = (int) db.insert(TABLE_RECIPE_CATEGORY_LIST, null, contentValues);
        recipeCategory.setKeyID(newRowId);

        db.close();
        return newRowId;
    }

    /**
     * This method creates a new row in the Category table using the provided category
     *
     * @param category
     * @return returns the id of the inserted category, or -1 otherwise.
     */
    public int addCategory(Category category) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        if (getCategory(category.getName()) != -1) {
            return getCategory(category.getName());
        }
        //Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(CT_NAME, category.getName());

        // Insert the new row
        int res = (int) db.insert(TABLE_CATEGORY_LIST, null, contentValues);
        category.setKeyID(res);

        db.close();

        return res;

    }

    /**
     * This method returns a Category using the specified category ID
     *
     * @param categoryID
     * @return If successful in fetching the categorie return, if not, this method will return null.
     */
    public Category getCategory(int categoryID) {
        Category category = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY_LIST + "  WHERE " + IT_KEY_ID + " = ? ", new String[]{String.valueOf(categoryID)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            category = mapCategory(cursor);
            cursor.moveToNext();

            cursor.close();
        }

        return category;
    }

    /**
     * This method retrieves the category for the given category Title
     *
     * @param categoryTitle
     * @return The recipe corresponding to the provided category Title, or -1 if one is not found.
     */
    public int getCategory(String categoryTitle) {
        Category category = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY_LIST + "  WHERE " + CT_NAME + " = ? ", new String[]{String.valueOf(categoryTitle)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            category = mapCategory(cursor);
            cursor.moveToNext();
            cursor.close();
            return category.getKeyID();
        }

        return -1;
    }

    /**
     * This method returns a list of all Categories
     *
     * @return If successful in fetching the Categories this method will return an Array list of
     * Categories, if not this method will return null.
     */
    public ArrayList<Category> getAllCategories() {
        Category category;
        ArrayList<Category> categoryList = new ArrayList<Category>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY_LIST, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                category = mapCategory(cursor);
                categoryList.add(category);
                cursor.moveToNext();
            }
            cursor.close();
        }
        if (categoryList.size() == 0) {
            return null;
        }
        return categoryList;
    }

    /**
     * This method deletes the recipe category list in the recipe category table using the recipeId
     *
     * @param recipeId
     * @return If successful in updating, will return true
     */
    public boolean deleteRecipeCategory(int recipeId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long returned = sqLiteDatabase.delete(TABLE_RECIPE_CATEGORY_LIST, RC_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});

        if (returned == 0) {
            return false;
        }
        return true;
    }

    /**
     * This method deletes the category in the category table using the categoryId
     *
     * @param categoryId
     * @return If successful in updating, will return true
     */
    public boolean deleteCategory(int categoryId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long returned = sqLiteDatabase.delete(TABLE_CATEGORY_LIST, IT_KEY_ID + " = ?", new String[]{String.valueOf(categoryId)});
        if (returned == 0) {
            return false;
        }
        return true;
    }

    /**
     * This method returns the recipe based on the initial unit and the desired unit
     *
     * @param recipe, unit1, unit2
     * @return If successful, will return a value of the quantity in the new unit
     */
    public Recipe convertRecipeIngredientUnits(Recipe recipe, String origUnit, String reqUnit) {
        List<RecipeIngredient> ingredientList = recipe.getIngredientList();
        for (int i = 0; i < ingredientList.size(); i++) {
            if (ingredientList.get(i).getUnit().contentEquals(origUnit)) {
                double tempQuant = convertUnit(origUnit, reqUnit, ingredientList.get(i).getQuantity());
                if (tempQuant != -1) {
                    ingredientList.get(i).setQuantity(tempQuant);
                    ingredientList.get(i).setUnit(reqUnit);
                }
            }
        }
        recipe.setIngredientList(ingredientList);
        return recipe;
    }

    /**
     * This method check to make sure that either volume or mass and will return -1 a unit type of
     * none is given or a type mismatch occures
     *
     * @param origUnit, reqUnit, quantity
     * @return If successful, will return a value of the quantity in the new unit, else -1
     */
    public double convertUnit(String origUnit, String reqUnit, double quantity) {

        if (origUnit.equalsIgnoreCase("none") || reqUnit.equalsIgnoreCase("none")) {
            return -1;
        }

        //for checking Volume units
        if ((origUnit.equalsIgnoreCase("tablespoon(s)") || origUnit.equalsIgnoreCase("teaspoon(s)") ||
                origUnit.equalsIgnoreCase("pint(s)") || origUnit.equalsIgnoreCase("fluid ounce(s)") ||
                origUnit.equalsIgnoreCase("quart(s)") || origUnit.equalsIgnoreCase("gallon(s)") ||
                origUnit.equalsIgnoreCase("pinch(es)") || origUnit.equalsIgnoreCase("cup(s)"))
                &&
                (reqUnit.equalsIgnoreCase("tablespoon(s)") || reqUnit.equalsIgnoreCase("teaspoon(s)") ||
                        reqUnit.equalsIgnoreCase("pint(s)") || reqUnit.equalsIgnoreCase("fluid ounce(s)") ||
                        reqUnit.equalsIgnoreCase("quart(s)") || reqUnit.equalsIgnoreCase("gallon(s)") ||
                        reqUnit.equalsIgnoreCase("pinch(es)") || reqUnit.equalsIgnoreCase("cup(s)"))) {

            return convertUnitVolume(origUnit, reqUnit, quantity);
        }

        //for checking Mass units
        if ((origUnit.equalsIgnoreCase("grain(s)") || origUnit.equalsIgnoreCase("ounce(s)") ||
                origUnit.equalsIgnoreCase("pound(s)") || origUnit.equalsIgnoreCase("kilogram(s)") ||
                origUnit.equalsIgnoreCase("milligram(s)"))
                &&
                (reqUnit.equalsIgnoreCase("grain(s)") || reqUnit.equalsIgnoreCase("ounce(s)") ||
                        reqUnit.equalsIgnoreCase("pound(s)") || reqUnit.equalsIgnoreCase("kilogram(s)") ||
                        reqUnit.equalsIgnoreCase("milligram(s)"))) {
            return convertUnitMass(origUnit, reqUnit, quantity);
        }

        //if both are not mass or both are not volume then there must be a mismatch
        return -1;

    }


    /**
     * This method returns the updated quantity. First, the measurement is reduced
     * to the base unit of cups. Then rescaled back to the requested unit.
     *
     * @param origUnit, reqUnit, quantity
     * @return If successful, will return a value of the quantity in the new unit
     */
    public double convertUnitVolume(String origUnit, String reqUnit, double quantity) {
        //converting all values to cups
        if (origUnit.contentEquals("pinch(es)")) {
            quantity = quantity / 768;
        }
        if (origUnit.contentEquals("tablespoon(s)")) {
            quantity = quantity * 0.0625;
        }
        if (origUnit.contentEquals("teaspoon(s)")) {
            quantity = quantity * 0.0208333;
        }
        if (origUnit.contentEquals("fluid ounce(s)")) {
            quantity = quantity / 8;
        }
        if (origUnit.contentEquals("pint(s)")) {
            quantity = quantity * 2;
        }
        if (origUnit.contentEquals("quart(s)")) {
            quantity = quantity * 4;
        }
        if (origUnit.contentEquals("gallon(s)")) {
            quantity = quantity * 16;
        }


        //converting quantity to cups
        if (reqUnit.contentEquals("pinch(es)")) {
            quantity = quantity * 768;
        }
        if (reqUnit.contentEquals("teaspoon(s)")) {
            quantity = quantity / 0.0208333;
        }
        if (reqUnit.contentEquals("tablespoon(s)")) {
            quantity = quantity / 0.0625;
        }
        if (reqUnit.contentEquals("fluid ounce(s)")) {
            quantity = quantity * 8;
        }
        if (reqUnit.contentEquals("pint(s)")) {
            quantity = quantity / 2;
        }
        if (reqUnit.contentEquals("quart(s)")) {
            quantity = quantity / 4;
        }
        if (reqUnit.contentEquals("gallon(s)")) {
            quantity = quantity / 16;
        }

        //converts the Double to have 2 decimal places
        return Double.parseDouble(String.format("%.2f", quantity));
    }

    /**
     * This method returns the updated quantity. First, the measurement is reduced
     * to the base unit of grams. Then rescaled back to the requested unit.
     *
     * @param origUnit, reqUnit, quantity
     * @return If successful, will return a value of the quantity in the new unit
     */
    public double convertUnitMass(String origUnit, String reqUnit, double quantity) {
        if (origUnit.contentEquals("grain(s)")) {
            quantity = quantity * 0.06479891;
        }
        if (origUnit.contentEquals("ounce(s)")) {
            quantity = quantity * 28.3495231;
        }
        if (origUnit.contentEquals("pound(s)")) {
            quantity = quantity * 453.59237;
        }
        if (origUnit.contentEquals("kilogram(s)")) {
            quantity = quantity * 1000;
        }
        if (origUnit.contentEquals("milligram(s)")) {
            quantity = quantity / 1000;
        }

        //converting quantity to grams
        if (reqUnit.contentEquals("grain(s)")) {
            quantity = quantity / 0.06479891;
        }
        if (reqUnit.contentEquals("ounce(s)")) {
            quantity = quantity / 28.3495231;
        }
        if (reqUnit.contentEquals("pound(s)")) {
            quantity = quantity / 453.59237;
        }
        if (reqUnit.contentEquals("kilogram(s)")) {
            quantity = quantity / 1000;
        }
        if (reqUnit.contentEquals("milligram(s)")) {
            quantity = quantity * 1000;
        }

        //converts the Double to have 2 decimal places
        return Double.parseDouble(String.format("%.2f", quantity));
    }

    /**
     * This method scales and updates all ingredients as well as the recipe objects serving size.
     * This method does NOT update the database
     *
     * @param recipe, desiredServing
     * @return If successful, will return true
     */
    public Recipe scaleRecipe(Recipe recipe, double desiredServing) {
        if (recipe.getKeyID() == -1)
            return null;
        double scalar = desiredServing / recipe.getServings();
        List<RecipeIngredient> ingredientList = recipe.getIngredientList();
        for (int i = 0; i < ingredientList.size(); i++) {
            ingredientList.get(i).setQuantity(ingredientList.get(i).getQuantity() * scalar);
        }
        recipe.setIngredientList(ingredientList);
        recipe.setServings(desiredServing);
        return recipe;
    }

    /**
     * This method returns a built Recipe object
     *
     * @return If successful in fetching the cursor and building the recipe, it will
     * return an Recipe Object, if not the method will return null.
     */
    private Recipe mapRecipe(Cursor cursor) {
        Recipe recipe = new Recipe();
        if (cursor.getColumnIndex(IT_KEY_ID) != -1) {
            int idIndex = cursor.getColumnIndexOrThrow(IT_KEY_ID);
            recipe.setKeyID((cursor.getInt(idIndex)));
        }
        if (cursor.getColumnIndex(RT_TITLE) != -1) {
            int recipeTitleIndex = cursor.getColumnIndexOrThrow(RT_TITLE);
            recipe.setTitle(cursor.getString(recipeTitleIndex));
        }
        if (cursor.getColumnIndex(RT_TOTAL_TIME) != -1) {
            int recipetTimeIndex = cursor.getColumnIndexOrThrow(RT_TOTAL_TIME);
            recipe.setTotal_time(cursor.getInt(recipetTimeIndex));
        }
        if (cursor.getColumnIndex(RT_PREP_TIME) != -1) {
            int recipepTimeIndex = cursor.getColumnIndexOrThrow(RT_PREP_TIME);
            recipe.setPrep_time(cursor.getInt(recipepTimeIndex));
        }
        if (cursor.getColumnIndex(RT_SERVINGS) != -1) {
            int recipeServingsIndex = cursor.getColumnIndexOrThrow(RT_SERVINGS);
            recipe.setServings(cursor.getDouble(recipeServingsIndex));
        }
        //not sure how to get the bit type out of the cursor... could just do 1 or 0
        if (cursor.getColumnIndex(RT_FAVORITED) != -1) {
            int recipeFavoritedIndex = cursor.getColumnIndexOrThrow(RT_FAVORITED);
            if (cursor.getInt(recipeFavoritedIndex) == 1) {
                recipe.setFavorited(true);
            } else {
                recipe.setFavorited(false);
            }
        }
        return recipe;

    }

    /**
     * This method returns a built Recipe Ingredient object
     *
     * @return If successful in fetching the cursor and building the recipe ingredient, it will
     * return an Recipe Ingredient Object, if not the method will return null.
     */
    private RecipeIngredient mapRecipeIngredient(Cursor cursor) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        //had to take this out so ingredient can set name
        int idIndex = -1;
        if (cursor.getColumnIndex(IT_KEY_ID) != -1) {
            idIndex = cursor.getColumnIndexOrThrow(IT_KEY_ID);
            recipeIngredient.setKeyID((cursor.getInt(idIndex)));
        }
        if (cursor.getColumnIndex(RI_RECIPE_ID) != -1) {
            int recipeIdIndex = cursor.getColumnIndexOrThrow(RI_RECIPE_ID);
            recipeIngredient.setRecipeID(cursor.getInt(recipeIdIndex));
        }
        if (cursor.getColumnIndex(RI_INGREDIENT_ID) != -1) {
            int ingredientIdIndex = cursor.getColumnIndexOrThrow(RI_INGREDIENT_ID);
            recipeIngredient.setIngredientID(cursor.getInt(ingredientIdIndex));
        }
        if (cursor.getColumnIndex(RI_QUANTITY) != -1) {
            int quantityIndex = cursor.getColumnIndexOrThrow(RI_QUANTITY);
            recipeIngredient.setQuantity(cursor.getDouble(quantityIndex));
        }
        if (cursor.getColumnIndex(RI_UNIT) != -1) {
            int unitIndex = cursor.getColumnIndexOrThrow(RI_UNIT);
            recipeIngredient.setUnit(cursor.getString(unitIndex));
        }
        if (cursor.getColumnIndex(RI_DETAILS) != -1) {
            int detailsIndex = cursor.getColumnIndexOrThrow(RI_DETAILS);
            recipeIngredient.setDetails(cursor.getString(detailsIndex));
        }
        return recipeIngredient;

    }

    /**
     * This method returns a built recipe Direction object
     *
     * @return If successful in fetching the cursor and building the recipe Direction, it will
     * return an recipe Direction Object, if not the method will return null.
     */
    private RecipeDirection mapRecipeDirection(Cursor cursor) {
        RecipeDirection recipeDirection = new RecipeDirection();
        if (cursor.getColumnIndex(RD_KEY_ID) != -1) {
            int idIndex = cursor.getColumnIndexOrThrow(RD_KEY_ID);
            recipeDirection.setKeyID((cursor.getInt(idIndex)));
        }
        if (cursor.getColumnIndex(RD_RECIPE_ID) != -1) {
            int recipeIdIndex = cursor.getColumnIndexOrThrow(RD_RECIPE_ID);
            recipeDirection.setRecipeID(cursor.getInt(recipeIdIndex));
        }
        if (cursor.getColumnIndex(RD_DIRECTION_TEXT) != -1) {
            int directionTextIndex = cursor.getColumnIndexOrThrow(RD_DIRECTION_TEXT);
            recipeDirection.setDirectionText(cursor.getString(directionTextIndex));
        }
        if (cursor.getColumnIndex(RD_DIRECTION_NUMBER) != -1) {
            int quantityIndex = cursor.getColumnIndexOrThrow(RD_DIRECTION_NUMBER);
            recipeDirection.setDirectionNumber(cursor.getInt(quantityIndex));
        }
        return recipeDirection;
    }


    /**
     * This method returns a built Recipe Category object
     *
     * @return If successful in fetching the cursor and building the Recipe Category, it will
     * return an Recipe Category Object, if not the method will return null.
     */
    private RecipeCategory mapRecipeCategory(Cursor cursor) {
        RecipeCategory recipeCategory = new RecipeCategory();
        if (cursor.getColumnIndex(IT_KEY_ID) != -1) {
            int idIndex = cursor.getColumnIndexOrThrow(IT_KEY_ID);
            recipeCategory.setKeyID((cursor.getInt(idIndex)));
        }
        if (cursor.getColumnIndex(RC_RECIPE_ID) != -1) {
            int recipeIdIndex = cursor.getColumnIndexOrThrow(RC_RECIPE_ID);
            recipeCategory.setRecipeID(cursor.getInt(recipeIdIndex));
        }
        if (cursor.getColumnIndex(RC_CATEGORY_ID) != -1) {
            int categoryIdIndex = cursor.getColumnIndexOrThrow(RC_CATEGORY_ID);
            recipeCategory.setCategoryID(cursor.getInt(categoryIdIndex));
        }
        return recipeCategory;
    }

    /**
     * This method returns a built Category object
     *
     * @return If successful in fetching the cursor and building the Category, it will
     * return a Category Object, if not the method will return null.
     */
    private Category mapCategory(Cursor cursor) {
        Category category = new Category();
        if (cursor.getColumnIndex(IT_KEY_ID) != -1) {
            int idIndex = cursor.getColumnIndexOrThrow(IT_KEY_ID);
            category.setKeyID((cursor.getInt(idIndex)));
        }
        if (cursor.getColumnIndex(IT_NAME) != -1) {
            int categoryNameIndex = cursor.getColumnIndexOrThrow(IT_NAME);
            category.setName(cursor.getString(categoryNameIndex));
        }
        return category;
    }

    /**
     * This method returns a built Ingredient object
     *
     * @return If successful in fetching the cursor and building the Ingredient, it will
     * return a Ingredient Object, if not the method will return null.
     */
    private Ingredient mapIngredient(Cursor cursor) {
        Ingredient ingredient = new Ingredient();
        if (cursor.getColumnIndex(IT_KEY_ID) != -1) {
            int idIndex = cursor.getColumnIndexOrThrow(IT_KEY_ID);
            ingredient.setKeyID((cursor.getInt(idIndex)));
        }
        if (cursor.getColumnIndex(IT_NAME) != -1) {
            int categoryNameIndex = cursor.getColumnIndexOrThrow(IT_NAME);
            ingredient.setName(cursor.getString(categoryNameIndex));
        }
        return ingredient;
    }

    /**
     * This method returns a built Recipe Ingredient object
     *
     * @return If successful in fetching the cursor and building the recipe ingredient, it will
     * return an Recipe Ingredient Object, if not the method will return null.
     */
    private RecipeIngredient mapShoppingCartIngredient(Cursor cursor) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        //had to take this out so ingredient can set name
        int idIndex = -1;
        if (cursor.getColumnIndex(SC_KEY_ID) != -1) {
            idIndex = cursor.getColumnIndexOrThrow(SC_KEY_ID);
            recipeIngredient.setKeyID((cursor.getInt(idIndex)));
        }
        if (cursor.getColumnIndex(SC_INGREDIENT_ID) != -1) {
            int ingredientIdIndex = cursor.getColumnIndexOrThrow(SC_INGREDIENT_ID);
            recipeIngredient.setIngredientID(cursor.getInt(ingredientIdIndex));
        }
        if (cursor.getColumnIndex(SC_QUANTITY) != -1) {
            int quantityIndex = cursor.getColumnIndexOrThrow(SC_QUANTITY);
            recipeIngredient.setQuantity(cursor.getDouble(quantityIndex));
        }
        if (cursor.getColumnIndex(SC_UNIT) != -1) {
            int unitIndex = cursor.getColumnIndexOrThrow(SC_UNIT);
            recipeIngredient.setUnit(cursor.getString(unitIndex));
        }
        return recipeIngredient;

    }

    /**
     * This method adds a User to the User_Info table. Also, this method assumes the user is
     * right handed. this can be updated using editUser()
     *
     * @param username, password
     * @return The id of the added User.
     */
    public int addUser(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        PasswordEncryption md5 = new PasswordEncryption();

        password = md5.main(password);

        if (password == null) {
            return -1;
        }

        //if the user already exists
        if (getUser(username) != -1) {
            return -1;
        }

        //adding user_Info
        ContentValues cVals = new ContentValues();
        cVals.put(UI_USERNAME, username);
        cVals.put(UI_PASSWORD, password);
        cVals.put(UI_THEME, 1);
        int res = (int) sqLiteDatabase.insert(TABLE_USER_INFO, null, cVals);

        return res;
    }

    /**
     * This method returns a -1 or userId depending on if the user already exists
     *
     * @return If successful in username match, return the userId
     * otherwise, return -1
     */
    public int getUser(String username) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        //if User already Exists
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_USER_INFO + "  WHERE " + UI_USERNAME + " = ? ", new String[]{String.valueOf(username)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getColumnIndex(UI_KEY_ID) != -1) {
                int idIndex = cursor.getColumnIndexOrThrow(UI_KEY_ID);
                int userId = cursor.getInt(idIndex);
                return userId;
            }
        }
        return -1;
    }

    /**
     * This method returns a true or false on username and password match
     *
     * @return If successful in username and password matching, return true and put the user's settings
     * into shared preferences, otherwise, return false
     */
    public int loginCheck(String username, String password) {
        String storedPass = null;
        int userId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        PasswordEncryption md5 = new PasswordEncryption();
        int themeId = 0; //Default theme Id

        //Checks if username exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_INFO + "  WHERE " + UI_USERNAME + " = ? ", new String[]{String.valueOf(username)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getColumnIndex(UI_KEY_ID) != -1) {
                int idIndex = cursor.getColumnIndexOrThrow(UI_KEY_ID);
                userId = cursor.getInt(idIndex);
            }
            if (cursor.getColumnIndex(UI_PASSWORD) != -1) {
                int passIndex = cursor.getColumnIndexOrThrow(UI_PASSWORD);
                storedPass = cursor.getString(passIndex);
            }
            if (cursor.getColumnIndex(UI_THEME) != -1) {
                int themeIndex = cursor.getColumnIndexOrThrow(UI_THEME);
                themeId = cursor.getInt(themeIndex);
            }
        } else {
            return -1;
        }

        //update password with encryption for check
        password = md5.main(password);

        //checks if password is correct.
        if (password != null && password.equals(storedPass)) {

            SharedPreferences prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            prefs.edit().putString("Username", username);
            prefs.edit().putInt("UserId", userId);
            prefs.edit().putInt("ThemeId", themeId);

            return userId;
        }

        return -1;
    }

    /**
     * This method modifies the User details. must first pass through loginCheck()
     *
     * @param username, password, updateUsername, updatePassword
     * @return If successful in updating, will return true
     */
    public boolean editUser(String username, String password, String updateUsername, String updatePassword, int updateTheme) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        PasswordEncryption md5 = new PasswordEncryption();

        int userId = getUser(username);
        if (userId == -1) {
            return false;
        }

        //update password with encryption for check
        updatePassword = md5.main(updatePassword);

        ContentValues cVals = new ContentValues();
        cVals.put(UI_USERNAME, updateUsername);
        cVals.put(UI_PASSWORD, updatePassword);
        cVals.put(UI_THEME, updateTheme);
        long returned = sqLiteDatabase.update(TABLE_USER_INFO, cVals, UI_KEY_ID + " = ?", new String[]{String.valueOf(userId)});
        return true;
    }

    /**
     * This method deletes the User in the User Info table using the userId gotten from
     * passwordCheck
     *
     * @param userId
     * @return If successful in updating, will return true
     */
    public boolean deleteUser(int userId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long returned = sqLiteDatabase.delete(TABLE_USER_INFO, UI_KEY_ID + " = ?", new String[]{String.valueOf(userId)});

        if (returned == 0) {
            return false;
        }

        return true;
    }
}

