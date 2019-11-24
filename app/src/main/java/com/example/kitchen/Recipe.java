package com.example.kitchen;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class models a Recipe
 */
class Recipe implements Comparable<Recipe>{
    private int keyID;
    private String title;
    private double servings;
    private int prep_time;
    private int total_time;
    private boolean favorited;
    private Bitmap image;
    private List<RecipeIngredient> ingredientList;
    private List<RecipeDirection> directionsList;
    private List<RecipeCategory> categoryList;


    /**
     * This is the default constructor which sets all numerical values at -1 and prepares the class for use
     */
    public Recipe() {
        keyID = -1;
        servings = -1;
        prep_time = -1;
        total_time = -1;
        favorited = false;
        ingredientList = new ArrayList<RecipeIngredient>();
        directionsList = new ArrayList<RecipeDirection>();
        categoryList = new ArrayList<RecipeCategory>();
    }

    /**
     * This constructor will attempt to read the contents of a recipe from the string provided.
     * if that fails it will just call the default constructor.
     *
     * @param string the string representation of the Recipe
     */
    public Recipe(String string, Context context) {

        this();

        int keyID = -1;
        String title = "";
        double servings = -1;
        int prep_time = -1;
        int total_time = -1;
        boolean favorited = false;
        List<RecipeIngredient> ingredientList = new ArrayList<>();
        List<RecipeDirection> directionsList = new ArrayList<>();
        List<RecipeCategory> categoryList = new ArrayList<>();


        //Split the string on some identifiers.
        String[] segments = string.split("[=,]");


        //Try to get KeyID
        if (segments[0].equals("Recipe{keyID")) {

            try {
                keyID = Integer.parseInt(segments[1]);
            } catch (Exception e) {
                return;
            }

        } else {
            return;
        }

        //Try to get title
        if (segments[2].equals(" title")) {
            title = segments[3].trim();
        } else {
            return;
        }

        //Try to get # of servings
        if (segments[4].equals(" servings")) {
            try {
                servings = Double.parseDouble(segments[5].trim());
            } catch (Exception e) {
                return;
            }
        } else {
            return;
        }

        //Try to get prep time
        if (segments[6].equals(" prep_time")) {

            try {
                prep_time = Integer.parseInt(segments[7].trim());
            } catch (Exception e) {
                return;
            }

        } else {
            return;
        }

        //Try to get total time

        if (segments[8].equals(" total_time")) {

            try {
                total_time = Integer.parseInt(segments[9].trim());
            } catch (Exception e) {
                return;
            }

        } else {
            return;
        }


        /* //This is not needed since favorited status should not be shared between devices.
        //Try to get favorited status
        if (segments[10].equals(" favorited")) {

            if (segments[11].substring(0, 5).trim().equals("true")) {
                favorited = true;
            } else if (segments[11].substring(0, 5).trim().equals("false")) {
                favorited = false;
            } else {
                return;
            }

        } else {
            return;
        }
        */

        //Try to get Ingredients List

        segments = string.split("RecipeIngredient\\{");

        for (int i = 1; i < segments.length; i++) {

            String[] partsOfIngredient = segments[i].split("[=,]");

            int ingredientKeyID = -1;
            int recipeId = -1;
            int ingredientId = -1;
            double quantity = -1;
            String unit = "";
            String details = "";

            if (partsOfIngredient[0].trim().equals("keyID")) {
                try {
                    ingredientKeyID = Integer.parseInt(partsOfIngredient[1].trim());
                } catch (NumberFormatException ex) {
                    break;
                }
            }

            if (partsOfIngredient[2].trim().equals("recipeID")) {
                try {
                    recipeId = Integer.parseInt(partsOfIngredient[3].trim());
                } catch (NumberFormatException ex) {
                    break;
                }
            }

            if (partsOfIngredient[4].trim().equals("ingredientName")) {
                    String ingredientName = partsOfIngredient[5].trim();
                    DatabaseHelper db = new DatabaseHelper(context);
                    ingredientId = db.getIngredient(ingredientName);

                    if (ingredientId == -1) {
                        ingredientId = db.addIngredient(new Ingredient(-1, ingredientName));
                    }
            }

            if (partsOfIngredient[6].trim().equals("quantity")) {
                try {
                    quantity = Double.parseDouble(partsOfIngredient[7].trim());
                } catch (NumberFormatException ex) {
                    break;
                }
            }

            if (partsOfIngredient[8].trim().equals("unit")) {
                unit = partsOfIngredient[9].trim();
            }

            if (partsOfIngredient[10].trim().equals("details")) {

                if (partsOfIngredient[11].length() != 0) {
                    String part = partsOfIngredient[11].split("\\}")[0];
                    details = part.trim();
                }
            }

            RecipeIngredient ingredient = new RecipeIngredient(ingredientKeyID, recipeId, ingredientId, quantity, unit, details);

            ingredientList.add(ingredient);

        }

        //Try to get the directions list

        segments = string.split("RecipeDirection\\{");

        for (int i = 1; i < segments.length; i++) {

            String[] partsOfDirection = segments[i].split("[=,]");

            int directionKeyId = -1;
            int recipeId = -1;
            String directionText = "";
            int directionNumber = -1;

            if (partsOfDirection[0].trim().equals("keyID")) {
                try {
                    directionKeyId = Integer.parseInt(partsOfDirection[1].trim());
                } catch (NumberFormatException ex) {
                    break;
                }
            }

            if (partsOfDirection[2].trim().equals("recipeID")) {
                try {
                    recipeId = Integer.parseInt(partsOfDirection[3].trim());
                } catch (NumberFormatException ex) {
                    break;
                }
            }

            if (partsOfDirection[4].trim().equals("directionText")) {
                directionText = partsOfDirection[5].trim();
            }

            if (partsOfDirection[6].trim().equals("directionNumber")) {
                try {
                    String part = partsOfDirection[7].split("\\}")[0];
                    directionNumber = Integer.parseInt(part.trim());
                } catch (NumberFormatException ex) {
                    break;
                }
            }

            RecipeDirection direction = new RecipeDirection(directionKeyId, recipeId, directionText, directionNumber);

            directionsList.add(direction);
        }

        //Try to get the categories
        segments = string.split("RecipeCategory\\{");

        for (int i = 1; i < segments.length; i++) {


            String[] partsOfCategory = segments[i].split("[=,]");

            int categoryKeyID = -1;
            int recipeId = -1;
            int categoryId = -1;

            if (partsOfCategory[0].trim().equals("keyID")) {
                try {
                    categoryKeyID = Integer.parseInt(partsOfCategory[1].trim());
                } catch (NumberFormatException ex) {
                    break;
                }
            }

            if (partsOfCategory[2].trim().equals("recipeID")) {
                try {
                    recipeId = Integer.parseInt(partsOfCategory[3].trim());
                } catch (NumberFormatException ex) {
                    break;
                }
            }

            if (partsOfCategory[4].trim().equals("categoryID")) {
                    String categoryName = partsOfCategory[5].split("\\}")[0];
                    DatabaseHelper db = new DatabaseHelper(context);
                    categoryId = db.getCategory(categoryName);

                    //Check for the category not being found
                    if (categoryId == -1) {
                        categoryId = db.addCategory(new Category(categoryName));
                    }

            }

            RecipeCategory category = new RecipeCategory(categoryKeyID, recipeId, categoryId);
            categoryList.add(category);

        }

        this.keyID = keyID;
        this.title = title;
        this.servings = servings;
        this.prep_time = prep_time;
        this.total_time = total_time;
        this.favorited = favorited;
        this.ingredientList = ingredientList;
        this.directionsList = directionsList;
        this.categoryList = categoryList;


    }

