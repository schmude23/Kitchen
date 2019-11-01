package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;


public class EditDirectionActivity extends AppCompatActivity implements OnClickListener {
    DatabaseHelper database = new DatabaseHelper(this);
    Recipe recipe;
    Dialog myDialog;
    private Button btnAddDirection, btnNext, btnCancel;
    private EditText editDirection;
    private ListView directionListView;
    ArrayList<String> directionList = new ArrayList<String>();
    ArrayAdapter<String> directionAdapter;
    int dirNum; // direction number
    boolean newRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_direction);
        // get recipe and create new RecipeDirection ArrayList
        int recipeID = getIntent().getIntExtra("recipeId", -1);
        newRecipe = getIntent().getBooleanExtra("newRecipe", true);
        recipe = database.getRecipe(recipeID);


        myDialog = new Dialog(this);
        dirNum = 0;

        btnAddDirection = (Button) findViewById(R.id.add_direction_btn);
        btnAddDirection.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        btnCancel = findViewById(R.id.cancel_btn);
        editDirection = (EditText) findViewById(R.id.edit_text_direction);

        directionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, directionList);

        // set the ingredientListView variable to your ingredientList in the xml
        directionListView = (ListView) findViewById(R.id.direction_list);
        directionListView.setAdapter(directionAdapter);
        if (newRecipe)
            addRecipe();
        else
            editRecipe();
        btnCancel.setOnClickListener(this);

        directionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPopup(view, position);
            }
        });

    }

    private void addRecipe() {
        recipe.setDirectionsList(new ArrayList<RecipeDirection>());
        recipe.setCategoryList(new ArrayList<RecipeCategory>());
    }

    private void editRecipe() {
        btnCancel.setText("Finish");
        for (int i = 0; i < recipe.getDirectionsList().size(); i++) {
            String text = recipe.getDirectionsList().get(i).getDirectionText();
            directionAdapter.add(text);
        }
    }

    /**
     * @param v
     */
    public void onClick(View v) {
        String input;
        switch (v.getId()) {
            case R.id.add_direction_btn:
                input = editDirection.getText().toString();
                editDirection.getText().clear();
                if (input.length() > 0) {
                    // add string to the categoryAdapter, not the listview
                    directionAdapter.add(input);

                    // Create new RecipeDirection object and add it to database
                    RecipeDirection recipeDirection = new RecipeDirection(-1, recipe.getKeyID(), input, ++dirNum);
                    database.addRecipeDirection(recipeDirection);
                    recipe.getDirectionsList().add(recipeDirection);
                }
                break;
            case R.id.next_btn:
                if (recipe.getDirectionsList().size() > 0) {
                    // Next Activity
                    database.editRecipe(recipe);
                    Intent intent = new Intent(this, EditCategoryActivity.class);
                    intent.putExtra("recipeId", recipe.getKeyID());
                    intent.putExtra("newRecipe", newRecipe);
                    startActivity(intent);
                }
                break;
            case R.id.cancel_btn:
                if(newRecipe) {
                    database.deleteRecipe(recipe.getKeyID());
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    if(database.editRecipe(recipe)) {
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
        final EditText edit_direction;
        Button btnOkay, btnRemove;

        myDialog.setContentView(R.layout.edit_direction_popup);

        edit_direction = myDialog.findViewById(R.id.edit_direction);
        edit_direction.setText(recipe.getDirectionsList().get(position).getDirectionText());
        btnOkay = myDialog.findViewById(R.id.button_okay);
        btnRemove = myDialog.findViewById(R.id.button_remove);
        if(position != -1)
            btnRemove.setVisibility(View.VISIBLE);

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edit the direction that was clicked and update
                String input = edit_direction.getText().toString();
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
        });
        btnRemove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                recipe.getDirectionsList().remove(position);
                directionList.remove(position);
                directionAdapter.notifyDataSetChanged();
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
    } */

}
