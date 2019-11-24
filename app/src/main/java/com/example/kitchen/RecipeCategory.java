package com.example.kitchen;

import android.content.Context;

import java.util.List;

/**
 * This class models a RecipeCategory
 */
public class RecipeCategory {
    private int keyID;
    private int recipeID;
    private int categoryID;

    /**
     * Default constructor that sets all values to -1
     */
    public RecipeCategory() {
        keyID = -1;
        recipeID = -1;
        categoryID = -1;
    }

    /**
     * This constructor allows setting of all of the data in the class.
     *
     * @param keyID the keyID for this RecipeCategory
     * @param recipeID the recipeID for the recipe this belongs to
     * @param categoryID the category ID for the category this is referencing.
     */
    public RecipeCategory(int keyID, int recipeID, int categoryID) {
        this.keyID = keyID;
        this.recipeID = recipeID;
        this.categoryID = categoryID;
    }

    /**
     * Getter method for keyId
     *
     * @return the keyID
     */
    public int getKeyID() {
        return keyID;
    }

    /**
     * Setter method for the keyId
     *
     * @param keyID the new keyID
     */
    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    /**
     * getter method for the recipeID
     *
     * @return the recipeID
     */
    public int getRecipeID() {
        return recipeID;
    }

    /**
     * Setter method for the RecipeID
     *
     * @param recipeID the new RecipeID
     */
    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    /**
     * Getter method for the Category ID
     *
     * @return the Category ID
     */
    public int getCategoryID() {
        return categoryID;
    }

    /**
     * Setter method for the CategoryID
     *
     * @param categoryID the new categoryID
     */
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    /**
     * This method gives a string representation of the data in the class for debugging purposes.
     *
     * @return a string representation of all of the data in the class.
     */
    @Override
    public String toString() {
        return "RecipeCategory{" +
                "keyID=" + keyID +
                ", recipeID=" + recipeID + "\n" +
                ", categoryID=" + categoryID +
                '}' + "\n";
    }

    /**
     * This method gives a string representation of the data in the class for sending to another device
     *
     * @return a string representation of all of the data in the class.
     */
    public String toString(Context context) {
        DatabaseHelper db = new DatabaseHelper(context);
        String categoryName = db.getCategory(categoryID).getName();
        return "RecipeCategory{" +
                "keyID=" + keyID +
                ", recipeID=" + recipeID + "\n" +
                ", categoryName=" + categoryName +
                '}' + "\n";
    }
}
