package com.example.kitchen;

public class RecipeDirection {
    private int keyID = -1;
    private int recipeID = -1;
    private String directionText = null;
    private int directionNumber = -1;

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

    public String getDirectionText() {
        return directionText;
    }

    public void setDirectionText(String directionText) {
        this.directionText = directionText;
    }

    public int getDirectionNumber() {
        return directionNumber;
    }

    public void setDirectionNumber(int directionNumber) {
        this.directionNumber = directionNumber;
    }
}
