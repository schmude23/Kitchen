package com.example.kitchen;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class IngredientListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseHelper database = new DatabaseHelper(this);
    Recipe recipe;
    private EditText editIngredientPopupIngredientEditText;

    // ListView variables
    private ListView ingredientListView;
    private List<String> ingredientList = new ArrayList<String>();
    private ArrayAdapter<String> ingredientAdapter;
    List<Ingredient> ingredients;
    private int position = -1;
    Dialog myDialog; // Dialog for popup window

    String ingredientName, ingredientDetails, ingredientQuantity, ingredientUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get theme
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        int themeId = sharedPreferences.getInt("ThemeId", 0);
        if (themeId == 0)
            setTheme(R.style.AppTheme);
        else
            setTheme(R.style.DarkMode);
        setContentView(R.layout.activity_ingredient_list);
        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        myDialog = new Dialog(this);

        ingredients = database.getAllIngredients();
        for(int i = 0;i < ingredients.size(); i++){
            ingredientList.add(ingredients.get(i).getName());
        }
        ingredientList = stringQuickSort(ingredientList, true);
        ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);
        ingredientListView = (ListView) findViewById(R.id.activity_ingredient_list_ingredient_list);

        // set the ingredientListView variable to your ingredientList in the xml
        ingredientListView.setAdapter(ingredientAdapter);
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPopup(view, position);
            }
        });

    }



    /**
     * This method is run when an item from the options menu is selected
     *
     * @param item the item that was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.advanced_search_item:
                Intent advancedSearch = new Intent(this, AdvancedSearchActivity.class);
                startActivity(advancedSearch);
                this.finish();
                return true;
            case R.id.action_add_recipe:
                Intent addRecipe = new Intent(this, EditRecipeActivity.class);
                addRecipe.putExtra("recipeId", -1); // New recipe
                addRecipe.putExtra("newRecipe", true); // New recipe
                startActivity(addRecipe);
                this.finish();
                return true;
            case R.id.action_view_cart:
                Intent viewCart = new Intent(this, ShoppingCartActivity.class);
                viewCart.putExtra("recipeId", -1);
                startActivity(viewCart);
                this.finish();
                return true;
            case R.id.action_ingredient_list:
                Intent intent = new Intent(this, IngredientListActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @param v
     */
    public void onEditIngredientPopupOkayButtonPressed(View v) {
        ingredientName = editIngredientPopupIngredientEditText.getText().toString();
        if (ingredientName.length() > 0) {
            int id = database.getIngredient(ingredientList.get(position));
            Ingredient i = database.getIngredient(id);
            i.setName(ingredientName);
            database.editIngredient(i);
            ingredientList.set(position, ingredientName);
            myDialog.dismiss();
        }
    }

    public void onEditIngredientPopupRemoveButtonPressed(View v) {
        database.deleteIngredient(ingredients.get(position).getKeyID());
        ingredientList.remove(position);
        ingredientAdapter.notifyDataSetChanged();
        myDialog.dismiss();
    }


    /**
     * This method displays a popup window for editing the selected direction
     *
     * @param v
     * @param position element in ListView that was clicked
     */
    public void ShowPopup(View v, final int position) {

        myDialog.setContentView(R.layout.activity_edit_ingredient_popup);
        editIngredientPopupIngredientEditText = myDialog.findViewById(R.id.edit_ingredient_edit_text);
        editIngredientPopupIngredientEditText.setText(ingredientList.get(position));
        this.position = position;

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

    public void onToolbarTextClicked(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
    public static List<String> stringQuickSort(List<String> list, boolean ascending) {
        List<String> sorted;  // this shall be the sorted list to return, no needd to initialise
        List<String> smaller = new ArrayList<>(); // Vehicles smaller than pivot
        List<String> greater = new ArrayList<>(); // Vehicles greater than pivot

        if (list.isEmpty())
            return list; // start with recursion base case
        String pivot = list.get(0);  // first Vehicle in list, used as pivot
        int i;
        String j;     // Variable used for Vehicles in the loop
        for (i = 1; i < list.size(); i++) {
            j = list.get(i);
            if (ascending) {
                if (j.compareTo(pivot) < 0)   // make sure Vehicle has proper compareTo method
                    smaller.add(j);
                else
                    greater.add(j);
            } else {
                if (j.compareTo(pivot) > 0)   // make sure Vehicle has proper compareTo method
                    smaller.add(j);
                else
                    greater.add(j);
            }
        }
        smaller = stringQuickSort(smaller, ascending);  // capitalise 's'
        greater = stringQuickSort(greater, ascending);  // sort both halfs recursively
        smaller.add(pivot);          // add initial pivot to the end of the (now sorted) smaller Vehicles
        smaller.addAll(greater);     // add the (now sorted) greater Vehicles to the smaller ones (now smaller is essentially your sorted list)
        sorted = smaller;            // assign it to sorted; one could just as well do: return smaller

        return sorted;


    }
}
