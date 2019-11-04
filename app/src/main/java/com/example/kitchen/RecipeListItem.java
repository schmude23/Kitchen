package com.example.kitchen;

import android.graphics.Bitmap;

public class RecipeListItem {
    private String recipe_name;
    private String servings;
    private String prep_time;
    private String total_time;
    private Bitmap image;
    private boolean favorited;

    RecipeListItem(String recipe_name, double servings,  int prep_time, int total_time, Bitmap image, boolean favorited){
        this.recipe_name = recipe_name;
        this.servings = String.valueOf(servings);
        this.prep_time = prep_time + " min";
        this.total_time = total_time + " min";
        this.image = image;
        this.favorited = favorited;
    }

    public String getRecipeName() {
        return recipe_name;
    }

    public void setRecipeName(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }
    public String getPrepTime() {
        return prep_time;
    }

    public void setPrep_time(String prep_time) {
        this.prep_time = prep_time;
    }

    public String getTotalTime() {
        return total_time;
    }

    public void setTotalTime(String total_time) {
        this.total_time = total_time;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
