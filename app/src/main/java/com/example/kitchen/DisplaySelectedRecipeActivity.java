package com.example.kitchen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
//TODO: Dealing with long recipe titles
// TODO: back button pressed go to recipe list

public class DisplaySelectedRecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DatabaseHelper dbHandler = new DatabaseHelper(DisplaySelectedRecipeActivity.this);
    private TextView recipe_title;
    private TextView servings;
    private TextView prep_time;
    private TextView total_time;
    private ImageView image;
    private ImageButton favoriteIcon;
    Recipe recipe;

    // Ingredient ListView variables
    private ListView ingredientListView;
    private ArrayList<String> ingredientList = new ArrayList<String>();
    private ArrayAdapter<String> ingredientAdapter;

    // Direction ListView variables
    private ListView directionListView;
    ArrayList<String> directionList = new ArrayList<String>();
    ArrayAdapter<String> directionAdapter;

    // Category ListView variables
    private ListView categoryListView;
    ArrayList<String> categoryList = new ArrayList<String>();
    ArrayAdapter<String> categoryAdapter;

    //Popup window dialogs
    Dialog scaleRecipeDialog, convertUnitDialog;
    String oldUnit, newUnit;
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
        //image.setRotation(90);
        getIngredients();
        getDirections();
        getCategories();
        if(recipe.getFavorited()){
            favoriteIcon.setImageResource(R.drawable.ic_favorite);

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
        ingredientListView = (ListView) findViewById(R.id.ingredient_list);
        ingredientListView.setAdapter(ingredientAdapter);

        // Retrieve ingredients and add to ListView
        for (int i = 0; i < recipe.getIngredientList().size(); i++) {
            int ingredientID = recipe.getIngredientList().get(i).getIngredientID();
            Ingredient ingredient = dbHandler.getIngredient(ingredientID);
            String name = ingredient.getName();
            String quantity = String.valueOf(recipe.getIngredientList().get(i).getQuantity());
            String unit = recipe.getIngredientList().get(i).getUnit();
            // Add to ListView and update height
            if(unit.compareTo("none") == 0)
                ingredientList.add(name + " [ " + quantity + " ]");
            else
                ingredientList.add(name + " [ " + quantity + " " + unit + " ]");
            ingredientAdapter.notifyDataSetChanged();
            ingredientListView.setAdapter(ingredientAdapter);
            setListViewHeightBasedOnItems(ingredientListView);
        }
        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();

                CharSequence text = "Details: " + recipe.getIngredientList().get(position).getDetails();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    /**
     *
     */
    private void getDirections() {
        directionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, directionList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = ((TextView) view.findViewById(android.R.id.text1));
                textView.setMinHeight(0); // Min Height
                textView.setMinimumHeight(0); // Min Height
                textView.setHeight(300); // Height
                return view;
            }
        };
        // set the directionListView variable to directionList in the xml
        directionListView = (ListView) findViewById(R.id.direction_list);
        directionListView.setAdapter(directionAdapter);
        for (int i = 0; i < recipe.getDirectionsList().size(); i++) {
            String text = recipe.getDirectionsList().get(i).getDirectionText();
            int number = recipe.getDirectionsList().get(i).getDirectionNumber();
            // fix incorrect numbers from removed directions
            if(number != (i + 1)) {
                number = i + 1;
                recipe.getDirectionsList().get(i).setDirectionNumber(number);
            }
            // Add to ListView and update height
            directionList.add(number + ") " + text);
            directionAdapter.notifyDataSetChanged();
            directionListView.setAdapter(directionAdapter);
            setListViewHeightBasedOnItems(directionListView);
        }
    }

    /**
     *
     */
    private void getCategories() {
        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryList) {
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
        // set the directionListView variable to directionList in the xml
        categoryListView = (ListView) findViewById(R.id.category_list);
        categoryListView.setAdapter(categoryAdapter);
        for (int i = 0; i < recipe.getCategoryList().size(); i++) {
            int categoryID = recipe.getCategoryList().get(i).getCategoryID();
            Category category = dbHandler.getCategory(categoryID);
            String text = category.getName();
            // Add to ListView and update height
            categoryList.add(text);
            categoryAdapter.notifyDataSetChanged();
            categoryListView.setAdapter(categoryAdapter);
            setListViewHeightBasedOnItems(categoryListView);
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
                return true;
            case R.id.action_delete_recipe:
                dbHandler.deleteRecipe(recipe.getKeyID());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
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
    public void onDisplaySelectedRecipeFavoriteButtonPressed(View v){
        boolean favorited = recipe.getFavorited();
        if(favorited){
            recipe.setFavorited(false);
            dbHandler.editRecipe(recipe);
            favoriteIcon.setImageResource(R.drawable.ic_favorite_outline);
        }
        else{
            recipe.setFavorited(true);
            dbHandler.editRecipe(recipe);
            favoriteIcon.setImageResource(R.drawable.ic_favorite);
        }
    }

    /**
     * When the "Convert Units" menu option is selected, a popup window is
     * displayed.This popup contains two spinners prompting the user to select
     * the unit to be converted and its newly converted unit.
     * @param item
     */
    public void onConvertUnitSelected(MenuItem item){
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


    public void onUnitConversionPopupOkayButtonPressed(View v){

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
    public void onUnitConversionPopupCancelButtonPressed(View v){
        convertUnitDialog.dismiss();
    }
    public void onScaleRecipeSelected(MenuItem item){

    }
    public void onScaleRecipePopupCancelButtonPressed(View v){

    }
    public void onScaleRecipePopupOkayButtonPressed(View v){

    }

    // Spinner Methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // A unit was selected, assign to String
        switch(parent.getId()) {
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
