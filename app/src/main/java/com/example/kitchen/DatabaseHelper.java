package com.example.kitchen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//create read update delete
public class DatabaseHelper extends SQLiteOpenHelper {
    //set to true for printouts.
    private static boolean IS_IN_TESTING_MODE = true;

    public static final int VERSION_NUMBER = 4;
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


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
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
            if (IS_IN_TESTING_MODE) {
                System.out.println("updating recipe table failed");
            }
            return -1;
        }

        //add new ingredients from ingredientList
        for (int i = 0; i < recipeIngredientList.size(); i++) {
            recipeIngredientList.get(i).setRecipeID(res);
            int result = addRecipeIngredient(recipeIngredientList.get(i));

            if (result == -1) {
                //Inserting recipeIngredient didn't work.
            }
        }

        //add new categorys from recipeCategoryList
        for (int i = 0; i < recipeCategoryList.size(); i++) {
            recipeCategoryList.get(i).setRecipeID(res);
            int result = addRecipeCategory(recipeCategoryList.get(i));

            if (result == -1) {
                //Inserting recipeCategory didn't work
            }
        }

        //add new directions from recipeDirectionList
        for (int i = 0; i < recipeDirectionList.size(); i++) {
            recipeDirectionList.get(i).setRecipeID(res);
            int result = addRecipeDirection(recipeDirectionList.get(i));

            if (result == -1) {
                //Inserting recipeDirection didn't work
            }
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
            if (cursor != null) {
                cursor.moveToFirst();
                recipe = mapRecipe(cursor);
                cursor.moveToNext();

                cursor.close();
            }

        if(recipe != null) {
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

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_LIST + "  WHERE " + RT_TITLE + " = ? ", new String[]{String.valueOf(recipeTitle)});
        if (cursor != null) {
            cursor.moveToFirst();
            recipe = mapRecipe(cursor);
            cursor.moveToNext();

            cursor.close();
        }

        if(recipe != null) {
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
        if (cursor != null) {
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
        for (int i = 0; i < recipeIngredientList.size(); i++) {
            recipeIngredientList.get(i).setRecipeID(recipeId);
            int ingredientRes = addRecipeIngredient(recipeIngredientList.get(i));
            if (ingredientRes == -1) {
                return false;
            }
        }

        //delete recipecategorylist and add new recipecategorylist
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_RECIPE_CATEGORY_LIST, RC_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
        for (int i = 0; i < recipeCategoryList.size(); i++) {
            recipeCategoryList.get(i).setRecipeID(recipeId);
            int recipeCategoryResult = addRecipeCategory(recipeCategoryList.get(i));

            if (recipeCategoryResult == -1) {
                return false;
            }
        }

        //delete recipeDirections and add new recipeDirections
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_RECIPE_DIRECTIONS_LIST, RD_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
        for (int i = 0; i < recipeDirectionList.size(); i++) {
            recipeDirectionList.get(i).setRecipeID(recipeId);
            int recipeDirectionResult = addRecipeDirection(recipeDirectionList.get(i));
            if (recipeDirectionResult == -1) {
                return false;
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
        if (cursor != null) {
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

        //adding ingredients
        ContentValues cVals = new ContentValues();
        cVals.put(IT_NAME, ingredient.getName());
        int res = (int) sqLiteDatabase.insert(TABLE_INGREDIENT_LIST, null, cVals);
        ingredient.setKeyID(res); //possibly dangerous cast?

        if (res == -1) {
            if (IS_IN_TESTING_MODE) {
                System.out.println("updating ingredient table failed");
            }
            return -1;
        }

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
        if (cursor != null) {
            cursor.moveToFirst();
            ingredient = mapIngredient(cursor);
            cursor.moveToNext();

            cursor.close();
        }

        return ingredient;
    }

    /**
     * This method modifies the ingredient in the ingredients table with the same key id
     *
     * @param ingredient
     * @return If successful in updating, will return true
     */
    public boolean editIngredient(Ingredient ingredient) {
        int id = ingredient.getKeyID();
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
        if (cursor != null) {
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
        if (cursor != null) {
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
        if (cursor != null) {
            cursor.moveToFirst();
            category = mapCategory(cursor);
            cursor.moveToNext();

            cursor.close();
        }

        return category;
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
     * This method returns a built Recipe object
     *
     * @return If successful in fetching the cursor and building the recipe, it will
     * return an Recipe Object, if not the method will return null.
     */
    private Recipe mapRecipe(Cursor cursor) {
        Recipe recipe = new Recipe();
        try {
            if (cursor != null) {
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
            }
        } catch (CursorIndexOutOfBoundsException ex) {
            return null;
        }
        if (recipe.getKeyID() == -1) {
            return null;
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
        try {
            if (cursor != null) {
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
            }
        } catch (CursorIndexOutOfBoundsException ex) {
            return null;
        }
        if (recipeIngredient.getKeyID() == -1) {
            return null;
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
        try {
            if (cursor != null) {
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
            }
        } catch (CursorIndexOutOfBoundsException ex) {
            return null;
        }
        if (recipeDirection.getKeyID() == -1) {
            return null;
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
        try {
            if (cursor != null) {
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
            }
        } catch (CursorIndexOutOfBoundsException ex) {
            return null;
        }
        if (recipeCategory.getKeyID() == -1) {
            return null;
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
        try {
            if (cursor != null) {
                if (cursor.getColumnIndex(IT_KEY_ID) != -1) {
                    int idIndex = cursor.getColumnIndexOrThrow(IT_KEY_ID);
                    category.setKeyID((cursor.getInt(idIndex)));
                }
                if (cursor.getColumnIndex(IT_NAME) != -1) {
                    int categoryNameIndex = cursor.getColumnIndexOrThrow(IT_NAME);
                    category.setName(cursor.getString(categoryNameIndex));
                }

            }
        } catch (CursorIndexOutOfBoundsException ex) {
            return null;
        }
        if (category.getKeyID() == -1) {
            return null;
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
        try {
            if (cursor != null) {
                if (cursor.getColumnIndex(IT_KEY_ID) != -1) {
                    int idIndex = cursor.getColumnIndexOrThrow(IT_KEY_ID);
                    ingredient.setKeyID((cursor.getInt(idIndex)));
                }
                if (cursor.getColumnIndex(IT_NAME) != -1) {
                    int categoryNameIndex = cursor.getColumnIndexOrThrow(IT_NAME);
                    ingredient.setName(cursor.getString(categoryNameIndex));
                }

            }
        } catch (CursorIndexOutOfBoundsException ex) {
            return null;
        }
        if (ingredient.getKeyID() == -1) {
            return null;
        }
        return ingredient;
    }

}
