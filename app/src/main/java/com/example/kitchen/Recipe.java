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

class Recipe {
    private int keyID;
    private String title;
    private double servings;
    private int prep_time;
    private int total_time;
    private boolean favorited;
    private List<RecipeIngredient> ingredientList;
    private List<RecipeDirection> directionsList;
    private List<RecipeCategory> categoryList;


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

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public Recipe(String title, double servings, int prep_time, int total_time, boolean favorited) {
        this(); //Call default constructor
        this.title = title;
        this.servings = servings;
        this.prep_time = prep_time;
        this.total_time = total_time;
        this.favorited = favorited;
    }

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

    public void createRecipe(String name){
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getServings() {
        return servings;
    }

    public void setServings(Double servings) {
        this.servings = servings;
    }

    public int getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(int prep_time) {
        this.prep_time = prep_time;
    }

    public int getTotal_time() {
        return total_time;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    //also updates favorite int for TABLE_Recipe
    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public List<RecipeIngredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<RecipeIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<RecipeDirection> getDirectionsList() {
        return directionsList;
    }

    public void setDirectionsList(List<RecipeDirection> directionsList) {
        this.directionsList = directionsList;
    }

    public List<RecipeCategory> getCategoryList() { return categoryList; }

    public void setCategoryList(List<RecipeCategory> categoryList){
        this.categoryList = categoryList;
    }

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
        while( i < ingList){
            strIngList = strIngList + ingredientList.get(i).toString();
            i++;
        }
        i = 0;

        //creates list for Direction list
        while( i < dirList){
            strDirList = strDirList + directionsList.get(i).toString();
            i++;
        }
        i = 0;

        //creates list for category list
        while( i < catList){
            strCatList = strCatList + categoryList.get(i).toString();
            i++;
        }

        return "Recipe{" +
                "keyID=" + keyID +
                ", title='" + title + '\'' + "\n" +
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
     * @param image the new image
     * @param context The application context
     * @return will return true if successful, false if not.
     */
    public boolean setImage(Bitmap image, Context context) {

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File file = new File(directory, keyID + ".jpg");
        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                return false;
            }
        }

        return true;

    }

    /**
     * This method retrieves the image stored for the Recipe
     *
     * @param context The application context
     *
     * @return the image stored for the Recipe or a default filler image if not.
     */
    public Bitmap getImage(Context context) {
        Bitmap image = null;

        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("images", Context.MODE_PRIVATE);
            File f = new File(directory, keyID + ".jpg");
            image = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e) {
            //Set default image
            image = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_default_image);
        }

        return image;
    }
}