    /**
     * Getter method for KeyId
     *
     * @return the keyID
     */
    public int getKeyID() {
        return keyID;
    }

    /**
     * Setter method for keyID
     *
     * @param keyID the new keyID
     */
    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    /**
     * This constructor for Recipe enables setting everything but the lists.
     *
     * @param title the title of the recipe
     * @param servings the number of servings in the recipe
     * @param prep_time the prep time for the recipe
     * @param total_time the total time for the recipe
     * @param favorited the favorited status for the recipe
     */
    public Recipe(String title, double servings, int prep_time, int total_time, boolean favorited) {
        this(); //Call default constructor
        this.title = title;
        this.servings = servings;
        this.prep_time = prep_time;
        this.total_time = total_time;
        this.favorited = favorited;
    }

    /**
     * This constructor enables setting everything but the id field in the Recipe class
     *
     * @param title the title of the recipe
     * @param servings the number of servings that the recipe makes
     * @param prep_time the prep time for the recipe
     * @param total_time the total time needed for the recipe
     * @param favorited the favorited status of the recipe
     * @param ingredients the list of ingredients in the recipe
     * @param directions the list of directions in the recipe
     * @param categories the list of categories for the recipe
     */
    public Recipe(String title, double servings, int prep_time, int total_time, boolean favorited, List<RecipeIngredient> ingredients, List<RecipeDirection> directions, List<RecipeCategory> categories) {
        this(); //Call default constructor
        this.title = title;
        this.servings = servings;
        this.prep_time = prep_time;
        this.total_time = total_time;
        this.favorited = favorited;
        this.ingredientList = ingredients;
        this.directionsList = directions;
        this.categoryList = categories;
    }

