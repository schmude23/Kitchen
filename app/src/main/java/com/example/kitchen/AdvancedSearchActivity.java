package com.example.kitchen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AdvancedSearchActivity extends AppCompatActivity implements View.OnClickListener, RecipeAdapter.OnClickListener {
    DatabaseHelper database = new DatabaseHelper(this);
    Recipe recipe;
    private List<Recipe> recipes;
    private List<RecipeListItem> recipeListItems;
    private RecipeAdapter recipeAdapter;
    private RecyclerView recyclerView;

    private RadioGroup recipeRadioGroup, orderRadioGroup;
    private RadioButton recipeTitle, servings, prepTime, totalTime, ascending, descending, random;

    private ListView ingredientListView;
    private ArrayList<String> ingredientList = new ArrayList<String>();
    private ArrayAdapter<String> ingredientAdapter;
    private Button btnAddIngredient;
    private EditText editIngredient;

    private ListView categoryListView;
    private ArrayList<String> categoryList = new ArrayList<String>();
    private ArrayAdapter<String> categoryAdapter;
    private Button btnAddCategory;
    private EditText editCategory;

    private ListView excludeIngredientListView;
    private ArrayList<String> excludeIngredientList = new ArrayList<String>();
    private ArrayAdapter<String> excludeIngredientAdapter;
    private Button excludeBtnAddIngredient;
    private EditText excludeEditIngredient;

    private ListView excludeCategoryListView;
    private ArrayList<String> excludeCategoryList = new ArrayList<String>();
    private ArrayAdapter<String> excludeCategoryAdapter;
    private Button excludeBtnAddCategory;
    private EditText excludeEditCategory;
    //private SearchView search;
    private EditText searchEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        initRadioGroups();
        limitToIngredientList();
        limitToCategoryList();
        excludeIngredientList();
        excludeCategoryList();
        //search = findViewById(R.id.search);
        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.advanced_search_search_button);
        searchButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.advanced_search_recipe_list_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setLayoutManager(layoutManager);


    }

    private void search() {
        Toast.makeText(this,  "." + searchEditText.getText().toString() + ".", Toast.LENGTH_SHORT).show();
        if(searchEditText.getText().toString().compareTo("") != 0) {
            switch (recipeRadioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_recipe_name:
                    recipes = new ArrayList<>();
                    recipes.add(database.getRecipe(searchEditText.getText().toString()));
                    getRecipeListItems();
                    recipeAdapter = new RecipeAdapter(recipeListItems, this);
                    recyclerView.setAdapter(recipeAdapter);
                    break;
                case R.id.radio_prep_time:
                    recipes = database.getRecipeByPrepTime(Integer.valueOf(searchEditText.getText().toString()));
                    getRecipeListItems();
                    recipeAdapter = new RecipeAdapter(recipeListItems, this);
                    recyclerView.setAdapter(recipeAdapter);
                    break;
                case R.id.radio_total_time:
                    recipes = database.getRecipeByTotalTime(Integer.valueOf(searchEditText.getText().toString()));
                    getRecipeListItems();
                    recipeAdapter = new RecipeAdapter(recipeListItems, this);
                    recyclerView.setAdapter(recipeAdapter);
                    break;
                case R.id.radio_servings:
                    Toast.makeText(this, "Search by servings not implemented", Toast.LENGTH_SHORT).show();
                    //recipes = database.getRecipeByPrepTime(Integer.valueOf(searchEditText.getText().toString()));
                    //getRecipeListItems();
                    //recipeAdapter = new RecipeAdapter(recipeListItems, this);
                    //recyclerView.setAdapter(recipeAdapter);
            }
        }
        if(ingredientList != null){
            if(ingredientList.size() == 1){
                int ingredientId = database.getIngredient(ingredientList.get(0));
                recipes = database.getRecipeByIngredientId(ingredientId);
            }else {
                int [] ingredientId = new int[ingredientList.size()];
                for(int i =0; i < ingredientList.size(); i++){
                    ingredientId[i] = database.getIngredient(ingredientList.get(i));
                }
                recipes = database.getRecipeByIngredientIdList(ingredientId);
            }
        }
        if(categoryList != null){
            if(categoryList.size() == 1){
                int categoryId = database.getCategory(categoryList.get(0));
                recipes = database.getRecipeByCategoryId(categoryId);
            }
        }
        getRecipeListItems();
        recipeAdapter = new RecipeAdapter(recipeListItems, this);
        recyclerView.setAdapter(recipeAdapter);
    }

    private void getRecipeListItems() {
        if (recipes != null) {
            recipeListItems = new ArrayList<>();
            for (int i = 0; i < recipes.size(); i++) {
                String recipe_name = recipes.get(i).getTitle();
                double servings = recipes.get(i).getServings();
                int prep_time = recipes.get(i).getPrep_time();
                int total_time = recipes.get(i).getTotal_time();
                Bitmap image = recipes.get(i).getImage(this);
                recipeListItems.add(new RecipeListItem(recipe_name, servings, prep_time, total_time, image, recipes.get(i).getFavorited()));
            }
        }
    }

    private void initRadioGroups() {
        recipeRadioGroup = (RadioGroup) findViewById(R.id.radio_group_recipe);
        orderRadioGroup = (RadioGroup) findViewById(R.id.radio_group_order);
        orderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                        Toast.makeText(getApplicationContext(), "not implemented",
                                Toast.LENGTH_SHORT).show();

            }

        });
    }


    private void limitToIngredientList() {
        editIngredient = (EditText) findViewById(R.id.edit_ingredient);
        btnAddIngredient = (Button) findViewById(R.id.button_add_ingredient);
        btnAddIngredient.setOnClickListener(this);

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

        // set the ingredientListView variable to your ingredientList in the xml
        ingredientListView = (ListView) findViewById(R.id.ingredient_list);
        ingredientListView.setAdapter(ingredientAdapter);
    }

    private void excludeIngredientList() {
        excludeEditIngredient = (EditText) findViewById(R.id.exclude_edit_ingredient);
        excludeBtnAddIngredient = (Button) findViewById(R.id.exclude_button_add_ingredient);
        excludeBtnAddIngredient.setOnClickListener(this);

        //ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);
        excludeIngredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, excludeIngredientList) {
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

        // set the ingredientListView variable to your ingredientList in the xml
        excludeIngredientListView = (ListView) findViewById(R.id.exclude_ingredient_list);
        excludeIngredientListView.setAdapter(excludeIngredientAdapter);
    }


    private void limitToCategoryList() {
        editCategory = (EditText) findViewById(R.id.edit_category);
        btnAddCategory = (Button) findViewById(R.id.button_add_category);
        btnAddCategory.setOnClickListener(this);

        //ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);
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

        // set the ingredientListView variable to your ingredientList in the xml
        categoryListView = (ListView) findViewById(R.id.category_list);
        categoryListView.setAdapter(categoryAdapter);
    }

    private void excludeCategoryList() {
        excludeEditCategory = (EditText) findViewById(R.id.exclude_edit_category);
        excludeBtnAddCategory = (Button) findViewById(R.id.exclude_button_add_category);
        excludeBtnAddCategory.setOnClickListener(this);

        //ingredientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ingredientList);
        excludeCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, excludeCategoryList) {
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

        // set the ingredientListView variable to your ingredientList in the xml
        excludeCategoryListView = (ListView) findViewById(R.id.exclude_category_list);
        excludeCategoryListView.setAdapter(excludeCategoryAdapter);
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
            case R.id.exclude_button_add_ingredient:
                Toast.makeText(getApplicationContext(), "not implemented",
                        Toast.LENGTH_SHORT).show();
                input = excludeEditIngredient.getText().toString();
                if (input.length() > 0) {
                    if (database.getIngredient(input) != -1) {
                        excludeIngredientList.add(input);
                        excludeIngredientAdapter.notifyDataSetChanged();
                        excludeIngredientListView.setAdapter(excludeIngredientAdapter);
                        setListViewHeightBasedOnItems(excludeIngredientListView);
                    } else
                        Toast.makeText(this, input + " is not a valid ingredient", Toast.LENGTH_LONG).show();
                }
                excludeEditIngredient.getText().clear();
                break;
            case R.id.exclude_button_add_category:
                Toast.makeText(getApplicationContext(), "not implemented",
                        Toast.LENGTH_SHORT).show();
                input = excludeEditCategory.getText().toString();
                if (input.length() > 0) {
                    if (database.getCategory(input) != -1) {
                        excludeCategoryList.add(input);
                        excludeCategoryAdapter.notifyDataSetChanged();
                        excludeCategoryListView.setAdapter(excludeCategoryAdapter);
                        setListViewHeightBasedOnItems(excludeCategoryListView);
                    } else
                        Toast.makeText(this, input + " is not a valid category", Toast.LENGTH_LONG).show();
                }
                excludeEditCategory.getText().clear();
                break;
            case R.id.advanced_search_search_button:
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


    /**
     * This is the onclick method for the recipe adapter.
     *
     * @param position the position of the item clicked on within the dataset
     */
    @Override
    public void onClick(int position) {
        Context context = getApplicationContext();
        CharSequence text = "Retrieving recipe";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        int recipeId = recipes.get(position).getKeyID();
        Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);

        //adding extra information from intent
        intent.putExtra("recipeId", recipeId);
        startActivity(intent);
    }
}
