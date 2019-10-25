package com.example.kitchen;

import java.sql.Time;
import java.util.List;

class Recipe {
    private int keyID = -1;
    private String title = null;
    private double servings = -1;
    private int prep_time = -1;
    private int total_time = -1;
    private boolean favorited = false;
    private List<RecipeIngredient> ingredientList;
    private List<RecipeDirections> directionsList;

    public Recipe() {    }

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public Recipe(String title, double servings, int prep_time, int total_time, boolean favorited) {
        this.title = title;
        this.servings = servings;
        this.prep_time = prep_time;
        this.total_time = total_time;
        this.favorited = favorited;
    }

    public Recipe(String title, double servings, int prep_time, int total_time, boolean favorited, List<RecipeIngredient> ingredients, List<RecipeDirections> directions) {
        this.title = title;
        this.servings = servings;
        this.prep_time = prep_time;
        this.total_time = total_time;
        this.favorited = favorited;
        this.ingredientList = ingredients;
        this.directionsList = directions;
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

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public List<RecipeIngredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<RecipeIngredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<RecipeDirections> getDirectionsList() {
        return directionsList;
    }

    public void setDirectionsList(List<RecipeDirections> directionsList) {
        this.directionsList = directionsList;
    }

    //TODO: Add method to retrive image
}