    /**
     * This constructor enables setting everything in the Recipe class
     *
     * @param keyID the id of the recipe
     * @param title the title of the recipe
     * @param servings the number of servings that the recipe makes
     * @param prep_time the prep time for the recipe
     * @param total_time the total time needed for the recipe
     * @param favorited the favorited status of the recipe
     * @param ingredients the list of ingredients in the recipe
     * @param directions the list of directions in the recipe
     * @param categories the list of categories for the recipe
     */
    public Recipe(int keyID, String title, double servings, int prep_time, int total_time, boolean favorited, List<RecipeIngredient> ingredients, List<RecipeDirection> directions, List<RecipeCategory> categories) {
        this(); //Call default constructor
        this.keyID = keyID;
        this.title = title;
        this.servings = servings;
        this.prep_time = prep_time;
        this.total_time = total_time;
        this.favorited = favorited;
        this.ingredientList = ingredients;
        this.directionsList = directions;
        this.categoryList = categories;
    }

    /**
     * This method is a renamed copy of setTitle. I am not sure why this exists.
     *
     * @param name the new title of the recipe
     */
    public void createRecipe(String name) {
        this.title = name;
    }

    /**
     * Getter method for title
     *
     * @return the title of the recipe
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter method for the title
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter method for the servings
     *
     * @return the number of servings that the recipe makes
     */
    public Double getServings() {
        return servings;
    }

    /**
     * Setter method for the servings
     *
     * @param servings the new number of servings
     */
    public void setServings(Double servings) {
        this.servings = servings;
    }

    /**
     * Getter method for preptime
     *
     * @return the prep time for the recipe
     */
    public int getPrep_time() {
        return prep_time;
    }

    /**
     * Setter method for prep_time
     *
     * @param prep_time the new prep_time
     */
    public void setPrep_time(int prep_time) {
        this.prep_time = prep_time;
    }

    /**
     * Getter method for total_time
     *
     * @return the total time needed to make the recipe
     */
    public int getTotal_time() {
        return total_time;
    }

    /**
     * Setter method for total_time
     *
     * @param total_time the new total_time
     */
    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    /**
     * Getter method for the favoriteed status
     *
     * @return the favorited status
     */
    public Boolean getFavorited() {
        return favorited;
    }

    /**
     * Setter method for the favorited status
     *
     * @param favorited
     */
    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    /**
     * Getter method for the ingredientList
     *
     * @return the list of all RecipeIngredients in the recipe
     */
    public List<RecipeIngredient> getIngredientList() {
        return ingredientList;
    }

