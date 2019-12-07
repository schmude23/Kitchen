package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

//TODO: Dealing with long recipe titles
public class EditRecipeActivity extends AppCompatActivity {
    DatabaseHelper database = new DatabaseHelper(this);
    public Recipe recipe;// = new Recipe();
    private EditText recipeNameEditText, servingsEditText, prepTimeEditText, totalTimeEditText;
    private ImageView recipeImage;
    private static final int RESULT_LOAD_IMAGE = 1;
    private int recipeId;
    private boolean newRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // Display Toolbar
        Toolbar toolbar = findViewById(R.id.edit_recipe_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recipeNameEditText = (EditText) findViewById(R.id.edit_recipe_recipe_name_edit_text);
        servingsEditText = (EditText) findViewById(R.id.edit_recipe_servings_edit_text);
        prepTimeEditText = (EditText) findViewById(R.id.edit_recipe_prep_time_edit_text);
        totalTimeEditText = (EditText) findViewById(R.id.edit_recipe_total_time_edit_text);
        recipeImage = (ImageView) findViewById(R.id.edit_recipe_image);
        //retrieving the extra information from intent
        newRecipe = getIntent().getBooleanExtra("newRecipe", true);
        recipeId = getIntent().getIntExtra("recipeId", -1);
        if (newRecipe && recipeId == -1) {
            addRecipe();
        } else {
            editRecipe();
        }
    }

    /**
     * @param v
     */
    public void onEditRecipeNextButtonPressed(View v) {
        String inputTitle = recipeNameEditText.getText().toString();
        String inputServings = servingsEditText.getText().toString();
        String inputPrepTime = prepTimeEditText.getText().toString();
        String inputTotalTime = totalTimeEditText.getText().toString();
        if (inputTitle.length() > 0 && inputServings.length() > 0 && inputPrepTime.length() > 0 && inputTotalTime.length() > 0) {

            recipe.setTitle(inputTitle);

            //Try to get the servings
            try {
                recipe.setServings(Double.valueOf(inputServings));
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "Servings is not a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            //Try to get the prep time
            try {
                recipe.setPrep_time(Integer.parseInt(inputPrepTime));
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "Prep Time is not a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            //Try to get the Total Time
            try {
                recipe.setTotal_time(Integer.parseInt(inputTotalTime));
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "Total time is not a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!database.editRecipe(recipe)) {
                Context context = getApplicationContext();
                CharSequence text = "Error creating recipe";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                Intent intent = new Intent(this, EditIngredientActivity.class);
                intent.putExtra("recipeId", recipe.getKeyID());
                intent.putExtra("newRecipe", newRecipe);
                startActivity(intent);
                this.finish();
            }
        }
    }

    /**
     * @param v
     */
    public void onEditRecipeImagePressed(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    public void onEditRecipeCancelButtonPressed(View v) {
        if (newRecipe) {
            database.deleteRecipe(recipe.getKeyID());
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    // add image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap imageBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //recipeImage.setImageURI(selectedImage);
                recipeImage.setImageBitmap(imageBitMap);
                //recipeImage.setRotation(90);
                // add image to recipe
                if (!recipe.setImage(imageBitMap, this)) {
                    Context context = getApplicationContext();
                    CharSequence text = "Error uploading photo to database";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            } catch (Exception e) {
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
    private void addRecipe() {
        recipe = new Recipe();
        database.addRecipe(recipe);
    }

    /**
     * Fills in the EditTexts with the current recipe's attributes
     */
    private void editRecipe() {
        recipe = database.getRecipe(recipeId);
        recipeNameEditText.setText(recipe.getTitle());
        servingsEditText.setText(String.valueOf(recipe.getServings()));
        prepTimeEditText.setText(String.valueOf(recipe.getPrep_time()));
        totalTimeEditText.setText(String.valueOf(recipe.getTotal_time()));
        recipeImage.setImageBitmap(recipe.getImage(this));
    }

    /**
     * This method monitors android button keys, i.e. back button
     * deletes the recipe and returns to RecipeList if recipe is new
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // Log.d(this.getClass().getName(), "back button pressed");
            if (newRecipe) {
                database.deleteRecipe(recipe.getKeyID());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // Toolbar functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_recipe_menu, menu);
        return true;
    }


    public void onEditRecipeBackButtonPressed(View view) {
        if (newRecipe) {
            database.deleteRecipe(recipe.getKeyID());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
