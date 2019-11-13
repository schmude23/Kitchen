package com.example.kitchen;

public class ShoppingCartItem {
    private int keyID;
    private int recipeID;
    private int ingredientID;
    private String name;
    private String details;
    private double quantity;
    private String unit;

    public ShoppingCartItem(int keyID, int recipeID, int ingredientID, String name, String details, double quantity, String unit) {
        this.keyID = keyID;
        this.recipeID = recipeID;
        this.ingredientID = ingredientID;
        this.name = name;
        this.details = details;
        this.quantity = quantity;
        this.unit = unit;

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

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
