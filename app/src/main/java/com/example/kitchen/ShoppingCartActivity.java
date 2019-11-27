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
import android.widget.EditText;
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
public class ShoppingCartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseHelper database = new DatabaseHelper(this);
    private Recipe recipe;
    private List<ShoppingCartItem> shoppingCart; // Used for recycler
    private List<RecipeIngredient> recipeIngredients; // Current shopping cart from database
    private RecyclerView recyclerView;
    ShoppingCartAdapter shoppingCartAdapter;
    List<ShoppingCartItem> checkedItems = new ArrayList<>();
    boolean editMode = false;
    String oldUnit, newUnit;
    double originalQuantity;
    ShoppingCartItem editItem;

    private Dialog convertUnitDialog, editIngredientDialog;
    private EditText shoppingEditIngredientCartPopupQuantityEditText;
    private double editIngredientQuantity;

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
        Toolbar toolbar = findViewById(R.id.shopping_cart_toolbar);
        //TextView toolbarText = (TextView) findViewById(R.id.toolbar_text);
        setSupportActionBar(toolbar);
        //toolbarText.setText(getTitle());


        int recipeId = getIntent().getIntExtra("recipeId", -1);
        if (recipeId != -1) {
            recipe = database.getRecipe(recipeId);
            database.addRecipeToCart(recipeId);
        }
        getShoppingCart();
        getShoppingCartItems();


        recyclerView = findViewById(R.id.shopping_cart_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        shoppingCartAdapter = new ShoppingCartAdapter(shoppingCart, new ShoppingCartAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(ShoppingCartItem item) {
                checkedItems.add(item);
                if (editMode) {
                    editIngredientPopup(item);
                }

            }

            @Override
            public void onItemUncheck(ShoppingCartItem item) {
                if (editMode) {
                    editIngredientPopup(item);
                }
                checkedItems.remove(item);
            }
        });
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
                int keyID = recipeIngredients.get(i).getKeyID();
                int recipeID = recipeIngredients.get(i).getRecipeID();
                int ingredientID = recipeIngredients.get(i).getIngredientID();
                String ingredient = database.getIngredient(recipeIngredients.get(i).getIngredientID()).getName();
                String details = recipeIngredients.get(i).getDetails();
                double quantity = recipeIngredients.get(i).getQuantity();
                String unit = recipeIngredients.get(i).getUnit();
                if (unit.compareTo("none") == 0) {
                    unit = "";
                }

                database.updateShoppingCartIngredient(recipeIngredient);
                shoppingCart.add(new ShoppingCartItem(keyID, recipeID, ingredientID, ingredient, details, quantity, unit, false));

            }
        }
    }

    private void editIngredientPopup(ShoppingCartItem item) {
        editMode = false;
        editItem = item;
        editIngredientDialog = new Dialog(this);
        editIngredientDialog.setContentView(R.layout.shopping_cart_edit_ingredient_popup);

        shoppingEditIngredientCartPopupQuantityEditText = editIngredientDialog.findViewById(R.id.shopping_cart_edit_ingredient_popup_quantity_edit_text);
        editIngredientQuantity = item.getQuantity();
        shoppingEditIngredientCartPopupQuantityEditText.setText(String.valueOf(editIngredientQuantity));
        editIngredientDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editIngredientDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        editIngredientDialog.show();
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
        getMenuInflater().inflate(R.menu.shopping_cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onRemoveSelectedItemsSelected(MenuItem menuItem) {
        if (checkedItems.size() > 0) {
            for (int i = 0; i < checkedItems.size(); i++) {
                shoppingCart.remove(checkedItems.get(i));
            }
            for(int i = checkedItems.size() - 1; i >= 0; i--){
                database.deleteShoppingCartIngredient(checkedItems.get(i).getIngredientID());
                checkedItems.remove(i);
            }
            shoppingCartAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "No items selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEditCartIngredientsSelected(MenuItem item) {
        editMode = true;
        Toast.makeText(getApplicationContext(), "Select the ingredient to edit", Toast.LENGTH_SHORT).show();

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
        // Apply the adapter to the spinner
        oldUnitSpinner.setAdapter(oldUnitAdapter);
        oldUnitSpinner.setOnItemSelectedListener(this);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> newUnitAdapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        newUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
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
        if (shoppingCart != null) {
            for (int i = 0; i < shoppingCart.size(); i++) {
                if (shoppingCart.get(i).getUnit().compareTo(oldUnit) == 0) {
                    shoppingCart.get(i).setUnit(newUnit);
                    double newQuantity = database.convertUnit(oldUnit, newUnit, shoppingCart.get(i).getQuantity());
//                    Toast.makeText(this, " " + newQuantity, Toast.LENGTH_SHORT).show();
                    shoppingCart.get(i).setQuantity(newQuantity);
                }

            }
            shoppingCartAdapter.notifyDataSetChanged();
        }

//        double quanitity = database.convertUnit(oldUnit, newUnit, originalQuantity);
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

    public void onShoppingCartEditIngredientPopupRemoveButtonPressed(View v) {
        editIngredientQuantity = Double.valueOf(shoppingEditIngredientCartPopupQuantityEditText.getText().toString());
        if (editIngredientQuantity != 0)
            editIngredientQuantity--;
        shoppingEditIngredientCartPopupQuantityEditText.setText(String.valueOf(editIngredientQuantity));
    }

    public void onShoppingCartEditIngredientPopupAddButtonPressed(View v) {
        editIngredientQuantity = Double.valueOf(shoppingEditIngredientCartPopupQuantityEditText.getText().toString());
        editIngredientQuantity++;
        shoppingEditIngredientCartPopupQuantityEditText.setText(String.valueOf(editIngredientQuantity));
    }

    public void onShoppingCartEditIngredientPopupCancelButtonPressed(View v) {
        editIngredientDialog.dismiss();
    }

    public void onShoppingCartEditIngredientPopupOkayButtonPressed(View v) {
        editIngredientQuantity = Double.valueOf(shoppingEditIngredientCartPopupQuantityEditText.getText().toString());
        editItem.setQuantity(editIngredientQuantity);
        shoppingCartAdapter.notifyDataSetChanged();
        editIngredientDialog.dismiss();
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


}
