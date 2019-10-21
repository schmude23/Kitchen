package com.example.kitchen;

import java.sql.Time;

class Recipe {
    private int keyID = -1;
    private String title = null;
    private CategoryList category = null;
    private IngredientList ingredients = null;
    private DirectionList directions = null;
    private double servings = -1;
    private int prep_time = -1;
    private int total_time = -1;
    private boolean favorited = false;
    private int imageId = -1;

    public Recipe() {    }

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public Recipe(String title, CategoryList category, IngredientList ingredients, DirectionList directions, double servings, int prep_time, int total_time, boolean favorited, int imageId) {
        this.title = title;
        this.category = category;
        this.ingredients = ingredients;
        this.directions = directions;
        this.servings = servings;
        this.prep_time = prep_time;
        this.total_time = total_time;
        this.favorited = favorited;
        this.imageId = imageId;
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

    public CategoryList getCategory() {
        return category;
    }

    public void setCategory(CategoryList category) {
        this.category = category;
    }

    public IngredientList getIngredients() {
        return ingredients;
    }

    public void setIngredients(IngredientList ingredients) {
        this.ingredients = ingredients;
    }

    public DirectionList getDirections() {
        return directions;
    }

    public void setDirections(DirectionList directions) {
        this.directions = directions;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
