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
import android.widget.Toast;

import java.util.ArrayList;


public class EditCategoryActivity extends AppCompatActivity {
    private DatabaseHelper database = new DatabaseHelper(this);
    private Recipe recipe;
    private Dialog myDialog;
    private Button cancelButton;
    private EditText editCategory, editCategoryPopupCategoryEditText;
    private ListView categoryListView;
    private ArrayList<String> categoryList = new ArrayList<String>();
    private ArrayAdapter<String> categoryAdapter;
    private boolean newRecipe;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        // get recipe and create new RecipeDirection ArrayList
        int recipeID = getIntent().getIntExtra("recipeId", -1);
        newRecipe = getIntent().getBooleanExtra("newRecipe", true);
        recipe = database.getRecipe(recipeID);

        myDialog = new Dialog(this);
        cancelButton = findViewById(R.id.edit_category_cancel_button);

        editCategory = (EditText) findViewById(R.id.edit_category_category_edit_text);
        categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, categoryList);

        // set the ingredientListView variable to your ingredientList in the xml
        categoryListView = (ListView) findViewById(R.id.edit_category_category_list);
        categoryListView.setAdapter(categoryAdapter);
        if (newRecipe)
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

    /**
     *
     */
    private void addRecipe() {
        recipe.setCategoryList(new ArrayList<RecipeCategory>());

    }

    /**
     *
     */
    private void editRecipe() {
        cancelButton.setVisibility(View.INVISIBLE);
        for (int i = 0; i < recipe.getCategoryList().size(); i++) {
            int id = recipe.getCategoryList().get(i).getCategoryID();
            Category category = database.getCategory(id);
            String name = category.getName();
            categoryAdapter.add(name);
        }
    }

    /**
     *
     * @param v
     */
    public void onEditCategoryAddCategoryButtonPressed(View v) {
        String input = editCategory.getText().toString();
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
    }

    /**
     *
     * @param v
     */
    public void onEditCategoryNextButtonPressed(View v) {
        if (recipe.getCategoryList().size() > 0) {
            // Next Activity
            if(database.editRecipe(recipe)) {
                Context context = getApplicationContext();

                CharSequence text = "Recipe Added!";
                if (!newRecipe)
                    text = "Recipe Updated!";
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
    public void onEditCategoryCancelButtonPressed(View v){
        database.deleteRecipe(recipe.getKeyID());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param v
     */
    public void onEditCategoryPopupOkayButtonPressed(View v){
        // edit the category that was clicked and update
        String input = editCategoryPopupCategoryEditText.getText().toString();
        if (input.length() > 0) {

            categoryList.set(position, input);
            categoryAdapter.notifyDataSetChanged();

            // Remove the old category and create a new one
            RecipeCategory recipeCategory = recipe.getCategoryList().get(position);
            database.deleteCategory(recipeCategory.getCategoryID());
            // set the new categoryID for recipeCategory
            recipeCategory.setCategoryID(database.addCategory(new Category(-1, input)));
            recipe.getCategoryList().set(position, recipeCategory);
            myDialog.dismiss();
        }
    }

    /**
     *
     * @param v
     */
    public void onEditCategoryPopupRemoveButtonPressed(View v){
        database.deleteCategory(recipe.getCategoryList().get(position).getCategoryID());
        recipe.getCategoryList().remove(position);
        categoryList.remove(position);
        categoryAdapter.notifyDataSetChanged();
        myDialog.dismiss();
    }

    /**
     * This method displays a popup window for editing the selected direction
     *
     * @param v
     * @param position element in ListView that was clicked
     */
    public void ShowPopup(View v, final int position) {
        Button removeButton;

        myDialog.setContentView(R.layout.edit_category_popup);

        editCategoryPopupCategoryEditText = myDialog.findViewById(R.id.edit_category_popup_category_edit_text);
        int categoryID = recipe.getCategoryList().get(position).getCategoryID();
        editCategoryPopupCategoryEditText.setText(database.getCategory(categoryID).getName());
        removeButton = myDialog.findViewById(R.id.edit_category_popup_remove_button);
        if(position != -1)
            removeButton.setVisibility(View.VISIBLE);
        this.position = position;
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        myDialog.show();
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
    }*/
}
