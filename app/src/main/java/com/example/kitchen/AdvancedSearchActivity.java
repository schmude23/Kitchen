package com.example.kitchen;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class AdvancedSearchActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper database = new DatabaseHelper(this);
    Recipe recipe;

    private RadioGroup recipeRadioGroup, orderRadioGroup;


    private ListView ingredientListView;
    private ArrayList<String> ingredientList = new ArrayList<String>();
    private RemovableItemsAdapter ingredientAdapter;
    private Button btnAddIngredient;
    AutoCompleteTextView editIngredient;

    private ListView categoryListView;
    private ArrayList<String> categoryList = new ArrayList<String>();
    private RemovableItemsAdapter categoryAdapter;

    private Button btnAddCategory;
    private EditText editCategory;

    //private SearchView search;
    private EditText searchEditText;
    private Button searchButton;

    public final int SEARCH_BY_NAME = 0;
    public final int SEARCH_BY_PREP_TIME = 1;
    public final int SEARCH_BY_TOTAL_TIME = 2;
    private int recipeRadioSelected = 0;
    private boolean ascending = true;

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
        setContentView(R.layout.activity_advanced_search);

        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initRadioGroups();
        limitToIngredientList();
        limitToCategoryList();
        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.advanced_search_search_button);
        searchButton.setOnClickListener(this);


    }

    private void search() {
        Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("input", searchEditText.getText().toString());
        intent.putExtra("recipeRadio", recipeRadioSelected);
        int[] ingredientId = new int[ingredientList.size()];
        for (int i = 0; i < ingredientList.size(); i++)
            ingredientId[i] = database.getIngredient(ingredientList.get(i).toLowerCase());
        intent.putExtra("ingredientArray", ingredientId);
        if (categoryList.size() > 0)
            intent.putExtra("categoryId", database.getCategory(categoryList.get(0).toLowerCase()));
        intent.putExtra("advancedSearch", true);
        intent.putExtra("ascending", ascending);
        startActivity(intent);
        this.finish();
    }


    private void initRadioGroups() {
        recipeRadioGroup = (RadioGroup) findViewById(R.id.radio_group_recipe);
        recipeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_recipe_name:
                        recipeRadioSelected = SEARCH_BY_NAME;
                        break;
                    case R.id.radio_prep_time:
                        recipeRadioSelected = SEARCH_BY_PREP_TIME;
                        break;
                    case R.id.radio_total_time:
                        recipeRadioSelected = SEARCH_BY_TOTAL_TIME;
                        break;

                }

            }

        });
        orderRadioGroup = (RadioGroup) findViewById(R.id.radio_group_order);
        orderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_ascending:
                        ascending = true;
                        break;
                    case R.id.radio_descending:
                        ascending = false;
                        break;
                }

            }

        });
    }


    private void limitToIngredientList() {
        //editIngredient = (EditText) findViewById(R.id.edit_ingredient);
        ArrayList<Ingredient> ingredients = database.getAllIngredients();
        String[] ingredientStrings = new String[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientStrings[i] = ingredients.get(i).getName();
        }
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ingredientStrings);
        editIngredient = findViewById(R.id.edit_ingredient);
        editIngredient.setAdapter(autoCompleteAdapter);
        btnAddIngredient = (Button) findViewById(R.id.button_add_ingredient);
        btnAddIngredient.setOnClickListener(this);

        //ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);
        ingredientAdapter = new RemovableItemsAdapter(ingredientList, this);
        // set the ingredientListView variable to your ingredientList in the xml
        ingredientListView = (ListView) findViewById(R.id.ingredient_list);
        ingredientListView.setAdapter(ingredientAdapter);
    }


    private void limitToCategoryList() {
        editCategory = (EditText) findViewById(R.id.edit_category);

        btnAddCategory = (Button) findViewById(R.id.button_add_category);
        btnAddCategory.setOnClickListener(this);

        //ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);
        categoryAdapter = new RemovableItemsAdapter(categoryList, this);

        // set the ingredientListView variable to your ingredientList in the xml
        categoryListView = (ListView) findViewById(R.id.category_list);
        categoryListView.setAdapter(categoryAdapter);
    }


    @Override
    public void onClick(View v) {
        String input;
        switch (v.getId()) {
            case R.id.button_add_ingredient:
                input = editIngredient.getText().toString();
                if (input.length() > 0) {
                    if (database.getIngredient(input) != -1) {
                        ingredientList.add(input);
                        ingredientAdapter.notifyDataSetChanged();
                        ingredientListView.setAdapter(ingredientAdapter);
                        setListViewHeightBasedOnItems(ingredientListView);
                    } else
                        Toast.makeText(this, input + " is not a valid ingredient", Toast.LENGTH_LONG).show();
                }
                editIngredient.getText().clear();
                break;
            case R.id.button_add_category:
                input = editCategory.getText().toString();
                if (input.length() > 0) {
                    if (categoryList.size() == 1) {
                        Toast.makeText(this, "Only one category allowed for Advanced Search", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if (database.getCategory(input) != -1) {
                        categoryList.add(input);
                        categoryAdapter.notifyDataSetChanged();
                        categoryListView.setAdapter(categoryAdapter);
                        setListViewHeightBasedOnItems(categoryListView);
                    } else
                        Toast.makeText(this, input + " is not a valid category", Toast.LENGTH_LONG).show();
                }
                editCategory.getText().clear();
                break;
            case R.id.advanced_search_search_button:
                if(recipeRadioSelected == SEARCH_BY_PREP_TIME){
                    try{
                        Integer.valueOf(searchEditText.getText().toString());
                    } catch(NumberFormatException e){
                        Toast.makeText(this, "Prep time must be an integer (with no spaces)", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if(recipeRadioSelected == SEARCH_BY_PREP_TIME){
                    try{
                        Integer.valueOf(searchEditText.getText().toString());
                    } catch(NumberFormatException e){
                        Toast.makeText(this, "Total time must be an integer (with no spaces)", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                search();
                break;

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

    public void onToolbarTextClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

}
