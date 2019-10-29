package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class EditRecipeActivity extends AppCompatActivity implements OnClickListener {

    public static Recipe recipe = new Recipe();
    private Button btnNext;
    private EditText editTitle, editServings, editPrepTime, editTotalTime;
    private ImageView recipeImage;
    private static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        btnNext = (Button) findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editServings = (EditText) findViewById(R.id.edit_servings);
        editPrepTime = (EditText) findViewById(R.id.edit_prep_time);
        editTotalTime = (EditText) findViewById(R.id.edit_total_time);
        recipeImage = (ImageView) findViewById(R.id.recipe_image);
        recipeImage.setOnClickListener(this);
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
                    Intent intent = new Intent(this, EditIngredientActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.recipe_image:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
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
}
