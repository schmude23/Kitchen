package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import static java.lang.String.valueOf;

public class EditIngredientActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    DatabaseHelper database = new DatabaseHelper(this);
    Recipe recipe;
    private Button btnAddIngredient, btnNext;
    private EditText editIngredient;

    // ListView variables
    private ListView ingredientListView;
    private ArrayList<String> ingredientList = new ArrayList<String>();
    private ArrayAdapter<String> ingredientAdapter;

    Dialog myDialog; // Dialog for popup window

    String ingredient_name, ingredient_quantity, ingredient_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);

        int recipeID = getIntent().getIntExtra("recipeId", -1);
        recipe = database.getRecipe(recipeID);
        recipe.setIngredientList(new ArrayList<RecipeIngredient>());
        recipe.setDirectionsList(new ArrayList<RecipeDirection>());
        recipe.setCategoryList(new ArrayList<RecipeCategory>());


        myDialog = new Dialog(this);

        btnAddIngredient = (Button) findViewById(R.id.add_ingredient_btn);
        btnAddIngredient.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);

        editIngredient = (EditText) findViewById(R.id.edit_text_ingredient);
        ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);

        // set the ingredientListView variable to your ingredientList in the xml
        ingredientListView = (ListView) findViewById(R.id.ingredient_list);
        ingredientListView.setAdapter(ingredientAdapter);
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPopup(view, position);
            }
        });

    }

    public void onClick(View v) {
        String input;
        switch (v.getId()) {

            case R.id.add_ingredient_btn:
                // Show popup if valid ingredient to select quantity and units
                input = editIngredient.getText().toString();
                if (input.length() > 0) {
                    ShowPopup(v, -1);
                }
                break;

            case R.id.next_btn:
                // Next Activity
                database.editRecipe(recipe);
                Intent intent = new Intent(this, EditDirectionActivity.class);
                intent.putExtra("recipeId", recipe.getKeyID());
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    /**
     * Popup to add/edit ingredient. Prompts for quantity and units of ingredient. Then adds to ListView
     * and creates a new RecipeIngredient with the given inputs
     *
     * @param v
     * @param position position of ingredient to be modified, -1 if new ingredient
     */
    public void ShowPopup(View v, final int position) {
        final EditText edit_name, edit_quantity;
        Button btnOkay;

        myDialog.setContentView(R.layout.edit_ingredient_popup);
        edit_name = myDialog.findViewById(R.id.ingredient_text);
        edit_quantity = myDialog.findViewById(R.id.ingredient_quantity_text);

        if (position != -1) {
            // update ingredient --> get current ingredient ingredient
            Ingredient i = database.getIngredient(recipe.getIngredientList().get(position).getIngredientID());
            edit_name.setText(i.getName());
            edit_quantity.setText(valueOf(recipe.getIngredientList().get(position).getQuantity()));
        } else {
            edit_name.setText(editIngredient.getText().toString());
            edit_quantity.setText(edit_quantity.getText().toString());
        }

        btnOkay = myDialog.findViewById(R.id.button_okay);


        Spinner spinner = myDialog.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredient_name = edit_name.getText().toString();
                ingredient_quantity = edit_quantity.getText().toString();
                RecipeIngredient recipeIngredient;
                if (ingredient_name.length() > 0 && ingredient_quantity.length() > 0) {

                    if (position != -1) {
                        // edit (position) recipe ingredient
                        ingredientList.set(position, edit_name.getText().toString() + " (" + ingredient_quantity + " " + ingredient_unit + ")");
                        ingredientAdapter.notifyDataSetChanged();

                        Ingredient i = database.getIngredient(recipe.getIngredientList().get(position).getIngredientID());
                        // change name of ingredient
                        i.setName(ingredient_name);
                        database.editIngredient(i);

                        // create new RecipeIngredient object for updated Ingredient
                        recipeIngredient = recipe.getIngredientList().get(position);
                        //recipeIngredient.setDetails(edit_name.getText().toString());
                        recipeIngredient.setQuantity(Double.valueOf(ingredient_quantity));
                        recipeIngredient.setUnit(ingredient_unit);

                        // update ingredient list
                        recipe.getIngredientList().set(position, recipeIngredient);
                    } else {
                        // new ingredient
                        Ingredient ingredient = new Ingredient(-1, ingredient_name);
                        int ingredientID = database.addIngredient(ingredient);

                        recipeIngredient = new RecipeIngredient(-1, recipe.getKeyID(), ingredientID, Double.valueOf(ingredient_quantity), ingredient_unit, null);
                        int ret = database.addRecipeIngredient(recipeIngredient);
                        if(ret == -1) {
                            Context context = getApplicationContext();
                            CharSequence text = "Error adding ingredient";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        // add ingredient
                        ingredientAdapter.add(edit_name.getText().toString() + " (" + ingredient_quantity + " " + ingredient_unit + ")");
                        recipe.getIngredientList().add(recipeIngredient);
                    }
                    myDialog.dismiss();
                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    // Spinner Methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // A unit was selected, assign to String
        ingredient_unit = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
