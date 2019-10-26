package com.example.kitchen;

import java.util.List;

class Recipe {
    private int keyID = -1;
    private String title = null;
    private double servings = -1;
    private int prep_time = -1;
    private int total_time = -1;
    private boolean favorited = false;
    private int favoritedInt = -1;
    private List<RecipeIngredient> ingredientList;
    private List<RecipeDirection> directionsList;
    private List<RecipeCategory> categoryList;


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

    public Recipe(String title, double servings, int prep_time, int total_time, boolean favorited, List<RecipeIngredient> ingredients, List<RecipeDirection> directions, List<RecipeCategory> categories) {
        this.title = title;
        this.servings = servings;
        this.prep_time = prep_time;
        this.total_time = total_time;
        this.favorited = favorited;
        this.ingredientList = ingredients;
        this.directionsList = directions;
        this.categoryList = categories;
        setFavoritedInt();
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
        setFavoritedInt();
    }

    public int getFavoritedInt() { return favoritedInt; }

    //updates favorite int for TABLE_Recipe
    public void setFavoritedInt() {
        if(favorited){
            favoritedInt = 1;
        }
        else{
            favoritedInt=0;
        }
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
                ", favoritedInt=" + favoritedInt + "\n" +
                strIngList +
                strDirList +
                strCatList +
                '}';
    }

    //TODO: Add method to retrive image
}