    /**
     * Setter method for the ingredientList
     *
     * @param ingredientList the new ingredientList
     */
    public void setIngredientList(List<RecipeIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    /**
     * this method gets the directions list
     *
     * @return the list of directions for the recipe
     */
    public List<RecipeDirection> getDirectionsList() {
        return directionsList;
    }

    /**
     * setter method for the directions list
     *
     * @param directionsList the new directions list
     */
    public void setDirectionsList(List<RecipeDirection> directionsList) {
        this.directionsList = directionsList;
    }

    /**
     * Getter method for the category list
     *
     * @return a list of categories for the recipe
     */
    public List<RecipeCategory> getCategoryList() {
        return categoryList;
    }

    /**
     * setter method for the category list
     *
     * @param categoryList the new list of categories
     */
    public void setCategoryList(List<RecipeCategory> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public int compareTo(Recipe obj) {
        //TODO: TEst (used only in getAllRecipeSorted()
        //sort in ascending order
        return this.getTitle().compareTo(obj.getTitle());
        //sort in descending order
        //return obj.age-this.age;
    }

    /**
     * This method converts the recipe to a string format for debugging
     *
     * @return a string representation of all the data in the class
     */
    @Override
    public String toString() {
        int ingList = ingredientList.size();
        int dirList = directionsList.size();
        int catList = categoryList.size();
        String strIngList = null;
        String strDirList = null;
        String strCatList = null;
        int i = 0;

        //creates list for ingredient list
        while (i < ingList) {
            strIngList = strIngList + ingredientList.get(i).toString();
            i++;
        }
        i = 0;

        //creates list for Direction list
        while (i < dirList) {
            strDirList = strDirList + directionsList.get(i).toString();
            i++;
        }
        i = 0;

        //creates list for category list
        while (i < catList) {
            strCatList = strCatList + categoryList.get(i).toString();
            i++;
        }

        return "Recipe{" +
                "keyID=" + keyID +
                ", title=" + title + "\n" +
                ", servings=" + servings + "\n" +
                ", prep_time=" + prep_time + "\n" +
                ", total_time=" + total_time + "\n" +
                ", favorited=" + favorited + "\n" +
                strIngList +
                strDirList +
                strCatList +
                '}';
    }

    /**
     * This method converts the recipe to a string format for sending to another device
     *
     * @return a string representation of all the data in the class
     */
    public String toString(Context context) {
        int ingList = ingredientList.size();
        int dirList = directionsList.size();
        int catList = categoryList.size();
        String strIngList = null;
        String strDirList = null;
        String strCatList = null;
        int i = 0;

        //creates list for ingredient list
        while (i < ingList) {
            strIngList = strIngList + ingredientList.get(i).toString(context);
            i++;
        }
        i = 0;

        //creates list for Direction list
        while (i < dirList) {
            strDirList = strDirList + directionsList.get(i).toString();
            i++;
        }
        i = 0;

        //creates list for category list
        while (i < catList) {
            strCatList = strCatList + categoryList.get(i).toString(context);
            i++;
        }

        return "Recipe{" +
                "keyID=" + keyID +
                ", title=" + title + "\n" +
                ", servings=" + servings + "\n" +
                ", prep_time=" + prep_time + "\n" +
                ", total_time=" + total_time + "\n" +
                ", favorited=" + favorited + "\n" +
                strIngList +
                strDirList +
                strCatList +
                '}';
    }

    /**
     * This method will set the image for the Recipe to the specified image
     *
     * @param image   the new image
     * @param context The application context
     * @return will return true if successful, false if not.
     */
    public boolean setImage(Bitmap image, Context context) {

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File file = new File(directory, keyID + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (java.io.IOException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }

        this.image = image;


        return true;

    }

    /**
     * This method retrieves the image stored for the Recipe
     *
     * @param context The application context
     * @return the image stored for the Recipe or a default filler image if not.
     */
    public Bitmap getImage(Context context) {

        if (image != null) {
            return image;
        }


        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("images", Context.MODE_PRIVATE);
            File f = new File(directory, keyID + ".jpg");
            image = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            //Set default image
            image = BitmapFactory.decodeResource(context.getResources(), R.drawable.plate);
        }

        return image;
    }

}
