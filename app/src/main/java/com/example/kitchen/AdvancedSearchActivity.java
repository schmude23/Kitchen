package com.example.kitchen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdvancedSearchActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper database = new DatabaseHelper(this);
    Recipe recipe;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        initRadioGroups();
        limitToIngredientList();
        limitToCategoryList();
        excludeIngredientList();
        excludeCategoryList();

    }

    private void initRadioGroups(){
        recipeRadioGroup = (RadioGroup) findViewById(R.id.radio_group_recipe);
        orderRadioGroup = (RadioGroup) findViewById(R.id.radio_group_order);
        recipeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId){
                    case R.id.radio_recipe_name:
                        Toast.makeText(getApplicationContext(), "choice: Recipe Title",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_servings:
                        Toast.makeText(getApplicationContext(), "choice: Servings",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_prep_time:
                        Toast.makeText(getApplicationContext(), "choice: Prep Time",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_total_time:
                        Toast.makeText(getApplicationContext(), "choice: Total Time",
                                Toast.LENGTH_SHORT).show();
                        break;
                }

            }

        });
        orderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                switch (checkedId){
                    case R.id.radio_ascending:
                        Toast.makeText(getApplicationContext(), "choice: Ascending",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_descending:
                        Toast.makeText(getApplicationContext(), "choice: Descending",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_random:
                        Toast.makeText(getApplicationContext(), "choice: Random",
                                Toast.LENGTH_SHORT).show();
                        break;
                }

            }

        });
    }


    private void limitToIngredientList(){
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
    private void excludeIngredientList(){
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


    private void limitToCategoryList(){
        editCategory= (EditText) findViewById(R.id.edit_category);
        btnAddCategory= (Button) findViewById(R.id.button_add_category);
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
    private void excludeCategoryList(){
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
        switch (v.getId()){
            case R.id.button_add_ingredient:
                input = editIngredient.getText().toString();
                if(input.length() > 0){
                    ingredientList.add(input);
                    ingredientAdapter.notifyDataSetChanged();
                    ingredientListView.setAdapter(ingredientAdapter);
                    setListViewHeightBasedOnItems(ingredientListView);
                }
                editIngredient.getText().clear();
                break;
            case R.id.button_add_category:
                input = editCategory.getText().toString();
                if(input.length() > 0){
                    categoryList.add(input);
                    categoryAdapter.notifyDataSetChanged();
                    categoryListView.setAdapter(categoryAdapter);
                    setListViewHeightBasedOnItems(categoryListView);
                }
                editCategory.getText().clear();
                break;
            case R.id.exclude_button_add_ingredient:
                input = excludeEditIngredient.getText().toString();
                if(input.length() > 0){
                    excludeIngredientList.add(input);
                    excludeIngredientAdapter.notifyDataSetChanged();
                    excludeIngredientListView.setAdapter(excludeIngredientAdapter);
                    setListViewHeightBasedOnItems(excludeIngredientListView);
                }
                excludeEditIngredient.getText().clear();
                break;
            case R.id.exclude_button_add_category:
                input = excludeEditCategory.getText().toString();
                if(input.length() > 0){
                    excludeCategoryList.add(input);
                    excludeCategoryAdapter.notifyDataSetChanged();
                    excludeCategoryListView.setAdapter(excludeCategoryAdapter);
                    setListViewHeightBasedOnItems(excludeCategoryListView);
                }
                excludeEditCategory.getText().clear();
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
}
