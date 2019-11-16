package com.example.kitchen;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class IngredientListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseHelper database = new DatabaseHelper(this);
    Recipe recipe;
    private Button finishButton;
    private EditText editIngredientPopupIngredientEditText,
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
        setContentView(R.layout.activity_ingredient_list);


        myDialog = new Dialog(this);

        finishButton = findViewById(R.id.activity_ingredient_list_cancel_button);
        getIngredients();
       // ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);

        // set the ingredientListView variable to your ingredientList in the xml
//        ingredientListView.setAdapter(ingredientAdapter);
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
    private void getIngredients() {
        // setup Ingredient List
        //ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);
        ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = ((TextView) view.findViewById(android.R.id.text1));
                textView.setMinHeight(0); // Min Height
                textView.setMinimumHeight(0); // Min Height
                textView.setHeight(100); // Height
                return view;
            }
        };
        // set the ingredientListView variable to ingredientList in the xml
        ingredientListView = (ListView) findViewById(R.id.activity_ingredient_list_ingredient_list);
        ingredientListView.setAdapter(ingredientAdapter);
        List<Recipe> recipes = database.getAllRecipes();
        // Retrieve ingredients and add to ListView
        for (int j = 0; j < recipes.size();j++) {
            recipe = database.getRecipe(recipes.get(j).getKeyID());
            for (int i = 0; i < recipe.getIngredientList().size(); i++) {
                int ingredientID = recipe.getIngredientList().get(i).getIngredientID();
                Ingredient ingredient = database.getIngredient(ingredientID);
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
                ingredientAdapter.notifyDataSetChanged();
                ingredientListView.setAdapter(ingredientAdapter);
                setListViewHeightBasedOnItems(ingredientListView);
            }
        }
    }
    /**
     * Sets the size of ListView based on the number of items in the list
     *
     * @param listView to be updated
     * @return true on success, otherwise false
     */
    public boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }
    /**
     * @param v
     */
    public void onEditIngredientPopupOkayButtonPressed(View v) {
        ingredientName = editIngredientPopupIngredientEditText.getText().toString();
        ingredientDetails = editIngredientPopupIngredientDetailsEditText.getText().toString();
        ingredientQuantity = editIngredientPopupQuantityEditText.getText().toString();
        if (ingredientName.length() > 0 && ingredientQuantity.length() > 0) {
            editIngredient(position);
            myDialog.dismiss();
        }
    }

    public void onEditIngredientPopupRemoveButtonPressed(View v) {
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
            if (details != null)
                editIngredientPopupIngredientDetailsEditText.setText(details);
            editIngredientPopupQuantityEditText.setText(valueOf(recipe.getIngredientList().get(position).getQuantity()));
        } else {
            //editIngredientPopupIngredientEditText.setText(editIngredientIngredientEditText.getText().toString());
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
}
