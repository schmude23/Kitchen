package com.example.kitchen;

/**
 * This class models a Recipe Ingredient and contains all of the relevant information
 */
public class RecipeIngredient {
    private int keyID;
    private int recipeID;
    private int ingredientID;
    private double quantity;
    private String unit;
    private String details;

    /**
     * This is the default constructor. Initializes all numerical values to -1
     */
    public RecipeIngredient() {
        keyID = -1;
        recipeID = -1;
        ingredientID = -1;
        quantity = -1;
    }

    /**
     * This is a constructor for setting all of the values in the Recipe Ingredient
     *
     * @param keyID the keyID for the Recipe Ingredient (should be -1 if not registered in the database)
     * @param recipeID the id of the recipe this recipe ingredient is attached to
     * @param ingredientID the id of the ingredient this recipe ingredient is referencing
     * @param quantity the quantity of the ingredient
     * @param unit the unit for the ingredient
     * @param details any other details on the ingredient.
     */
    public RecipeIngredient(int keyID, int recipeID, int ingredientID, double quantity, String unit, String details) {
        this.keyID = keyID;
        this.recipeID = recipeID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
        this.unit = unit;
        this.details = details;
    }

    /**
     * Getter method for Recipe ID
     *
     * @return the recipe id
     */
    public int getRecipeID() { return recipeID; }

    /**
     * Setter method for Recipe ID
     *
     * @param recipeID the new Recipe ID
     */
    public void setRecipeID(int recipeID) { this.recipeID = recipeID; }

    /**
     * Getter method for Ingredient ID
     *
     * @return the ingredient id
     */
    public int getIngredientID() { return ingredientID;}

    /**
     * Setter method for Ingredient ID
     *
     * @param ingredientID the new ingredient id
     */
    public void setIngredientID(int ingredientID) { this.ingredientID = ingredientID; }

    /**
     * Getter method for the keyID
     *
     * @return the key ID
     */
    public int getKeyID() { return keyID; }

    /**
     * Setter method for keyID
     *
     * @param keyID the new keyID
     */
    public void setKeyID(int keyID) { this.keyID = keyID; }

    /**
     * Getter method for quantity
     *
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Setter method for quantity
     *
     * @param quantity the new quantity
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * getter method for unit
     *
     * @return the unit
     */
    public String getUnit() { return unit; }

    /**
     * setter method for unit
     *
     * @param unit the new unit
     */
    public void setUnit(String unit) { this.unit = unit; }

    /**
     * Getter method for details
     *
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * Setter method for details
     *
     * @param details the new details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * This method converts the data in this class into a string form for debugging purposes.
     *
     * @return a string representation of the contents of the class.
     */
    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "keyID=" + keyID +
                ", recipeID=" + recipeID + "\n" +
                ", ingredientID=" + ingredientID + "\n" +
                ", quantity=" + quantity + "\n" +
                ", unit=" + unit + "\n" +
                ", details=" + details +
                '}' + "\n";
    }
}
