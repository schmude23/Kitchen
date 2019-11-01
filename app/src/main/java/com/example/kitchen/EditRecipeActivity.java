package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EditRecipeActivity extends AppCompatActivity implements OnClickListener {
    DatabaseHelper database = new DatabaseHelper(this);
    public Recipe recipe;// = new Recipe();
    private Button btnNext, btnCancel;
    private EditText editTitle, editServings, editPrepTime, editTotalTime;
    private ImageView recipeImage;
    private static final int RESULT_LOAD_IMAGE = 1;
    private int recipeId;
    private boolean newRecipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        btnNext = (Button) findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        btnCancel = findViewById(R.id.cancel_btn);
        btnCancel.setOnClickListener(this);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editServings = (EditText) findViewById(R.id.edit_servings);
        editPrepTime = (EditText) findViewById(R.id.edit_prep_time);
        editTotalTime = (EditText) findViewById(R.id.edit_total_time);
        recipeImage = (ImageView) findViewById(R.id.recipe_image);
        recipeImage.setOnClickListener(this);
        //retrieving the extra infromation from intent
        newRecipe = getIntent().getBooleanExtra("newRecipe", true);
        if(newRecipe) {
            addRecipe();
        }
        else {
            editRecipe();
        }
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.next_btn:
                String inputTitle = editTitle.getText().toString();
                String inputServings = editServings.getText().toString();
                String inputPrepTime = editPrepTime.getText().toString();
                String inputTotalTime = editTotalTime.getText().toString();
                if (inputTitle.length() > 0 && inputServings.length() > 0 && inputPrepTime.length() > 0 && inputTotalTime.length() > 0) {

                    recipe.setTitle(inputTitle);
                    recipe.setServings(Double.valueOf(inputServings));
                    recipe.setPrep_time(Integer.parseInt(inputPrepTime));
                    recipe.setTotal_time(Integer.parseInt(inputTotalTime));
                    if(!database.editRecipe(recipe)) {
                        Context context = getApplicationContext();
                        CharSequence text = "Error creating recipe";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                    else {
                        Intent intent = new Intent(this, EditIngredientActivity.class);
                        intent.putExtra("recipeId", recipe.getKeyID());
                        intent.putExtra("newRecipe", newRecipe);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.recipe_image:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.cancel_btn:
                if(newRecipe)
                {
                    database.deleteRecipe(recipe.getKeyID());
                }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }



    }
    // add image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap imageBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //recipeImage.setImageURI(selectedImage);
                recipeImage.setImageBitmap(imageBitMap);
                //recipeImage.setRotation(90);
                // add image to recipe
                if(!recipe.setImage(imageBitMap, this))
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Error uploading photo to database";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            } catch(Exception e){
                Context context = getApplicationContext();
                CharSequence text = "Error retrieving photo";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    /**
     * Creates a new recipe object
     */
    private void addRecipe(){
        recipe = new Recipe();
        database.addRecipe(recipe);
    }

    /**
     * Fills in the EditTexts with the current reciepe's arrtibutes
     */
    private void editRecipe(){
        recipeId = getIntent().getIntExtra("recipeId", -1);
        recipe = database.getRecipe(recipeId);
        editTitle.setText(recipe.getTitle());
        editServings.setText(String.valueOf(recipe.getServings()));
        editPrepTime.setText(String.valueOf(recipe.getPrep_time()));
        editTotalTime.setText(String.valueOf(recipe.getTotal_time()));
        recipeImage.setImageBitmap(recipe.getImage(this));
    }
    /**
     * This method monitors android button keys, i.e. back button
     * deletes the recipe and returns to RecipeList if recipe is new
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
           // Log.d(this.getClass().getName(), "back button pressed");
            if(newRecipe)
            {
                database.deleteRecipe(recipe.getKeyID());
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
