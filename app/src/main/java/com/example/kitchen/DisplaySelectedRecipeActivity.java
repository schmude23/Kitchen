package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
//TODO: Dealing with long recipe titles
// TODO: back button pressed go to recipe list

public class DisplaySelectedRecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseHelper dbHandler = new DatabaseHelper(DisplaySelectedRecipeActivity.this);
    private TextView recipe_title;
    private TextView servings;
    private TextView prep_time;
    private TextView total_time;
    private ImageView image;
    private ImageButton favoriteIcon;
    Recipe recipe;

    // Ingredient RecyclerView variables
    private ArrayList<String> ingredientList = new ArrayList<String>();
   RecyclerView ingredientRecyclerView;
   DisplayRecipeAdapter iAdapter;

    // Direction RecyclerView variables
    ArrayList<String> directionList = new ArrayList<String>();
    DisplayRecipeAdapter dAdapter;
    RecyclerView directionRecyclerView;


    //Popup window dialogs
    Dialog scaleRecipeDialog, deleteRecipeDialog, convertUnitDialog;
    private String oldUnit, newUnit;

    private EditText scaleRecipePopupServingsEditText;
    private double scaleRecipeServings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_selected_recipe);
        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.recipe_toolbar);
        //TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        setSupportActionBar(toolbar);
        //toolbarText.setText(getTitle());


        //retrieving the extra infromation from intent
        int recipeId = getIntent().getIntExtra("recipeId", -1);
        recipe = dbHandler.getRecipe(recipeId);

        recipe_title = (TextView) findViewById(R.id.text_recipe_name);
        servings = (TextView) findViewById(R.id.recipe_servings);
        prep_time = (TextView) findViewById(R.id.recipe_prep_time);
        total_time = (TextView) findViewById(R.id.recipe_total_time);
        image = findViewById(R.id.recipe_image);
        favoriteIcon = findViewById(R.id.recipe_favorite_image);

        recipe_title.setText(recipe.getTitle());
        servings.setText(String.valueOf(recipe.getServings()));
        String text = recipe.getPrep_time() + " min";
        prep_time.setText(text);
        text = recipe.getTotal_time() + " min";
        total_time.setText(text);
        image.setImageBitmap(recipe.getImage(this));
        getIngredients();
        getDirections();
        if (recipe.getFavorited()) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite);

        }

    }

   /**
     *
     */
    private void getIngredients() {
        // setup Ingredient List
        ingredientRecyclerView = findViewById(R.id.ingredient_list);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve ingredients and add to ListView
        for (int i = 0; i < recipe.getIngredientList().size(); i++) {
            int ingredientID = recipe.getIngredientList().get(i).getIngredientID();
            Ingredient ingredient = dbHandler.getIngredient(ingredientID);
            String name = ingredient.getName();
            String quantity = String.valueOf(recipe.getIngredientList().get(i).getQuantity());
            String unit = recipe.getIngredientList().get(i).getUnit();
            String details = recipe.getIngredientList().get(i).getDetails();
            // Add to ListView and update height
            if (unit.compareTo("none") == 0) {
                if (details.compareTo("") != 0)
                    ingredientList.add(name + " (" + details + ") " + " [ " + quantity + " ]");
                else
                    ingredientList.add(name + " [ " + quantity + " ]");
            } else {
                if (details.compareTo("") != 0)
                    ingredientList.add(name + " (" + details + ") " + " [ " + quantity + " " + unit + " ]");
                else
                    ingredientList.add(name + " [ " + quantity + " " + unit + " ]");
            }
            iAdapter = new DisplayRecipeAdapter(ingredientList);
            ingredientRecyclerView.setAdapter(iAdapter);
        }
    }

    /**
     *
     */
    private void getDirections() {
        directionRecyclerView = findViewById(R.id.direction_list);
        directionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < recipe.getDirectionsList().size(); i++) {
            String text = recipe.getDirectionsList().get(i).getDirectionText();
            int number = recipe.getDirectionsList().get(i).getDirectionNumber();
            // fix incorrect numbers from removed directions
            if (number != (i + 1)) {
                number = i + 1;
                recipe.getDirectionsList().get(i).setDirectionNumber(number);
            }
            // Add to ListView and update height
            directionList.add(number + ") " + text);
            dAdapter = new DisplayRecipeAdapter(directionList);
            directionRecyclerView.setAdapter(dAdapter);
        }
    }


    // Toolbar functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_edit_recipe:
                int recipeId = recipe.getKeyID();
                Intent editRecipe = new Intent(this, EditRecipeActivity.class);
                editRecipe.putExtra("recipeId", recipeId);
                editRecipe.putExtra("newRecipe", false);
                startActivity(editRecipe);
                this.finish();
                return true;
            case R.id.action_home:
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                this.finish();
                return true;
            case R.id.action_share_recipe:
                Intent shareRecipe = new Intent(this, ShareRecipeActivity.class);
                shareRecipe.putExtra("recipeId", recipe.getKeyID());
                startActivity(shareRecipe);
                this.finish();
                return true;
            case R.id.action_view_cart:
                Intent viewCart = new Intent(this, ShoppingCartActivity.class);
                viewCart.putExtra("recipeId", -1);
                startActivity(viewCart);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When the user selects the delete recipe menu item,
     * they will be prompted with a popup to confirm deleting
     * the recipe. This is to prevent accidental deletions.
     *
     * @param item
     */
    public void onDeleteRecipeSelected(MenuItem item) {
        deleteRecipeDialog = new Dialog(this);
        deleteRecipeDialog.setContentView(R.layout.delete_recipe_popup);
        deleteRecipeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //deleteRecipeDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        deleteRecipeDialog.show();
    }

    /**
     * Close the popup and do not delete recipe.
     *
     * @param v
     */
    public void onDeleteRecipePopupCancelButtonPressed(View v) {
        deleteRecipeDialog.dismiss();
    }

    /**
     * Close the popup and delete the recipe.
     *
     * @param v
     */
    public void onDeleteRecipePopupConfirmationButtonPressed(View v) {
        deleteRecipeDialog.dismiss();
        dbHandler.deleteRecipe(recipe.getKeyID());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void onDisplaySelectedRecipeFavoriteButtonPressed(View v) {
        boolean favorited = recipe.getFavorited();
        if (favorited) {
            recipe.setFavorited(false);
            dbHandler.editRecipe(recipe);
            favoriteIcon.setImageResource(R.drawable.ic_favorite_outline);
        } else {
            recipe.setFavorited(true);
            dbHandler.editRecipe(recipe);
            favoriteIcon.setImageResource(R.drawable.ic_favorite);
        }
    }

    /**
     * When the "Convert Units" menu option is selected, a popup window is
     * displayed.This popup contains two spinners prompting the user to select
     * the unit to be converted and its newly converted unit.
     *
     * @param item
     */
    public void onConvertUnitSelected(MenuItem item) {
        convertUnitDialog = new Dialog(this);
        convertUnitDialog.setContentView(R.layout.unit_conversion_popup);
        Spinner oldUnitSpinner, newUnitSpinner;
        oldUnitSpinner = convertUnitDialog.findViewById(R.id.unit_conversion_popup_old_unit_spinner);
        newUnitSpinner = convertUnitDialog.findViewById(R.id.unit_conversion_popup_new_unit_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> oldUnitAdapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        oldUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the dAdapter to the spinner
        oldUnitSpinner.setAdapter(oldUnitAdapter);
        oldUnitSpinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> newUnitAdapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        newUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the dAdapter to the spinner
        newUnitSpinner.setAdapter(newUnitAdapter);
        newUnitSpinner.setOnItemSelectedListener(this);

        convertUnitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        convertUnitDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        convertUnitDialog.show();
    }


    public void onUnitConversionPopupOkayButtonPressed(View v) {

        if(oldUnit.compareTo("none") == 0 || newUnit.compareTo("none") == 0|| oldUnit.compareTo(newUnit) == 0) {
            Toast.makeText(this, "Cannot convert units", Toast.LENGTH_SHORT).show();
            convertUnitDialog.dismiss();
        }
        recipe = dbHandler.convertRecipeIngredientUnits(recipe, oldUnit, newUnit);
        ingredientList = new ArrayList<>();
        getIngredients();
        Context context = getApplicationContext();

        CharSequence text = oldUnit + " converted to " + newUnit;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        convertUnitDialog.dismiss();
    }

    public void onUnitConversionPopupCancelButtonPressed(View v) {
        convertUnitDialog.dismiss();
    }

    /**
     * When the "Scale Recipe" menu item is selected, a popup window
     * for scaling the recipe appears.
     *
     * @param item
     */
    public void onScaleRecipeSelected(MenuItem item) {
        scaleRecipeDialog = new Dialog(this);
        scaleRecipeDialog.setContentView(R.layout.scale_recipe_popup);

        scaleRecipePopupServingsEditText = scaleRecipeDialog.findViewById(R.id.scale_recipe_popup_servings_edit_text);
        scaleRecipeServings = recipe.getServings();
        scaleRecipePopupServingsEditText.setText(String.valueOf(scaleRecipeServings));
        scaleRecipeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        scaleRecipeDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        scaleRecipeDialog.show();
    }

    /**
     * Subtract the servings total by 1 iff it is greater than 0
     * and change the scaleRecipePopupEditText accordingly
     *
     * @param v
     */
    public void onScaleRecipePopupRemoveButtonPressed(View v) {
        scaleRecipeServings = Double.valueOf(scaleRecipePopupServingsEditText.getText().toString());
        if (scaleRecipeServings > 1)
            scaleRecipeServings--;
        scaleRecipePopupServingsEditText.setText(String.valueOf(scaleRecipeServings));
    }

    /**
     * Add to the servings total by 1 and change the
     * scaleRecipePopupEditText accordingly
     *
     * @param v
     */
    public void onScaleRecipePopupAddButtonPressed(View v) {
        scaleRecipeServings = Double.valueOf(scaleRecipePopupServingsEditText.getText().toString());
        scaleRecipeServings++;
        scaleRecipePopupServingsEditText.setText(String.valueOf(scaleRecipeServings));
    }

    public void onScaleRecipePopupCancelButtonPressed(View v) {
        scaleRecipeDialog.dismiss();
    }

    public void onScaleRecipePopupOkayButtonPressed(View v) {
        scaleRecipeServings = Double.valueOf(scaleRecipePopupServingsEditText.getText().toString());
        dbHandler.scaleRecipe(recipe, scaleRecipeServings);
        servings.setText(String.valueOf(recipe.getServings()));
        ingredientList = new ArrayList<>();
        getIngredients();
        scaleRecipeDialog.dismiss();
    }

    // Spinner Methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // A unit was selected, assign to String
        switch (parent.getId()) {
            case R.id.unit_conversion_popup_old_unit_spinner:
                oldUnit = parent.getItemAtPosition(position).toString();
                break;
            case R.id.unit_conversion_popup_new_unit_spinner:
                newUnit = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onAddToCartSelected(MenuItem item) {
        Intent shoppingCartIntent = new Intent(this, ShoppingCartActivity.class);
        shoppingCartIntent.putExtra("recipeId", recipe.getKeyID());
        startActivity(shoppingCartIntent);
        this.finish();
    }


}
