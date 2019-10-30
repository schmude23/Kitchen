package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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

import java.util.ArrayList;



public class EditDirectionActivity extends AppCompatActivity implements OnClickListener {
    DatabaseHelper database = new DatabaseHelper(this);
    Recipe recipe;
    Dialog myDialog;
    private Button btnAddDirection, btnNext;
    private EditText editDirection;
    private ListView directionListView;
    ArrayList<String> directionList = new ArrayList<String>();
    ArrayAdapter<String> directionAdapter;
    int dirNum; // direction number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_direction);
        // get recipe and create new RecipeDirection ArrayList
        int recipeID = getIntent().getIntExtra("recipeId", -1);
        recipe = database.getRecipe(recipeID);
        recipe.setDirectionsList(new ArrayList<RecipeDirection>());
        recipe.setCategoryList(new ArrayList<RecipeCategory>());


        myDialog = new Dialog(this);
        dirNum = 0;
        btnAddDirection = (Button) findViewById(R.id.add_direction_btn);
        btnAddDirection.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        editDirection = (EditText) findViewById(R.id.edit_text_direction);
        directionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, directionList);

        // set the ingredientListView variable to your ingredientList in the xml
        directionListView = (ListView) findViewById(R.id.direction_list);
        directionListView.setAdapter(directionAdapter);
        directionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPopup(view, position);
            }
        });

    }

    /**
     * @param v
     */
    public void onClick(View v) {
        String input;
        switch (v.getId()) {
            case R.id.add_direction_btn:
                input = editDirection.getText().toString();
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
                // Next Activity
                database.editRecipe(recipe);
                Intent intent = new Intent(this, EditCategoryActivity.class);
                intent.putExtra("recipeId", recipe.getKeyID());
                startActivity(intent);
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
        Button btnOkay;

        myDialog.setContentView(R.layout.edit_direction_popup);

        edit_direction = myDialog.findViewById(R.id.edit_direction);
        edit_direction.setText(recipe.getDirectionsList().get(position).getDirectionText());
        btnOkay = myDialog.findViewById(R.id.button_okay);

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
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
