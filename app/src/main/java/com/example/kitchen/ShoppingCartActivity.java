package com.example.kitchen;

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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * This code controls the Shopping Cart Activity
 */
public class ShoppingCartActivity extends AppCompatActivity {
    private DatabaseHelper database = new DatabaseHelper(this);
    private Recipe recipe;
    private List<ShoppingCartItem> shoppingCart; // Used for recycler
    private List<RecipeIngredient> recipeIngredients; // Current shopping cart from database
    private RecyclerView recyclerView;

    /**
     * This method is run when the activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.recipe_toolbar);
        //TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        setSupportActionBar(toolbar);
        //toolbarText.setText(getTitle());


        int recipeId = getIntent().getIntExtra("recipeId", -1);
        recipe = database.getRecipe(recipeId);
        database.addRecipeToCart(recipeId);
        getShoppingCart();
        getShoppingCartItems();


        recyclerView = findViewById(R.id.shopping_cart_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ShoppingCartAdapter shoppingCartAdapter = new ShoppingCartAdapter(shoppingCart, null);
        recyclerView.setAdapter(shoppingCartAdapter);
    }

    /**
     * This method takes the List of RecipeIngredients in the shopping cart
     * and maps them to a list of ShoppingCartItems that are used for the
     * recycler
     */
    private void getShoppingCartItems() {
        shoppingCart = new ArrayList<>();
        if (recipeIngredients != null) {
            for (int i = 0; i < recipeIngredients.size(); i++) {
                RecipeIngredient recipeIngredient = recipeIngredients.get(i);
                for(int j = 0; j<recipeIngredients.size(); j++) {
                    if(i != j) {
                        int keyID = recipeIngredients.get(i).getKeyID();
                        int recipeID = recipeIngredients.get(i).getRecipeID();
                        int ingredientID = recipeIngredients.get(i).getIngredientID();
                        String ingredient = database.getIngredient(recipeIngredients.get(i).getIngredientID()).getName();
                        String details = recipeIngredients.get(i).getDetails();
                        double quantity = recipeIngredients.get(i).getQuantity();
                        String unit = recipeIngredients.get(i).getUnit();
                        if(recipeIngredient.getKeyID() == keyID)
                        {
                            if(recipeIngredient.getUnit().compareTo(unit) == 0){
                                recipeIngredient.setQuantity(recipeIngredient.getQuantity() + quantity);
                            }
                            database.updateShoppingCartIngredient(recipeIngredient);
                        }
                        shoppingCart.add(new ShoppingCartItem(keyID, recipeID, ingredientID, ingredient, details, quantity, unit));
                    }
                }
            }
        }
    }

    /**
     *
     */
    private void getShoppingCart() {
        recipeIngredients = database.getAllShoppingCartIngredients();
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
                return true;
            case R.id.action_home:
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                return true;
            case R.id.action_share_recipe:
                Intent shareRecipe = new Intent(this, ShareRecipeActivity.class);
                shareRecipe.putExtra("recipeId", recipe.getKeyID());
                startActivity(shareRecipe);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onDeleteRecipeSelected(MenuItem item){

    }

    /**
     * Close the popup and do not delete recipe.
     * @param v
     */
    public void onDeleteRecipePopupCancelButtonPressed(View v){

    }

    /**
     * Close the popup and delete the recipe.
     * @param v
     */
    public void onDeleteRecipePopupConfirmationButtonPressed(View v) {}
    public void onDisplaySelectedRecipeFavoriteButtonPressed(View v) {}

    /**
     * When the "Convert Units" menu option is selected, a popup window is
     * displayed.This popup contains two spinners prompting the user to select
     * the unit to be converted and its newly converted unit.
     * @param item
     */
    public void onConvertUnitSelected(MenuItem item) {}


    public void onUnitConversionPopupOkayButtonPressed(View v) {}
    public void onUnitConversionPopupCancelButtonPressed(View v){

    }

    /**
     * When the "Scale Recipe" menu item is selected, a popup window
     * for scaling the recipe appears.
     * @param item
     */
    public void onScaleRecipeSelected(MenuItem item){}

    /**
     * Subtract the servings total by 1 iff it is greater than 0
     * and change the scaleRecipePopupEditText accordingly
     * @param v
     */
    public void onScaleRecipePopupRemoveButtonPressed(View v){
    }
    /**
     * Add to the servings total by 1 and change the
     * scaleRecipePopupEditText accordingly
     * @param v
     */
    public void onScaleRecipePopupAddButtonPressed(View v){
    }

    public void onScaleRecipePopupCancelButtonPressed(View v){

    }
    public void onScaleRecipePopupOkayButtonPressed(View v){

    }
    public void onAddToCartSelected(MenuItem item){}


}
