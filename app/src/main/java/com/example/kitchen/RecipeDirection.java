package com.example.kitchen;

public class RecipeDirection {

    private int keyID;
    private int recipeID;
    private String directionText;
    private int directionNumber;

    public RecipeDirection() {
        keyID = -1;
        recipeID = -1;
        directionNumber = -1;
    }

    public RecipeDirection(int keyID, int recipeID, String directionText, int directionNumber) {
        this.keyID = keyID;
        this.recipeID = recipeID;
        this.directionText = directionText;
        this.directionNumber = directionNumber;
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

    @Override
    public String toString() {
        return "RecipeDirection{" +
                "keyID=" + keyID +
                ", recipeID=" + recipeID + + '\'' +
                ", directionText='" + directionText  + "\n" +
                ", directionNumber=" + directionNumber +
                '}' + "\n";
    }
}
