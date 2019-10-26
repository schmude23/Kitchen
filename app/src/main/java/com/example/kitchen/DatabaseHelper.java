package com.example.kitchen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int VERSION_NUMBER = 1;
    public static final String DATABASE_NAME = "RECIPE_DATABASE";

    //Ingredient Table
    private static final String TABLE_Ingredient_List = "INGREDIENT_LIST";
    public static final String KEY_ID = "ID";
    public static final String name = "NAME";

    //Recipe Ingredient Table
    private static final String TABLE_Recipe_Ingredient_List = "RECIPE_INGREDIENT_LIST";
    //public static final String KEY_ID = "ID";
    public static final String recipeID = "RECIPE_ID";
    public static final String ingredientID = "INGREDIENT_ID";
    public static final String quantity = "QUANTITY";
    public static final String details = "DETAILS";

    //Recipe Table
    private static final String TABLE_Recipe_List = "RECIPE_LIST";
    // public static final String KEY_ID = "ID";
    public static final String title = "TITLE";
    public static final String tTime = "TOTAL_TIME";
    public static final String pTime = "PREP_TIME";
    public static final String servings = "SERVINGS";
    public static final String favorited = "FAVORITED";

    //Recipe Directions Table
    private static final String TABLE_Recipe_Directions_List = "DIRECTION_LIST";
    //public static final String KEY_ID = "ID";
    //public static final String recipeID = "RECIPE_ID";
    public static final String directionText = "DIRECTION_TEXT";
    public static final String directionNumber = "DIRECTION_NUMBER";

    //Category Table
    private static final String TABLE_Category_List = "CATEGORY_LIST";
    //public static final String KEY_ID = "ID";
    //public static final String name = "NAME";

    //Recipe Category Table
    private static final String TABLE_Recipe_Category_List = "RECIPE_CATEGORY_LIST";
    //public static final String KEY_ID = "ID";
    //public static final String recipeID = "RECIPE_ID";
    public static final String categoryID = "CATEGORY_ID";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    /**
     * Runs on creation of the database.
     * This method does all of the setup neccesary for the database such as creating tables.
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //TODO: TEST

        //Recipe Table
        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_Recipe_List + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + title + " TEXT,"
                + tTime + " INTEGER,"
                + pTime + " INTEGER,"
                + servings + " INTEGER,"
                + favorited + " BOOLEAN " + ")";
        sqLiteDatabase.execSQL(CREATE_RECIPE_TABLE);

         //Ingredients Table
        String CREATE_INGREDIENT_TABLE = "CREATE TABLE " + TABLE_Ingredient_List + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + name + " TEXT" +")";
        sqLiteDatabase.execSQL(CREATE_INGREDIENT_TABLE);

        //Recipe Ingredient Table
        String CREATE_RECIPE_INGREDIENT_TABLE = "CREATE TABLE " + TABLE_Recipe_Ingredient_List + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + recipeID + " INTEGER, "
                + ingredientID + " INTEGER,"
                + quantity + " DECIMAL,"
                + details + " TEXT" +")";
        sqLiteDatabase.execSQL(CREATE_RECIPE_INGREDIENT_TABLE);

        //Recipe Directions Table
        String CREATE_RECIPE_DIRECTIONS_TABLE = "CREATE TABLE " + TABLE_Recipe_Directions_List + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + recipeID + " INTEGER, "
                + directionText + " TEXT,"
                + directionNumber + " INTEGER"+")";
        sqLiteDatabase.execSQL(CREATE_RECIPE_DIRECTIONS_TABLE);

        //Category Table
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_Category_List + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + name + " TEXT" +")";
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);

        //Recipe Category Table
        String CREATE_RECIPE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_Recipe_Category_List + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + recipeID + " INTEGER, "
                + categoryID + " INTEGER" + ")";
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
        //TODO: TEST

        //find and drop existing databases
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Recipe_List);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Ingredient_List);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Recipe_Ingredient_List);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Category_List);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Recipe_Category_List);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Recipe_Directions_List);

        //rebuild the database
        onCreate(sqLiteDatabase);
    }

    /**
     * This method creates a new row in the Recipe table using the provided Recipe
     *
     * @param recipe
     * @return true if the operation was successful, false otherwise
     */
    public boolean createRecipe(Recipe recipe) {
        return false; //TODO: implement this method
    }

    /**
     * This method retrieves the recipe for the given recipeId
     *
     * @param recipeId
     * @return The recipe corresponding to the provided recipeId, or null if one is not found.
     */
    public Recipe getRecipe(int recipeId) {

        //TODO: TEST
        Recipe recipe = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_Recipe_List + "  WHERE " + recipeID +" = ? ", new String[] {String.valueOf(recipeId)});
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                    recipe = mapRecipe(cursor);
                    cursor.moveToNext();

                cursor.close();
            }
        } catch (Exception ex) {
            // Log.w("getAllRecipeIngredients()", ex.getMessage());
            return null;
        }


        return recipe;
    }

    /**
     * This method modifies the recipe in the recipe table with the same recipeId as the recipe provided
     * to be the recipe provided
     *
     * @param recipe the provided recipe
     * @return true if the operation was successful, false otherwise.
     */
    public boolean editRecipe(Recipe recipe, List<RecipeIngredient> recipeIngredient, List<RecipeCategory> recipeCategoryList, List<RecipeDirection> recipeDirectionList) {

        //TODO: Test
        //I want to save a version of recipe incase something goes wrong.
        //if so, update with original recipe.
        Recipe temp = getRecipe(recipe.getKeyID());
        boolean allpassed = true;
        int recipeId = recipe.getKeyID();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        //updating recipe portion of table
        try {
            ContentValues cVals = new ContentValues();
            cVals.put(title, recipe.getTitle());
            cVals.put(pTime, recipe.getPrep_time());
            cVals.put(tTime, recipe.getTotal_time());
            cVals.put(servings, recipe.getServings());
            cVals.put(favorited, recipe.getFavorited());
            sqLiteDatabase.update(TABLE_Ingredient_List, cVals, KEY_ID + " = ?", new String[]{String.valueOf(recipeId)});
        }
        catch( Exception e){
            System.out.println("updating recipe table failed");
            allpassed = false;
        }


        //delet ingredientlist and add new ingredientlist
        try {
            sqLiteDatabase.delete(TABLE_Recipe_Ingredient_List, recipeID + " = ?", new String[]{String.valueOf(recipeId)});
            int size = recipeIngredient.size();
            for(int i = 0; i < size; i++){
                if(addRecipeIngredient(recipeIngredient.get(i))){
                    allpassed = false;
                    System.out.println("addRecipeIngredient failed");
                }
            }
        }
        catch( Exception e){
            System.out.println("delete and add new ingredients failed");
            allpassed = false;
        }

        //delete recipecategorylist and add new recipecategorylist
        try {
            sqLiteDatabase.delete(TABLE_Recipe_Category_List, recipeID + " = ?", new String[]{String.valueOf(recipeId)});
            int size = recipeCategoryList.size();
            for(int i = 0; i < size; i++){
                if(addRecipeCategory(recipeCategoryList.get(i))){
                    allpassed = false;
                    System.out.println("addRecipeCategory failed");
                }
            }
        }
        catch( Exception e){
            System.out.println("delete and add new Category failed");
            allpassed = false;
        }

        //delete recipeDirections and add new recipeDirections
        try {
            sqLiteDatabase.delete(TABLE_Recipe_Directions_List, recipeID + " = ?", new String[]{String.valueOf(recipeId)});
            int size = recipeDirectionList.size();
            for(int i = 0; i < size; i++){
                if(addRecipeDirections(recipeDirectionList.get(i))){
                    allpassed = false;
                    System.out.println("addRecipeDirection failed");
                }
            }
        }
        catch( Exception e){
            System.out.println("delete and add new direction failed");
            allpassed = false;
        }

        //in case one of the recipe portions were not updated.
        if(!allpassed){
            //editRecipe(temp);
        }

        return allpassed;
    }

    /**
     * This method removes the recipe with the specified recipe id from the recipe's table and
     * removes all associated data from other tables
     *
     * @param recipeId
     * @return true if successful, false if not
     */
    public boolean deleteRecipe(int recipeId) {

         //TODO: TEST

        boolean allpassed = true;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        try {
            //delete recipe
            sqLiteDatabase.delete(TABLE_Recipe_List, KEY_ID + " = ?", new String[]{String.valueOf(recipeId)});

            //delete recipeIngredients
            sqLiteDatabase.delete(TABLE_Recipe_Ingredient_List, recipeID + " = ?", new String[]{String.valueOf(recipeId)});

            //delete recipeCategories
            sqLiteDatabase.delete(TABLE_Recipe_Category_List, recipeID + " = ?", new String[]{String.valueOf(recipeId)});

            //delete recipeDirections
            sqLiteDatabase.delete(TABLE_Recipe_Directions_List, recipeID + " = ?", new String[]{String.valueOf(recipeId)});

            //close writable database
            sqLiteDatabase.close();
        }catch(Exception e){
            allpassed = false;
        }
        return allpassed;
    }


    /**
     * This method returns a list of all recipes
     *
     * @return If successful in fetching the recipes it will return an Array list of recipes, if not
     *  it will return null.
     */
    public ArrayList<Recipe> getAllRecipes() {
        //TODO: TEST
        Recipe recipe;
        ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_Recipe_List, null );
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    recipe = mapRecipe(cursor);
                    recipeList.add(recipe);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception ex) {
            // Log.w("getAllRecipeIngredients()", ex.getMessage());
            return null;
        }


        return recipeList;
    }

    public ArrayList<RecipeIngredient> getAllRecipeIngredients(int recipeId) {
        //TODO: TEST
        RecipeIngredient recipeIngredient;
        ArrayList<RecipeIngredient> recipeIngredientList = new ArrayList<RecipeIngredient>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_Recipe_Ingredient_List + "  WHERE " + recipeID +" = ? ", new String[] {String.valueOf(recipeId)});
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    recipeIngredient = mapRecipeIngredient(cursor);
                    recipeIngredientList.add(recipeIngredient);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (Exception ex) {
           // Log.w("getAllRecipeIngredients()", ex.getMessage());
            return null;
        }


        return recipeIngredientList;
    }

    /**
     * This method returns a built ingredient object
     *
     * @return If successful in fetching the cursor and building the ingredient, it will
     * return an Ingredient Object, if not the method will return null.
     */
    private RecipeIngredient mapRecipeIngredient(Cursor cursor) {
        //TODO: TEST
        RecipeIngredient recipeIngredient = new RecipeIngredient();

        if (cursor != null) {
            if (cursor.getColumnIndex(KEY_ID) != -1) {
                int idIndex = cursor.getColumnIndexOrThrow(KEY_ID);
                recipeIngredient.setKeyID((cursor.getInt(idIndex)));
            }
            if (cursor.getColumnIndex(recipeID) != -1) {
                int recipeIdIndex = cursor.getColumnIndexOrThrow(recipeID);
                recipeIngredient.setRecipeID(cursor.getInt(recipeIdIndex));
            }
            if (cursor.getColumnIndex(ingredientID) != -1) {
                int ingredientIdIndex = cursor.getColumnIndexOrThrow(ingredientID);
                recipeIngredient.setIngredientID(cursor.getInt(ingredientIdIndex));
            }
            if (cursor.getColumnIndex(quantity) != -1) {
                int quantityIndex = cursor.getColumnIndexOrThrow(quantity);
                recipeIngredient.setQuantity(cursor.getDouble(quantityIndex));
            }
            if (cursor.getColumnIndex(details) != -1) {
                int detailsIndex = cursor.getColumnIndexOrThrow(details);
                recipeIngredient.setDetails(cursor.getString(detailsIndex));
            }


        }
        if (recipeIngredient.getKeyID() == -1){
            return null;
        }
        return recipeIngredient;

    }

    /**
     * This method returns a built Recipe object
     *
     * @return If successful in fetching the cursor and building the recipe, it will
     * return an Recipe Object, if not the method will return null.
     */
    private Recipe mapRecipe(Cursor cursor) {
        //TODO: TEST
        Recipe recipe = new Recipe();

        if (cursor != null) {
            if (cursor.getColumnIndex(KEY_ID) != -1) {
                int idIndex = cursor.getColumnIndexOrThrow(KEY_ID);
                recipe.setKeyID((cursor.getInt(idIndex)));
            }
            if (cursor.getColumnIndex(title) != -1) {
                int recipeTitleIndex = cursor.getColumnIndexOrThrow(title);
                recipe.setTitle(cursor.getString(recipeTitleIndex));
            }
            if (cursor.getColumnIndex(tTime) != -1) {
                int recipetTimeIndex = cursor.getColumnIndexOrThrow(tTime);
                recipe.setTotal_time(cursor.getInt(recipetTimeIndex));
            }
            if (cursor.getColumnIndex(pTime) != -1) {
                int recipepTimeIndex = cursor.getColumnIndexOrThrow(pTime);
                recipe.setPrep_time(cursor.getInt(recipepTimeIndex));
            }
            if (cursor.getColumnIndex(servings) != -1) {
                int recipeServingsIndex = cursor.getColumnIndexOrThrow(details);
                recipe.setServings(cursor.getDouble(recipeServingsIndex));
            }
            //TODO not sure how to get the bit type out of the cursor... could just do 1 or 0
            if (cursor.getColumnIndex(favorited) != -1) {
                int recipeFavoritedIndex = cursor.getColumnIndexOrThrow(favorited);
                if(cursor.getInt(recipeFavoritedIndex) == 1) {
                    recipe.setFavorited(true);
                }else{
                    recipe.setFavorited(true);
                }
            }

        }

        if (recipe.getKeyID() == -1){
            return null;
        }
        return recipe;

    }
    //TODO: Add methods to get recipes by ingredients, category, both, etc. (Iteration 1 task 5.5)


    /**
     * This method modifies the ingredient in the ingredients table with the same key id
     *
     * @param ingredient
     * @return If successful in updating, will return true
     */
    public boolean editIngredient(Ingredient ingredient) {
        //TODO: TEST
        //Ingredient temp = recipe.getKeyID());
        boolean allpassed = true;
        try {
            int id = ingredient.getKeyID();
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues cVals = new ContentValues();
            cVals.put(name, ingredient.getName());
            sqLiteDatabase.update(TABLE_Ingredient_List, cVals, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        }
        catch( Exception e){
            allpassed = false;
        }

        return allpassed;
    }

    boolean addRecipeIngredient(RecipeIngredient recipeIngredient) {
        //TODO: TEST
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();

        boolean allpassed = true;

        try {
            //contentValues.put(KEY_ID, Ingredient.getKey());  //not sure if this line is needed or if database will auto increment
            contentValues.put(recipeID, recipeIngredient.getRecipeID());
            contentValues.put(ingredientID, recipeIngredient.getIngredientID());
            contentValues.put(quantity, recipeIngredient.getQuantity());
            contentValues.put(details, recipeIngredient.getDetails());


            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(TABLE_Recipe_Ingredient_List, null, contentValues);
        }catch (Exception e){
            allpassed = false;
        }
        db.close();
        return allpassed;
    }

    boolean addRecipeCategory(RecipeCategory recipeCategory) {
        //TODO: TEST
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();

        boolean allpassed = true;

        try {
            //contentValues.put(KEY_ID, Ingredient.getKey());  //not sure if this line is needed or if database will auto increment
            contentValues.put(recipeID, recipeCategory.getRecipeID());
            contentValues.put(categoryID, recipeCategory.getCategoryID());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(TABLE_Recipe_Category_List, null, contentValues);
        }catch (Exception e){
            allpassed = false;
        }
        db.close();
        return allpassed;
    }

    boolean addCategory(Category category) {
        //TODO: TEST
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();

        boolean allpassed = true;

        try {
            //contentValues.put(KEY_ID, Ingredient.getKey());  //not sure if this line is needed or if database will auto increment
            contentValues.put(name, category.getName());

            // Insert the new row
            db.insert(TABLE_Recipe_Category_List, null, contentValues);
        }catch (Exception e){
            allpassed = false;
        }
        db.close();
        return allpassed;
    }

    boolean addRecipeDirections(RecipeDirection recipeDirection) {
        //TODO: TEST
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();

        boolean allpassed = true;

        try {
            //contentValues.put(KEY_ID, Ingredient.getKey());  //not sure if this line is needed or if database will auto increment
            contentValues.put(recipeID, recipeDirection.getRecipeID());
            contentValues.put(directionText, recipeDirection.getDirectionText());
            contentValues.put(directionNumber, recipeDirection.getDirectionNumber());

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(TABLE_Recipe_Directions_List, null, contentValues);
        }catch (Exception e){
            allpassed = false;
        }
        db.close();
        return allpassed;
    }
}
