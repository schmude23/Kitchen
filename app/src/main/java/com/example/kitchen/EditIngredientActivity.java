package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


import static java.lang.String.valueOf;

public class EditIngredientActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseHelper database = new DatabaseHelper(this);
    Recipe recipe;
    private FloatingActionButton finishButton;
    private EditText editIngredientIngredientEditText,
            editIngredientPopupIngredientEditText,
            editIngredientPopupIngredientDetailsEditText,
            editIngredientPopupQuantityEditText;

    // ListView variables
    private ListView ingredientListView;
    private ArrayList<String> ingredientList = new ArrayList<String>();
    private ArrayAdapter<String> ingredientAdapter;
    private boolean newRecipe;
    private int position = -1;
    Dialog myDialog; // Dialog for popup window

    String ingredientName, ingredientDetails, ingredientQuantity, ingredientUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);

        int recipeID = getIntent().getIntExtra("recipeId", -1);
        newRecipe = getIntent().getBooleanExtra("newRecipe", true);
        recipe = database.getRecipe(recipeID);

        myDialog = new Dialog(this);

        finishButton = findViewById(R.id.edit_ingredient_finish_button);

        editIngredientIngredientEditText = (EditText) findViewById(R.id.edit_ingredient_ingredient_edit_text);
        ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);

        // set the ingredientListView variable to your ingredientList in the xml
        ingredientListView = (ListView) findViewById(R.id.edit_ingredient_ingredient_list);
        ingredientListView.setAdapter(ingredientAdapter);
        if (newRecipe)
            addRecipe();
        else
            editRecipe();
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPopup(view, position);
            }
        });


    }

    /**
     *
     */
    private void addRecipe() {
        recipe.setIngredientList(new ArrayList<RecipeIngredient>());
        recipe.setDirectionsList(new ArrayList<RecipeDirection>());
        recipe.setCategoryList(new ArrayList<RecipeCategory>());
    }

    /**
     *
     */
    private void editRecipe() {
        finishButton.setImageResource(R.drawable.ic_check_mark);
        for (int i = 0; i < recipe.getIngredientList().size(); i++) {
            String quantity = String.valueOf(recipe.getIngredientList().get(i).getQuantity());
            String unit = recipe.getIngredientList().get(i).getUnit();
            int id = recipe.getIngredientList().get(i).getIngredientID();
            Ingredient ingredient = database.getIngredient(id);
            String name = ingredient.getName();
            if (unit.compareTo("none") == 0)
                ingredientAdapter.add(name + " [ " + quantity + " ]");
            else
                ingredientAdapter.add(name + " [ " + quantity + " " + unit + " ]");

        }
    }

    /**
     * @param v
     */
    public void onEditIngredientAddIngredientButtonPressed(View v) {
        // Show popup if valid ingredient to select quantity and units
        String input = editIngredientIngredientEditText.getText().toString();
        if (input.length() > 0) {
            ShowPopup(v, -1);
        }
    }

    /**
     * @param v
     */
    public void onEditIngredientNextButtonPressed(View v) {
        if (recipe.getIngredientList().size() > 0) {
            // Next Activity
            database.editRecipe(recipe);
            Intent intent = new Intent(this, EditDirectionActivity.class);
            intent.putExtra("recipeId", recipe.getKeyID());
            intent.putExtra("newRecipe", newRecipe);
            startActivity(intent);
        }
    }

    /**
     * @param v
     */
    public void onEditIngredientFinishButtonPressed(View v) {
        if (newRecipe) {
            database.deleteRecipe(recipe.getKeyID());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            if (database.editRecipe(recipe)) {
                Context context = getApplicationContext();

                CharSequence text = "Recipe Updated!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);
            intent.putExtra("recipeId", recipe.getKeyID());
            startActivity(intent);
        }
    }

    /**
     *
     * @param v
     */
    public void onEditIngredientPopupOkayButtonPressed(View v) {
        ingredientName = editIngredientPopupIngredientEditText.getText().toString();
        ingredientDetails = editIngredientPopupIngredientDetailsEditText.getText().toString();
        ingredientQuantity = editIngredientPopupQuantityEditText.getText().toString();
        if (ingredientName.length() > 0 && ingredientQuantity.length() > 0) {

            if (position != -1) {
                // edit (position) recipe ingredient
                editIngredient(position);
            } else {
                newIngredient();
            }
            myDialog.dismiss();
        }
    }
    public void onEditIngredientPopupRemoveButtonPressed(View v){
        database.deleteIngredient(recipe.getIngredientList().get(position).getIngredientID());
        recipe.getIngredientList().remove(position);
        ingredientList.remove(position);
        ingredientAdapter.notifyDataSetChanged();
        myDialog.dismiss();
    }

    /**
     * Popup to add/edit ingredient. Prompts for quantity and units of ingredient. Then adds to ListView
     * and creates a new RecipeIngredient with the given inputs
     *
     * @param v
     * @param position position of ingredient to be modified, -1 if new ingredient
     */
    public void ShowPopup(View v, final int position) {
        Button popupRemoveButton;
        this.position = position;
        myDialog.setContentView(R.layout.edit_ingredient_popup);
        editIngredientPopupIngredientEditText = myDialog.findViewById(R.id.edit_ingredient_popup_ingredient_edit_text);
        editIngredientPopupIngredientDetailsEditText = myDialog.findViewById(R.id.edit_ingredient_popup_ingredient_details_edit_text);
        editIngredientPopupQuantityEditText = myDialog.findViewById(R.id.edit_ingredient_popup_ingredient_quantity_edit_text);

        if (position != -1) {
            // update ingredient --> get current ingredient ingredient
            Ingredient i = database.getIngredient(recipe.getIngredientList().get(position).getIngredientID());
            editIngredientPopupIngredientEditText.setText(i.getName());
            String details = recipe.getIngredientList().get(position).getDetails();
            if(details != null)
                editIngredientPopupIngredientDetailsEditText.setText(details);
            editIngredientPopupQuantityEditText.setText(valueOf(recipe.getIngredientList().get(position).getQuantity()));
        } else {
            editIngredientPopupIngredientEditText.setText(editIngredientIngredientEditText.getText().toString());
            //editIngredientPopupQuantityEditText.setText(editIngredientQuantity.getText().toString());
        }

        popupRemoveButton = myDialog.findViewById(R.id.edit_ingredient_popup_remove_button);
        if (position != -1)
            popupRemoveButton.setVisibility(View.VISIBLE);


        Spinner spinner = myDialog.findViewById(R.id.edit_ingredient_popup_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        myDialog.show();
    }

    /**
     *
     */
    public void newIngredient() {
        // new ingredient
        Ingredient ingredient = new Ingredient(-1, ingredientName);
        int ingredientID = database.addIngredient(ingredient);

        RecipeIngredient recipeIngredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, Double.valueOf(ingredientQuantity), ingredientUnit, ingredientDetails);
        int ret = database.addRecipeIngredient(recipeIngredient);
        if (ret == -1) {
            Context context = getApplicationContext();
            CharSequence text = "Error adding ingredient";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        // add ingredient
        if (ingredientUnit.compareTo("none") == 0)
            ingredientAdapter.add(editIngredientPopupIngredientEditText.getText().toString() + " [ " + ingredientQuantity + " ]");
        else
            ingredientAdapter.add(editIngredientIngredientEditText.getText().toString() + " [ " + ingredientQuantity + " " + ingredientUnit + " ]");
        recipe.getIngredientList().add(recipeIngredient);
        editIngredientIngredientEditText.getText().clear();
    }

    /**
     *
     * @param position
     */
    public void editIngredient(int position) {
        if (ingredientUnit.compareTo("none") == 0)
            ingredientList.set(position, editIngredientPopupIngredientEditText.getText().toString() + " [ " + ingredientQuantity + " ]");
        else
            ingredientList.set(position, editIngredientPopupIngredientEditText.getText().toString() + " [ " + ingredientQuantity + " " + ingredientUnit + " ]");
        ingredientAdapter.notifyDataSetChanged();

        Ingredient i = database.getIngredient(recipe.getIngredientList().get(position).getIngredientID());
        // change name of ingredient
        i.setName(ingredientName);
        database.editIngredient(i);

        // create new RecipeIngredient object for updated Ingredient
        RecipeIngredient recipeIngredient = recipe.getIngredientList().get(position);
        recipeIngredient.setDetails(ingredientDetails);
        recipeIngredient.setQuantity(Double.valueOf(ingredientQuantity));
        recipeIngredient.setUnit(ingredientUnit);

        // update ingredient list
        recipe.getIngredientList().set(position, recipeIngredient);
    }

    // Spinner Methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // A unit was selected, assign to String
        ingredientUnit = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    /**
     * This method monitors android button keys, i.e. back button
     * deletes the recipe and returns to RecipeList if recipe is new
     * @param keyCode
     * @param event
     * @return
     */ /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // Log.d(this.getClass().getName(), "back button pressed");
            if(newRecipe)
            {
                database.deleteRecipe(recipe.getKeyID());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return false;
                //return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    } */

}
