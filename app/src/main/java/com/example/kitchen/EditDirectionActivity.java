package com.example.kitchen;

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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class EditDirectionActivity extends AppCompatActivity {
    private DatabaseHelper database = new DatabaseHelper(this);
    private Recipe recipe;
    private Dialog myDialog;
    private FloatingActionButton finishButton;
    private EditText directionEditText, editDirectionPopupDirectionEditText;
    private ListView directionListView;
    private ArrayList<String> directionList = new ArrayList<String>();
    private ArrayAdapter<String> directionAdapter;
    int dirNum; // direction number
    int position = -1;
    boolean newRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_direction);
        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.edit_recipe_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // get recipe and create new RecipeDirection ArrayList
        int recipeID = getIntent().getIntExtra("recipeId", -1);
        newRecipe = getIntent().getBooleanExtra("newRecipe", true);
        recipe = database.getRecipe(recipeID);


        myDialog = new Dialog(this);
        dirNum = 0;

        finishButton = findViewById(R.id.edit_direction_finish_button);
        directionEditText = (EditText) findViewById(R.id.edit_direction_direction_edit_text);

        directionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, directionList);

        // set the ingredientListView variable to your ingredientList in the xml
        directionListView = (ListView) findViewById(R.id.edit_direction_direction_list);
        directionListView.setAdapter(directionAdapter);
        if (newRecipe && recipe.getDirectionsList() == null)
            addRecipe();
        else
            editRecipe();

        directionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        recipe.setDirectionsList(new ArrayList<RecipeDirection>());
        //recipe.setCategoryList(new ArrayList<RecipeCategory>());
    }

    /**
     *
     */
    private void editRecipe() {
        if (!newRecipe)
            finishButton.setImageResource(R.drawable.ic_check_mark);
        for (int i = 0; i < recipe.getDirectionsList().size(); i++) {
            String text = recipe.getDirectionsList().get(i).getDirectionText();
            directionAdapter.add(text);
        }
    }

    /**
     * @param v
     */
    public void onEditDirectionAddDirectionButtonPressed(View v) {
        String input = directionEditText.getText().toString();
        directionEditText.getText().clear();
        if (input.length() > 0) {
            // add string to the categoryAdapter, not the listview
            directionAdapter.add(input);

            // Create new RecipeDirection object and add it to database
            RecipeDirection recipeDirection = new RecipeDirection(-1, recipe.getKeyID(), input, ++dirNum);
            database.addRecipeDirection(recipeDirection);
            recipe.getDirectionsList().add(recipeDirection);
        }
    }

    /**
     * @param v
     */
    public void onEditDirectionFinishButtonPressed(View v) {
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
     * @param v
     */
    public void onEditDirectionNextButtonPressed(View v) {
        if (recipe.getDirectionsList().size() > 0) {
            // Next Activity
            database.editRecipe(recipe);
            Intent intent = new Intent(this, EditCategoryActivity.class);
            intent.putExtra("recipeId", recipe.getKeyID());
            intent.putExtra("newRecipe", newRecipe);
            startActivity(intent);
        }
    }

    /**
     * @param v
     */
    public void onEditDirectionPopupOkayButtonPressed(View v) {
        // edit the direction that was clicked and update
        String input = editDirectionPopupDirectionEditText.getText().toString();
        if (input.length() > 0) {
            directionList.set(position, input);
            directionAdapter.notifyDataSetChanged();

            // Change directionText but not directionNumber
            RecipeDirection recipeDirection = recipe.getDirectionsList().get(position);
            recipeDirection.setDirectionText(input);
            recipe.getDirectionsList().set(position, recipeDirection);
            myDialog.dismiss();
        }
    }

    /**
     * @param v
     */
    public void onEditDirectionPopupRemoveButtonPressed(View v) {
        recipe.getDirectionsList().remove(position);
        directionList.remove(position);
        directionAdapter.notifyDataSetChanged();
        myDialog.dismiss();
    }


    /**
     * This method displays a popup window for editing the selected direction
     *
     * @param v
     * @param position element in ListView that was clicked
     */
    public void ShowPopup(View v, final int position) {
        Button btnRemove;

        myDialog.setContentView(R.layout.edit_direction_popup);
        editDirectionPopupDirectionEditText = myDialog.findViewById(R.id.edit_direction_popup_direction_edit_text);
        editDirectionPopupDirectionEditText.setText(recipe.getDirectionsList().get(position).getDirectionText());
        this.position = position;
        btnRemove = myDialog.findViewById(R.id.edit_direction_popup_remove_button);
        if (position != -1)
            btnRemove.setVisibility(View.VISIBLE);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        myDialog.show();
    }

    // Toolbar functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        return super.onOptionsItemSelected(item);
    }

    public void onEditRecipeBackButtonPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), EditIngredientActivity.class);
        intent.putExtra("recipeId", recipe.getKeyID());
        intent.putExtra("newRecipe", newRecipe);
        startActivity(intent);
    }

}
