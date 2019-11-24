package com.example.kitchen;

/**
 * This class models a recipe direction
 */
public class RecipeDirection {

    private int keyID;
    private int recipeID;
    private String directionText;
    private int directionNumber;

    /**
     * This is the default constructor that sets all neumerical values to -1
     */
    public RecipeDirection() {
        keyID = -1;
        recipeID = -1;
        directionNumber = -1;
    }

    /**
     * This is a constructor that enables setting all the fields of RecipeDirection
     *
     * @param keyID the keyID for the RecipeDirection
     * @param recipeID the recipeID for the recipe this belongs to
     * @param directionText the text portion of the direction
     * @param directionNumber the position of this direction in order.
     */
    public RecipeDirection(int keyID, int recipeID, String directionText, int directionNumber) {
        this.keyID = keyID;
        this.recipeID = recipeID;
        this.directionText = directionText;
        this.directionNumber = directionNumber;
    }

    /**
     * getter method for keyID
     *
     * @return the keyID
     */
    public int getKeyID() {
        return keyID;
    }

    /**
     * setter method for the keyID
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
     * Setter method for the recipeID
     *
     * @param recipeID the new recipeID
     */
    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    /**
     * getter method for the direction text
     *
     * @return the direction text
     */
    public String getDirectionText() {
        return directionText;
    }

    /**
     * setter method for the direction text
     *
     * @param directionText the new direction text
     */
    public void setDirectionText(String directionText) {
        this.directionText = directionText;
    }

    /**
     * getter method for the direction number
     *
     * @return the direction number
     */
    public int getDirectionNumber() {
        return directionNumber;
    }

    /**
     * setter method for the direction number
     *
     * @param directionNumber the new direction number
     */
    public void setDirectionNumber(int directionNumber) {
        this.directionNumber = directionNumber;
    }

    /**
     * This method converts the data of the class to a string for debugging purposes.
     *
     * @return a string representation of the data in the class.
     */
    @Override
    public String toString() {
        return "RecipeDirection{" +
                "keyID=" + keyID +
                ", recipeID=" + recipeID  +
                ", directionText=" + directionText  + "\n" +
                ", directionNumber=" + directionNumber +
                '}' + "\n";
    }
}
