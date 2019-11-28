package com.example.kitchen;
import android.content.Context;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class RecipeCopyPasteCreator {
    private Scanner recipeScanner;
    private ArrayList<RecipeIngredient> recipeIngredientList;
    private ArrayList<RecipeDirection> recipeDirectionList;
    private Recipe recipe = new Recipe();
    //TODO: how do you set up the database?
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    DatabaseHelper database = new DatabaseHelper(appContext.getApplicationContext());

    /**
     * This method creates takes a string made from a found recipe and converts and normalizes the
     * string. then creates a properly formatted recipe.
     *
     * @param recipeTxt
     * @return true if the operation was successful, false otherwise
     */
    public Boolean main(String recipeTxt) {
        //TODO: implement
        recipeScanner = new Scanner(recipeTxt);
        List<String> tokens = new ArrayList<>();
        String recipeTitle = "";
        Boolean titleFound = false;

        while (recipeScanner.hasNextLine()) {
            tokens = new ArrayList<>();
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());

            //this should only run once to add the title in the beggining. it might be able to be
            //taken out of the while loop.
            while (!titleFound && lineScanner.hasNext()) {
                recipeTitle += lineScanner.next() + " ";
            }
            titleFound = true;

            //if the line is not blank, send to token checker for validation.
            while (lineScanner.hasNext()) {
                tokens.add(lineScanner.next());
            }
            tokenChecker(tokens);

            lineScanner.close();
        }

        recipeScanner.close();

        //make sure ingredient and directions actually have something in them.
        if(recipeIngredientList.size() > 0 && recipeDirectionList.size() >0 ){
            recipe.setTitle(recipeTitle);
            database.addRecipe(recipe);
            return true;
        }
            return false;

    }

    /**
     * This method creates takes a string made from a found recipe and converts and normalizes the
     * string. then creates a properly formatted recipe.
     *
     * @param tokens
     * @return true if the operation was successful, false otherwise
     */
    private void tokenChecker(List<String>  tokens){
        //maybe check for if any of them error.
        boolean errorFound;
        //TODO: implement
        tokens.set(0, tokens.get(0).replaceAll(":",""));
        if(tokens.get(0).equalsIgnoreCase("servings") ||
                tokens.get(0).equalsIgnoreCase("serving") ||
                tokens.get(0).equalsIgnoreCase("yields") ||
                tokens.get(0).equalsIgnoreCase("yield") ){
            servingMapper(tokens);
        }
        if(tokens.get(0).equalsIgnoreCase("prep") ||
                tokens.get(0).equalsIgnoreCase("preparation") ||
                tokens.get(0).equalsIgnoreCase("prepare")){
            prepMapper(tokens);
        }
        if(tokens.get(0).equalsIgnoreCase("total")){
            totalMapper(tokens);
        }
        if(tokens.get(0).equalsIgnoreCase("Ingredient")){
            ingredinetsMapper();
        }
        if(tokens.get(0).equalsIgnoreCase("Directions")){
            directionsMapper();
        }


    }

    /**
     * This method finds and updates the proper serving amount for the created recipe.
     *
     * @param tokens
     * @return true if the operation was successful, false otherwise
     */
    private boolean servingMapper(List<String> tokens) {
        //TODO: Test/Correct
        //TODO: think about situation "serves 6-8"
        int servings = 0;

        //checks to see if the original tokens have the number
        for (int i = 1; i < tokens.size(); i++) {
            try {
                servings = Integer.parseInt(tokens.get(i));
                recipe.setServings((double) servings);
                return true;
            } catch (NumberFormatException e) { /*do nothing*/}
        }

        //check following lines for number
        while (recipeScanner.hasNextLine()) {
            tokens = new ArrayList<>();
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());

            //if the line is not blank, send to token checker for validation.
            while (lineScanner.hasNext()) {
                try {
                    servings = Integer.parseInt(lineScanner.next());
                    recipe.setServings((double) servings);
                    return true;
                } catch (NumberFormatException e) { /*do nothing*/}
            }
        }
        return false;
    }

    /**
     * This method finds and updates the proper prep time for the created recipe.
     *
     * @param tokens
     * @return true if the operation was successful, false otherwise
     */
    private boolean prepMapper(List<String> tokens) {
        //TODO: Test/Correct
        //TODO: think about 1 h / 1 hour / 30 m / 30 minutes
        int prepTime = 0;

        //checks to see if the original tokens have the number
        for (int i = 1; i < tokens.size(); i++) {
            try {
                prepTime = Integer.parseInt(tokens.get(i));
                recipe.setPrep_time(prepTime);
                return true;
            } catch (NumberFormatException e) { /*do nothing*/}
        }

        //check following lines for number
        while (recipeScanner.hasNextLine()) {
            tokens = new ArrayList<>();
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());

            //if the line is not blank, send to token checker for validation.
            while (lineScanner.hasNext()) {
                try {
                    prepTime = Integer.parseInt(lineScanner.next());
                    recipe.setPrep_time(prepTime);
                    return true;
                } catch (NumberFormatException e) { /*do nothing*/}
            }
        }
        return false;
    }

    /**
     * This method finds and updates the proper prep time for the created recipe.
     *
     * @param tokens
     * @return true if the operation was successful, false otherwise
     */
    private boolean totalMapper(List<String> tokens) {
        //TODO: Test/Correct
        //TODO: think about 1 h / 1 hour / 30 m / 30 minutes
        int totalTime = 0;

        //checks to see if the original tokens have the number
        for (int i = 1; i < tokens.size(); i++) {
            try {
                totalTime = Integer.parseInt(tokens.get(i));
                recipe.setTotal_time(totalTime);
                return true;
            } catch (NumberFormatException e) { /*do nothing*/}
        }

        //check following lines for number
        while (recipeScanner.hasNextLine()) {
            tokens = new ArrayList<>();
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());

            //if the line is not blank, send to token checker for validation.
            while (lineScanner.hasNext()) {
                try {
                    totalTime = Integer.parseInt(lineScanner.next());
                    recipe.setTotal_time(totalTime);
                    return true;
                } catch (NumberFormatException e) { /*do nothing*/}
            }
        }
        return false;
    }

    /**
     * This method finds and builds a list of ingredients for the created recipe.
     *
     * @return RecipeIngredientList if the operation was successful, null otherwise
     */
    private ArrayList<RecipeIngredient> ingredinetsMapper(){
        //TODO: TEST/CORRECT
        //TODO: think about "1/2 cup"
        //TODO: think about split ingredients i.e. Frosting:
        ArrayList<RecipeIngredient> ingredientList = new ArrayList<>();
        RecipeIngredient ingredient = new RecipeIngredient();
        while(recipeScanner.hasNextLine()){
            List<String> tokens = new ArrayList<>();
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());
            String NameDetail = "";
            int beginAt = 2;

            //check once for title "Directions"
            tokens.add(lineScanner.next());
            if(tokens.get(0).equalsIgnoreCase("directions")){
                directionsMapper();
                return null;
            }
            while (lineScanner.hasNext()) {
                tokens.add(lineScanner.next());
            }
            //Check and update quantity if possible.
            try{
                int quantity = Integer.parseInt(tokens.get(0));
                ingredient.setQuantity(quantity);

            }catch(NumberFormatException nfe){
                ingredient.setQuantity(0);
            }
            //sets unit if possible
            ingredient.setUnit(unitChecker(tokens.get(1)));

            //if "none" is found then the next token is part of the ingredient name
            if(ingredient.getUnit().equalsIgnoreCase("none")){
                beginAt -= 1;
            }

            //since fluid ounces is a two word measurement, we must make sure the second word is not
            //added to the ingredient name.
            if(ingredient.getUnit().equalsIgnoreCase("fluid ounce(s)")){
                if(tokens.get(2).equalsIgnoreCase("ounce(s)") || tokens.get(2).equalsIgnoreCase("ounces") || tokens.get(2).equalsIgnoreCase("oz")){
                    beginAt += 1;
                }

            }
            for(int j = beginAt; j < tokens.size(); j++){
                NameDetail += tokens.get(j) + " ";
            }

            //adding either name and detail to a recipeIngredient or just a name.
            if(NameDetail.contains(",")){
                String name = NameDetail.substring( 0, NameDetail.indexOf(","));
                String detail = NameDetail.substring(NameDetail.indexOf(",")+1, NameDetail.length());
                Ingredient testIngredient = new Ingredient();
                testIngredient.setName(name);
                ingredient.setIngredientID(database.addIngredient(testIngredient));
                ingredient.setDetails(detail);
            }else{
                Ingredient testIngredient = new Ingredient();
                testIngredient.setName(NameDetail);
                ingredient.setIngredientID(database.addIngredient(testIngredient));
            }

            ingredientList.add(ingredient);
        }
        recipeIngredientList = ingredientList;
        return ingredientList;
    }

    /**
     * This method finds and builds a list of directions for the created recipe.
     *
     * @return RecipeDirectiontList if the operation was successful, null otherwise
     */
    private ArrayList<RecipeDirection> directionsMapper() {
        //TODO: TEST/CORRECT
        //TODO: think about if directions are pre-numbered
        ArrayList<RecipeDirection> directionList = new ArrayList<>();
        RecipeDirection direction = new RecipeDirection();
        int dirNum = 1;


        while(recipeScanner.hasNextLine()) {
            String directionText = "";
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());

            while (lineScanner.hasNext()) {
                directionText += lineScanner.next() + " ";
            }
            direction.setDirectionNumber(dirNum);
            direction.setDirectionText(directionText);
            directionList.add(direction);
            dirNum++;
        }

        recipeDirectionList = directionList;
        return directionList;
    }

    /**
     * This method checks to see if the specific token is of a particular volume or mass
     *
     *
     * @return unit String if the operation was successful, none otherwise
     */
    private String unitChecker(String token){
        //TODO: TEST/CORRECT

        token = token.replaceAll(".","");
        token = token.replaceAll(" ","");
        if (token.equalsIgnoreCase("pinch(es)") || token.equalsIgnoreCase("pinches") || token.equalsIgnoreCase("pinch") || token.equalsIgnoreCase("pn") || token.equalsIgnoreCase("pns")) {
            return "pinch(es)";
        }
        if (token.equalsIgnoreCase("tablespoon(s)") || token.equalsIgnoreCase("tablespoons") || token.equalsIgnoreCase("tablespoon")|| token.equalsIgnoreCase("tblsp") || token.equalsIgnoreCase("tbsp")) {
            return "tablespoon(s)";
        }
        if (token.equalsIgnoreCase("teaspoon(s)") || token.equalsIgnoreCase("teaspoons") || token.equalsIgnoreCase("teaspoon")|| token.equalsIgnoreCase("tsp") || token.equalsIgnoreCase("tspn")) {
            return "teaspoon(s)";
        }
        if (token.equalsIgnoreCase("pint(s)") || token.equalsIgnoreCase("pints") || token.equalsIgnoreCase("pint") || token.equalsIgnoreCase("pts")|| token.equalsIgnoreCase("pt")|| token.equalsIgnoreCase("pnt")) {
            return "pint(s)";
        }
        if (token.equalsIgnoreCase("quart(s)") || token.equalsIgnoreCase("quarts") || token.equalsIgnoreCase("quart") || token.equalsIgnoreCase("qt") || token.equalsIgnoreCase("qts")) {
            return "quarts(s)";
        }
        if (token.equalsIgnoreCase("gallon(s)") || token.equalsIgnoreCase("gallons") || token.equalsIgnoreCase("gallon") || token.equalsIgnoreCase("gl") || token.equalsIgnoreCase("gls") || token.equalsIgnoreCase("gals") || token.equalsIgnoreCase("gal")) {
            return "gallon(s)";
        }
        if (token.equalsIgnoreCase("fluid") || token.equalsIgnoreCase("floz") || token.equalsIgnoreCase("fl")) {
            return "fluid ounce(s)";
        }
        if (token.equalsIgnoreCase("grain(s)") || token.equalsIgnoreCase("grains") || token.equalsIgnoreCase("grain")|| token.equalsIgnoreCase("grs") || token.equalsIgnoreCase("grns")) {
            return "grain(s)";
        }
        if (token.equalsIgnoreCase("ounce(s)") || token.equalsIgnoreCase("ounces") || token.equalsIgnoreCase("ounce") || token.equalsIgnoreCase("oz")) {
            return "ounce(s)";
        }
        if (token.equalsIgnoreCase("pound(s)") || token.equalsIgnoreCase("pounds") || token.equalsIgnoreCase("pound") || token.equalsIgnoreCase("lb") || token.equalsIgnoreCase("lbs")) {
            return "pound(s)";
        }
        if (token.equalsIgnoreCase("kilogram(s)") || token.equalsIgnoreCase("kilograms") || token.equalsIgnoreCase("kilogram") || token.equalsIgnoreCase("kgs") || token.equalsIgnoreCase("kg") || token.equalsIgnoreCase("kgm") || token.equalsIgnoreCase("kgms")) {
            return "kilogram(s)";
        }
        if (token.equalsIgnoreCase("milligram(s)") || token.equalsIgnoreCase("milligrams") || token.equalsIgnoreCase("milligram") || token.equalsIgnoreCase("mg") || token.equalsIgnoreCase("mgs")) {
            return "milligram(s)";
        }
        if (token.equalsIgnoreCase("none")) {
            return "none";
        }

        return "none";
    }
}
