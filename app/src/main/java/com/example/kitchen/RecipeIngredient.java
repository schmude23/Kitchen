package com.example.kitchen;

public class RecipeIngredient {
    private int keyID;
    private int recipeID;
    private int ingredientID;
    private double quantity;
    private String unit;
    private String details;

    public RecipeIngredient() {
        keyID = -1;
        recipeID = -1;
        ingredientID = -1;
        quantity = -1;
    }

    public RecipeIngredient(int keyID, int recipeID, int ingredientID, double quantity, String unit, String details) {
        this.keyID = keyID;
        this.recipeID = recipeID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
        this.unit = unit;
        this.details = details;
    }

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

    public String getUnit() { return unit; }

    public void setUnit(String unit) { this.unit = unit; }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "keyID=" + keyID +
                ", recipeID=" + recipeID + "\n" +
                ", ingredientID=" + ingredientID + "\n" +
                ", quantity=" + quantity + "\n" +
                ", unit='" + unit + "\n" +
                ", details='" + details +
                '}' + "\n";
    }
}
