package com.example.kitchen;

import java.util.List;

public class RecipeCategory {
    private int keyID = -1;
    private int recipeID = -1;
    private int categoryID = -1;

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}
