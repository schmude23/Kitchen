package com.example.kitchen;

import java.util.List;

public class RecipeCategory {
    private int keyID;
    private String name;
    private int recipeID;
    private int categoryID;

    public RecipeCategory() {
        keyID = -1;
        recipeID = -1;
        categoryID = -1;
    }

    public RecipeCategory(int keyID, String name, int recipeID, int categoryID) {
        this.keyID = keyID;
        this.name = name;
        this.recipeID = recipeID;
        this.categoryID = categoryID;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RecipeCategory{" +
                "keyID=" + keyID +
                ", name='" + name + '\'' +
                ", recipeID=" + recipeID + "\n" +
                ", categoryID=" + categoryID +
                '}' + "\n";
    }
}
