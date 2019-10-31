package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;


public class EditCategoryActivity extends AppCompatActivity implements OnClickListener {
    private DatabaseHelper database = new DatabaseHelper(this);
    private Recipe recipe;
    Dialog myDialog;
    private Button btnAddCategory, btnNext;
    private EditText editCategory;
    private ListView categoryListView;
    private ArrayList<String> categoryList = new ArrayList<String>();
    private ArrayAdapter<String> categoryAdapter;
    private boolean newRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        // get recipe and create new RecipeDirection ArrayList
        int recipeID = getIntent().getIntExtra("recipeId", -1);
        newRecipe = getIntent().getBooleanExtra("newRecipe", true);
        recipe = database.getRecipe(recipeID);

        myDialog = new Dialog(this);
        btnAddCategory = (Button) findViewById(R.id.btn_add_category);
        btnAddCategory.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        editCategory = (EditText) findViewById(R.id.edit_category);
        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, categoryList);

        // set the ingredientListView variable to your ingredientList in the xml
        categoryListView = (ListView) findViewById(R.id.category_list);
        categoryListView.setAdapter(categoryAdapter);
        if(newRecipe)
            addRecipe();
        else
            editRecipe();
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPopup(view, position);
            }
        });

    }

    private void addRecipe() {
        recipe.setCategoryList(new ArrayList<RecipeCategory>());
    }
    private void editRecipe() {
        for(int i = 0; i < recipe.getCategoryList().size(); i++)
        {
            int id = recipe.getCategoryList().get(i).getCategoryID();
            Category category = database.getCategory(id);
            String name = category.getName();
            categoryAdapter.add(name);
        }
    }


    /**
     * @param v
     */
    public void onClick(View v) {
        String input;
        switch (v.getId()) {
            case R.id.btn_add_category:
                input = editCategory.getText().toString();
                editCategory.getText().clear();
                if (input.length() > 0) {
                    // add string to the categoryAdapter, not the listview
                    categoryAdapter.add(input);

                    // Create new RecipeDirection object and add it to database
                    Category category = new Category(-1, input);
                    int categoryID = database.addCategory(category);
                    RecipeCategory recipeCategory = new RecipeCategory(-1, recipe.getKeyID(), categoryID);
                    database.addRecipeCategory(recipeCategory);
                    recipe.getCategoryList().add(recipeCategory);
                }
                break;
            case R.id.next_btn:
                if(recipe.getCategoryList().size() > 0) {
                    // Next Activity
                    database.editRecipe(recipe);
                    Context context = getApplicationContext();
                    CharSequence text = "Recipe Added!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    Intent intent = new Intent(this, DisplaySelectedRecipeActivity.class);
                    intent.putExtra("recipeId", recipe.getKeyID());
                    startActivity(intent);
                }
                break;

            default:
                break;
        }

    }

    /**
     * This method displays a popup window for editing the selected direction
     *
     * @param v
     * @param position element in ListView that was clicked
     */
    public void ShowPopup(View v, final int position) {
        final EditText edit_category;
        Button btnOkay;

        myDialog.setContentView(R.layout.edit_category_popup);

        edit_category = myDialog.findViewById(R.id.edit_category);
        final int categoryID = recipe.getCategoryList().get(position).getCategoryID();
        edit_category.setText(database.getCategory(categoryID).getName());
        btnOkay = myDialog.findViewById(R.id.button_okay);

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edit the category that was clicked and update
                String input = edit_category.getText().toString();
                if (input.length() > 0) {

                    categoryList.set(position, input);
                    categoryAdapter.notifyDataSetChanged();

                    // Remove the old category and create a new one
                    RecipeCategory recipeCategory = recipe.getCategoryList().get(position);
                    database.deleteCategory(categoryID);
                    // set the new categoryID for recipeCategory
                    recipeCategory.setCategoryID(database.addCategory(new Category(-1, input)));
                    recipe.getCategoryList().set(position, recipeCategory);
                    myDialog.dismiss();
                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
