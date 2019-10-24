package com.example.kitchen;

public class RecipeIngredient {
    private int keyID = -1;
    private int recipeID = -1;
    private int ingredientID = -1;
    private double quantity = -1;
    private String details = null;

    public int getRecipeID() { return recipeID; }

    public void setRecipeID(int recipeID) { this.recipeID = recipeID; }

    public int getIngredientID() { return ingredientID;}

    public void setIngredientID(int ingredientID) { this.ingredientID = ingredientID; }

    public int getKeyID() { return keyID; }

    public void setKeyID(int keyID) { this.keyID = keyID; }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
