package com.example.kitchen;
import android.content.Context;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class RecipeCopyPasteCreator {
    private Scanner recipeScanner;
    private ArrayList<RecipeIngredient> recipeIngredientList;
    private ArrayList<RecipeDirection> recipeDirectionList;
    private ArrayList<RecipeCategory> recipeCategoryList;
    private Recipe recipe;
    Context appContext;
    DatabaseHelper database;

    /**
     * This is the constructor for this class.
     *
     * @param context The application context. This is needed for database access.
     */
    public RecipeCopyPasteCreator(Context context) {
        recipe = new Recipe();
        appContext = context;
        database = new DatabaseHelper(appContext);
    }

    /**
     * This method creates takes a string made from a found recipe and converts and normalizes the
     * string. then creates a properly formatted recipe.
     *
     * @param recipeTxt
     * @return true if the operation was successful, false otherwise
     */
    public int main(String recipeTxt) {
        recipeScanner = new Scanner(recipeTxt);
        List<String> tokens;
        String recipeTitle = "";
        boolean passed = false;
        boolean titleFound = false;
        recipe = new Recipe();
        recipeIngredientList = new ArrayList<RecipeIngredient>();
        recipeDirectionList = new ArrayList<RecipeDirection>();
        recipeCategoryList = new ArrayList<RecipeCategory>();

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
            if(tokens.size() > 0) {
                //should return a double that explains the situation int being method and dec bing part
               passed = tokenChecker(tokens);
            }

            lineScanner.close();
        }
        recipeScanner.close();

        //make sure ingredient and directions actually have something in them.
        if(recipeIngredientList.size() > 0 && recipeDirectionList.size() > 0){
            recipeTitle = capitalize(recipeTitle);
            recipe.setTitle(recipeTitle);
            recipe.setIngredientList(recipeIngredientList);
            recipe.setDirectionsList(recipeDirectionList);
            if(recipeCategoryList.size() > 0){
                recipe.setCategoryList(recipeCategoryList);
            }
            return database.addRecipe(recipe);
        }
            return -1;

    }

    /**
     * This method creates takes a string made from a found recipe and converts and normalizes the
     * string. then creates a properly formatted recipe.
     *
     * @param tokens
     * @return true if the operation was successful, false otherwise
     */
    private boolean tokenChecker(List<String>  tokens){
        //maybe check for if any of them error.
        boolean passed = false;
        if(tokens.size() == 0){
            return false;
        }
        tokens.set(0, tokens.get(0).replaceAll(":",""));
        if(tokens.get(0).equalsIgnoreCase("servings") ||
                tokens.get(0).equalsIgnoreCase("serving") ||
                tokens.get(0).equalsIgnoreCase("yields") ||
                tokens.get(0).equalsIgnoreCase("yield")){
            passed = servingMapper(tokens);
        }
        if(!passed && (tokens.get(0).equalsIgnoreCase("Course")||
                tokens.get(0).equalsIgnoreCase("Courses"))){
            passed = categoriesMapper(tokens);
        }
        if(!passed && (tokens.get(0).equalsIgnoreCase("Cuisine"))){
            passed = categoriesMapper(tokens);
        }
        if(!passed && (tokens.get(0).equalsIgnoreCase("prep") ||
                tokens.get(0).equalsIgnoreCase("preparation") ||
                tokens.get(0).equalsIgnoreCase("prepare"))){
            passed = prepMapper(tokens);
        }
        if(!passed && (tokens.get(0).equalsIgnoreCase("total") ||
                tokens.get(0).equalsIgnoreCase("Ready"))){
            passed = totalMapper(tokens);
        }
        if(!passed && (tokens.get(0).equalsIgnoreCase("Ingredient") ||
                tokens.get(0).equalsIgnoreCase("Ingredients"))){
            passed = ingredientsMapper();
        }
        if(!passed && (tokens.get(0).equalsIgnoreCase("Directions")||
                tokens.get(0).equalsIgnoreCase("Instructions"))){
            passed = directionsMapper();
        }

        return passed;
    }

    /**
     * This method finds and updates the proper serving amount for the created recipe.
     *
     * @param tokens
     * @return true if the operation was successful, false otherwise
     */
    private boolean servingMapper(List<String> tokens) {
        int servings = 0;

        //checks to see if the original tokens have the number
        if(tokens.size() > 1) {
            servings = numberChecker("servings", tokens).intValue();
            if (servings != -1.0) {
                recipe.setServings(servings + 0.0);
                return true;
            }
        }
        //check following lines for number
        while (recipeScanner.hasNextLine()) {
            tokens = new ArrayList<>();
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());

            //if the line is not blank, send to token checker for validation.
            while (lineScanner.hasNext()) {
                tokens.add(lineScanner.next());
            }
            //check to see if no number found and next instance begins
            if(tokenChecker(tokens)){
                return false;
            }
            //check to see if no number found
            servings = numberChecker("servings", tokens).intValue();
            if(servings != -1.0) {
                recipe.setServings(servings + 0.0);
                return true;
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
        int prepTime = 0;

        //checks to see if the original tokens have the number
        if(tokens.size() > 1) {
            prepTime = numberChecker("prep", tokens).intValue();
            if (prepTime != -1.0) {
                recipe.setPrep_time(prepTime);
                return true;
            }
        }

        //check following lines for number
        while (recipeScanner.hasNextLine()) {
            tokens = new ArrayList<>();
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());

            //if the line is not blank, send to token checker for validation.
            while (lineScanner.hasNext()) {
                tokens.add(lineScanner.next());
            }
            //check to see if no number found and next instance begins
            if(tokenChecker(tokens)){
                return false;
            }
            //check to see if no number found
            prepTime = numberChecker("prep", tokens).intValue();
            if(prepTime != -1.0) {
                recipe.setPrep_time(prepTime);
                return true;
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
        int totalTime = 0;

        //checks to see if the original tokens have the number
        if(tokens.size() > 1) {
            totalTime = numberChecker("total", tokens).intValue();
            if (totalTime != -1.0) {
                recipe.setTotal_time(totalTime);
                return true;
            }
        }

        //check following lines for number
        while (recipeScanner.hasNextLine()) {
            tokens = new ArrayList<>();
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());

            //if the line is not blank, send to token checker for validation.
            while (lineScanner.hasNext()) {
                tokens.add(lineScanner.next());
            }
            //check to see if no number found and next instance begins
            if(tokenChecker(tokens)){
                return false;
            }
            //check to see if no number found
            totalTime = numberChecker("total", tokens).intValue();
            if(totalTime != -1.0) {
                recipe.setTotal_time(totalTime);
                return true;
            }
        }
        return false;
    }

    /**
     * This method finds and builds a list of categories for the created recipe.
     *
     * @return RecipeCategory if the operation was successful, null otherwise
     */
    private boolean categoriesMapper(List<String> tokens) {
        ArrayList<RecipeCategory> categoryList = recipeCategoryList;
        Category temp = new Category();
        RecipeCategory category;
        boolean passed = false;
        String temptxt;
        category = new RecipeCategory();
        String categoryText = "";

        for(int i = 1; i < tokens.size(); i++) {
            temptxt = tokens.get(i);
            if(temptxt.equalsIgnoreCase("cuisine") || temptxt.equalsIgnoreCase("course")){
                //do nothing
            }else{
                categoryText += temptxt + " ";
            }
        }

        String[] categories = categoryText.split( "," );
        for(String cat : categories) {
            temp.setName(cat.trim());
            category.setCategoryID(database.addCategory(temp));
            categoryList.add(category);
        }

        if(categoryList.size() > 0){
            passed = true;
            recipeCategoryList = categoryList;
        }else{
            passed = false;
        }
        return passed;
    }

    /**
     * This method finds and builds a list of ingredients for the created recipe.
     *
     * @return RecipeIngredientList if the operation was successful, null otherwise
     */
    private boolean ingredientsMapper(){
        ArrayList<RecipeIngredient> ingredientList = new ArrayList<>();
        RecipeIngredient ingredient;
        boolean passed = false;

        while(recipeScanner.hasNextLine()){
            List<String> tokens = new ArrayList<>();
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());
            ingredient = new RecipeIngredient();
            String NameDetail = "";
            int beginAt = 2;

            //check for empty lines
            while(!lineScanner.hasNext()){
                lineScanner.close();
                lineScanner = new Scanner(recipeScanner.nextLine());
            }
            //check once for title "Directions"
            tokens.add(lineScanner.next());
            if(tokens.get(0).equalsIgnoreCase("directions")||
                    tokens.get(0).equalsIgnoreCase("Instructions")){
                recipeIngredientList = ingredientList;
                return (directionsMapper() && passed);
            }
            while (lineScanner.hasNext()) {
                tokens.add(lineScanner.next());
            }
            //check to see if no number found and next instance begins
            if(tokenChecker(tokens)){
                recipeIngredientList = ingredientList;
                return false;
            }
            //Check to see if quantity is found
            double quantity = numberChecker("Ingredient", tokens);
            if(quantity != -1.0) {
                ingredient.setQuantity(quantity);
            }

            //sets unit if possible
            if(tokens.size() > 1) {
                ingredient.setUnit(unitChecker(tokens.get(1)));
            }else{
                ingredient.setUnit("none");
            }

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
            if(tokens.size() == 1){
                NameDetail = tokens.get(0).toString();
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
                ingredient.setDetails("");
            }

            ingredientList.add(ingredient);
            passed = true;
        }
        recipeIngredientList = ingredientList;
        return passed;
    }

    /**
     * This method finds and builds a list of directions for the created recipe.
     *
     * @return RecipeDirectiontList if the operation was successful, null otherwise
     */
    private boolean directionsMapper() {
        ArrayList<RecipeDirection> directionList = new ArrayList<>();
        RecipeDirection direction = new RecipeDirection();
        boolean passed = false;
        int dirNum = 1;

        while(recipeScanner.hasNextLine()) {
            //new line!!!
            direction = new RecipeDirection();
            //new line!!
            String directionText = "";
            Scanner lineScanner = new Scanner(recipeScanner.nextLine());

            //check for empty lines
            while(!lineScanner.hasNext()){
                lineScanner.close();
                lineScanner = new Scanner(recipeScanner.nextLine());
            }

            while (lineScanner.hasNext()) {
                directionText += lineScanner.next() + " ";
            }
            direction.setDirectionNumber(dirNum);
            direction.setDirectionText(directionText);
            directionList.add(direction);
            dirNum++;
            passed = true;
        }

        recipeDirectionList = directionList;
        return passed;
    }

    /**
     * This method checks the numbers for the given string list
     *
     *
     * @return a number or -1 for the given string
     */
    private Double numberChecker(String type, List<String> tokens){
        Double number = 0.0;
        Double tempNum = 0.0;
        boolean found = false;

        //This if statement is used for servings specified syntax
        if(type.equalsIgnoreCase("servings")){
            for(int i = 0; i < tokens.size(); i++) {
                try {//if just a number
                    if(!tokens.get(i).contains("-")){
                        tempNum = Double.parseDouble(String.valueOf(tokens.get(i)));
                        found = true;
                    }
                    else{//if 6-8
                        char[] letters = tokens.get(i).toCharArray();
                        if(letters.length == 3) {
                            found = true;
                            tempNum = Double.parseDouble(String.valueOf(letters[1]));
                            tempNum = (tempNum + Double.parseDouble(String.valueOf(letters[3])))/2;
                        }
                    }
                } catch (NumberFormatException e) { /*do nothing*/}
                if(found){
                    number = tempNum;
                    break; }
            }

        }

        //This if statement is used for prep or total specified syntax
        if(type.equalsIgnoreCase("prep") || type.equalsIgnoreCase("total")) {
            for(int i = 0; i < tokens.size(); i++) {
                try {
                    tempNum += Double.parseDouble(tokens.get(i));
                    if(tokens.get(i+1).equalsIgnoreCase("h") ||
                            tokens.get(i+1).equalsIgnoreCase("hr") ||
                            tokens.get(i+1).equalsIgnoreCase("hrs") ||
                            tokens.get(i+1).equalsIgnoreCase("hour") ||
                            tokens.get(i+1).equalsIgnoreCase("hours")){
                        number += tempNum * 60;
                        found = true;
                    } else{
                        if(tokens.get(i+1).equalsIgnoreCase("m") ||
                                tokens.get(i+1).equalsIgnoreCase("min") ||
                                tokens.get(i+1).equalsIgnoreCase("mins") ||
                                tokens.get(i+1).equalsIgnoreCase("minute") ||
                                tokens.get(i+1).equalsIgnoreCase("minutes")) {
                            number += tempNum;
                            found = true;
                        }
                    }
                } catch (NumberFormatException e) { /*do nothing*/}
                if(found){ break; }
            }
        }

        //This if statement is used for Ingredient specified syntax
        if(type.equalsIgnoreCase("ingredient")){
            try{
                number = Double.parseDouble(tokens.get(0));
                found = true;
            }catch (NumberFormatException nfe){
                String temp = tokens.get(0);
                String[] arr = temp.split("");
                //arr.length == 2 && arr[1].equalsIgnoreCase("/")
                temp.trim();
                try {
                    if (temp.contains("/") || temp.contains("\\")) {
                        if(arr[0].equalsIgnoreCase("")){
                            arr[0] = arr[1];
                            arr[2] = arr[3];
                        }
                        number = Double.parseDouble(arr[0]) / Double.parseDouble(arr[2]);
                        found = true;
                    }
                    if (temp.contains("½")){
                        number = 0.5;
                        found = true;
                    }
                    if (temp.contains("⅓")){
                        number = (double) (1/3);
                        found = true;
                    }
                    if (temp.contains("⅔")){
                        number = (double) (2/3);
                        found = true;
                    }
                    if (temp.contains("¼")){
                        number = (double) (1/4);
                        found = true;
                    }
                }catch(NumberFormatException nfe2){/*do nothing*/}
            }
            //checks for 1/2 values
            try {if(tokens.size() > 1) {
                String temp = tokens.get(1);
                String[] arr = temp.split("");
                if ((temp.contains("/") || temp.contains("\\")) && arr != null) {
                    number += Double.parseDouble(arr[0]) / Double.parseDouble(arr[2]);
                    found = true;
                }
                if (temp.contains("½")){
                    number = 0.5;
                    found = true;
                }
                if (temp.contains("⅓")){
                    number = (double) (1/3);
                    found = true;
                }
                if (temp.contains("⅔")){
                    number = (double) (2/3);
                    found = true;
                }
                if (temp.contains("¼")){
                    number = (double) (1/4);
                    found = true;
                }
            }
            }catch(NumberFormatException nfe3){/*do nothing*/}
        }

        //want to cycle till found.
        if(found){
            return number;
        }
        else{
            return -1.0;
        }
    }

    /**
     * This method checks to see if the specific token is of a particular volume or mass
     *
     *
     * @return unit String if the operation was successful, none otherwise
     */
    private String unitChecker(String token){
        //TODO: TEST/CORRECT
        token = token.replaceAll("/\\./g","");
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
        if (token.equalsIgnoreCase("cups(s)") || token.equalsIgnoreCase("cups") || token.equalsIgnoreCase("cup") || token.equalsIgnoreCase("cp") || token.equalsIgnoreCase("cps")) {
            return "cup(s)";
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
        if (token.equalsIgnoreCase("gram(s)") || token.equalsIgnoreCase("grams") || token.equalsIgnoreCase("gram") || token.equalsIgnoreCase("g") || token.equalsIgnoreCase("gs")) {
            return "gram(s)";
        }
        if (token.equalsIgnoreCase("milligram(s)") || token.equalsIgnoreCase("milligrams") || token.equalsIgnoreCase("milligram") || token.equalsIgnoreCase("mg") || token.equalsIgnoreCase("mgs")) {
            return "milligram(s)";
        }
        if (token.equalsIgnoreCase("none")) {
            return "none";
        }

        return "none";
    }

    public static String capitalize(String temp){
        String wordArr[]=temp.split("\\s");
        String capWords="";
        for(String word:wordArr){
            String first=word.substring(0,1);
            String afterfirst=word.substring(1);
            capWords+=first.toUpperCase()+afterfirst+" ";
        }
        return capWords.trim();
    }
}
