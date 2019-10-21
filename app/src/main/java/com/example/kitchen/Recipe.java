package com.example.kitchen;

import java.sql.Time;

class Recipe {
    private String title;
    private CategoryList category;
    private IngredientList ingredients;
    private DirectionList directions;
    private double servings;
    private Time prep_time;
    private Time total_time;
    private boolean favorited;
    private int imageId;

    public Recipe() {
        favorited = false;
        imageId = -1;
    }

    public Recipe(String title, CategoryList category, IngredientList ingredients, DirectionList directions, double servings, Time prep_time, Time total_time, boolean favorited, int imageId) {
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

    public Time getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(Time prep_time) {
        this.prep_time = prep_time;
    }

    public Time getTotal_time() {
        return total_time;
    }

    public void setTotal_time(Time total_time) {
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
